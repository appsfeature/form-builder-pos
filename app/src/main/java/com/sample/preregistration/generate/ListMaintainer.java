package com.sample.preregistration.generate;


import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;

import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.util.FBPreferences;
import com.formbuilder.util.GsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ListMaintainer {

    public static final String KEY_DEFAULT = "default";



    /**
     * @param item     : list item
     * @param title    : for unique check
     */
    public static void saveData(Context context, FormBuilderModel item, int title) {
        saveData(context, KEY_DEFAULT, 0, item, title);
    }

    /**
     * @param key      : unique key for save data
     * @param listSize : list size
     * @param item     : list item
     * @param formId    : for unique check
     */
    public static void saveData(Context context, String key, int listSize, FormBuilderModel item, int formId) {
        String previousData = updateRecentList(context, key, listSize, item, formId);
        if (!TextUtils.isEmpty(previousData)) {
            FBPreferences.setRecentFeatureData(context, key, previousData);
        }
    }

    @MainThread
    public static void clear(Context context) {
        clear(context, KEY_DEFAULT);
    }

    @MainThread
    public static void clear(Context context, String key) {
        FBPreferences.setRecentFeatureData(context, key, "");
    }

    private static String updateRecentList(Context context, String key, int listSize, FormBuilderModel item, int formId) {
        List<FormBuilderModel> value = GsonParser.fromJson((FBPreferences.getRecentFeatureData(context, key)), new TypeToken<List<FormBuilderModel>>() {
        });
        if (value == null) {
            value = new ArrayList<>();
        }
        if (formId > 0)
            for (FormBuilderModel mItem : value) {
                if (mItem.getFormId() == formId) {
                    value.remove(mItem);
                    break;
                }
            }
        value.add(0, item);
        if (value.size() == 0) {
            return "";
        } else {
            if (listSize > 0 && value.size() > listSize) {
                return GsonParser.toJson(value.subList(0, listSize), new TypeToken<List<FormBuilderModel>>() {
                });
            } else {
                return GsonParser.toJson(value, new TypeToken<List<FormBuilderModel>>() {
                });
            }
        }
    }

    /**
     * @param typeCast : new TypeToken<List<ModelName>>() {}
     *                 or  new TypeToken<ArrayList<ModelName>>() {}
     *                 or new TypeToken<HashMap<Integer, PropertyModel>>() {}
     */
    @Nullable
    public static <T> T getList(Context context, TypeToken<T> typeCast) {
        return GsonParser.fromJson(FBPreferences.getRecentFeatureData(context, KEY_DEFAULT), typeCast);
    }



    static boolean removeData(Context context, String key, int matchText) {
        List<FormBuilderModel> value = GsonParser.fromJson((FBPreferences.getRecentFeatureData(context, key)), new TypeToken<List<FormBuilderModel>>(){});
        if (value == null) {
            value = new ArrayList<>();
        }
        int prevSize = value.size();
        for (FormBuilderModel item : value) {
            if(item.getFormId() == matchText){
                value.remove(item);
                break;
            }
        }
        if (value.size() <= 0) {
            FBPreferences.setRecentFeatureData(context, key, "");
        } else {
            String newJson = GsonParser.toJson(value, new TypeToken<List<FormBuilderModel>>() {
            });
            if (!TextUtils.isEmpty(newJson)) {
                FBPreferences.setRecentFeatureData(context, key, newJson);
            }
        }
        return prevSize != value.size();
    }

}
