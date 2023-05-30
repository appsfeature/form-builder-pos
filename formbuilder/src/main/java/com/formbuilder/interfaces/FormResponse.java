package com.formbuilder.interfaces;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.IntDef;

import com.formbuilder.model.FBNetworkModel;
import com.formbuilder.model.FormBuilderModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface FormResponse {

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visibility {
    }

    interface Callback<T> {
        void onSuccess(T response);

        default void onFailure(Exception e){}
    }

    interface FormSubmitListener {
        void onFormSubmitted(Context activity, Boolean status);
    }

    interface SyncSignupForm {
        void onSyncSignupForm();
    }

    interface Progress {
        void onStartProgressBar();
        void onStopProgressBar();
    }

    interface Form {
        void onFormSubmit(FormBuilderModel formData, String gsonData, FormResponse.Callback<FBNetworkModel> callback);
    }
}