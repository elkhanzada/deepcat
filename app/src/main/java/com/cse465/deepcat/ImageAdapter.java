package com.cse465.deepcat;

import static androidx.core.view.ViewCompat.setTransitionName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

    private ArrayList<ImageInfo> pictureList;
    private Context pictureContx;
    private final ItemClickListener picListerner;

    /**
     *
     * @param pictureList ArrayList of pictureFacer objects
     * @param pictureContx The Activities Context
     * @param picListerner An interface for listening to clicks on the RecyclerView's items
     */
    public ImageAdapter(ArrayList<ImageInfo> pictureList, Context pictureContx, ItemClickListener picListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = picListerner;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        // layoutInflater will create new layout of a custom xml file
        // https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.pic_holder_item, container, false);
        return new ImageHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ImageInfo image = pictureList.get(position);
        Glide.with(pictureContx)
                .load(image.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        setTransitionName(holder.picture, String.valueOf(position) + "_image");

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picListerner.onPicClicked(holder, position, pictureList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}