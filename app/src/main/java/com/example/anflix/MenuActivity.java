package com.example.anflix;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MenuActivity extends AppCompatActivity {

    private TextView tvWelcomeMessage;
    private LinearLayout categoryCaricature, categoryAction, categoryHorror;
    private UserRepository userRepository;

    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        categoryCaricature = findViewById(R.id.category_caricature);
        categoryAction = findViewById(R.id.category_action);
        categoryHorror = findViewById(R.id.category_horror);

        userRepository = new UserRepository(this);

        displayUserData();
        setupCategoryListeners();
    }

    private void displayUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "Usuario");
        int age = sharedPreferences.getInt("age", 0);

        tvWelcomeMessage.setText("Bienvenido, " + name);


        userRepository.insertUser(name, age, sharedPreferences.getString("gender", ""));

        if (age < 12) {
            categoryCaricature.setVisibility(View.VISIBLE);
        } else if (age >= 12 && age < 18) {
            categoryCaricature.setVisibility(View.VISIBLE);
            categoryAction.setVisibility(View.VISIBLE);
        } else {
            categoryCaricature.setVisibility(View.VISIBLE);
            categoryAction.setVisibility(View.VISIBLE);
            categoryHorror.setVisibility(View.VISIBLE);
        }
    }

    private void setupCategoryListeners() {
        categoryCaricature.setOnClickListener(v -> showProfilePhotoDialog("caricature"));
        categoryAction.setOnClickListener(v -> showProfilePhotoDialog("action"));
        categoryHorror.setOnClickListener(v -> showProfilePhotoDialog("horror"));
    }

    private void showProfilePhotoDialog(String category) {
        new AlertDialog.Builder(this)
                .setTitle("Foto de Perfil")
                .setMessage("Debe tomar una foto de perfil antes de ver el video.")
                .setPositiveButton("Aceptar", (dialog, which) -> requestCameraPermission(category))
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void requestCameraPermission(String category) {
        if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MenuActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera(category);
        }
    }

    private void openCamera(String category) {
        Intent intent = new Intent(MenuActivity.this, CameraActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openCamera(getIntent().getStringExtra("category"));
            }
        }
    }
}
