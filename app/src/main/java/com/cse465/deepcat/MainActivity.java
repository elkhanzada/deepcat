package com.cse465.deepcat;
/*
* with the help of https://medium.com/codex/android-simple-image-gallery-30c0f00abe64
* */

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import org.tensorflow.lite.Interpreter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static Interpreter interpreter;
    static Map<String, String> imgPath = new HashMap<>();
    static ArrayList<FolderInfo> folders = new ArrayList<>();
    static ArrayList<ImageInfo> images = new ArrayList<>();
    static Map<String, Integer> map = new HashMap<>();
    static List<String> labels;
    static String currentCat;
    String MODEL_FILE = "my_model.tflite";
    String LABEL_FILE = "labels.txt";
    static int INPUT_SIZE = 224;
    static int PIXEL_SIZE = 3;
    ImageView uploaded;
    Button changeViewButton = null;
    static public ArrayList<String> category;

    public static String getClass(Bitmap image_bit, android.net.Uri uri) {
        Float[] results = classify(image_bit);
        int maxIndex = getMaximumIndex(results);
        float maxProb = results[maxIndex];
        String classified = labels.get(maxIndex);
        return classified;
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
    uri -> {
        // Handle the returned Uri
        try {
            Bitmap image_bit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            uploaded.setImageURI(uri);
            Float[] results = classify(image_bit);
            int maxIndex = getMaximumIndex(results);
            float maxProb = results[maxIndex];
            String classified = labels.get(maxIndex);
            Toast.makeText(this, classified, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeViewButton = (Button)findViewById(R.id.mainChangeView);
        changeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FolderDisplay.class));
            }
        });
        try {
            uploaded = findViewById(R.id.image);
            interpreter = new Interpreter(loadModelFile(getAssets()));
            labels = loadLabelList(getAssets());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void upload(View view){
        mGetContent.launch("image/*");
    }
    private static int getMaximumIndex(Float[] results) {
        float max = -1;
        int maxIndex = -1;
        for(int i = 0; i< results.length;i++){
            if(max<results[i]){
                max = results[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        float IMAGE_MEAN = 0;
        float IMAGE_STD = 255.0f;
        ByteBuffer byteBuffer;
        byteBuffer = ByteBuffer.allocateDirect(4  * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[INPUT_SIZE * INPUT_SIZE];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < INPUT_SIZE; ++i) {
            for (int j = 0; j < INPUT_SIZE; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                byteBuffer.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                byteBuffer.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
            }
        }
        return byteBuffer;
    }
    public static Float[] classify(Bitmap image_bit) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image_bit, INPUT_SIZE, INPUT_SIZE, false);
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);
        float [][] temp  = new float[1][labels.size()];
        interpreter.run(byteBuffer, temp);
        Float [] results = new Float[labels.size()];
        for(int i = 0; i<temp[0].length;i++)
            results[i] = temp[0][i];
        return results;
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    private List<String> loadLabelList(AssetManager assetManager) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(LABEL_FILE)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

}