package com.example.provaapp.mode_create_2_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.provaapp.operative_activity_changer_1.FirstFragment;
import com.example.provaapp.R;

public class CreateActivity extends AppCompatActivity { //HOST ACTIVITY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (savedInstanceState == null) { //ritorna un manager per la creazione di transizioni

            Toolbar toolbar = findViewById(R.id.toolbarCreate);
            setSupportActionBar(toolbar);
            Intent intent = getIntent();
            Bundle args = intent.getBundleExtra(FirstFragment.CreateKey);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.room_creation_fragment_container, RoomParentFragment.class, args)
                    .commit();
        }
    }
}