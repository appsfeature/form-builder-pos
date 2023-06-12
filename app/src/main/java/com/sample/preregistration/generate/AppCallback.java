package com.sample.preregistration.generate;

import android.view.View;

import com.formbuilder.model.DynamicInputModel;

public interface AppCallback {
    /**
     * @param <T> object Type
     * default method type means not mandatory implementation of onFailure method
     */
    interface Callback<T> {
        void onSuccess(T response);

        default void onFailure(Exception e){}
    }

    interface Status<T> {
        void onSuccess(T response);
    }

    interface NetworkCallback<T> {
        void onCompleted();
        void onDataLoaded();
        void onSuccess(T response);
        void onFailure(Exception e);
    }

    interface OnItemClickListener<T> {
        void onItemClicked(View view, T item);
    }


    interface OnClickListener {
        void onItemClicked(View view, int position, DynamicInputModel item);
    }


    interface OnListClickListener<T> {
        void onItemClicked(View view, int position, T item);
        void onDeleteClicked(View view, int position, T item);
    }

    interface Progress {
        void onStartProgressBar();
        void onStopProgressBar();
    }
}