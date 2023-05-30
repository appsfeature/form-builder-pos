package com.formbuilder.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amit on 3/29/2018.
 */

public class FBNetworkModel {

    @SerializedName("status")
    @Expose
    private boolean status;

    public FBNetworkModel() {}

    public FBNetworkModel(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
