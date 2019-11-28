package com.example.fillinggood.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.Boundary.home.MainActivity;
import com.example.fillinggood.R;

public class LoginActivity extends AppCompatActivity {
    private EditText ID, pw;
    private Button loginButton, registerButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ID = (EditText)findViewById(R.id.ID);
        pw = (EditText)findViewById(R.id.pw);

        loginButton = (Button)findViewById(R.id.loginButton);
        registerButton = (Button)findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ID.getText().toString().length() == 0 ) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    ID.requestFocus();
                    return;
                }
                if( pw.getText().toString().length() == 0 ) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                    return;
                }
                //if 존재하지 않는 아이디라면, Toast~
                //if 아이디와 비번이 매칭되지 않는다면, Toast~

                //if에 안 걸릴 경우,
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
