package com.tasree7a.enums;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;

import java.io.Serializable;

/**
 * Created by Khalid on 8/1/17.
 * Types of calender, depending on what the calender will be used for
 */

public enum CalenderType implements Serializable{

    DEFAULT, GENERAL_SELECTION;

    public String getFragmentButtonText() {

        return ThisApplication.getCurrentActivity().getString(R.string.CONFIRM);

    }


    public String getFragmentTitle() {

        return ThisApplication.getCurrentActivity().getString(R.string.CALENDAR);

    }

}
