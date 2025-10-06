package com.example.tripapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {

    private ImageView back;
    private EditText etAccount;
    private EditText etPassword;
    private Button btLogin;

    private MySQLiteOpenHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        back = findViewById(R.id.back);
    }
    private void initData() {
        dbHelper = new MySQLiteOpenHelper(this);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
    }
    private void initEvent() {
        back.setOnClickListener(v -> {
            finish();
        });
        btLogin.setOnClickListener(v -> {
            loginUser();
        });
    }

    private void loginUser() {
        // 获取输入的值
        String username = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 验证输入是否为空
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 查询用户是否存在
        User user = dbHelper.queryUserByUsername(username);
        if (user == null) {
            Toast.makeText(this, "该用户不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        // 验证密码是否正确
        User authenticatedUser = dbHelper.queryUser(username, password);
        if (authenticatedUser == null) {
            Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
            return;
        }

        // 登录成功
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        // 这里可以跳转到主页面或其他操作
        startActivity(new Intent(this, MainActivity.class));
    }

}