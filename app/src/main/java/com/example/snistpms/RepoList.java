package com.example.snistpms;

import com.google.gson.annotations.SerializedName;

class RepoList {
    @SerializedName("name")
    private String name;
    public String getRepoName(){return name;}
}
