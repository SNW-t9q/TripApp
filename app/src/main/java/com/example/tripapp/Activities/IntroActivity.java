package com.example.tripapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripapp.R;

public class IntroActivity extends AppCompatActivity {

    Button loginButton;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initView();
        initEvent();
    }

    private void initView() {
        loginButton = findViewById(R.id.bt_login);
        registerButton = findViewById(R.id.bt_register);
    }

    private void initEvent() {
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        });
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, RegisterActivity.class));
        });
    }
}