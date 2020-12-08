package com.example.snistpms;

import android.view.View;

public class showpptsdata {
    String mdocname;
    String murl;
    public showpptsdata(String docname,String url){
        mdocname=docname;
        murl=url;
    }
    public String getMurl() {
        return murl;
    }

    public String getMdocname() {
        return mdocname;
    }

    public void setMdocname(String mdocname) {
        this.mdocname = mdocname;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }


}
