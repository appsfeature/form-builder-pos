package com.formbuilder.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PopupEntity implements Serializable {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("descriptionSecondary")
    @Expose
    public String descriptionSecondary; // Show 2nd time
    @SerializedName("buttonText")
    @Expose
    public String buttonText;

    public PopupEntity() {
    }

    public PopupEntity(String description) {
        this.description = description;
    }

    public PopupEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public PopupEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PopupEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getButtonText() {
        return buttonText;
    }

    public PopupEntity setButtonText(String buttonText) {
        this.buttonText = buttonText;
        return this;
    }

    public String getDescriptionSecondary() {
        return descriptionSecondary;
    }

    public PopupEntity setDescriptionSecondary(String descriptionSecondary) {
        this.descriptionSecondary = descriptionSecondary;
        return this;
    }
}
