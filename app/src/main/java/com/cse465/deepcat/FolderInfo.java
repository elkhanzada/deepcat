package com.cse465.deepcat;

public class FolderInfo {
    private  String path;
    private  String folderName;
    private  String firstPic;

    public FolderInfo(){

    }

    public FolderInfo(String path, String folderName) {
        this.path = path;
        this.folderName = folderName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}
