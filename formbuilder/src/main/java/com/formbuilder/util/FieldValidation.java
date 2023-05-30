package com.formbuilder.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.formbuilder.R;
import com.formbuilder.activity.FormBuilderActivity;
import com.formbuilder.interfaces.ValidationCheck;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

/**
 * @author Created by Abhijit on 15-Oct-16.
 * <p>
 * if (!FieldValidation.isEmpty(getContext(), etName, "Invalid product name")) {
 * return;
 * } else if (!FieldValidation.isEmpty(getContext(), etUnit, "Invalid unit code")) {
 * return;
 * } else if (!FieldValidation.isEmpty(getContext(), etCostPrice, "Invalid Purchase price")) {
 * return;
 * } else if (!FieldValidation.isEmpty(getContext(), etSalePrice, "Invalid sale price")) {
 * return;
 * } else if (!FieldValidation.isEmpty(getContext(), etTaxRate, "Invalid tax rate")) {
 * return;
 * }
 */

public class FieldValidation {


    public static boolean isAlpNum(Context context, EditText edittext, boolean requiredREGEX) {
        return isValid(context, edittext, ALPHA_NUM_REGEX, ALP_NUM_MSG, requiredREGEX);
    }


    public static boolean isPANNumber(Context context, EditText edittext, boolean requiredREGEX) {
        return isValid(context, edittext, ALPHA_NUM_REGEX, PAN_MSG, requiredREGEX);
    }

    public static boolean isIFSCCode(Context context, EditText edittext, boolean required) {
        return isValid(context, edittext, IFSC_CODE_REGEX, IFSC_MSG, required);
    }

    public static boolean isGST(Context context, EditText edittext, String errorMsg, boolean required) {
        return isValid(context, edittext, GST_REGEX, errorMsg, required);
    }

    public static boolean isGSTIfAvailable(Context context, EditText edittext, String errorMsg, boolean required) {
        String text = edittext.getText().toString().trim();
        edittext.setError(null);
        // length 0 means there is no text
        if (text.length() == 0) {
            return true;
        }
        return isValid(context, edittext, GST_REGEX, errorMsg, required);
    }

    private static boolean isButton(Context context, Button button, String defaultValue) {
        String validText = button.getText().toString();
        return TextUtils.isEmpty(validText) && validText.equals(defaultValue);
    }

    public static boolean isPassword(Context context, EditText editText, EditText editText2) {
        return hasTextMatched(context, editText, editText2);
    }

    public static boolean isEmpty(Context context, EditText editText) {
        return hasText(context, editText, null);
    }

    public static boolean isEmptyString(Context context, String editText) {
        return isEmpty(context, editText, EMPTY);
    }

