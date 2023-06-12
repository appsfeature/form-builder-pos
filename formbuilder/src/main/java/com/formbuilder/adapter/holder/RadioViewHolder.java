package com.formbuilder.adapter.holder;

import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.R;
import com.formbuilder.adapter.DynamicInputAdapter;
import com.formbuilder.model.DynamicInputModel;
import com.formbuilder.model.entity.MasterEntity;
import com.formbuilder.util.GsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Resource layout
 * R.layout.pre_slot_radio_button
 */
public class RadioViewHolder extends RecyclerView.ViewHolder {
    private final DynamicInputAdapter mAdapter;
    public final TextView tvInputHint;
    private final RadioGroup rgGroup;
    private final View inputLayout;

    public RadioViewHolder(DynamicInputAdapter mAdapter, View view) {
        super(view);
        this.mAdapter = mAdapter;
        inputLayout = view.findViewById(R.id.input_layout);
        tvInputHint = view.findViewById(R.id.tv_input_hint);
        rgGroup = view.findViewById(R.id.rg_radio_group);
    }

    public void setData(DynamicInputModel item) throws Exception {
        if (tvInputHint != null) {
            tvInputHint.setText(item.getFieldName());
        }
        List<MasterEntity> radioList = getRadioList(item.getFieldData());
        if(radioList != null) {
            for (int i = 0; i < radioList.size(); i++) {
                RadioButton button = new RadioButton(mAdapter.context);
                button.setId(i);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        mAdapter.context.getResources().getDimension(R.dimen.form_builder_text_size));
                button.setTextColor(ContextCompat.getColor(mAdapter.context, R.color.form_builder_text_color));
                button.setText(radioList.get(i).getTitle());
                rgGroup.addView(button);
            }
            rgGroup.setVisibility(View.VISIBLE);
        }else {
            rgGroup.setVisibility(View.GONE);
//            textView.setText(item.getFieldName() + "(No data)");
        }
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                item.setInputData(radioButton.getText().toString());
                inputLayout.setBackgroundResource(R.drawable.pre_bg_shape_select_normal);
            }
        });
    }

    public List<MasterEntity> getRadioList(String fieldData) {
        List<MasterEntity> fieldList = null;
        if(fieldData != null) {
            fieldList = GsonParser.fromJson(fieldData, new TypeToken<List<MasterEntity>>() {
            });
        }
        return fieldList;
    }
}
