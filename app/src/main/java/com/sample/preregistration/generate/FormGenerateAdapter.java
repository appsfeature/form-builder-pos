package com.sample.preregistration.generate;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.interfaces.FieldType;
import com.formbuilder.model.DynamicInputModel;
import com.sample.preregistration.R;

import java.util.HashMap;
import java.util.List;


public class FormGenerateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final AppCallback.OnListClickListener<DynamicInputModel> clickListener;
    private final List<DynamicInputModel> mList;
    private final HashMap<String, String> mFieldType;

    public FormGenerateAdapter(List<DynamicInputModel> mList, AppCallback.OnListClickListener<DynamicInputModel> clickListener) {
        this.mList = mList;
        this.clickListener = clickListener;
        this.mFieldType = getFieldTypeMap();
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
        myViewHolder.tvTitle.setText(mList.get(i).getFieldName());
        myViewHolder.tvSubTitle.setText(mFieldType.get(mList.get(i).getFieldType()));
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle, tvSubTitle;
        private ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvSubTitle = v.findViewById(R.id.tvSubTitle);
            tvSubTitle.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(this);
            v.findViewById(R.id.iv_delete).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() >= 0 && getAdapterPosition() < mList.size()) {
                if (v.getId() == R.id.iv_delete) {
                    clickListener.onDeleteClicked(v, getAdapterPosition(), mList.get(getAdapterPosition()));
                } else {
                    clickListener.onItemClicked(v, getAdapterPosition(), mList.get(getAdapterPosition()));
                }
            }
        }
    }

    public HashMap<String, String> getFieldTypeMap() {
        HashMap<String, String> mList = new HashMap<>();
        mList.put(FieldType.EDIT_TEXT, "Edit Text");
        mList.put(FieldType.TEXT_VIEW, "Text View");
        mList.put(FieldType.SPINNER, "Spinner");
        mList.put(FieldType.RADIO_BUTTON, "Radio Button");
        mList.put(FieldType.CHECK_BOX, "Check Box");
        mList.put(FieldType.DATE_PICKER, "Date Picker");
        mList.put(FieldType.EMPTY_VIEW, "Empty");
        return mList;
    }


}