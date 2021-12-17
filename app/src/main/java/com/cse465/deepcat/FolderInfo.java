package com.cse465.deepcat;

import android.icu.text.CaseMap;

public class FolderInfo {
    private  String cat;
    private  String folderName;
    private  String firstPic;
    private int cnt;

    public FolderInfo(){
        this.cnt = 0;
        this.firstPic = "";
    }

    public FolderInfo(String path, String folderName) {
        this.cat = path;
        this.folderName = folderName;
        this.cnt = 0;
        this.firstPic = "";
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public int getCnt() {return this.cnt;}

    public void inc() {this.cnt ++;}

    public void dec() {this.cnt --;}
}
