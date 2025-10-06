package com.example.tripapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tripapp.Bean.User;
import com.example.tripapp.R;
import com.example.tripapp.SQLite.MySQLiteOpenHelper;

public class RegisterActivity extends AppCompatActivity {

    private ImageView back;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private CheckBox cbAccept;
    private Button btRegister;
    private MySQLiteOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        back = findViewById(R.id.back);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        cbAccept = findViewById(R.id.cb_accept);
        btRegister = findViewById(R.id.bt_register);
    }

    private void initData() {
        dbHelper = new MySQLiteOpenHelper(this);
    }
    private void initEvent() {
        back.setOnClickListener(v -> {
            finish();
        });
        btRegister.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void registerUser() {
        // 获取输入的值
        String username = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 验证输入是否为空
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "请填写所有必填项", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查是否同意用户协议
        if (!cbAccept.isChecked()) {
            Toast.makeText(this, "请先同意用户协议", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查密码是否一致
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查用户名是否已存在
        User existingUser = dbHelper.queryUserByUsername(username);
        if (existingUser != null) {
            Toast.makeText(this, "该用户名已被注册，请更换用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建新用户并保存到数据库
        User newUser = new User(username, password);
        long result = dbHelper.insertUser(newUser);

        if (result != -1) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            // 可以在这里跳转到登录页面或主页面
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "注册失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }
}