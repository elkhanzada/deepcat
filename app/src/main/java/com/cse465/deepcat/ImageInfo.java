package com.cse465.deepcat;

public class ImageInfo {

    private String pictureName;
    private String picturePath;
    private  String pictureSize;
    private  String imageUri;
    private Boolean selected = false;
    private String cat = "";

    public ImageInfo(){

    }

    public ImageInfo(String pictureName, String picturePath, String pictureSize, String imageUri) {
        this.pictureName = pictureName;
        this.picturePath = picturePath;
        this.pictureSize = pictureSize;
        this.imageUri = imageUri;
    }


    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String picturName) {
        this.pictureName = picturName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPictureSize() {
        return pictureSize;
    }

    public void setPictureSize(String pictureSize) {
        this.pictureSize = pictureSize;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public void setCat(String cat) { this.cat = cat; }

    public String getCat() {return this.cat; }
}
