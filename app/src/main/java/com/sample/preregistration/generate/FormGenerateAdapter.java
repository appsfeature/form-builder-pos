package com.sample.preregistration.generate;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.model.DynamicInputModel;
import com.sample.preregistration.R;

import java.util.List;


public class FormGenerateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final AppCallback.OnListClickListener<DynamicInputModel> clickListener;
    private final List<DynamicInputModel> mList;

    public FormGenerateAdapter(List<DynamicInputModel> mList, AppCallback.OnListClickListener<DynamicInputModel> clickListener) {
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
        myViewHolder.tvTitle.setText(mList.get(i).getFieldName());
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);

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

}