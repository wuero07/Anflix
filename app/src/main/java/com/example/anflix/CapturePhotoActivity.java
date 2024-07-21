package com.example.anflix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

public class CapturePhotoActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView ivCapturedPhoto;

    private String name;
    private int age;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);

        ivCapturedPhoto = findViewById(R.id.ivCapturedPhoto);
        Button btnCapturePhoto = findViewById(R.id.btnCapturePhoto);


        btnCapturePhoto.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivCapturedPhoto.setImageBitmap(imageBitmap);


            getUserData();


            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("age", age);
            intent.putExtra("gender", gender);
            intent.putExtra("photo", imageBitmap);
            startActivity(intent);
            finish();
        }
    }

    private void getUserData() {

        name = "John Doe";
        age = 25;
        gender = "Male";
    }
}