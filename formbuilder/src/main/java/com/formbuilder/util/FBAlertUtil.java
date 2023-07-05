package com.formbuilder.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.formbuilder.FormBuilder;
import com.formbuilder.R;
import com.formbuilder.model.entity.PopupEntity;

public class FBAlertUtil {

    public static void showSuccessDialog(Context context, PopupEntity popup) {
        showSuccessDialog(context, popup, false);
    }
    public static void showSuccessDialog(Context context, PopupEntity popup, boolean isFinish) {
        showSuccessDialog(context, popup, isFinish, false);
    }
    public static void showSuccessDialog(Context context, PopupEntity popup, boolean isFinish, boolean isAutoDismiss) {
        if(context instanceof Activity) {
            Handler handler = new Handler();
            Activity activity = (Activity) context;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.pre_alert_success, null);
            builder.setView(dialogView);
            View alertBackground = dialogView.findViewById(R.id.alert_background);
            TextView tvTitle = dialogView.findViewById(R.id.tv_alert_title);
            TextView tvDescription = dialogView.findViewById(R.id.tv_alert_description);
            Button btnAlert = dialogView.findViewById(R.id.btn_alert);
            if (popup != null) {
                if(!TextUtils.isEmpty(popup.getTitle())) {
                    tvTitle.setText(popup.getTitle());
                }
                tvDescription.setText(popup.getDescription());
                if(!TextUtils.isEmpty(popup.getButtonText())) {
                    btnAlert.setText(popup.getButtonText());
                }
            }
            alertBackground.setBackgroundColor(Color.parseColor(FBUtility.getColorValue(activity, "20", R.color.colorPrimary)));
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            btnAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        FormBuilder.getInstance().dispatchOnFormSubmit(activity, true);
                        if(isFinish) {
                            activity.finish();
                        }
                        dialog.dismiss();
                        handler.removeCallbacksAndMessages(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.show();
            if(isAutoDismiss){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (btnAlert != null) {
                            btnAlert.performClick();
                        }
                    }
                }, 5000);
            }
        }else {
            FBUtility.showToastCentre(context, popup.getDescription());
        }
    }
}
