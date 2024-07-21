package com.example.anflix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button btnGoToMenu = findViewById(R.id.btnGoToMenu);
        btnGoToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataCaptureDialog();
            }
        });
    }

    private void showDataCaptureDialog() {
        DataCaptureDialog dialog = new DataCaptureDialog();
        dialog.show(getSupportFragmentManager(), "DataCaptureDialog");
    }
}

