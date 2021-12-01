package com.cse465.deepcat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderHolder>{
    private ArrayList<FolderInfo> folders;
    private Context folderContx;
    private ItemClickListener listenToClick;

    public FolderAdapter(ArrayList<FolderInfo> folders, Context folderContx, ItemClickListener listen) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.folder_holder, parent, false);
        return new FolderHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final FolderInfo folder = folders.get(position);
        Glide.with(folderContx)
                .load(folder.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.folderPic);

        //setting the number of images
        String text = ""+folder.getFolderName();
        String folderSizeString="("+folder.getCnt()+")";
        holder.folderSize.setText(folderSizeString);
        holder.folderName.setText(text);

        holder.folderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenToClick.onPicClicked(folder.getCat(), folder.getFolderName());
            }
        });
    }
    @Override
    public int getItemCount() {
        return folders.size();
    }
}
