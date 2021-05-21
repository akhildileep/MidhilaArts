package com.wisewolf.midhilaarts.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hosseiniseyro.apprating.AppRatingDialog;
import com.hosseiniseyro.apprating.listener.RatingDialogListener;
import com.wisewolf.midhilaarts.Feedback;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.ui.settings.Settings;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class FeedbackAct extends AppCompatActivity implements RatingDialogListener {
    AlertDialog.Builder builder;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        builBuilder();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setThreshold(5)
                .setTitle("Rate Midhila")
                .setDescription("Please select some stars and give your happiness")
                .setCommentInputEnabled(true)
                .setDefaultComment("Midhila is awsome !")
                .setStarColor(R.color.colorAccent)
                .setNoteDescriptionTextColor(R.color.white)
                .setTitleTextColor(R.color.white)
                .setDescriptionTextColor(R.color.white)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.Basic)
                .setCommentTextColor(R.color.Basic)
                .setCommentBackgroundColor(R.color.white)
                .setDialogBackgroundColor(R.color.Basic)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(FeedbackAct.this)
                .show();
        }
    }

    private void builBuilder() {
        builder = new  AlertDialog.Builder(FeedbackAct.this);
        // create an alert builder
        builder.setCancelable(false);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_loading, null);
        builder.setView(customLayout);
        // add a button
        // create and show the alert dialog
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void onNegativeButtonClicked() {
        finish();
    }

    @Override
    public void onNeutralButtonClicked() {
        finish();
    }

    @Override
    public void onPositiveButtonClickedWithComment(int com, @NotNull String s) {
        s="Total Star "+ com +"\n"+s;
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"midhilaart@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Feedback From App");
        i.putExtra(Intent.EXTRA_TEXT   , s);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedbackAct.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onPositiveButtonClickedWithoutComment(int i) {

    }
}