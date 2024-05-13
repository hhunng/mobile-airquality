package com.example.airqualityprojectfinish;

import com.google.gson.annotations.SerializedName;

public class AttributeData {
    @SerializedName("fromTimestamp")
    public long fromTimestamp;

    @SerializedName("toTimestamp")
    public long toTimestamp;

    @SerializedName("type")
    public String type;

    public AttributeData(long fromTimestamp, long toTimestamp, String type) {
        this.fromTimestamp = fromTimestamp;
        this.toTimestamp = toTimestamp;
        this.type = type;
    }
}