    public static boolean isEmpty(Context context, String string, String errorMsg) {
        if (TextUtils.isEmpty(string)) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEmpty(Context context, String string, String errorMsg, CoordinatorLayout coordinatorLayout) {
        if (TextUtils.isEmpty(string)) {
            showSnackBar(coordinatorLayout, errorMsg);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEmpty(Context context, EditText editText, String errorMsg) {
        return hasText(context, editText, null, errorMsg);
    }

    public static boolean isHSNCode(Context context, EditText editText, String errorMsg) {
        return hasHSNNo(context, editText, errorMsg);
    }


    // call this method when you need to check email validation
    public static boolean isEmail(Context context, EditText editText, boolean required) {
        return isValid(context, editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    public static boolean isMobileNumber(Context context, EditText editText, boolean required) {
        return isValid(context, editText, MOBILE_REGEX, MOBILE_MSG, required);
    }

    public static boolean isPinCode(Context context, EditText editText, boolean required) {
        return isValid(context, editText, PIN_CODE_REGEX, PIN_MSG, required);
    }


    public static boolean isName(Context context, EditText editText, boolean required) {
//        return isValid(context, editText, NAME_REGEX, NAME_MSG, required);
        return hasText(context, editText, null, NAME_MSG);
    }


    public static boolean check(Context context, EditText editText, TextInputLayout inputLayout, String validation) {
        switch (validation) {
            case ValidationCheck.NOT_REQUIRED:
                return true;
            case ValidationCheck.EMPTY:
                return hasText(context, editText, inputLayout);
            case ValidationCheck.EMAIL:
                return isValid(context, editText, inputLayout, EMAIL_REGEX, EMAIL_MSG, true);
            case ValidationCheck.MOBILE:
                return isValid(context, editText, inputLayout, MOBILE_REGEX, MOBILE_MSG, true);
            case ValidationCheck.GST_NUMBER:
                return isValid(context, editText, inputLayout, GST_REGEX, INVALID, true);
            case ValidationCheck.PIN_CODE:
                return isValid(context, editText, inputLayout, PIN_CODE_REGEX, PIN_MSG, true);
            case ValidationCheck.IFSC_CODE:
                return isValid(context, editText, inputLayout, IFSC_CODE_REGEX, IFSC_MSG, true);
            case ValidationCheck.ALPHA_NUMERIC:
                return isValid(context, editText, inputLayout, ALPHA_NUM_REGEX, ALP_NUM_MSG, true);
            default:
                return isValid(context, editText, inputLayout, validation, INVALID, true);
        }
    }

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MOBILE_REGEX = "^[4-9][0-9]{9}$";
    //    private static final String GST_REGEX = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
    private static final String GST_REGEX = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9A-Z]{1}[0-9A-Z]{1}[0-9A-Z]{1}$";

    private static final String PIN_CODE_REGEX = "^[0-9]{6}$";
    private static final String IFSC_CODE_REGEX = "^[A-Za-z]{4}[0-9]{7}$";

    private static final String PHONE_REGEX_2 = "^[0-9][0-9]{6,10}$";

    private static final String AGE_REGEX = "^[7-9][0-9]{9}$";
    private static final String NAME_REGEX = "[a-zA-Z ]{1,100}";
    private static final String PRODUCT_REGEX = "^[a-zA-Z0-9 ]+$";
    private static final String ALPHA_NUM_REGEX = "^[a-zA-Z0-9 ]+$";
    //dd-MM-yyyy,dd/MM/yyyy
    private static final String DATE_REGEX = "^(((0[13-9]|1[012])[-/]?(0[1-9]|[12][0-9]|30)|(0[13578]|1[02])[-/]?31|02[-/]?(0[1-9]|1[0-9]|2[0-8]))[-/]?[0-9]{4}|02[-/]?29[-/]?([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00))$";
    //MM-dd-yyyy,MM/dd/yyyy
    private static final String DATE1_REGEX = "^(((0[1-9]|[12][0-9]|30)[-/]?(0[13-9]|1[012])|31[-/]?(0[13578]|1[02])|(0[1-9]|1[0-9]|2[0-8])[-/]?02)[-/]?[0-9]{4}|29[-/]?02[-/]?([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00))$";

    // Error Messages
    private static final String EMAIL_MSG = "Invalid email";
    private static final String PHONE_MSG = "Invalid phone";
    private static final String MOBILE_MSG = "Invalid mobile";
    private static final String PIN_MSG = "Please enter 6 digit pin code";
    private static final String IFSC_MSG = "Please enter valid IFSC code";
    private static final String PAN_MSG = "Please enter valid PAN number";
    private static final String ALP_NUM_MSG = "Please enter alpha numeric value";
    private static final String NAME_MSG = "invalid Name alphabets allow only";
    private static final String EMPTY = "Empty";
    private static final String INVALID = "Invalid";

    // return true if the input field is valid, based on the parameter passed
    private static boolean isValid(Context context, EditText editText, String regex, String errMsg, boolean required) {
        return isValid(context, editText, null, regex, errMsg, required);
    }

    private static boolean isValid(Context context, EditText editText, TextInputLayout inputLayout, String regex, String errMsg, boolean required) {
        try {
            String text = editText.getText().toString().trim();
            // clearing the error, if it was previously set by some other values
            if(inputLayout != null) {
                inputLayout.setError(null);
            }else {
                editText.setError(null);
            }
            editText.setOnTouchListener(null);
            // text required and editText is blank, so return false
            if (!hasText(context, editText, inputLayout)) return false;

            // pattern doesn't match so returning false
            if (required && !Pattern.matches(regex, text)) {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.pre_ic_fail);
                if (drawable != null)
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                if(inputLayout != null) {
                    inputLayout.setError(errMsg);
                }else {
                    editText.setError(errMsg, drawable);
                }
                setEdittextEventListener(editText, inputLayout, errMsg);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    // check the input field has any text or not
    // return true if it contains text otherwise false
    private static boolean hasText(Context context, EditText editText, TextInputLayout inputLayout) {
        return hasText(context, editText, inputLayout, EMPTY);
    }

    private static boolean hasText(Context context, EditText editText, TextInputLayout inputLayout, String errorMessage) {

        String text = editText.getText().toString().trim();
        if(inputLayout != null) {
            inputLayout.setError(null);
        }else {
            editText.setError(null);
        }
        // length 0 means there is no text
        if (text.length() == 0) {

            SpannableString s = new SpannableString(errorMessage);
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
            s.setSpan(new RelativeSizeSpan(1.1f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//            editText.requestFocus();
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.pre_ic_fail);
            if (drawable != null)
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            if(inputLayout != null) {
                inputLayout.setError(errorMessage);
            }else {
                editText.setError(s, drawable);
            }
            setEdittextEventListener(editText, inputLayout, errorMessage);
            return false;
        }
//        Drawable drawable=ContextCompat.getDrawable(context,R.drawable.ic_success);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        editText.setError("done",drawable);
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    private static void setEdittextEventListener(EditText editText, TextInputLayout inputLayout, String errorMessage) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputLayout != null && !TextUtils.isEmpty(inputLayout.getError())) {
                    inputLayout.setError(null);
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (editText.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                        if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(inputLayout != null) {
                                Toast.makeText(editText.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                editText.requestFocus();
                            }else {
                                editText.setError(null);
                                editText.setText("");
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    private static boolean hasTextMatched(Context context, EditText editText, EditText editText2) {

        String text = editText.getText().toString().trim();
        String text2 = editText2.getText().toString().trim();
        editText.setError(null);
        if (text.length() != 0 && text2.length() != 0 && text.equalsIgnoreCase(text2)) {
            return true;
        } else {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.pre_ic_fail);
            if (drawable != null)
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            // length 0 means there is no text
            if (text.length() == 0) {
                editText.setError(getMessage("Invalid Password"), drawable);
                return false;
            } else if (text2.length() == 0) {
                editText2.setError(getMessage("Invalid Confirm Password"), drawable);
                return false;
            } else {
                editText2.setError(getMessage("Password doesn't match"), drawable);
                return false;
            }
        }
    }

    private static SpannableString getMessage(String message) {
        SpannableString s = new SpannableString(message);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.1f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private static boolean hasHSNNo(Context context, EditText editText, String errorMessage) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0 || text.length() < 4) {

            SpannableString s = new SpannableString(errorMessage);
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), 0);
            s.setSpan(new RelativeSizeSpan(1.1f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//            editText.requestFocus();
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.pre_ic_fail);
            if (drawable != null)
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            editText.setError(s, drawable);
            editText.requestFocus();
            return false;
        }

//        Drawable drawable=ContextCompat.getDrawable(context,R.drawable.ic_success);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        editText.setError("done",drawable);
        return true;
    }


    public static boolean isVariable(Context context, String mCustomerId) {
        return !TextUtils.isEmpty(mCustomerId);
    }

    private static void showSnackBar(CoordinatorLayout view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        snackbar.setActionTextColor(Color.WHITE);

        snackbar.show();
    }
}


