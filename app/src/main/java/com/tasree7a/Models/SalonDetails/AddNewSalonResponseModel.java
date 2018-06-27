package com.tasree7a.models.salondetails;

import com.google.gson.annotations.SerializedName;
import com.tasree7a.enums.ResponseCode;

/**
 * Created by SamiKhleaf on 10/23/17.
 */

public class AddNewSalonResponseModel {


    @SerializedName("response_code")
    ResponseCode responseCode;

    @SerializedName("details")
    CreateSalonResponseDetails details;


    public CreateSalonResponseDetails getDetails() {

        return details;
    }


    public void setDetails(CreateSalonResponseDetails details) {

        this.details = details;
    }


    public ResponseCode getResponseCode() {

        return responseCode;
    }


    public void setResponseCode(ResponseCode responseCode) {

        this.responseCode = responseCode;
    }
}
