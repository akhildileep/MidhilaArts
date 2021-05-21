package com.wisewolf.midhilaarts.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.HomeActivity;
import com.wisewolf.midhilaarts.LoginActivity;
import com.wisewolf.midhilaarts.Model.LoginMod;
import com.wisewolf.midhilaarts.PackageDetails;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.RetrofitClientInstance;
import com.wisewolf.midhilaarts.SignUp;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPass extends AppCompatActivity {
    EditText pass, cpas;

    String passs = "", cpass = "";
    LinearLayout mainL;
    TextView login;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pass = findViewById(R.id.pass);
        cpas = findViewById(R.id.cpass);
        login = findViewById(R.id.login);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        builBuilder();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passs = pass.getText().toString();
                cpass = cpas.getText().toString();
                if (cpass.equals(passs)) {
                    try {
                        updatePass();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(ConfirmPass.this, "Both password should be same", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void builBuilder() {
        builder = new AlertDialog.Builder(ConfirmPass.this);
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

    private void updatePass() {
        dialog.show();
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").
                whereEqualTo("mobile_no", GlobalData.phone).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    List<DocumentSnapshot> aaq = queryDocumentSnapshots.getDocuments();
                    String id = "";
                    for (int i = 0; i < aaq.size(); i++) {
                        DocumentSnapshot ne = aaq.get(i);
                        id = ne.getId();

                    }
                    updatefield(id);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatefield(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {


            db.collection("users").document(id)
                .update("password", passs).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dialog.cancel();
                    startActivity(new Intent(ConfirmPass.this, LoginActivity.class));
                    Toast.makeText(ConfirmPass.this, "Password changed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}