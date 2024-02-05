package com.sample.preregistration.generate;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.model.DynamicInputModel;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.model.entity.PopupEntity;
import com.formbuilder.util.FBConstant;
import com.formbuilder.util.FBUtility;
import com.formbuilder.util.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.sample.preregistration.R;
import com.sample.preregistration.model.ExportModel;

import java.util.ArrayList;
import java.util.List;


public class FormGeneratorActivity extends AppCompatActivity implements AppCallback.OnClickListener{

    private FormGenerateAdapter adapter;
    private final List<DynamicInputModel> mList = new ArrayList<>();
    private String title = "Campaign";
    private View btnAdd;
    private EditText et_form_id, et_form_name, et_form_title, et_form_sub_title, et_form_button;
    private FormBuilderModel mProperty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_generator2);
        setUpToolBar();
        initArguments();
        initView();

        loadData();
    }

    private void initArguments() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getSerializable(FBConstant.CATEGORY_PROPERTY) instanceof FormBuilderModel) {
            mProperty = (FormBuilderModel) bundle.getSerializable(FBConstant.CATEGORY_PROPERTY);
            title = "Edit Campaign";
        }else {
            mProperty = new FormBuilderModel();
            title = "New Campaign";
        }
        updateTitle(title);
    }
    private void loadData() {
        if(mProperty != null){
            if(mProperty.getFormId() > 0) {
                et_form_id.setText(mProperty.getFormId() + "");
            }
            et_form_name.setText(mProperty.getFormName());
            et_form_title.setText(mProperty.getTitle());
            et_form_sub_title.setText(mProperty.getSubTitle());
            et_form_button.setText(mProperty.getButtonText());

            loadList(mProperty.getInputList());
        }
    }

    private void initView() {
        btnAdd = findViewById(R.id.btn_add);
        et_form_id = findViewById(R.id.et_form_id);
        et_form_name = findViewById(R.id.et_form_name);
        et_form_title = findViewById(R.id.et_form_title);
        et_form_sub_title = findViewById(R.id.et_form_sub_title);
        et_form_button = findViewById(R.id.et_form_button);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormFieldFragment(null, -1);
            }
        });
        findViewById(R.id.btn_popup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormDialogUtil.showPopup(view.getContext(), getPopup());
            }
        });
        findViewById(R.id.btn_generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(et_form_id.getText()) && !TextUtils.isEmpty(et_form_name.getText()) && mList.size() > 0){
                    generateJson(true);
                }else {
                    Toast.makeText(FormGeneratorActivity.this, "Invalid Form", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(et_form_id.getText()) && !TextUtils.isEmpty(et_form_name.getText()) && mList.size() > 0){
                    generateJson(false);
                    Toast.makeText(FormGeneratorActivity.this, "Saved Successful.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(FormGeneratorActivity.this, "Invalid Form", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RecyclerView rvList = findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new FormGenerateAdapter(mList, new AppCallback.OnListClickListener<DynamicInputModel>() {
            @Override
            public void onItemClicked(View view, int position, DynamicInputModel item) {
                showFormFieldFragment(item, position);
            }

            @Override
            public void onDeleteClicked(View view, int position, DynamicInputModel item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(mList.size() > position && position >= 0 ) {
                                    mList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    adapter.notifyItemRangeChanged(position, mList.size());
                                }
                                dialog.dismiss();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        rvList.setAdapter(adapter);
    }

    private void showFormFieldFragment(DynamicInputModel item, int position) {
        updateTitle("Add Field");
        Fragment fragment = FormFieldFragment.newInstance(item, position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onItemClicked(View view, int position, DynamicInputModel item) {
        updateList(item, position);
    }

    private void updateList(DynamicInputModel item, int position) {
        if(position < 0) {
            mList.add(item);
        }else {
            mList.set(position, item);
        }
        adapter.notifyDataSetChanged();
    }

    private void loadList(List<DynamicInputModel> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        adapter.notifyDataSetChanged();
    }

    private void setUpToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!TextUtils.isEmpty(title)) {
                actionBar.setTitle(title);
            }
        }
    }

    private void updateTitle(String mTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (!TextUtils.isEmpty(mTitle)) {
                actionBar.setTitle(mTitle);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home ){
            onBackPressed();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateTitle(title);
    }

    private void generateJson(boolean isShare) {
        FormBuilderModel model = createFormModel();
        ListMaintainer.saveData(this, model, model.getFormId());

        if(isShare) {
            String actionText = GsonParser.toJsonAll(model, new TypeToken<FormBuilderModel>() {
            });
            ExportModel shareModel = new ExportModel();
            shareModel.setActionText(actionText);
            String jsonData = GsonParser.toJsonAll(shareModel, new TypeToken<ExportModel>() {
            });
            shareText(this, jsonData);
        }else {
            finish();
        }
    }

    public static void shareText(Context context, String text) {
        if(context != null && text != null) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        }
    }

    private FormBuilderModel createFormModel() {
        mProperty.setFormId(FBUtility.parseInt(et_form_id.getText().toString()));
        mProperty.setFormName(et_form_name.getText().toString().trim());
        mProperty.setTitle(et_form_title.getText().toString().trim());
        mProperty.setSubTitle(et_form_sub_title.getText().toString().trim());
        if(!TextUtils.isEmpty(et_form_button.getText().toString())) {
            mProperty.setButtonText(et_form_button.getText().toString().trim());
        }
        mProperty.setPopup(getPopup());
        mProperty.setInputList(mList);
        return mProperty;
    }

    private PopupEntity getPopup() {
        if(mProperty.getPopup() == null) {
            mProperty.setPopup(new PopupEntity()
                    .setTitle("Thank You!")
                    .setDescription("You will get your updates soon")
                    .setButtonText("Continue"));
        }
        return mProperty.getPopup();
    }
}
