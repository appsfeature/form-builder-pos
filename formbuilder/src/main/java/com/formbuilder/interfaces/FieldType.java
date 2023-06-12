package com.formbuilder.interfaces;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        FieldType.TEXT_VIEW,
        FieldType.EDIT_TEXT,
        FieldType.SPINNER,
        FieldType.RADIO_BUTTON,
        FieldType.CHECK_BOX,
        FieldType.DATE_PICKER,
        FieldType.EMPTY_VIEW,
})
@Retention(RetentionPolicy.SOURCE)
public @interface FieldType {
    String TEXT_VIEW = "text_view"; //0
    String EDIT_TEXT = "edit_text"; //1
    String SPINNER = "spinner"; //2
    String RADIO_BUTTON = "radio"; //3
    String CHECK_BOX = "check_box"; //4
    String DATE_PICKER = "date_picker"; //5
    String EMPTY_VIEW = "empty"; //6
}
//public @interface FieldType {
//    int TEXT_VIEW = 0;
//    int EDIT_TEXT = 1;
//    int SPINNER = 2;
//    int RADIO_BUTTON = 3;
//    int CHECK_BOX = 4;
//    int DATE_PICKER = 5;
//    int EMPTY_VIEW = 6;
//}
