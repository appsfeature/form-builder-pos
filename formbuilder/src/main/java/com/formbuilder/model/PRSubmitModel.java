package com.formbuilder.model;

import com.formbuilder.util.GsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class PRSubmitModel {
    @Expose
    @SerializedName(value = "camp_id")
    private int formId;
    @Expose
    @SerializedName(value = "camp_name")
    private String formName;
    @Expose
    @SerializedName(value = "lib_ver")
    private String versionLib;
    @Expose
    @SerializedName(value = "camp_ver")
    private String versionAdv;
    @Expose
    @SerializedName(value = "camp_data")
    private Map<String, String> formData;

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getVersionLib() {
        return versionLib;
    }

    public void setVersionLib(String versionLib) {
        this.versionLib = versionLib;
    }

    public String getVersionAdv() {
        return versionAdv;
    }

    public void setVersionAdv(String versionAdv) {
        this.versionAdv = versionAdv;
    }

    public Map<String, String> getFormData() {
        return formData;
    }

    public void setFormData(Map<String, String> inputFieldData) {
        this.formData = inputFieldData;
    }

    public String toJson() {
        return GsonParser.toJson(this, new TypeToken<PRSubmitModel>() {
        });
    }
}
