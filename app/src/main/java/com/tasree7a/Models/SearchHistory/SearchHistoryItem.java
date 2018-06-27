package com.tasree7a.models.searchhistory;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 5/21/17.
 */

public class SearchHistoryItem {

    @SerializedName("display_name")
    String displayName;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
