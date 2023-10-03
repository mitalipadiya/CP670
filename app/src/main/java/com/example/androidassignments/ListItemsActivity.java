package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "ListItemsActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton imageButton;
    CheckBox checkBox;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "Returned to ListItemsActivity.onCreate");
        setContentView(R.layout.activity_list_items);
        imageButton = findViewById(R.id.image_btn);
        aSwitch = findViewById(R.id.switch_btn);
        checkBox = findViewById(R.id.checkbox_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked(view);
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    print("Switch is On");
                } else {
                    Toast.makeText(ListItemsActivity.this, "Switch is Off", Toast.LENGTH_LONG).show();
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // If the checkbox is checked, show a dialog box
                    showDialog();
                }
            }
        });
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        builder.setTitle("Confirm Finish")
                .setMessage("Do you want to finish the activity?")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Response", "Here is my response");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();

        builder.show();
    }

    public void imageClicked(View imageView) {
        Intent takePictureIntent =
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(
                getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    public void print(String message) {
        Toast.makeText(ListItemsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "Returned to ListItemsActivity.onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "Returned to ListItemsActivity.onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "Returned to ListItemsActivity.onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "Returned to ListItemsActivity.onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "Returned to ListItemsActivity.onDestroy");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(ACTIVITY_NAME, "Returned to ListItemsActivity.onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(ACTIVITY_NAME, "Returned to ListItemsActivity.onRestoreInstanceState");
    }
}