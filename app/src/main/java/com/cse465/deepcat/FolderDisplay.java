package com.cse465.deepcat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FolderDisplay extends AppCompatActivity implements ItemClickListener {
    RecyclerView folderRecycler;
    ArrayList<FolderInfo> allFolders;
    String folderPath = "";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_display_container);
        CheckPermissionREAD_EXTERNAL_STORAGE(this);
        {
            folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            allFolders = new ArrayList<>();
            folderRecycler = findViewById(R.id.folderRecycler);
            folderRecycler.addItemDecoration(new MarginDecoration(this));
            folderRecycler.hasFixedSize();

            if(allFolders.isEmpty()){
                allFolders = getAllCategories();
                RecyclerView.Adapter folderAdapter = new FolderAdapter(allFolders, FolderDisplay.this, this);
                folderRecycler.setAdapter(folderAdapter);
            }else{

            }
        }
    }

    @Override
    public void onPicClicked(String category, String folderName) {
        MainActivity.currentCat = category;
        startActivity(new Intent(FolderDisplay.this, ImageDisplay.class));
    }

    @Override
    public void onPicClicked(ImageHolder holder, int position, ArrayList<ImageInfo> pics) {
        // nothing here
    }

    public ArrayList<FolderInfo> getAllCategories(){
//        if(MainActivity.allFolders.size()>0)
//        {
//            return MainActivity.allFolders;
//        }
        ArrayList<FolderInfo> folders = new ArrayList<>();
        ArrayList<ImageInfo> images = new ArrayList<>();
        ArrayList<String> imgCat = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();

        //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = FolderDisplay.this.getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, null);
        try {
            cursor.moveToFirst();
            do{
                ImageInfo pic = new ImageInfo();
                pic.setPictureName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                //Log.e("path of image is", pic.getPicturePath());
                try {
                    Bitmap image_bit = BitmapFactory.decodeFile(pic.getPicturePath());
                    String imgClass = MainActivity.getClass(image_bit, allVideosuri);
                    //Log.d("category is: ", imgClass);
                    Integer key = map.get(imgClass);
                    if(key == null || key == 0) {
                        imgCat.add(imgClass);
                        FolderInfo f = new FolderInfo();
                        f.setCat(imgClass);
                        f.setFolderName(imgClass);
                        f.setFirstPic(pic.getPicturePath());
                        f.inc();
                        folders.add(f);
                        map.put(imgClass, folders.size());
                    } else {
                        folders.get(key - 1).inc();
                    }
                    pic.setCat(imgClass);
                    images.add(pic);
                } catch (Exception e) {
                    Log.e("error: ", e.toString());
                }

            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<ImageInfo> reSelection = new ArrayList<>();
            for(int i = images.size()-1; i > -1; i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainActivity.allImg = images;
        MainActivity.allFolders = folders;
        Log.e("folder count: ", ""+folders.size());
        return folders;
    }

    public boolean CheckPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Log.e("error: ", "Doesn't grant storage permission");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}
