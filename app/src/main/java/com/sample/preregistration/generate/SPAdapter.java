package com.sample.preregistration.generate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.formbuilder.R;

import java.util.List;

public class SPAdapter extends ArrayAdapter<SPEntity> {

    public SPAdapter(Context context, List<SPEntity> algorithmList) {
        super(context, 0, algorithmList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_simple_spinner_dropdown_item, parent, false);
        }
        TextView textViewName = convertView.findViewById(android.R.id.text1);
        SPEntity currentItem = getItem(position);
        if (currentItem != null) {
            textViewName.setText(currentItem.getTitle());
        }
        return convertView;
    }
}