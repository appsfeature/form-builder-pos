package com.formbuilder.network;

import android.content.Context;

import com.formbuilder.FormBuilder;
import com.formbuilder.R;
import com.formbuilder.interfaces.FieldType;
import com.formbuilder.interfaces.FormResponse;
import com.formbuilder.model.DynamicInputModel;
import com.formbuilder.model.FBNetworkModel;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.model.PRSubmitModel;
import com.formbuilder.util.FBUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class FBNetworkManager {

    private final Context context;


    public FBNetworkManager(Context context) {
        this.context = context;
    }

    public void submitForm(Context context, FormBuilderModel property, List<DynamicInputModel> mList, FormResponse.Callback<FBNetworkModel> callback) {
        if (property != null && mList != null && mList.size() > 0) {
            sendRequestByBulkJson(context, property, mList, callback);
        }
    }

    private void sendRequestByBulkJson(Context context, FormBuilderModel formData, List<DynamicInputModel> mList, FormResponse.Callback<FBNetworkModel> callback) {
        String userData = getSubmitModel(context, formData, mList).toJson();
        FBUtility.log("json : " + userData);
        FormBuilder.getInstance().getFormSubmitCallback().onFormSubmit(formData, userData, callback);
    }

    private PRSubmitModel getSubmitModel(Context context, FormBuilderModel formData, List<DynamicInputModel> mList) {
        PRSubmitModel model = new PRSubmitModel();
        model.setFormId(formData.getFormId());
        model.setFormName(formData.getFormName());
        model.setVersionLib(FormBuilder.LIBRARY_VERSION);
        model.setVersionAdv(FormBuilder.getInstance().getAdvertiseVersionCode(context) + "");
        model.setFormData(getHashMapInputData(mList));
//        model.setAppName(this.context.getString(R.string.app_name));
//        model.setTimestamp(getServerTimeStamp());
//        model.setPackageName(this.context.getPackageName());
//        model.setVersionApp(FormBuilder.getInstance().getAppVersionCode(context) + "");
        return model;
    }

    private Map<String, String> getHashMapInputData(List<DynamicInputModel> mList) {
        Map<String, String> map = new TreeMap<>();
        for (DynamicInputModel item : mList){
            if (item.getFieldType() != FieldType.TEXT_VIEW) {
                map.put(item.getParamKey(), item.getInputData());
            }
        }
        return map;
    }

    private String getServerTimeStamp() {
        SimpleDateFormat outFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = new Date(System.currentTimeMillis());
        return outFmt.format(date);
    }
}