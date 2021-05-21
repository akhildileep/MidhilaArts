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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.HomeActivity;
import com.wisewolf.midhilaarts.Model.LoginMod;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.RetrofitClientInstance;
import com.wisewolf.midhilaarts.SignUp;
import com.wisewolf.midhilaarts.firebaseModel.SignupModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotNumber extends AppCompatActivity {

    AlertDialog.Builder builder;
    AlertDialog dialog;


    String Uphone="",countryCode="+91";
    EditText phone;TextView verify;
    CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotnum);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        builBuilder();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        phone=findViewById(R.id.phone);
        verify=findViewById(R.id.login);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Toast.makeText(ForgotNumber.this, "Updated " + selectedCountry.getPhoneCode(), Toast.LENGTH_SHORT).show();
                countryCode="+"+selectedCountry.getPhoneCode();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uphone=phone.getText().toString();
                try {
                    checkNUm(Uphone);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });





    }

    private void builBuilder() {
        builder = new  AlertDialog.Builder(ForgotNumber.this);
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
    private void checkNUm(String uphone) {

        try {

            dialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<ArrayList> call = service.checkNum("/app/api/validation/"+uphone);
            call.enqueue(new Callback<ArrayList>() {
                @Override
                public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                    String a = "";
                    dialog.cancel();
                    if (response.body()!=null) {
                        if (response.body().get(0).equals("already exist")) {
                            Intent intent = new Intent(ForgotNumber.this, OTPForgotPass_Activity.class);
                            intent.putExtra("p", countryCode+Uphone);
                            intent.putExtra("pO", Uphone);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(ForgotNumber.this, "Number not present", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ForgotNumber.this, "Network Error FN122", Toast.LENGTH_SHORT).show();
                    }



                }

                @Override
                public void onFailure(Call<ArrayList> call, Throwable t) {
                    String a = "";

                }
            });


        }catch (Exception ignored){
            String a="";
        }

    }


}
