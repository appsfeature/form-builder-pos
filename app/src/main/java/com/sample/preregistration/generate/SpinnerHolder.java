package com.sample.preregistration.generate;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.formbuilder.R;
import com.formbuilder.model.entity.MasterEntity;
import com.formbuilder.util.FBUtility;
import com.formbuilder.util.GsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Resource layout
 * R.layout.pre_slot_spinner
 */
public class SpinnerHolder {
    private static final String SPINNER_HINT_PLACEHOLDER = "Select";
    public final Spinner spinnerInput;
    private View inputLayout;

    public SpinnerHolder(View parentLayout, Spinner spinnerInput) {
        this.spinnerInput = spinnerInput;
        (parentLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerInput.performClick();
            }
        });
    }

    public List<MasterEntity> getSpinnerList(String fieldData) {
        List<MasterEntity> fieldList = null;
        if(fieldData != null) {
            fieldList = GsonParser.fromJson(fieldData, new TypeToken<List<MasterEntity>>() {
            });
            if (fieldList != null) {
                fieldList.add(0, new MasterEntity(0, SPINNER_HINT_PLACEHOLDER));
            }
        }
        if(fieldList == null){
            fieldList = new ArrayList<>();
            fieldList.add(new MasterEntity(0, "No Data"));
        }
        return fieldList;
    }

    public static void showValidationError(Context context, String spinnerTitle) {
        FBUtility.showToastCentre(context, "Invalid " + spinnerTitle);
    }

    public void setData(Context context, List<SPEntity> item, int position, AppCallback.Status<String> callback) {
        SPAdapter adapter = new SPAdapter(context, item);
        spinnerInput.setAdapter(adapter);
        spinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPEntity mSelectedItem = (SPEntity) parent.getItemAtPosition(position);
                if (mSelectedItem != null && !mSelectedItem.getTitle().equals(SPINNER_HINT_PLACEHOLDER)) {
                    callback.onSuccess(mSelectedItem.getKey());
//                    item.setInputData(item.isSpinnerSelectTitle() ? mSelectedItem.getTitle() : mSelectedItem.getId() + "");
                }
                if (inputLayout != null) {
                    inputLayout.setBackgroundResource(R.drawable.pre_bg_shape_select_normal);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinnerInput.setSelection(position);
            }
        }, 400);
    }
}
