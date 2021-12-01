package com.cse465.deepcat;

import java.util.ArrayList;

public interface itemClickListener {
    void onPicClicked(String category, String folderName);
    void onPicClicked(PicHolder holder, int position, ArrayList<PictureInfo> pics);
}
