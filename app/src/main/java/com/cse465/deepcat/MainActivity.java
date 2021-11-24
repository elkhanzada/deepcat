package com.cse465.deepcat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Interpreter interpreter;
    List<String> labels;
    String MODEL_FILE = "my_model.tflite";
    String LABEL_FILE = "labels.txt";
    int INPUT_SIZE = 224;
    int PIXEL_SIZE = 3;
    ImageView uploaded;
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
    });;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    private int getMaximumIndex(Float[] results) {
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

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
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
    private Float[] classify(Bitmap image_bit) {
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