package com.example.snistpms;

public class showandaddsubjectsdata {
    String reponame;
    showandaddsubjectsdata(String subjectname){
        reponame=subjectname;
    }

    public String getReponame() {
        return reponame;
    }

    public void setReponame(String reponame) {
        this.reponame = reponame;
    }
}
