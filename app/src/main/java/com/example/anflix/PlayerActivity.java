package com.example.anflix;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private Button btnPlay, btnPause, btnReturnToMenu;
    private ImageView ivProfilePhoto;
    private TextView tvUserInfo;

    private String category;
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
        photo = intent.getParcelableExtra("photo");
        category = intent.getStringExtra("category");

        displayUserInfo();
        setupVideoPlayer();
        setupButtons();
    }

    private void displayUserInfo() {
        UserRepository userRepository = new UserRepository(this);
        Cursor cursor = userRepository.getUserData();

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_NAME));
            int age = cursor.getInt(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_AGE));
            String gender = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_GENDER));

            tvUserInfo.setText("Nombre: " + name + "\nEdad: " + age + "\nGénero: " + gender);
            cursor.close();
        }

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
    }

    private void setupButtons() {
        btnPlay.setOnClickListener(v -> {
            videoView.start();
            btnPlay.setEnabled(false);
        });

        btnPause.setOnClickListener(v -> {
            videoView.pause();
            btnPlay.setEnabled(true);
        });

        btnReturnToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
