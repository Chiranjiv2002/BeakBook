package app.ij.mlwithtensorflowlite.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.io.ByteArrayOutputStream;

import android.media.AudioFormat;
import android.media.AudioRecord;

import com.squareup.picasso.Picasso;

import app.ij.mlwithtensorflowlite.data.Bird;
import app.ij.mlwithtensorflowlite.data.BirdApi;
import app.ij.mlwithtensorflowlite.data.Birds;
import app.ij.mlwithtensorflowlite.data.RetrofitClient;
import app.ij.mlwithtensorflowlite.ml.Model;
import app.ij.mlwithtensorflowlite.ml.Audio;
import app.ij.mlwithtensorflowlite.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IdentifyFragment extends Fragment {
    Button audio, camera, gallery;
    ImageView imageView;
    TextView result;
    TextView accuracyValue;
    LinearLayout linearLayout;
    TextView birdname, sciname, family, region;
    ImageView birdimage;
    int imageSize = 240;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_identify, container, false);
        audio = view.findViewById(R.id.button3);
        camera = view.findViewById(R.id.button);
        gallery = view.findViewById(R.id.button2);
        result = view.findViewById(R.id.result);
        birdname = view.findViewById(R.id.birdName);
        sciname = view.findViewById(R.id.sciName);
        family = view.findViewById(R.id.family);
        region = view.findViewById(R.id.region);
        birdimage = view.findViewById(R.id.birdImage);
        imageView = view.findViewById(R.id.imageView);
        accuracyValue = view.findViewById(R.id.accuracyValue);
        linearLayout = view.findViewById(R.id.results);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    Intent audioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                    startActivityForResult(audioIntent, 2);
                } else {
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Select File Type");
                builder.setItems(new CharSequence[]{"Image", "Audio"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0: // Image
                                Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(imageIntent, 1);
                                break;
                            case 1: // Audio
                                Intent audioIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                audioIntent.setType("audio/*"); // Limit to audio files
                                startActivityForResult(audioIntent, 4);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        return view;
    }
    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(requireContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 240, 240, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            Log.d("MyApp",String.format("%.3f", maxConfidence));
            List<String> classes = this.loadJSONFromAsset();
            result.setText(classes.get(maxPos));
            accuracyValue.setText(String.format("%.3f", maxConfidence));

            linearLayout.setVisibility(View.VISIBLE);
            getData(classes.get(maxPos));
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public void classifyAudio(InputStream  inputStream){
        try {
            Audio audioModel = Audio.newInstance(requireContext());

            TensorBuffer inputFeature0 = extractAudioFeatures(inputStream);

            Audio.Outputs audioOutputs = audioModel.process(inputFeature0);
            TensorBuffer outputFeature0 = audioOutputs.getOutputFeature0AsTensorBuffer();
            float[] probabilities = outputFeature0.getFloatArray();

            // Find the class with the highest probability
            int maxPos = 0;
            float maxProbability = 0;
            for (int i = 0; i < probabilities.length; i++) {
                if (probabilities[i] > maxProbability) {
                    maxProbability = probabilities[i];
                    maxPos = i;
                }
            }

            Map<String, String> classDictionary = loadAudioJSONFromAsset();
            String predictedClassLabel = classDictionary.get(maxPos);
            String predictedClass = (new ArrayList<String>(classDictionary.values())).get(maxPos);
            result.setText(predictedClass);
            accuracyValue.setText(String.format("%.3f", maxProbability));
            linearLayout.setVisibility(View.VISIBLE);
            getData(predictedClass);
            audioModel.close();
        } catch (Exception e) {
            Log.e("classifyAudio", "Error ", e);
        }
    }

    public TensorBuffer extractAudioFeatures(InputStream inputStream) {
        int SAMPLE_RATE = 44100;
        int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
        int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] audioBytes = outputStream.toByteArray();

            // Ensure that the byte buffer is large enough to hold 5120 float values
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(5120 * Float.SIZE / Byte.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());

            // Convert byte array to float array and load into byte buffer
            for (int i = 0; i < 5120; i++) {
                // Convert two bytes to a signed 16-bit integer
                short value = (short) ((audioBytes[2 * i + 1] << 8) | (audioBytes[2 * i] & 0xFF));
                // Convert the integer to a float value between -1 and 1
                float floatValue = value / 32768.0f;
                // Put the float value into the byte buffer
                byteBuffer.putFloat(floatValue);
            }

            // Reset the position of the byte buffer to start
            byteBuffer.rewind();

            // Create a TensorBuffer with the desired shape {1, 5120}
            TensorBuffer tensorBuffer = TensorBuffer.createFixedSize(new int[]{1, 5120}, DataType.FLOAT32);
            tensorBuffer.loadBuffer(byteBuffer);

            return tensorBuffer;
        } catch (IOException e) {
            Log.e("AudioProcessor", "Error reading audio file", e);
        }

        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            // Camera
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                classifyImage(image);
            }
            // Audio
            else if(requestCode == 2 ||requestCode == 4 ){
                Uri audioUri = data.getData();
                InputStream inputStream = null;
                try {
                    inputStream = requireContext().getContentResolver().openInputStream(audioUri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                classifyAudio(inputStream);
            }
            // Gallery
            else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public List<String> loadJSONFromAsset() {
        String json = null;
        List<String> list = new ArrayList<String>();
        try {
            InputStream is = requireContext().getAssets().open("class_names.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray arr = new JSONArray(json);

            for(int i = 0; i < arr.length(); i++){
                list.add(arr.getString(i));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Map<String, String> loadAudioJSONFromAsset() {
        String json = null;
        Map<String, String> dictionary = new HashMap<String, String>();
        try {
            InputStream is = requireContext().getAssets().open("audio_class_names.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray arr = new JSONArray(json);
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                String primary_label = obj.keys().next();
                String common_name = obj.getString(primary_label);
                dictionary.put(primary_label, common_name);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return dictionary;
    }
    public void getData(String name){
        String[] bname = name.split("-");
        String bn;
        if(bname.length == 1){
            bn = bname[0];
        }
        else {
            bn = bname[0].toLowerCase()+" "+bname[1].toLowerCase();
        }
        Log.e("t",bn);
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        BirdApi birdApi = retrofit.create(BirdApi.class);
        birdApi.getBirds(RetrofitClient.apiKey,bn,"true","AND","1","25").enqueue(new Callback<Birds>() {
            @Override
            public void onResponse(Call<Birds> call, Response<Birds> response) {
                if(response.isSuccessful()){
                    Birds birds = response.body();
                    if(!birds.getEntities().isEmpty()){
                        Bird bird = birds.getEntities().get(0);
                        birdname.setText(bird.getName());
                        sciname.setText(bird.getSciName());
                        family.setText(bird.getFamily());
                        region.setText(bird.getRegion().get(0));
                        String url = bird.getImages().get(0);
                        if(!bird.getImages().isEmpty()){
                            Picasso.get().load(url).fit().centerCrop().into(birdimage);
                        }
                    }
                    Log.e("url",call.request().url().toString());
                }
                else {

                    Log.e("Error", "Response code: " + response.code() + ", Error message: " + response.message());
                    try {
                        Log.e("Error", "Raw response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Birds> call, Throwable throwable) {
                Log.e("weeor",throwable.getMessage());
            }
        });
    }
}
