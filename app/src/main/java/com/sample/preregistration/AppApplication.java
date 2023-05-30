package com.sample.preregistration;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.formbuilder.FormBuilder;
import com.formbuilder.interfaces.FormResponse;
import com.formbuilder.model.FBNetworkModel;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.model.FormLocationProperties;

public class AppApplication extends Application {
    private static AppApplication instance;

    public static AppApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FormBuilder.getInstance()
                .onFormSubmit(new FormResponse.Form() {
                    @Override
                    public void onFormSubmit(FormBuilderModel formData, String gsonData, FormResponse.Callback<FBNetworkModel> callback) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(new FBNetworkModel(true));
                            }
                        }, 2000);
                    }
                })
//                .addProgressListener(new FormResponse.Progress(){
//
//                    @Override
//                    public void onStartProgressBar() {
//
//                    }
//
//                    @Override
//                    public void onStopProgressBar() {
//
//                    }
//                })
                .setDebugModeEnabled(BuildConfig.DEBUG);
    }
}