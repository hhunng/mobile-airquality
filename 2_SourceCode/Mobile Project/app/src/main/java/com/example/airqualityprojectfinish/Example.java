
package com.example.airqualityprojectfinish;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Example {

    @SerializedName("id")
    public String id;
    @SerializedName("version")
    
    public Integer version;
    @SerializedName("createdOn")
    
    public Long createdOn;
    @SerializedName("name")
    
    public String name;
    @SerializedName("accessPublicRead")
    
    public Boolean accessPublicRead;
    @SerializedName("parentId")
    
    public String parentId;
    @SerializedName("realm")
    
    public String realm;
    @SerializedName("type")
    
    public String type;
    @SerializedName("path")
    
    public List<String> path;
    @SerializedName("attributes")
    public Object attributes;
}
