package com.example.mapa2;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class SelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selector);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton btnVerMapaActual = findViewById(R.id.btnVerMapaActual);
        MaterialButton btnVerGoogleMaps = findViewById(R.id.btnVerGoogleMaps);

        btnVerMapaActual.setOnClickListener(v -> {
            Intent intent = new Intent(SelectorActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnVerGoogleMaps.setOnClickListener(v -> {
            Intent intent = new Intent(SelectorActivity.this, GoogleMapsActivity.class);
            startActivity(intent);
        });
    }
}