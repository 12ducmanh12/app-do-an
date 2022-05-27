package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginAuthen(View view){
        EditText edtuser,edtpassword;
        Intent intent = new Intent(this, MainActivity.class);
        edtuser = (EditText) findViewById(R.id.username);
        String username = edtuser.getText().toString();
        edtpassword = (EditText) findViewById(R.id.password);
        String password = edtpassword.getText().toString();
        if (username.equals("admin") && password.equals("admin")){
            startActivity(intent);
        }
    }

//    private void map(){
//        edtuser = (EditText) findViewById(R.id.username);
//        String username = edtuser.getText().toString();
//        edtpassword = (EditText) findViewById(R.id.password);
//        String password = edtpassword.getText().toString();
//        if (username == "admin" && password == "admin")
//        btnlogin = (Button)findViewById(R.id.login);
//    }
}