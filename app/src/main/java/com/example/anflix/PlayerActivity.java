package com.example.anflix;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PlayerActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1002;

    private VideoView videoView;
    private Button btnPlay, btnPause, btnReturnToMenu;
    private ImageView ivProfilePhoto;
    private TextView tvUserInfo;

    private String category;
    private String name;
    private int age;
    private String gender;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        videoView = findViewById(R.id.videoView);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnReturnToMenu = findViewById(R.id.btnReturnToMenu);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        name = intent.getStringExtra("name");
        age = intent.getIntExtra("age", 0);
        gender = intent.getStringExtra("gender");
        photo = intent.getParcelableExtra("photo");


        showProfilePhotoDialog();
    }

    private void showProfilePhotoDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Foto de perfil requerida")
                .setMessage("Para ver el video, necesitas tomar una foto de perfil.")
                .setPositiveButton("Aceptar", (dialog, which) -> {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    } else {
                        dispatchTakePictureIntent();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {

                    Intent intent = new Intent(PlayerActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                })
                .create()
                .show();
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
            photo = (Bitmap) extras.get("data");
            displayUserInfo();
            setupVideoPlayer();
            setupButtons();
        }
    }

    private void displayUserInfo() {
        tvUserInfo.setText("Nombre: " + name + "\nEdad: " + age + "\nGénero: " + gender);

        if (photo != null) {
            ivProfilePhoto.setImageBitmap(photo);
        }
    }

    private void setupVideoPlayer() {
        String videoUri = "";

        switch (category) {
            case "horror":
                videoUri = "android.resource://" + getPackageName() + "/" + R.raw.horror_video;
                break;
            case "action":
                videoUri = "android.resource://" + getPackageName() + "/" + R.raw.action_video;
                break;
            case "caricature":
                videoUri = "android.resource://" + getPackageName() + "/" + R.raw.cartoon_video;
                break;
        }

        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.setOnErrorListener((mp, what, extra) -> {
            new AlertDialog.Builder(PlayerActivity.this)
                    .setTitle("Error")
                    .setMessage("No se puede reproducir el video.")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        });
    }

    private void setupButtons() {
        btnPlay.setOnClickListener(v -> {
            videoView.start();
            btnPlay.setEnabled(false);
            btnPause.setEnabled(true);
        });

        btnPause.setOnClickListener(v -> {
            videoView.pause();
            btnPlay.setEnabled(true);
            btnPause.setEnabled(false);
        });

        btnReturnToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {

                new AlertDialog.Builder(this)
                        .setTitle("Permiso denegado")
                        .setMessage("No se puede acceder a la cámara sin el permiso.")
                        .setPositiveButton("OK", (dialog, which) -> finish())
                        .create()
                        .show();
            }
        }
    }
}
