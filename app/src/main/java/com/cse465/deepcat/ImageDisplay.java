package com.cse465.deepcat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageDisplay extends AppCompatActivity implements ItemClickListener {
    RecyclerView imageRecycler;
    ArrayList<ImageInfo> allpictures;
    ProgressBar load;
    String folderPath = "";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_display_container);
        CheckPermissionREAD_EXTERNAL_STORAGE(this);
        {
            folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

            allpictures = new ArrayList<>();
            imageRecycler = findViewById(R.id.recycler);
            imageRecycler.addItemDecoration(new MarginDecoration(this));
            imageRecycler.hasFixedSize();
            load = findViewById(R.id.loader);

            if(allpictures.isEmpty()){
                load.setVisibility(View.VISIBLE);
                allpictures = getAllImagesByFolder(folderPath);
                    imageRecycler.setAdapter(new ImageAdapter(allpictures,ImageDisplay.this,this));
                load.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPicClicked(String category, String folderName) {
        // nothing
    }

    @Override
    public void onPicClicked(ImageHolder holder, int position, ArrayList<ImageInfo> pics) {
        // nothing
    }

    public ArrayList<ImageInfo> getAllImagesByFolder(String path){
        Log.d("Cat is ", ""+MainActivity.currentCat);
        ArrayList<ImageInfo> images = new ArrayList<>();
        for(int i = 0; i < MainActivity.allImg.size(); ++i)
        {
            if(MainActivity.allImg.get(i).getCat().compareTo(MainActivity.currentCat) == 0)
            {
                images.add(MainActivity.allImg.get(i));
            }
        }
        return images;
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
