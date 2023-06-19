package com.sample.preregistration.generate;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.formbuilder.interfaces.FieldInputType;
import com.formbuilder.interfaces.FieldType;
import com.formbuilder.interfaces.ValidationCheck;
import com.formbuilder.model.DynamicInputModel;
import com.formbuilder.model.entity.MasterEntity;
import com.formbuilder.util.FBUtility;
import com.formbuilder.util.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.sample.preregistration.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FormFieldFragment extends Fragment {

    private DynamicInputModel item;
    private int position;
    DynamicInputModel finalResult = new DynamicInputModel();
    private AppCallback.OnClickListener mListener;

    public static FormFieldFragment newInstance(DynamicInputModel item, int position) {
        FormFieldFragment fragment = new FormFieldFragment();
        fragment.item = item;
        fragment.position = position;
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCallback.OnClickListener) {
            mListener = (AppCallback.OnClickListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return initView(inflater.inflate(R.layout.dialog_form_field, container, false));
    }

    private View initView(View view) {
        if (view != null ) {
            EditText et_field_name = view.findViewById(R.id.et_field_name);
            EditText et_form_param = view.findViewById(R.id.et_form_param);
            EditText et_form_max_length = view.findViewById(R.id.et_form_max_length);
            EditText et_form_suggestions = view.findViewById(R.id.et_form_suggestions);
            EditText et_form_field_data = view.findViewById(R.id.et_form_field_data);

            View il_form_field_data = view.findViewById(R.id.il_form_field_data);
            View il_form_suggestions = view.findViewById(R.id.il_form_suggestions);

            View ll_sp_field_type = view.findViewById(R.id.ll_sp_field_type);
            View ll_sp_input_type = view.findViewById(R.id.ll_sp_input_type);
            View ll_sp_validation = view.findViewById(R.id.ll_sp_validation);

            Spinner sp_field_type = view.findViewById(R.id.sp_field_type);
            Spinner sp_input_type = view.findViewById(R.id.sp_input_type);
            Spinner sp_validation = view.findViewById(R.id.sp_validation);

            List<SPEntity> mFieldTypeList = getFieldTypeList();
            List<SPEntity> mInputTypeList = getInputTypeList();
            List<SPEntity> mValidationList = getValidationList();

            int mFieldTypePos = 0, mItemTypePos = 0, mValidationPos =0;
            if(item != null){
                et_field_name.setText(item.getFieldName());
                et_form_param.setText(item.getParamKey());
                et_form_max_length.setText(item.getMaxLength() + "");


                et_form_field_data.setText(getValidFormDataValue(item.getFieldData()));
                et_form_suggestions.setText(getValidSuggestionsValue(item.getFieldSuggestions()));

                mFieldTypePos = getListPosition(mFieldTypeList, item.getFieldType());
                mItemTypePos = getListPosition(mInputTypeList, item.getInputType());
                mValidationPos = getListPosition(mValidationList, item.getValidation());
            }

            new SpinnerHolder(ll_sp_field_type, sp_field_type).setData(requireContext(), mFieldTypeList, mFieldTypePos, new AppCallback.Status<String>() {
                @Override
                public void onSuccess(String response) {
                    if(response.equalsIgnoreCase(FieldType.SPINNER) || response.equalsIgnoreCase(FieldType.RADIO_BUTTON)){
                        il_form_field_data.setVisibility(View.VISIBLE);
                    }else {
                        il_form_field_data.setVisibility(View.GONE);
                    }
                    if(response.equalsIgnoreCase(FieldType.EDIT_TEXT)){
                        il_form_suggestions.setVisibility(View.VISIBLE);
                        ll_sp_input_type.setVisibility(View.VISIBLE);
                    }else {
                        il_form_suggestions.setVisibility(View.GONE);
                        ll_sp_input_type.setVisibility(View.GONE);
                    }

                    finalResult.setFieldType(response);
                }
            });
            new SpinnerHolder(ll_sp_input_type, sp_input_type).setData(requireContext(), mInputTypeList, mItemTypePos, new AppCallback.Status<String>() {
                @Override
                public void onSuccess(String response) {
                    finalResult.setInputType(response);
                }
            });
            new SpinnerHolder(ll_sp_validation, sp_validation).setData(requireContext(), mValidationList, mValidationPos, new AppCallback.Status<String>() {
                @Override
                public void onSuccess(String response) {
                    finalResult.setValidation(response);
                }
            });

            (view.findViewById(R.id.btn_done)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalResult.setFieldName(et_field_name.getText().toString().trim());
                    finalResult.setParamKey(et_form_param.getText().toString().trim());
                    finalResult.setFieldData(getValidFormData(et_form_field_data.getText().toString()));
                    finalResult.setFieldSuggestions(getValidSuggestions(et_form_suggestions.getText().toString()));
                    finalResult.setMaxLength(FBUtility.parseInt(et_form_max_length.getText().toString()));
                    mListener.onItemClicked(v, position, finalResult);
                    requireActivity().onBackPressed();
                }
            });

            (view.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().onBackPressed();
                }
            });
        }
        return view;
    }

    private String getValidSuggestions(String value) {
        if(!TextUtils.isEmpty(value)){
            String[] arrays = value.split(",");
            for (int i = 0; i < arrays.length; i++) {
                arrays[i] = arrays[i].trim();
            }
            return GsonParser.toJsonAll(arrays, new TypeToken<String[]>(){});
        }
        return null;
    }

    private String getValidSuggestionsValue(String value) {
        String[] array = GsonParser.fromJsonAll(value, new TypeToken<String[]>() {
        });
        if(array != null && array.length > 0){
            return TextUtils.join(",", array);
        }
        return value;
    }

    private String getValidFormData(String value) {
        List<MasterEntity> mList = new ArrayList<>();
        if(!TextUtils.isEmpty(value)){
            String[] arrays = value.split(",");
            for (int i = 0; i < arrays.length; i++) {
                mList.add(new MasterEntity(i, arrays[i].trim()));
            }
            return GsonParser.toJsonAll(mList, new TypeToken<List<MasterEntity>>(){});
        }
        return value;
    }

    private String getValidFormDataValue(String value) {
        List<MasterEntity> mList = GsonParser.fromJsonAll(value, new TypeToken<List<MasterEntity>>() {
        });
        if(mList != null && mList.size() > 0){
            String[] array = new String[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                array[i] = mList.get(i).getTitle();
            }
            return TextUtils.join(",", array);
        }
        return value;
    }


    private static List<SPEntity> getValidationList() {
        List<SPEntity> mList = new ArrayList<>();
        mList.add(new SPEntity(ValidationCheck.NOT_REQUIRED, "Not Required"));
        mList.add(new SPEntity(ValidationCheck.EMPTY, "Empty"));
        mList.add(new SPEntity(ValidationCheck.EMAIL, "Email"));
        mList.add(new SPEntity(ValidationCheck.MOBILE, "Mobile"));
        mList.add(new SPEntity(ValidationCheck.GST_NUMBER, "GST Number"));
        mList.add(new SPEntity(ValidationCheck.PIN_CODE, "PIN Code"));
        mList.add(new SPEntity(ValidationCheck.IFSC_CODE, "IFSC Code"));
        mList.add(new SPEntity(ValidationCheck.ALPHA_NUMERIC, "Alpha Numeric"));
        mList.add(new SPEntity(ValidationCheck.SPINNER, "Spinner"));
        mList.add(new SPEntity(ValidationCheck.CHECK_BOX, "Check Box"));
        mList.add(new SPEntity(ValidationCheck.DATE, "Date"));
        mList.add(new SPEntity(ValidationCheck.RADIO, "Radio"));
        return mList;
    }

    private static List<SPEntity> getInputTypeList() {
        List<SPEntity> mList = new ArrayList<>();
        mList.add(new SPEntity(FieldInputType.text, "text"));
        mList.add(new SPEntity(FieldInputType.number, "number"));
        mList.add(new SPEntity(FieldInputType.textPersonName, "textPersonName"));
        mList.add(new SPEntity(FieldInputType.textEmailAddress, "textEmailAddress"));
        mList.add(new SPEntity(FieldInputType.numberSigned, "numberSigned"));
        mList.add(new SPEntity(FieldInputType.phone, "phone"));
        mList.add(new SPEntity(FieldInputType.textMultiLine, "textMultiLine"));
        mList.add(new SPEntity(FieldInputType.textCapWords, "textCapWords"));
        mList.add(new SPEntity(FieldInputType.textCapCharacters, "textCapCharacters"));
        return mList;
    }

    private static List<SPEntity> getFieldTypeList() {
        List<SPEntity> mList = new ArrayList<>();
        mList.add(new SPEntity(FieldType.EDIT_TEXT, "Edit Text"));
        mList.add(new SPEntity(FieldType.TEXT_VIEW, "Text View"));
        mList.add(new SPEntity(FieldType.SPINNER, "Spinner"));
        mList.add(new SPEntity(FieldType.RADIO_BUTTON, "Radio Button"));
        mList.add(new SPEntity(FieldType.CHECK_BOX, "Check Box"));
        mList.add(new SPEntity(FieldType.DATE_PICKER, "Date Picker"));
        mList.add(new SPEntity(FieldType.EMPTY_VIEW, "Empty"));
        return mList;
    }

    private static int getListPosition(List<SPEntity> list, String value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }
}
