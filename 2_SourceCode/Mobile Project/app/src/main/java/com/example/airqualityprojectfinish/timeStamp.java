package com.example.airqualityprojectfinish;
import com.google.gson.annotations.SerializedName;

public class timeStamp {
    @SerializedName("day")
    public int day;
    @SerializedName("month")
    public int month;
    @SerializedName("year")
    public int year;
    @SerializedName("hour")
    public int hour;
    @SerializedName("minute")
    public int minute;
    public timeStamp(int day, int month, int year, int hour, int minute)
    {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }
    public timeStamp()
    {}
    public  timeStamp (int day, int month, int year)
    {
        this.day = day;
        this.month = month;
        this.year = year;
    }


}
