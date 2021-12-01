package com.cse465.deepcat;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FolderHolder extends RecyclerView.ViewHolder{
    public ImageView picture;

    FolderHolder(@NonNull View itemView){
        super(itemView);
        picture = itemView.findViewById(R.id.folderPic);
    }
}
