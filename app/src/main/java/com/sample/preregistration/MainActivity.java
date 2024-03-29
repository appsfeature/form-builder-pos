package com.sample.preregistration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import com.formbuilder.FormBuilder;
import com.formbuilder.interfaces.FieldInputType;
import com.formbuilder.interfaces.FieldType;
import com.formbuilder.interfaces.FormResponse;
import com.formbuilder.interfaces.ValidationCheck;
import com.formbuilder.model.DynamicInputModel;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.model.entity.PopupEntity;
import com.formbuilder.util.FBAlertUtil;
import com.formbuilder.util.FBPreferences;
import com.sample.preregistration.generate.FormListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int SAMPLE_FORM_ID = 1;
    private static final String SAMPLE_FORM_NAME = "form-001";
    private static final String SAMPLE_FORM_TITLE = "Dynamic Form Builder";

    //Validation in all fields
//    private static final String sampleJson = "{\"campId\":1,\"campName\":\"Campaign-001\",\"title\":\"Bonushub Campaign\",\"subTitle\":\"Create all fields dynamic by json or Model structure.\",\"isShowActionbar\":false,\"buttonText\":\"Submit\",\"popup\":{\"buttonText\":\"Continue\",\"description\":\"You will get your updates soon\",\"title\":\"Thank You!\"},\"fieldList\":[{\"fieldName\":\"Name\",\"fieldType\":1,\"inputType\":\"textCapWords\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"name\",\"validation\":\"empty\"},{\"fieldData\":\"[{\\\"id\\\":1,\\\"title\\\":\\\"PCM\\\"},{\\\"id\\\":2,\\\"title\\\":\\\"PCMB\\\"},{\\\"id\\\":3,\\\"title\\\":\\\"Arts\\\"},{\\\"id\\\":4,\\\"title\\\":\\\"Commerce\\\"}]\",\"fieldName\":\"Select Steam\",\"fieldType\":2,\"inputType\":\"text\",\"isSpinnerSelectTitle\":true,\"maxLength\":0,\"paramKey\":\"steam\",\"validation\":\"spinner\"},{\"fieldData\":\"[\\\"Male\\\",\\\"Female\\\",\\\"Trans\\\"]\",\"fieldName\":\"Select Gender\",\"fieldType\":3,\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"gender\",\"validation\":\"radio\"},{\"fieldData\":\"Select Date\",\"fieldName\":\"Date of Birth\",\"fieldType\":5,\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"dob\",\"validation\":\"date\"},{\"fieldName\":\"Personal Detail\",\"fieldType\":0,\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"personal_detail\",\"validation\":\"\"},{\"fieldName\":\"Mobile No\",\"fieldSuggestions\":\"[\\\"9891983694\\\"]\",\"fieldType\":1,\"inputType\":\"phone\",\"isSpinnerSelectTitle\":false,\"maxLength\":10,\"paramKey\":\"mobile\",\"validation\":\"mobile\"},{\"fieldName\":\"Email Id\",\"fieldSuggestions\":\"[\\\"@gmail.com\\\", \\\"@yahoo.com\\\", \\\"@hotmail.com\\\", \\\"@outlook.com\\\"]\",\"fieldType\":1,\"inputType\":\"textEmailAddress\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"email\",\"validation\":\"email\"},{\"fieldName\":\"Address\",\"fieldType\":1,\"inputType\":\"textMultiLine\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"address\",\"validation\":\"\"},{\"fieldName\":\"Subscribe for news updates\",\"fieldType\":4,\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"agree\",\"validation\":\"check_box\"}]}";
    private static final String sampleJson = "{\"formButtonText\":\"Submit\",\"campId\":1,\"campName\":\"Campaign-001\",\"fieldList\":[{\"fieldData\":\"\",\"fieldName\":\"Name\",\"fieldSuggestions\":\"\",\"fieldType\":\"edit_text\",\"inputType\":\"textCapWords\",\"isSpinnerSelectTitle\":false,\"maxLength\":20,\"paramKey\":\"name\",\"validation\":\"alpha_numeric\"},{\"fieldData\":\"[{\\\"id\\\":0,\\\"title\\\":\\\"Delhi\\\"},{\\\"id\\\":1,\\\"title\\\":\\\"Noida\\\"},{\\\"id\\\":2,\\\"title\\\":\\\"Gurugram\\\"},{\\\"id\\\":3,\\\"title\\\":\\\"Haryana\\\"},{\\\"id\\\":4,\\\"title\\\":\\\"Faridabad\\\"}]\",\"fieldName\":\"Select State\",\"fieldSuggestions\":\"\",\"fieldType\":\"spinner\",\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"state\",\"validation\":\"spinner\"},{\"fieldData\":\"[{\\\"id\\\":0,\\\"title\\\":\\\"Male\\\"},{\\\"id\\\":1,\\\"title\\\":\\\"Female\\\"}]\",\"fieldName\":\"Select Gender\",\"fieldSuggestions\":\"\",\"fieldType\":\"radio\",\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"gender\",\"validation\":\"radio\"},{\"fieldData\":\"\",\"fieldName\":\"Select Date \",\"fieldSuggestions\":\"\",\"fieldType\":\"date_picker\",\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"date\",\"validation\":\"date_picker\"},{\"fieldData\":\"\",\"fieldName\":\"Personal Details \",\"fieldSuggestions\":\"\",\"fieldType\":\"text_view\",\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"\",\"validation\":\"\"},{\"fieldData\":\"\",\"fieldName\":\"Mobile No\",\"fieldSuggestions\":\"\",\"fieldType\":\"edit_text\",\"inputType\":\"numberSigned\",\"isSpinnerSelectTitle\":false,\"maxLength\":10,\"paramKey\":\"mobile\",\"validation\":\"mobile\"},{\"fieldData\":\"\",\"fieldName\":\"Email ID \",\"fieldSuggestions\":\"[\\\"@gmail.com\\\",\\\"@yahoo.com\\\"]\",\"fieldType\":\"edit_text\",\"inputType\":\"textEmailAddress\",\"isSpinnerSelectTitle\":false,\"maxLength\":20,\"paramKey\":\"email\",\"validation\":\"email\"},{\"fieldData\":\"\",\"fieldName\":\"Address \",\"fieldSuggestions\":\"\",\"fieldType\":\"edit_text\",\"inputType\":\"textMultiLine\",\"isSpinnerSelectTitle\":false,\"maxLength\":50,\"paramKey\":\"address\",\"validation\":\"empty\"},{\"fieldData\":\"\",\"fieldName\":\"Checked to continue.\",\"fieldSuggestions\":\"\",\"fieldType\":\"check_box\",\"inputType\":\"text\",\"isSpinnerSelectTitle\":false,\"maxLength\":0,\"paramKey\":\"agree\",\"validation\":\"check_box\"}],\"isShowActionbar\":false,\"popup\":{\"buttonText\":\"Continue\",\"description\":\"You will get your updates soon\",\"title\":\"Thank You!\"},\"subTitle\":\"Create all fields dynamic by json or model structure \",\"title\":\"BonusHub Campaign \"}";
    private static final boolean isOpenActivityByJson = true;
    private static final int ADVERTISE_VERSION_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FormBuilder.getInstance()
                .setAppVersionCode(this, BuildConfig.VERSION_CODE)
                .setAdvertiseVersionCode(this, ADVERTISE_VERSION_CODE)
                .setEnableJsonEncode(true)
                .setDebugModeEnabled(BuildConfig.DEBUG);
    }

    @MainThread
    public void onOpenDynamicForm(View view) {
        if(isOpenActivityByJson) {
            FormBuilder.getInstance().openDynamicFormActivity(this, sampleJson, new FormResponse.FormSubmitListener() {
                @Override
                public void onFormSubmitted(Context activity, String message, Boolean status) {
                    if(!status) {
                        if(TextUtils.isEmpty(message)){
                            message = getString(R.string.error_message_form_already_submitted_description);
                        }
                        FBAlertUtil.showSuccessDialog(activity, new PopupEntity(message), false, true);
                    }
                }
            });
        }else {
//            String property = GsonParser.toJsonAll(getCategoryProperty(), new TypeToken<FormBuilderModel>() {});
            FormBuilderModel property = getCategoryProperty();
            FormBuilder.getInstance().openDynamicFormActivity(this, property.getFormId(), property, new FormResponse.FormSubmitListener() {
                @Override
                public void onFormSubmitted(Context activity, String message, Boolean status) {
                    if(!status) {
                        if(TextUtils.isEmpty(message)){
                            message = getString(R.string.error_message_form_already_submitted_description);
                        }
                        FBAlertUtil.showSuccessDialog(activity, new PopupEntity(message), false, true);
                    }
                }
            });
        }
    }

    @MainThread
    public void onClearPreferences(View view) {
        FBPreferences.setFormSubmitted(this, SAMPLE_FORM_ID, false);
    }

    private FormBuilderModel getCategoryProperty() {
        FormBuilderModel item = new FormBuilderModel();
        item.setFormId(SAMPLE_FORM_ID);
        item.setFormName(SAMPLE_FORM_NAME);
        item.setTitle(SAMPLE_FORM_TITLE);
        item.setSubTitle("Create all fields dynamic by json or Model structure.");
        item.setPopup(getPopup());
        item.setInputList(getInputFieldList());
        item.setExtraParams(getExtraParams());
        return item;
    }

    private List<DynamicInputModel> getInputFieldList() {
        List<DynamicInputModel> fieldList = new ArrayList<>();
        DynamicInputModel item;

        item = new DynamicInputModel();
        item.setFieldName("Name");
        item.setParamKey("name");
        item.setValidation(ValidationCheck.EMPTY);
        item.setInputType(FieldInputType.textPersonName);
        item.setFieldType(FieldType.EDIT_TEXT);
        fieldList.add(item);

        item = new DynamicInputModel();
        item.setFieldName("Select Steam");
        item.setParamKey("steam");
        item.setFieldType(FieldType.SPINNER);
        item.setSpinnerSelectTitle(true);
//        item.setValidation(ValidationCheck.SPINNER);
        item.setFieldData("[{\"id\":1,\"title\":\"PCM\"},{\"id\":2,\"title\":\"PCMB\"},{\"id\":3,\"title\":\"Arts\"},{\"id\":4,\"title\":\"Commerce\"}]");
        fieldList.add(item);

        item = new DynamicInputModel();
        item.setFieldName("Select Gender");
        item.setParamKey("gender");
        item.setFieldType(FieldType.RADIO_BUTTON);
        item.setFieldData("[\"Male\",\"Female\"]");
        fieldList.add(item);

        item = new DynamicInputModel();
        item.setFieldName("Date of Birth");
        item.setParamKey("dob");
        item.setFieldType(FieldType.DATE_PICKER);
        item.setFieldData("Select Date");
        fieldList.add(item);

        item = new DynamicInputModel();
        item.setFieldName("Personal Detail");
        item.setParamKey("personal_detail");
        item.setFieldType(FieldType.TEXT_VIEW);
        fieldList.add(item);

        item = new DynamicInputModel();
        item.setFieldName("Mobile No");
        item.setParamKey("mobile");
        item.setInputType(FieldInputType.phone);
        item.setFieldType(FieldType.EDIT_TEXT);
        item.setMaxLength(10);
        item.setFieldSuggestions("[\"9891983694\"]");
        fieldList.add(item);

        item = new DynamicInputModel();
        item.setFieldName("Email Id");
        item.setParamKey("email_id");
        item.setInputType(FieldInputType.textEmailAddress);
        item.setFieldType(FieldType.EDIT_TEXT);
        item.setFieldSuggestions("[\"@gmail.com\", \"@yahoo.com\", \"@hotmail.com\", \"@outlook.com\"]");
        fieldList.add(item);

        item = new DynamicInputModel();
        item.setFieldName("Address");
        item.setParamKey("address");
        item.setInputType(FieldInputType.textMultiLine);
        item.setFieldType(FieldType.EDIT_TEXT);
        fieldList.add(item);

        item = new DynamicInputModel();
        item.setFieldName("Subscribe for news updates");
        item.setParamKey("agree_check_box");
        item.setFieldType(FieldType.CHECK_BOX);
        item.setValidation(ValidationCheck.CHECK_BOX);
        fieldList.add(item);

        return fieldList;
    }

    private PopupEntity getPopup() {
        return new PopupEntity()
                .setTitle("Thank You!")
                .setDescription("You will get your updates soon")
                .setDescriptionSecondary("Campaign already Submitted.")
                .setButtonText("Continue");
    }

    private Map<String, String> getExtraParams() {
        Map<String, String> params = new HashMap<>();
        params.put("app_name", getString(R.string.app_name));
        return params;
    }

    public void onGenerateForm(View view) {
        startActivity(new Intent(this, FormListActivity.class));
    }
}