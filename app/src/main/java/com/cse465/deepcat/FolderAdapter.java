package com.cse465.deepcat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderHolder>{
    private ArrayList<FolderInfo> folders;
    private Context folderContx;
    private itemClickListener listenToClick;

    public FolderAdapter(ArrayList<FolderInfo> folders, Context folderContx, itemClickListener listen) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.folder_display_container, parent, false);
        return new FolderHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return 0;
    }
}
