package com.formbuilder.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.FormBuilder;
import com.formbuilder.R;
import com.formbuilder.adapter.DynamicInputAdapter;
import com.formbuilder.adapter.holder.SpinnerViewHolder;
import com.formbuilder.interfaces.FieldType;
import com.formbuilder.interfaces.FormResponse;
import com.formbuilder.interfaces.ValidationCheck;
import com.formbuilder.model.DynamicInputModel;
import com.formbuilder.model.FBNetworkModel;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.network.FBNetworkManager;
import com.formbuilder.util.FBAlertUtil;
import com.formbuilder.util.FBConstant;
import com.formbuilder.util.FBPreferences;
import com.formbuilder.util.FBProgressButton;
import com.formbuilder.util.FBUtility;
import com.formbuilder.util.FieldValidation;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class BaseFormBuilderFragment extends Fragment {

    protected final HashMap<String, Integer> positionMap = new HashMap<>();

    protected View layoutNoData;
    protected DynamicInputAdapter adapter;
    protected final List<DynamicInputModel> mList = new ArrayList<>();
    protected FormBuilderModel property;
    protected TextView tvSubTitle;
    protected String title;
    protected FBNetworkManager networkManager;
    protected FBProgressButton btnAction;
    protected RecyclerView mRecyclerView;
    protected Activity activity;
    protected TextView actionBarTitle;
    protected ImageView ivBack;
    protected View layoutActionBar;
    private NestedScrollView nestedScrollView;

    @LayoutRes
    public abstract int getLayoutContentView();
    public abstract void onInitViews(View view);
    public abstract void onLoadData();
    public abstract boolean onValidationCheck();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FormBuilder.getInstance().setSyncSignupFormListener(new FormResponse.SyncSignupForm() {
            @Override
            public void onSyncSignupForm() {
                if(isVisible() && mRecyclerView != null) {
                    loadData();
                    onLoadData();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutContentView(), container, false);
        activity = getActivity();
        initUI(v);
        return v;
    }

    private void initUI(View v) {
        getIntentData();
        updatePositionMap();
        onInitViews(v);
        initView(v);

        loadData();
        onLoadData();
    }

    private void loadData() {
        if (!TextUtils.isEmpty(property.getSubTitle())) {
            tvSubTitle.setText(property.getSubTitle());
            tvSubTitle.setVisibility(View.VISIBLE);
        } else {
            tvSubTitle.setVisibility(View.GONE);
        }
        if(property.isShowActionbar()){
            actionBarTitle.setText(title);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity != null) {
                        activity.onBackPressed();
                    }
                }
            });
            layoutActionBar.setVisibility(View.VISIBLE);
        }else {
            layoutActionBar.setVisibility(View.GONE);
        }

        btnAction.setText(property.getButtonText());

        loadList(property.getInputList());
    }


    private void getIntentData() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable(FBConstant.CATEGORY_PROPERTY) instanceof FormBuilderModel) {
            property = (FormBuilderModel) bundle.getSerializable(FBConstant.CATEGORY_PROPERTY);
            title = property.getTitle();
            networkManager = new FBNetworkManager(activity);
        } else {
            FBUtility.showPropertyError(activity);
        }
    }


    private void initView(View v) {
        layoutNoData = v.findViewById(R.id.ll_no_data);
        tvSubTitle = v.findViewById(R.id.tv_sub_title);
        layoutActionBar = v.findViewById(R.id.layout_action_bar);
        nestedScrollView = v.findViewById(R.id.nested_scroll_view);
        ivBack = v.findViewById(R.id.iv_action_back);
        actionBarTitle = v.findViewById(R.id.tv_title);
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new DynamicInputAdapter(activity, mList);
        mRecyclerView.setAdapter(adapter);

        btnAction = FBProgressButton.newInstance(activity, v)
                .setText(FBConstant.DEFAULT_BUTTON_TEXT)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isValidationCheck()){
                            FBUtility.showToastCentre(v.getContext(), getString(R.string.error_message_form_validation_check));
                            return;
                        }
                        if(!onValidationCheck()){
                            return;
                        }
                        FBUtility.hideKeyboard(activity);
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (nestedScrollView != null) {
                                    nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                                }
                            }
                        }, 200);
                        submitRequest();
                    }
                });
    }

    private boolean isValidationCheck() {
        boolean isValidAllFields = true;
        if(mRecyclerView != null && mRecyclerView.getLayoutManager() != null) {
            for (int i = 0; i < mList.size(); i++){
                if(!mList.get(i).getValidation().equals(ValidationCheck.NOT_REQUIRED)) {
                    switch (mList.get(i).getValidation()) {
                        case ValidationCheck.SPINNER: {
                            View mRecycleView = mRecyclerView.getLayoutManager().getChildAt(i);
                            if (mRecycleView != null && mRecycleView.findViewById(R.id.input_layout) instanceof LinearLayout) {
                                LinearLayout inputLayout = mRecycleView.findViewById(R.id.input_layout);
                                if (TextUtils.isEmpty(mList.get(i).getInputData())
                                        || (mList.get(i).isSpinnerSelectTitle() ? mList.get(i).getInputData().equals("") : mList.get(i).getInputData().equals("0"))) {
                                    inputLayout.setBackgroundResource(R.drawable.pre_bg_shape_select_wrong);
                                    isValidAllFields = false;
                                } else {
                                    inputLayout.setBackgroundResource(R.drawable.pre_bg_shape_select_normal);
                                }
                            }
                            break;
                        }
                        case ValidationCheck.DATE:
                        case ValidationCheck.RADIO: {
                            View mRecycleView = mRecyclerView.getLayoutManager().getChildAt(i);
                            if (mRecycleView != null && mRecycleView.findViewById(R.id.input_layout) instanceof LinearLayout) {
                                LinearLayout inputLayout = mRecycleView.findViewById(R.id.input_layout);
                                if (TextUtils.isEmpty(mList.get(i).getInputData())) {
                                    inputLayout.setBackgroundResource(R.drawable.pre_bg_shape_select_wrong);
                                    isValidAllFields = false;
                                } else {
                                    inputLayout.setBackgroundResource(R.drawable.pre_bg_shape_select_normal);
                                }
                            }
                            break;
                        }
                        case ValidationCheck.CHECK_BOX: {
                            View mRecycleView = mRecyclerView.getLayoutManager().getChildAt(i);
                            if (mRecycleView != null && mRecycleView.findViewById(R.id.cb_check_box) instanceof CheckBox) {
                                CheckBox checkBox = mRecycleView.findViewById(R.id.cb_check_box);
                                if (checkBox != null && !checkBox.isChecked()) {
                                    checkBox.setTextColor(ContextCompat.getColor(checkBox.getContext(), R.color.fb_color_wrong));
                                    isValidAllFields = false;
                                }else {
                                    checkBox.setTextColor(ContextCompat.getColor(checkBox.getContext(), R.color.form_builder_text_color));
                                }
                            }
                            break;
                        }
                        default: {
                            View mRecycleView = mRecyclerView.getLayoutManager().getChildAt(i);
                            if (mRecycleView != null && mRecycleView.findViewById(R.id.et_input_text) instanceof EditText) {
                                TextInputLayout editTextLayout = mRecycleView.findViewById(R.id.et_input_layout);
                                EditText editText = mRecycleView.findViewById(R.id.et_input_text);
                                if (editText != null) {
                                    boolean status = FieldValidation.check(activity, editText, editTextLayout, mList.get(i).getValidation());
                                    if (!status) {
                                        isValidAllFields = status;
                                    }
                                }
                            } else if (mList.get(i).getValidation().equals(ValidationCheck.EMPTY)) {
                                if (TextUtils.isEmpty(mList.get(i).getInputData())) {
                                    SpinnerViewHolder.showValidationError(activity, mList.get(i).getFieldName());
                                    isValidAllFields = false;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return isValidAllFields;
    }

    private void loadList(List<DynamicInputModel> list) {
        FBUtility.showNoData(layoutNoData, View.GONE);
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        if (list == null || list.size() <= 0) {
            FBUtility.showNoData(layoutNoData, View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void submitRequest() {
        if (btnAction != null) {
            if(FormBuilder.getInstance().getProgressListener() != null){
                FormBuilder.getInstance().getProgressListener().onStartProgressBar();
                requestFormSubmit();
            }else {
                btnAction.startProgress(new FBProgressButton.Listener() {
                    @Override
                    public void onAnimationCompleted() {
                        requestFormSubmit();
                    }
                });
            }
        }
    }

    private void requestFormSubmit() {
        if (networkManager != null && property != null) {
            networkManager.submitForm(requireContext(), property, mList, new FormResponse.Callback<FBNetworkModel>() {
                @Override
                public void onSuccess(FBNetworkModel response) {
                    if (response != null && response.isStatus()) {
                        FBPreferences.setFormSubmitted(activity, property.getFormId(), true);
                        if(FormBuilder.getInstance().getProgressListener() != null){
                            FormBuilder.getInstance().getProgressListener().onStopProgressBar();
                            FBAlertUtil.showSuccessDialog(activity, property.getPopup(), true);
                        }else {
                            btnAction.revertSuccessProgress(new FBProgressButton.Listener() {
                                @Override
                                public void onAnimationCompleted() {
                                    FBAlertUtil.showSuccessDialog(activity, property.getPopup(), true);
                                }
                            });
                        }
                    } else {
                        if(FormBuilder.getInstance().getProgressListener() != null){
                            FormBuilder.getInstance().getProgressListener().onStopProgressBar();
                        }else{
                            btnAction.revertProgress();
                        }
                        FBUtility.showToastCentre(activity, FBConstant.Error.MSG_ERROR);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    if(FormBuilder.getInstance().getProgressListener() != null){
                        FormBuilder.getInstance().getProgressListener().onStopProgressBar();
                    }else{
                        btnAction.revertProgress();
                    }
                    FBUtility.showToastCentre(activity, FBConstant.Error.MSG_ERROR);
                }
            });
        }
    }


    protected void updateInputFieldList(String fieldName, String inputData) {
        if(mList.size() > 0) {
            mList.get(getFieldPosition(fieldName)).setInputData(inputData);
        }
    }

    protected DynamicInputModel getInputField(String fieldName) {
        if(mList.size() > 0) {
            return mList.get(getFieldPosition(fieldName));
        }else {
            return new DynamicInputModel();
        }
    }

    protected int getFieldPosition(String keyword) {
        return positionMap.get(keyword) == null ? 0 : positionMap.get(keyword);
    }

    private void updatePositionMap() {
        if(property.getInputList() != null && property.getInputList().size() > 0) {
            int index = 0;
            for (DynamicInputModel item : property.getInputList()) {
                if (item.getFieldType() == FieldType.EMPTY_VIEW) {
                    positionMap.put(item.getParamKey(), index);
                }
                index++;
            }
        }
    }

    private Handler handler;

    private Handler getHandler() {
        if(handler == null) handler = new Handler();
        return handler;
    }
}