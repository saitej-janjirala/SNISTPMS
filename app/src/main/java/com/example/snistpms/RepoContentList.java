package com.example.snistpms;

import com.google.gson.annotations.SerializedName;

public class RepoContentList {
    @SerializedName("name")
    String name;
    public String getContentname(){
        return name;
    }
    @SerializedName("download_url")
    String download_url;
    public String getContenturl(){
        return download_url;
    }
    @SerializedName("size")
    int size;
    public int getContentsize(){return size;}
    String html_url;
    public String getHtml_url(){return html_url;}

}
