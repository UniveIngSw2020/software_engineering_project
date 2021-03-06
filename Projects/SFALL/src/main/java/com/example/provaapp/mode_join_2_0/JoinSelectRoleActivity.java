package com.example.provaapp.mode_join_2_0;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.provaapp.R;
import com.example.provaapp.mode_create_2_1.CreateActivity;
import com.example.provaapp.operative_activity_changer_1.MainActivity;
import com.example.provaapp.useful_classes.P2PWorkerNearby;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Payload;

public class JoinSelectRoleActivity extends AppCompatActivity {


    public TextView roomText;
    public static TextView audioAvailable, videoAvailable, finishAlertText;
    public static Button audioBtn, videoBtn;
    public static int myRole = 0;


    public static void setView(int a, int v) {

        if (JoinSelectRoleActivity.audioAvailable != null && JoinSelectRoleActivity.videoAvailable != null) {

            JoinSelectRoleActivity.audioAvailable.setText(String.valueOf(a));
            JoinSelectRoleActivity.videoAvailable.setText(String.valueOf(v));

            if (v == 0) {
                JoinSelectRoleActivity.videoBtn.setClickable(false);
            }
            if (a == 0) {
                JoinSelectRoleActivity.audioBtn.setClickable(false);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_select_role_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = findViewById(R.id.peerConfigToolbar);
        setSupportActionBar(toolbar);
        P2PWorkerNearby.c = this;
        P2PWorkerNearby.workerAppMediaFolderPath = Environment.getExternalStorageDirectory() + "/DCIM/multi_rec/" + P2PWorkerNearby.room + "/";
        MainActivity.createStorageDir(P2PWorkerNearby.workerAppMediaFolderPath);

        roomText = findViewById(R.id.textRoomView);
        audioBtn = findViewById(R.id.JoinAudioButton);
        videoBtn = findViewById(R.id.JoinVideoButton);
        audioAvailable = findViewById(R.id.joinAudioAvailable);
        videoAvailable = findViewById(R.id.joinVideoAvailable);
        finishAlertText = findViewById(R.id.getReadyWorkerText);

        audioAvailable.setText(String.valueOf(P2PWorkerNearby.audioN));
        videoAvailable.setText(String.valueOf(P2PWorkerNearby.videoN));

        roomText.setText(P2PWorkerNearby.room);

        audioBtn.setOnClickListener(v -> {
            Payload bytesPayload = Payload.fromBytes("VA-0-1".getBytes()); //così significa che chiedo di avere il posto di audioRecorder
            Nearby.getConnectionsClient(getApplicationContext()).sendPayload(P2PWorkerNearby.managerEndpointID, bytesPayload);
            audioBtn.setClickable(false);
            myRole = 0;
            //QUI NON CI DOVREBBERO ESSERE PROBLEMI DI COORDINAMENTO IN QUANTO IL PAYLOADCALLBACK SI OCCUPA DI METTERE IL SETCLICKABLE DEL PULSANTE A FALSE APPENA FINISCONO I POSTI
            //C'è UN AGGIORNAMENTO CONTINUO DEI POSTI DA PARTE DEL MANAGER, BASTA TROVARE UNA VELOCITà ADATTA DI REFRESH DA PARTE DEL MANAGER PER EVITARE COLLISIONI!
        });

        videoBtn.setOnClickListener(v -> {
            Payload bytesPayload = Payload.fromBytes("VA-1-0".getBytes()); //così significa che chiedo di avere il posto di videoRecorder
            Nearby.getConnectionsClient(getApplicationContext()).sendPayload(P2PWorkerNearby.managerEndpointID, bytesPayload);
            videoBtn.setClickable(false);
            myRole = 1;
            //aggiungere codice per proseguire con la prossima activity!!!!!!!!!!!!!!!!!!
            //STESSO DISCORSO SOPRA
        });

        //visto che mi sono già collegato posso fermare la discovery, la connessione col manager non verrà persa!
        Nearby.getConnectionsClient(getApplicationContext()).stopDiscovery();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void sendMessage(Context c, String[] key, String[] vals, Class<? extends AppCompatActivity> nextActivity) {
        int i = 0;
        Intent intent = new Intent(c, nextActivity);
        for (i = 0; i < key.length; i++) {
            intent.putExtra(key[i], vals[i]);
        }
        c.startActivity(intent);
    }

}
