package com.example.individuaproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class first extends AppCompatActivity {

    Button welcomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        welcomeBtn = findViewById(R.id.btnWelcome);

        welcomeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(first.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
