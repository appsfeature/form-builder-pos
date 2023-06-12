package com.sample.preregistration.generate;

import com.formbuilder.util.GsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SPEntity implements Serializable {

    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("title")
    @Expose
    public String title;

    public SPEntity() {

    }

    public SPEntity(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
