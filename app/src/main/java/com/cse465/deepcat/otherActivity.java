package com.cse465.deepcat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class otherActivity extends Activity implements View.OnClickListener {
    /** Called when the activity is first created. */

    int image_index = 0;
    private static final int MAX_IMAGE_COUNT = 3;

    private int[] mImageIds = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        Button btnPrevious = (Button)findViewById(R.id.gallery_previous_btn);
        btnPrevious.setOnClickListener(this);
        Button btnNext = (Button)findViewById(R.id.gallery_next_btn);
        btnNext.setOnClickListener(this);

        showImage();

    }

    private void showImage() {

        ImageView imgView = (ImageView) findViewById(R.id.myimage);
        imgView.setImageResource(mImageIds[image_index]);

    }

    public void onClick(View v) {

        switch (v.getId()) {

            case (R.id.gallery_previous_btn):

                image_index--;

                if (image_index == -1) {
                    image_index = MAX_IMAGE_COUNT - 1;
                }

                showImage();

                break;

            case (R.id.gallery_next_btn):

                image_index++;

                if (image_index == MAX_IMAGE_COUNT) {
                    image_index = 0;
                }

                showImage();

                break;

        }

    }
}
