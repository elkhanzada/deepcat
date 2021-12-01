package com.cse465.deepcat;

import java.util.ArrayList;

public interface itemClickListener {
    void onPicClicked(PicHolder holder, int position, ArrayList<PictureInfo> pics);
}
