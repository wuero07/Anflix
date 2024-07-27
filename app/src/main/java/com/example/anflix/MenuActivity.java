package com.example.anflix;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button btnChangeUser;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        categoryCaricature = findViewById(R.id.category_caricature);
        categoryAction = findViewById(R.id.category_action);
        categoryHorror = findViewById(R.id.category_horror);
        btnChangeUser = findViewById(R.id.btnChangeUser);

        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        displayUserData();
        setupCategoryListeners();
        setupChangeUserListener();
    }

    private void displayUserData() {
        String userKey = sharedPreferences.getString("current_user_key", null);
        if (userKey == null) {
            tvWelcomeMessage.setText("Bienvenido, Usuario");
            return;
        }

        String name = sharedPreferences.getString(userKey + "_name", "Usuario");
        int age = sharedPreferences.getInt(userKey + "_age", 0);
        String gender = sharedPreferences.getString(userKey + "_gender", "");
        String photoPath = sharedPreferences.getString(userKey + "_photo", null);

        tvWelcomeMessage.setText("Bienvenido, " + name);

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
        categoryCaricature.setOnClickListener(v -> openPlayerActivity("caricature"));
        categoryAction.setOnClickListener(v -> openPlayerActivity("action"));
        categoryHorror.setOnClickListener(v -> openPlayerActivity("horror"));
    }

    private void setupChangeUserListener() {
        btnChangeUser.setOnClickListener(v -> {
            UserSelectionDialog dialog = new UserSelectionDialog();
            dialog.show(getSupportFragmentManager(), "UserSelectionDialog");
        });
    }

    private void openPlayerActivity(String category) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userKey = sharedPreferences.getString("current_user_key", null);
        if (userKey == null) return;

        String name = sharedPreferences.getString(userKey + "_name", "Usuario");
        int age = sharedPreferences.getInt(userKey + "_age", 0);
        String gender = sharedPreferences.getString(userKey + "_gender", "");
        String photoPath = sharedPreferences.getString(userKey + "_photo", null);

        Bitmap photo = null;
        if (photoPath != null) {
            photo = BitmapFactory.decodeFile(photoPath);
        }

        Intent intent = new Intent(MenuActivity.this, PlayerActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("name", name);
        intent.putExtra("age", age);
        intent.putExtra("gender", gender);
        intent.putExtra("photo", photo);
        startActivity(intent);


    }

    public void goBackToMenu(View view) {
        Intent intent = new Intent(MenuActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
