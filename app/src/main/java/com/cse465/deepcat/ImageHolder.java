package com.cse465.deepcat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class ImageHolder extends RecyclerView.ViewHolder{

    public ImageView picture;

    ImageHolder(@NonNull View itemView) {
        super(itemView);
        picture = itemView.findViewById(R.id.image);
    }
}
