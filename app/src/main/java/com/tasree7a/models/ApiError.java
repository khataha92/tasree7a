package com.tasree7a.models;

public class ApiError {
    private String mStatusCode;
    private String mErrorMessage;

    public String getmStatusCode() {
        return mStatusCode;
    }

    public ApiError setmStatusCode(String mStatusCode) {
        this.mStatusCode = mStatusCode;
        return this;
    }

    public String getmErrorMessage() {
        if (mErrorMessage.equalsIgnoreCase("Email_Exists")) ;
        return mErrorMessage.equalsIgnoreCase("Email_Exists") ? "Email Already Exists":  "Error registering user";
    }

    public ApiError setmErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
        return this;
    }
}
