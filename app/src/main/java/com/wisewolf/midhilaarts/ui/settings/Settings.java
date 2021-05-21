package com.wisewolf.midhilaarts.ui.settings;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hosseiniseyro.apprating.AppRatingDialog;
import com.hosseiniseyro.apprating.listener.RatingDialogListener;
import com.wisewolf.midhilaarts.Activity.FeedbackAct;
import com.wisewolf.midhilaarts.BuildConfig;
import com.wisewolf.midhilaarts.LoginActivity;
import com.wisewolf.midhilaarts.R;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Settings extends Fragment   {
    RecyclerView transactionList;
    TextView logout,checkUpd,privacy,share,rate,version,feedbck,terms;

    private com.wisewolf.midhilaarts.ui.settings.SettingsViewModel  settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
            ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.settings, container, false);
        privacy=root.findViewById(R.id.txt_privacy_policy);
        share=root.findViewById(R.id.shareApp);
        rate=root.findViewById(R.id.rateUs);
        feedbck=root.findViewById(R.id.feedback);
        checkUpd=root.findViewById(R.id.checkUpd);
        logout=root.findViewById(R.id.logout_id);
        version=root.findViewById(R.id.version);
        terms=root.findViewById(R.id.termsand);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPrivacy();
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertterms();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFunct();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               rateFunct();
            }
        });

        feedbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), FeedbackAct.class));
            }
        });

        checkUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               rateFunct();
            }
        });

         getVersion();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertLogout();
            }
        });
        //final TextView textView = root.findViewById(R.id.text_slideshow);
       // transactionList=root.findViewById(R.id.transactionList);

        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private void alertLogout() {

        new AlertDialog.Builder(getContext())
            .setTitle("CLOSE APP")
            .setMessage("Do you really want to logout")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPrefs.edit();

                    editor.putString("username", "json");
                    editor.putString("saved", "0");
                    editor.putString("logged_user", "0");
                    editor.apply();
                   startActivity(new Intent(getActivity(), LoginActivity.class));
                   getActivity().finish();

                }
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();

    }


    private void alertterms() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_terms, null);
        builder.setView(customLayout);
        // add a button
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void getVersion() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("androidVer").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> aaq=   queryDocumentSnapshots.getDocuments();
                    DocumentSnapshot ne = aaq.get(0);
                    Object a= ne.get("ver");
                     String ver =a.toString();
                     version.setText(new StringBuilder().append("Version ").append(ver).toString());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String a="";
                }
            });

    }

    private void rateFunct() {
        try{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.wisewolf.midhilaarts")));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.wisewolf.midhilaarts")));
        }
    }

    private void shareFunct() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Midhila Arts");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    private void alertPrivacy() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_privacy, null);
        builder.setView(customLayout);
        // add a button
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

}