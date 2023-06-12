package com.sample.preregistration.generate;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.formbuilder.model.entity.PopupEntity;
import com.sample.preregistration.R;


public class FormDialogUtil {

    public static void showPopup(Context context, PopupEntity item) {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_popup);

        EditText et_field_name = dialog.findViewById(R.id.et_field_name);
        EditText et_description = dialog.findViewById(R.id.et_description);
        EditText et_button_Text = dialog.findViewById(R.id.et_button_Text);

        if(item != null){
            et_field_name.setText(item.getTitle());
            et_description.setText(item.getDescription());
            et_button_Text.setText(item.getButtonText());
        }

        (dialog.findViewById(R.id.btn_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_field_name.getText().toString())){
                    item.setTitle(et_field_name.getText().toString());
                }
                if(!TextUtils.isEmpty(et_description.getText().toString())){
                    item.setDescription(et_description.getText().toString());
                }
                if(!TextUtils.isEmpty(et_button_Text.getText().toString())){
                    item.setButtonText(et_button_Text.getText().toString());
                }
                dialog.cancel();
            }
        });

        (dialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

}
