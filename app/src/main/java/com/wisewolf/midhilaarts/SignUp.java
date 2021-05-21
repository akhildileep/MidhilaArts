package com.wisewolf.midhilaarts;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.Model.LoginMod;
import com.wisewolf.midhilaarts.Model.NumChk;
import com.wisewolf.midhilaarts.firebaseModel.SignupModel;

import java.lang.reflect.Type;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    TextView signup, login;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.region)
    EditText region;
    @BindView(R.id.pass)
    EditText pass;
    @BindView(R.id.signup_btn)
    TextView signupBtn;
    AlertDialog.Builder builder ;
    AlertDialog dialog;
    TextView skip;
    EditText confirmPass;

    String Uphone="",Uusername,Uemail,Uregion,Upass,Cpass,countryCode="+91";
    CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.pan);
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        builBuilder();
        checkPerm();

        init();

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                Toast.makeText(SignUp.this, "Updated " + selectedCountry.getPhoneCode(), Toast.LENGTH_SHORT).show();
                countryCode="+"+selectedCountry.getPhoneCode();
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getValues()) {
                    if (isValidMail(Uemail)) {
                        if (isValidMobile(Uphone)) {
                            if (Upass.equals(Cpass)){

                                fillvalues();
                            }
                            else {
                                Toast.makeText(SignUp.this, "Password should match", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            Toast.makeText(SignUp.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        {
                            Toast.makeText(SignUp.this, "Invalid email", Toast.LENGTH_SHORT).show();
                        }


                }
                else {
                    Toast.makeText(SignUp.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             startActivity(new Intent(SignUp.this, LoginActivity.class));
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.logged=false;
                startActivity(new Intent(SignUp.this, HomeActivity.class));
            }
        });
    }

    private boolean isValidMobile(String uphone) {
        if(!Pattern.matches("[a-zA-Z]+", uphone)) {
            return uphone.length() > 6 && uphone.length() <= 10;
        }
        return false;
    }

    private boolean isValidMail(String uemail) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(uemail).matches();
    }

    private boolean fillvalues() {
        try {
            SignupModel signupModel=new SignupModel();
            signupModel.setAge(Uemail);
            signupModel.setActive(true);
            signupModel.setCity(Uregion);
            signupModel.setEmail(Uemail);
            signupModel.setDevice_token(getDeviceName());
            signupModel.setDevice_type("ANDROID");
            signupModel.setIsloged_in("true");
            signupModel.setMobile_no(Uphone);
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String today = df.format(c);
            signupModel.setCreatedAt(today);
            signupModel.setPassword(Upass);
            signupModel.setPlantype("");
            String[] uname=Uusername.split("\\s+");
            signupModel.setUser_name(Uusername);
            signupModel.setUser_type("student");
            checkNUm(signupModel);

            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    private void checkNUm(SignupModel signupModel) {

            try {

                dialog.show();
                final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
                Call<ArrayList> call = service.checkNum("/app/api/validation/"+Uphone);
                call.enqueue(new Callback<ArrayList>() {
                    @Override
                    public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                        String a = "";
                        if (response.body().get(0).equals("already exist")){
                            Toast.makeText(SignUp.this, "Number already present", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            GlobalData.user=signupModel;
                            GlobalData.logged=true;
                            Intent intent=new Intent(SignUp.this, OTP_Activity.class);
                            intent.putExtra("cc",countryCode);
                            startActivity(intent);
                            finish();

                        }


                        dialog.cancel();
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

    private void builBuilder() {
        builder  = new  AlertDialog.Builder(SignUp.this);
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

    private void addDB(SignupModel sigupmodel) {

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            db.collection("users")
                .add(sigupmodel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {


                        GlobalData.user=sigupmodel;
                        GlobalData.logged=true;
                        startActivity(new Intent(SignUp.this, OTP_Activity.class));
                        finish();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);


                        Toast.makeText(SignUp.this, "Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
        } catch (Exception e) {
            String a = "";

            Toast.makeText(SignUp.this, "Try Again", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean getValues() {
        Uphone = phone.getText().toString();
        Uusername = username.getText().toString();
        Uemail = email.getText().toString();
        Uregion = region.getText().toString();
        Upass = pass.getText().toString();
        Cpass=confirmPass.getText().toString();
        return !Uphone.equals("") && !Uusername.equals("") && !Uemail.equals("") && !Uregion.equals("") && !Upass.equals("")&& !Cpass.equals("");

    }

    private void init() {

        signup = findViewById(R.id.signup_btn);
        login = findViewById(R.id.login_btn);
        skip = findViewById(R.id.skip_btn);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        skip.setVisibility(View.GONE);
        confirmPass=findViewById(R.id.pass_confirm);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void testPhoneVerify() {
        // [START auth_test_phone_verify]
        String phoneNum ="+91"+ "9562099238";
        String testVerificationCode = "123456";

        // Whenever verification is triggered with the whitelisted number,
        // provided it is not set for auto-retrieval, onCodeSent will be triggered.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNum, 30L /*timeout*/, TimeUnit.SECONDS,
            this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    // Save the verification id somewhere
                    // ...
                    String testVerificationCode = "123456";
                    // The corresponding whitelisted code above should be used to complete sign-in.
                 //   SignUp.this.enableUserManuallyInputCode();
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    // Sign in with the credential

                    // ...
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // ...
                    String testVerificationCode = "123456";
                }

            });
        // [END auth_test_phone_verify]
    }

    private void checkPerm() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,Manifest.permission.INTERNET};
        String rationale = "Please provide  permission so that you can ...";
        Permissions.Options options = new Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning");

        Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
            }
        });
    }


}
