package com.sample.preregistration.generate;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.util.FBConstant;
import com.google.gson.reflect.TypeToken;
import com.sample.preregistration.R;

import java.util.ArrayList;
import java.util.List;


public class FormListActivity extends AppCompatActivity{

    private FormListAdapter adapter;
    private final List<FormBuilderModel> mList = new ArrayList<>();
    private final String title = "All Campaigns";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_list);
        setUpToolBar();
        initView();
    }

    private void initView() {
        RecyclerView rvList = findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new FormListAdapter(mList, new AppCallback.OnListClickListener<FormBuilderModel>() {
            @Override
            public void onItemClicked(View view, int position, FormBuilderModel item) {
                openCampaign(item);
            }

            @Override
            public void onDeleteClicked(View view, int position, FormBuilderModel item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(mList.size() > position && position >= 0 ) {
                                    mList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    adapter.notifyItemRangeChanged(position, mList.size());

                                    ListMaintainer.removeData(view.getContext(), ListMaintainer.KEY_DEFAULT, item.getFormId());
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

        findViewById(R.id.btn_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCampaign(null);
            }
        });

    }

    private void openCampaign(FormBuilderModel item) {
        startActivity(new Intent(this, FormGeneratorActivity.class)
                .putExtra(FBConstant.CATEGORY_PROPERTY, item));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList(getList());
    }

    private List<FormBuilderModel> getList() {
        return ListMaintainer.getList(this, new TypeToken<List<FormBuilderModel>>() {
        });
    }

    private void loadList(List<FormBuilderModel> list) {
//        BaseUtil.showNoData(layoutNoData, View.GONE);
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
//        if (list == null || list.size() <= 0) {
//            BaseUtil.showNoData(layoutNoData, View.VISIBLE);
//        }
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
}

