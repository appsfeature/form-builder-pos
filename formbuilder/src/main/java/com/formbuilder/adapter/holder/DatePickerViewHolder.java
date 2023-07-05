package com.formbuilder.adapter.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.formbuilder.R;
import com.formbuilder.adapter.DynamicInputAdapter;
import com.formbuilder.model.DynamicInputModel;
import com.formbuilder.util.DatePickerDialog;
import com.formbuilder.util.FBUtility;

/**
 * Resource layout
 * R.layout.pre_slot_date_picker
 */
public class DatePickerViewHolder extends RecyclerView.ViewHolder {
    private final DynamicInputAdapter mAdapter;
    private final TextView tvDatePicker;
    private final View inputLayout;
    private final TextView tvInputHint;
    private int sDay, sMonth, sYear;

    public DatePickerViewHolder(DynamicInputAdapter mAdapter, View view) {
        super(view);
        this.mAdapter = mAdapter;
        tvDatePicker = view.findViewById(R.id.tv_date_picker);
        tvInputHint = view.findViewById(R.id.tv_input_hint);
        inputLayout = view.findViewById(R.id.input_layout);
        int[] date = DatePickerDialog.initDate(null);
        sDay=date[0];
        sMonth=date[1];
        sYear=date[2];
    }

    public void setData(DynamicInputModel item) throws Exception{
//        tvDatePicker.setText(item.getFieldName());
        inputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FBUtility.hideKeyboard(v.getContext());

                DatePickerDialog.newInstance(mAdapter.context, new DatePickerDialog.DateSelectListener() {
                    @Override
                    public void onSelectDateClick(int day, int month, int year, String ddMMMyy) {
                        sDay = day;
                        sMonth = month;
                        sYear = year;
                        tvDatePicker.setText(ddMMMyy);
                        item.setInputData(DatePickerDialog.getFormattedDate(sDay,sMonth,sYear));
                        inputLayout.setBackgroundResource(R.drawable.pre_bg_shape_select_normal);
                    }
                }, sDay, sMonth, sYear).show();
            }
        });
        if (tvInputHint != null) {
            tvInputHint.setText(item.getFieldName());
        }
    }
}
