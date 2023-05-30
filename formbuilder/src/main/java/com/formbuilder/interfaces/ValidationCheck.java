package com.formbuilder.interfaces;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        ValidationCheck.NOT_REQUIRED,
        ValidationCheck.EMPTY,
        ValidationCheck.EMAIL,
        ValidationCheck.MOBILE,
        ValidationCheck.GST_NUMBER,
        ValidationCheck.PIN_CODE,
        ValidationCheck.IFSC_CODE,
        ValidationCheck.ALPHA_NUMERIC,
        ValidationCheck.SPINNER,
        ValidationCheck.CHECK_BOX,
        ValidationCheck.DATE,
        ValidationCheck.RADIO
})
@Retention(RetentionPolicy.SOURCE)
public @interface ValidationCheck {
    String NOT_REQUIRED = "";
    String EMPTY = "empty";
    String EMAIL = "email";
    String MOBILE = "mobile";
    String GST_NUMBER = "gst_number";
    String PIN_CODE = "pin_code";
    String IFSC_CODE = "ifsc_code";
    String ALPHA_NUMERIC = "alpha_numeric";
    String SPINNER = "spinner";
    String CHECK_BOX = "check_box";
    String DATE = "date";
    String RADIO = "radio";
}
