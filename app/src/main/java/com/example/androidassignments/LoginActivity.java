package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidassignments.utils.Helper;

public class LoginActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "LoginActivity";
    EditText loginEmailEditText, passwordEditText;
    SharedPreferences sharedPreferences;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "Returned to LoginActivity.onCreate");
        setContentView(R.layout.activity_login);
        helper = new Helper();

        loginEmailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);

        Button loginBtn = findViewById(R.id.login_btn);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String storedEmail = sharedPreferences.getString("email", "mdpadiya10@gmail.com");
        loginEmailEditText.setText(storedEmail);
    }

    public void onLoggedIn(View view) {
        String emailText = loginEmailEditText.getText().toString();
        String password =  passwordEditText.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!helper.isValidPassword(password)) {
            Toast.makeText(this, R.string.password_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", emailText);
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "Returned to LoginActivity.onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "Returned to LoginActivity.onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "Returned to LoginActivity.onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "Returned to LoginActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "Returned to LoginActivity.onDestroy");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(ACTIVITY_NAME, "Returned to LoginActivity.onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(ACTIVITY_NAME, "Returned to LoginActivity.onRestoreInstanceState");
    }
}