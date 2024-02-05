package com.sample.preregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExportModel implements Serializable {

    @Expose
    @SerializedName(value="actionType")
    private int actionType = 1001;

    @Expose
    @SerializedName(value="fileName")
    private String fileName = "fileName.webp";

    @Expose
    @SerializedName(value="showIn")
    private String showIn = "home";
    @Expose
    @SerializedName(value="ranking")
    private int ranking = 1;
    @Expose
    @SerializedName(value="actionText")
    private String actionText;
    @Expose
    @SerializedName(value="startDate")
    private String startDate = "2023-01-25 12:00:00";
    @Expose
    @SerializedName(value="endDate")
    private String endDate = "2025-01-30 12:00:00";


    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getShowIn() {
        return showIn;
    }

    public void setShowIn(String showIn) {
        this.showIn = showIn;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

