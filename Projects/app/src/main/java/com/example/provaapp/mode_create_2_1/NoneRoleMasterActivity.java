package com.example.provaapp.mode_create_2_1;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.provaapp.R;
import com.example.provaapp.useful_classes.P2PManagerNearby;
import com.example.provaapp.useful_classes.Permissions;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Payload;

public class NoneRoleMasterActivity extends AppCompatActivity {

    private Button stopRecording;
    private Long timeoutMs;
    private Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.none_role_master_activity);
        stopRecording = findViewById(R.id.stopNoneRecordingButton);
        stopRecording.setVisibility(View.INVISIBLE);
        stopRecording.setClickable(false);
        receivedIntent = getIntent();
        timeoutMs = receivedIntent.getLongExtra("timestamp", System.currentTimeMillis() + 5000);

        stopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownStopRecording(System.currentTimeMillis() + 3000);       //This button is clickable and visible only by master(see -> countDownStarRecording)
            }
        });
        countDownStartRecording(timeoutMs);
    }

    private void countDownStartRecording(long timeToWait) {

        new CountDownTimer(timeToWait - System.currentTimeMillis(), 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                stopRecording.setVisibility(View.VISIBLE);
                stopRecording.setClickable(true);
            }
        }.start();
    }


    private void countDownStopRecording(long timeToWait) {

        Payload bytesPayload = Payload.fromBytes(("STOPRECORDING-" + Long.toString(timeToWait)).getBytes());
        Nearby.getConnectionsClient(getApplicationContext()).sendPayload(P2PManagerNearby.endpoints, bytesPayload);

        new CountDownTimer(timeToWait - System.currentTimeMillis(), 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                finish();
            }
        }.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}