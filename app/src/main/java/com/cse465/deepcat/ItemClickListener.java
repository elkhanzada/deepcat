package com.cse465.deepcat;

import java.util.ArrayList;

public interface ItemClickListener {
    void onPicClicked(String category, String folderName);
    void onPicClicked(ImageHolder holder, int position, ArrayList<ImageInfo> pics);
}
