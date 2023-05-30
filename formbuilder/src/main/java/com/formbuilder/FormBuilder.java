package com.formbuilder;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.formbuilder.base.FormBuilderClass;
import com.formbuilder.interfaces.FormResponse;
import com.formbuilder.model.FormBuilderModel;

public interface FormBuilder {

    String LIBRARY_VERSION = "1";

    boolean isDebugModeEnabled();

    static FormBuilder getInstance() {
        return FormBuilderClass.Builder();
    }

    FormBuilder setDebugModeEnabled(boolean debugModeEnabled);

    boolean isFormSubmitted(Context context, int formId);

    void openDynamicFormActivity(Context context, int formId, String json, FormResponse.FormSubmitListener formSubmitListener);

    void openDynamicFormActivity(Context context, int formId, FormBuilderModel property, FormResponse.FormSubmitListener formSubmitListener);

    Fragment getFragment(FormBuilderModel property, FormResponse.FormSubmitListener formSubmitListener);

    void setFormSubmitListener(FormResponse.FormSubmitListener mFormSubmitListener);

    void dispatchOnFormSubmit(Boolean status);

    void syncSignupForm();

    void setSyncSignupFormListener(FormResponse.SyncSignupForm syncSignupFormListener);

    FormBuilder setEnableJsonEncode(boolean isEnableJsonEncode);

    boolean isEnableJsonEncode();

    FormBuilder addProgressListener(FormResponse.Progress progress);

    FormResponse.Progress getProgressListener();

    FormBuilder onFormSubmit(FormResponse.Form callback);

    FormResponse.Form getFormSubmitCallback();

    int getAppVersionCode(Context context);

    FormBuilder setAppVersionCode(Context context, int appVersion);

    FormBuilderClass setAdvertiseVersionCode(Context context, int value);

    int getAdvertiseVersionCode(Context context);
}
