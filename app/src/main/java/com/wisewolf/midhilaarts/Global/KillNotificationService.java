package com.wisewolf.midhilaarts.Global;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class KillNotificationService extends Service {

    @Override
    public void onTaskRemoved(Intent rootIntent) {
       Toast.makeText(this, "NJMS Closed ", Toast.LENGTH_LONG).show();
       updateFirebase();
        super.onTaskRemoved(rootIntent);
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getSystemService(ns);
        nMgr.cancelAll();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = df.format(c);
            HashMap hm = new HashMap();
            // TODO duration change
            if (GlobalData.username!=null) {
                hm.put("user", GlobalData.username);
                hm.put("time", c);
            }
            else {
                hm.put("user", GlobalData.username);
                hm.put("time", c);
            }


            db.collection("closedApp").add(hm)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {


                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });



        } catch (Exception e) {
            String a = "";
        }
    }
}

