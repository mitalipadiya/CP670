package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "MainActivity";
    Button mainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "Returned to MainActivity.onCreate");
        setContentView(R.layout.activity_main);
        mainBtn = findViewById(R.id.main_btn);
    }
    public void onButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
        startActivityForResult(intent, 10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10) {
            Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
            if(resultCode == RESULT_OK) {
                String messagePassed = data.getStringExtra("Response");
                Log.i("Message", messagePassed);
                Toast.makeText(MainActivity.this, "ListItemsActivity passed: " + messagePassed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "Returned to MainActivity.onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "Returned to MainActivity.onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "Returned to MainActivity.onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "Returned to MainActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "Returned to MainActivity.onDestroy");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(ACTIVITY_NAME, "Returned to MainActivity.onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(ACTIVITY_NAME, "Returned to MainActivity.onRestoreInstanceState");
    }
}