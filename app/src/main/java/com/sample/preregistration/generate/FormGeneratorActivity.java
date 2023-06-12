package com.sample.preregistration.generate;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.model.DynamicInputModel;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.model.entity.PopupEntity;
import com.formbuilder.util.FBUtility;
import com.formbuilder.util.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.sample.preregistration.R;

import java.util.ArrayList;
import java.util.List;


public class FormGeneratorActivity extends AppCompatActivity implements AppCallback.OnClickListener{

    private FormGenerateAdapter adapter;
    private final List<DynamicInputModel> mList = new ArrayList<>();
    private final String title = "Form Generate";
    private View btnAdd;
    private EditText et_form_id, et_form_name, et_form_title, et_form_sub_title, et_form_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_generator2);
        setUpToolBar();
        initView();
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
                    generateJson();
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
                if(mList.size() > position && position >= 0 ) {
                    mList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, mList.size());
                }
            }
        });
        rvList.setAdapter(adapter);
    }

    private void showFormFieldFragment(DynamicInputModel item, int position) {
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

    private void setUpToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!TextUtils.isEmpty(title)) {
                actionBar.setTitle(title);
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

    private void generateJson() {
        FormBuilderModel model = createFormModel();
        String jsonData = GsonParser.toJsonAll(model, new TypeToken<FormBuilderModel>() {
        });
        shareText(this, jsonData);
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
        FormBuilderModel item = new FormBuilderModel();
        item.setFormId(FBUtility.parseInt(et_form_id.getText().toString()));
        item.setFormName(et_form_name.getText().toString());
        item.setTitle(et_form_title.getText().toString());
        item.setSubTitle(et_form_sub_title.getText().toString());
        if(!TextUtils.isEmpty(et_form_button.getText().toString())) {
            item.setButtonText(et_form_button.getText().toString());
        }
        item.setPopup(getPopup());
        item.setInputList(mList);
        return item;
    }

    private PopupEntity mPopup;
    private PopupEntity getPopup() {
        if(mPopup == null) {
            mPopup = new PopupEntity()
                    .setTitle("Thank You!")
                    .setDescription("You will get your updates soon")
                    .setButtonText("Continue");
        }
        return mPopup;
    }
}
