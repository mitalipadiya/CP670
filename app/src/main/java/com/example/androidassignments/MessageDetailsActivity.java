package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MessageDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        if (savedInstanceState == null) {
            // Retrieve data from the Intent
            Bundle bundle = getIntent().getExtras();

            // Pass data to the fragment
            MessageDetailsFragment fragment = new MessageDetailsFragment(null);
            fragment.setArguments(bundle);

            // Begin FragmentTransaction
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}