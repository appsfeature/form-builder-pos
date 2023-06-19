package com.sample.preregistration.generate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.FormBuilder;
import com.formbuilder.interfaces.FormResponse;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.model.entity.PopupEntity;
import com.formbuilder.util.FBAlertUtil;
import com.formbuilder.util.FBPreferences;
import com.formbuilder.util.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.sample.preregistration.R;

import java.util.List;


public class FormListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final AppCallback.OnListClickListener<FormBuilderModel> clickListener;
    private final List<FormBuilderModel> mList;

    public FormListAdapter(List<FormBuilderModel> mList, AppCallback.OnListClickListener<FormBuilderModel> clickListener) {
        this.mList = mList;
        this.clickListener = clickListener;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_generate_form, parent, false));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {
        ViewHolder myViewHolder = (ViewHolder) holder;
        myViewHolder.tvTitle.setText("Campaign ID - " +mList.get(i).getFormId());
        myViewHolder.tvSubTitle.setText(mList.get(i).getFormName());
        myViewHolder.tvSubTitle.setVisibility(View.VISIBLE);
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle, tvSubTitle;
        private ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvSubTitle = v.findViewById(R.id.tvSubTitle);

            itemView.setOnClickListener(this);
            v.findViewById(R.id.iv_preview).setVisibility(View.VISIBLE);
            v.findViewById(R.id.iv_preview).setOnClickListener(this);
            v.findViewById(R.id.iv_delete).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() >= 0 && getAdapterPosition() < mList.size()) {
                if (v.getId() == R.id.iv_preview) {
                    FBPreferences.setFormSubmitted(v.getContext(), mList.get(getAdapterPosition()).getFormId(), false);
                    String sampleJson = GsonParser.toJsonAll(mList.get(getAdapterPosition()), new TypeToken<FormBuilderModel>(){});
                    FormBuilder.getInstance().openDynamicFormActivity(v.getContext(), sampleJson, new FormResponse.FormSubmitListener() {
                        @Override
                        public void onFormSubmitted(Context activity, Boolean status) {
                            if(!status) {
                                FBAlertUtil.showSuccessDialog(activity, new PopupEntity(v.getContext().getString(R.string.error_message_form_already_submitted_description)));
                            }
                        }
                    });
                } else if (v.getId() == R.id.iv_delete) {
                    clickListener.onDeleteClicked(v, getAdapterPosition(), mList.get(getAdapterPosition()));
                } else {
                    clickListener.onItemClicked(v, getAdapterPosition(), mList.get(getAdapterPosition()));
                }
            }
        }
    }

}