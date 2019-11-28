package com.example.fillinggood.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fillinggood.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, age, major, ID, pw;
    private Button registerButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        name = (EditText)findViewById(R.id.name);
        age = (EditText)findViewById(R.id.age);
        major = (EditText)findViewById(R.id.major);
        ID = (EditText)findViewById(R.id.ID);
        pw = (EditText)findViewById(R.id.pw);

        registerButton = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( name.getText().toString().length() == 0 ) {
                    Toast.makeText(RegisterActivity.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                if( age.getText().toString().length() == 0 ) {
                    Toast.makeText(RegisterActivity.this, "나이를 입력하세요", Toast.LENGTH_SHORT).show();
                    age.requestFocus();
                    return;
                }
                if( major.getText().toString().length() == 0 ) {
                    Toast.makeText(RegisterActivity.this, "전공을 입력하세요", Toast.LENGTH_SHORT).show();
                    major.requestFocus();
                    return;
                }
                if( ID.getText().toString().length() == 0 ) {
                    Toast.makeText(RegisterActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    ID.requestFocus();
                    return;
                }
                if( pw.getText().toString().length() == 0 ) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                    return;
                }

                //if 아이디 및 비밀번호 조건 미달일 경우

                //if에 안 걸릴 경우, 정보를 db로 보낸 뒤 로그인 페이지로 이동
                //여기에 db로 회원정보 넘기는 코드 작성 필요
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


    }
}
