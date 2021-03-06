package com.example.provaapp.player;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.provaapp.R;
import com.example.provaapp.operative_activity_changer_1.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends AppCompatActivity {

    private String dirPath;
    private ListView myListView;

    // name of files to show on the list
    private List<String> selectedFilesName;

    // path of files to play on the player
    private ArrayList<String> selectedFilesPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileslist);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dirPath = getIntent().getStringExtra("dirPath");

        selectedFilesPath = new ArrayList<>();
        selectedFilesName = new ArrayList<>();

        myListView = findViewById(R.id.myListView);
        Button montageButton = findViewById(R.id.montageButton);

        List<String> filePaths = MediaHandler.getFilesNameFromDirPath(dirPath);
        String[] paths = filePaths.toArray(new String[0]);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item1,
                R.id.myTextView1, paths);
        myListView.setAdapter(myAdapter);


        myListView.setOnItemClickListener((parent, view, position, id) -> {
            String itemValue = (String) myListView.getItemAtPosition(position);

            if (MediaHandler.isInFormat(itemValue, "mp4") || MediaHandler.isInFormat(itemValue, "mp3")) {

                MediaHandler.addOrRemoveElement(selectedFilesPath, dirPath + itemValue);
                if (MediaHandler.addOrRemoveElement(selectedFilesName, itemValue)) {
                    view.setBackgroundColor(0xFF00FF00);
                } else {
                    view.setBackgroundColor(0xFFFFFFFF);
                }
                Log.d("selectedVideos", selectedFilesPath.toString());

            } else {
                Toast.makeText(getApplicationContext(), "Not a valid format", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO add smarter function to count occurrences of paths that have a specified format
        montageButton.setOnClickListener(v -> {
            if (selectedFilesPath.size() > 0) {
                int mp3FileCount = 0;
                int mp4FileCount = 0;
                for (String p : selectedFilesPath) {
                    if (MediaHandler.isInFormat(p, "mp3")) {
                        mp3FileCount++;
                    } else {
                        mp4FileCount++;
                    }
                }
                if (mp3FileCount > 1 || mp4FileCount < 2 || mp4FileCount > 4) {
                    Toast.makeText(getApplicationContext(), "Choose max 1 mp3 and from 2 to 4 mp4", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle b = new Bundle();
                    String videosPathKey = "paths";
                    b.putStringArrayList(videosPathKey, selectedFilesPath);
                    switchActivity(b, MediaPlayerActivity.class);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Not enough videos", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void switchActivity(Bundle b, Class<? extends AppCompatActivity> nextActivity) {
        Intent intent = new Intent(getApplicationContext(), nextActivity);
        intent.putExtras(b);
        startActivity(intent);
    }

}
