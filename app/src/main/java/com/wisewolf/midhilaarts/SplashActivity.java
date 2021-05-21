package com.wisewolf.midhilaarts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
   ImageView splashimg;
    String currentVersion,new_Version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        splashimg=findViewById(R.id.splashimg);


        if (isNetworkAvailable()) {


            getserverStatus();
            /*  */


        } else {
            alertOffline();
        }


    }

    private void getserverStatus() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("server").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> aaq = queryDocumentSnapshots.getDocuments();

                        try {
                            DocumentSnapshot ne = aaq.get(0);
                            Object a = ne.get("up");
                            if (a != null) {
                                a = a.toString();

                                if (a.equals("down")) {
                                    alertServer();
                                } else {
                                    startApp();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            finish();
                            Toast.makeText(SplashActivity.this, "Network Down", Toast.LENGTH_SHORT).show();
                        }



                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String a="";
                }
            });




    }

    private void startApp() {
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        GetVersionCode getVersionCode=new GetVersionCode();
        getVersionCode.execute();
    }

    private void alertServer() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_server, null);
        builder.setView(customLayout);
        // add a button
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void alertOffline() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_offline, null);
        builder.setView(customLayout);
        // add a button
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("androidVer").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> aaq=   queryDocumentSnapshots.getDocuments();
                            DocumentSnapshot ne = aaq.get(0);
                            Object a= ne.get("ver");
                            a =a.toString();
                            new_Version=a.toString();

                            if (new_Version != null && !new_Version.isEmpty()) {
                                if (Float.parseFloat(currentVersion) < Float.parseFloat(new_Version)) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                                else {
                                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                                }

                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String a="";
                        }
                    });

                return new_Version;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

        }

    }

}
