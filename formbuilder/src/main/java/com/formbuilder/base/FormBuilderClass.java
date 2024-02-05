package com.formbuilder.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.formbuilder.FormBuilder;
import com.formbuilder.activity.FormBuilderActivity;
import com.formbuilder.fragment.FormBuilderFragment;
import com.formbuilder.interfaces.FormResponse;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.util.FBConstant;
import com.formbuilder.util.FBPreferences;
import com.formbuilder.util.FBUtility;
import com.formbuilder.util.GsonParser;

public class FormBuilderClass implements FormBuilder {

    private static volatile FormBuilderClass sSoleInstance;
    private boolean isDebugModeEnabled = false;
    public boolean isEnableJsonEncode = true;
    private FormResponse.SyncSignupForm syncSignupFormListener;
    private FormResponse.Progress mProgressListener;
    private FormResponse.Form mFormSubmitCallback;

    private FormBuilderClass() {

    }

    public static FormBuilderClass Builder() {
        if (sSoleInstance == null) {
            synchronized (FormBuilderClass.class) {
                if (sSoleInstance == null) sSoleInstance = new FormBuilderClass();
            }
        }
        return sSoleInstance;
    }

    @Override
    public boolean isDebugModeEnabled() {
        return isDebugModeEnabled;
    }

    @Override
    public FormBuilderClass setDebugModeEnabled(boolean debugModeEnabled) {
        isDebugModeEnabled = debugModeEnabled;
        return this;
    }

    @Override
    public boolean isFormSubmitted(Context context, int formId) {
        return FBPreferences.isFormSubmitted(context, formId);
    }

    @Override
    public void openDynamicFormActivity(Context context, String json, FormResponse.FormSubmitListener formSubmitListener) {
        FormBuilderModel property = GsonParser.getGson().fromJson(json, FormBuilderModel.class);
        if (property != null) {
            openDynamicFormActivity(context, property.getFormId(), property, formSubmitListener);
        } else {
            FBUtility.showToastCentre(context, FBConstant.Error.INVALID_FORM_DATA);
        }
    }

    @Override
    public void openDynamicFormActivity(Context context, int formId, FormBuilderModel property, FormResponse.FormSubmitListener formSubmitListener) {
        setFormSubmitListener(formSubmitListener);
        if (!isFormSubmitted(context, formId)) {
            context.startActivity(new Intent(context, FormBuilderActivity.class)
                    .putExtra(FBConstant.CATEGORY_PROPERTY, property));
        } else {
            String message = null;
            if(property != null && property.getPopup() != null){
                message = property.getPopup().getDescriptionSecondary();
            }
            dispatchOnFormSubmit(context, message,false);
        }
    }

    @Override
    public Fragment getFragment(FormBuilderModel property, FormResponse.FormSubmitListener formSubmitListener) {
        setFormSubmitListener(formSubmitListener);
        Fragment fragment = new FormBuilderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FBConstant.CATEGORY_PROPERTY, property);
        fragment.setArguments(bundle);
        return fragment;
    }

    private FormResponse.FormSubmitListener mFormSubmitListener;

    @Override
    public void setFormSubmitListener(FormResponse.FormSubmitListener mFormSubmitListener) {
        this.mFormSubmitListener = null;
        this.mFormSubmitListener = mFormSubmitListener;
    }

    @Override
    public void dispatchOnFormSubmit(Context context, String message, Boolean status) {
        if(mFormSubmitListener != null){
            mFormSubmitListener.onFormSubmitted(context, message, status);
        }
    }

    @Override
    public void syncSignupForm(){
        if(syncSignupFormListener != null){
            syncSignupFormListener.onSyncSignupForm();
        }
    }

    @Override
    public void setSyncSignupFormListener(FormResponse.SyncSignupForm syncSignupFormListener) {
        this.syncSignupFormListener = null;
        this.syncSignupFormListener = syncSignupFormListener;
    }

    @Override
    public FormBuilderClass setEnableJsonEncode(boolean isEnableJsonEncode) {
        this.isEnableJsonEncode = isEnableJsonEncode;
        return this;
    }

    @Override
    public boolean isEnableJsonEncode() {
        return isEnableJsonEncode;
    }

    @Override
    public FormBuilder addProgressListener(FormResponse.Progress progress) {
        mProgressListener = progress;
        return this;
    }

    @Override
    public FormResponse.Progress getProgressListener() {
        return mProgressListener;
    }

    @Override
    public FormBuilder onFormSubmit(FormResponse.Form formSubmitCallback) {
        mFormSubmitCallback = formSubmitCallback;
        return this;
    }

    @Override
    public FormResponse.Form getFormSubmitCallback() {
        return mFormSubmitCallback;
    }


    @Override
    public int getAppVersionCode(Context context) {
        return FBPreferences.getAppVersionCode(context);
    }

    @Override
    public FormBuilderClass setAppVersionCode(Context context, int appVersion) {
        FBPreferences.setAppVersionCode(context, appVersion);
        return this;
    }

    @Override
    public FormBuilderClass setAdvertiseVersionCode(Context context, int value) {
        FBPreferences.setAdvertiseVersionCode(context, value);
        return this;
    }

    @Override
    public int getAdvertiseVersionCode(Context context) {
        return FBPreferences.getAdvertiseVersionCode(context);
    }

}
