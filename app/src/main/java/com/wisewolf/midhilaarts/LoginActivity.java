package com.wisewolf.midhilaarts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;
import com.wisewolf.midhilaarts.Activity.ForgotNumber;
import com.wisewolf.midhilaarts.DB.OfflineDatabase;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.Model.LoginMod;
import com.wisewolf.midhilaarts.Model.Pack;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    OfflineDatabase dbb;
    @BindView(R.id.phone)
    TextInputEditText phone;
    @BindView(R.id.pass)
    TextInputEditText pass;
    @BindView(R.id.forget_pass)
    TextView forgetPass;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.click_here)
    TextView clickHere;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView9)
    TextView textView9;

    String Uphone="",Upass="";
    LinearLayout mainL;

    AlertDialog.Builder builder ;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainL = findViewById(R.id.main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        dbb = new OfflineDatabase(getApplicationContext());


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String saved = sharedPrefs.getString("saved", "");


        builder = new AlertDialog.Builder(LoginActivity.this);
        // create an alert builder
        builder.setCancelable(false);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_loading, null);
        builder.setView(customLayout);
        // add a button
        // create and show the alert dialog
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if (saved.equals("1")) {
            getUser();
        } else {


            clickHere.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    clickHere.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    SimpleTarget thirdTarget =
                        new SimpleTarget.Builder(LoginActivity.this).setPoint(findViewById(R.id.click_here))
                            .setRadius(200f)

                            .setTitle("New to MIDHILA APP?")

                            .setDescription("welcome , create your account here if you are new.")
                            .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                                @Override
                                public void onStarted(SimpleTarget target) {

                                }

                                @Override
                                public void onEnded(SimpleTarget target) {

                                }
                            })
                            .build();

                    Spotlight.with(LoginActivity.this)
                        .setClosedOnTouchedOutside(true)
                        .setOverlayColor(ContextCompat.getColor(LoginActivity.this, R.color.background)) // background overlay color
                        .setDuration(1000L) // duration of Spotlight emerging and disappearing in ms
                        .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
                        .setTargets(thirdTarget) // set targets. see below for more info
                        .setClosedOnTouchedOutside(true) // set if target is closed when touched outside
                        .setOnSpotlightStartedListener(new OnSpotlightStartedListener() { // callback when Spotlight starts
                            @Override
                            public void onStarted() {
                                //Toast.makeText(LoginActivity.this, "spotlight is started", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnSpotlightEndedListener(new OnSpotlightEndedListener() { // callback when Spotlight ends
                            @Override
                            public void onEnded() {
                                //   Toast.makeText(getContext(), "spotlight is ended", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .start();
                }
            });
        }
    }

    @OnClick({R.id.forget_pass, R.id.login, R.id.click_here})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_pass:
                startActivity(new Intent(LoginActivity.this, ForgotNumber.class));
                finish();
                break;
            case R.id.login:
                getValues();
                if (getValues()) {
                     loginCheck();
                   // getSpecificUser();
                }
                else {
                    Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.click_here:
                startActivity(new Intent(LoginActivity.this, SignUp.class));
                finish();
                break;
        }
    }

    private void loginCheck() {
        try {

        dialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<LoginMod>> call = service.loginCheck("/app/api/login/"+Uphone+"/"+Upass);
            call.enqueue(new Callback<List<LoginMod>>() {
                @Override
                public void onResponse(Call<List<LoginMod>> call, Response<List<LoginMod>> response) {
                    String a = "";
                    if (response.body() != null) {
                        if (response.body().size()!=0){
                        GlobalData.logged=true;
                            GlobalData.phone=Uphone;

                        GlobalData.username=response.body().get(0).getUserName();
                        GlobalData.Logged_user=response.body().get(0);
                        savedetails(GlobalData.username,GlobalData.Logged_user);}
                        else {
                            Toast.makeText(LoginActivity.this, "login error due to wrong credentials", Toast.LENGTH_SHORT).show();
                            phone.setText("");
                            pass.setText("");

                        }
                    }

                    dialog.cancel();
                }

                @Override
                public void onFailure(Call<List<LoginMod>> call, Throwable t) {
                    String a = "";

                }
            });


        }catch (Exception ignored){
            String a="";
        }
    }

    public void getSpecificUser() {
        //final MutableLiveData<UsersModel> usersData = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .whereEqualTo("mobile_no", Uphone)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                    if(e!=null || snapshot.size()==0){
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }

                    for (DocumentChange userDoc : snapshot.getDocumentChanges()) {
                        LoginMod user = userDoc.getDocument().toObject(LoginMod.class);
                        String a="";

                      /*  UsersModel user = userDoc.getDocument().toObject(UsersModel.class);

                        if (user.name != null) {
                            if (userDoc.getType() == DocumentChange.Type.ADDED || userDoc.getType() == DocumentChange.Type.MODIFIED) {
                                usersData.setValue(user);
                            }
                        }*/
                    }
                }
            });

        //return usersData;
    }

    private void savedetails(String username, LoginMod logged_user) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
//            did now        String json = gson.toJson(allVideoList);

        String logged_userjson = gson.toJson(logged_user);
        editor.putString("username", username);
        editor.putString("logged_user", logged_userjson);
        editor.putString("saved", "1");
        editor.putString("phone", Uphone);
        editor.putString("pass", Upass);
        editor.apply();


     topBanner();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainL.getWindowToken(), 0);

    }

    private boolean getValues() {
        Uphone=phone.getText().toString();
        Upass=pass.getText().toString();
        return !Uphone.equals("") && !Upass.equals("");
    }

   /* private void calcHandView() {
        Point size = new Point();  getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        pageWidth=size.x;
        pageLength=size.y;

        int contslet= (int) (pageLength*.42);
        ViewTreeObserver viewTreeObserver = commentList.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    commentList.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                    commentList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (contslet  /1.1)));


                }
            });
        }

    }*/

    private void getUser() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("username", "");

        GlobalData.username=json;


        json=sharedPrefs.getString("logged_user", "");
        Type type2 = new TypeToken<LoginMod>() {
        }.getType();
        LoginMod logged_user = gson.fromJson(json, type2);
        GlobalData.Logged_user=logged_user;
        GlobalData.logged=true;
        topBanner();
       /* startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
*/
    }
    private void topBanner() {
        Toast.makeText(this, "downloading contents", Toast.LENGTH_SHORT).show();
dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("topbanner").document("top").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    List<String> link = new ArrayList<>();
                    link = (List<String>) documentSnapshot.get("image");


                    try {


                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(link);
                        editor.putString("topbanner", json);
                        editor.apply();

                        callPackage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void callPackage() {
        Toast.makeText(this, "installing changes", Toast.LENGTH_SHORT).show();

        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<List<Pack>> call = service.getPackage();

        try {


            call.enqueue(new Callback<List<Pack>>() {
                @Override
                public void onResponse(Call<List<Pack>> call, Response<List<Pack>> response) {

                    int a =response.code();
                    if (a == 200) {

                        List<Pack> listPack=new ArrayList<>();

                        for (int i=0;i<response.body().size();i++) {
                            if (response.body().get(i).getId().equals("2425471")) {

                            } else if (response.body().get(i).getId().equals("2074279")) {

                            }
                            else {
                                listPack.add(response.body().get(i));

                            }
                        }
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listPack);
                        editor.putString("packlist", json);
                        editor.apply();

                        GlobalData.packs=listPack;

                        callSpecialEvents();

                    }

                }

                @Override
                public void onFailure(Call<List<Pack>> call, Throwable t) {
                    String a;
                    callPackage();
                }
            });
            String a;
        } catch (Exception e) {
            String a;
            callPackage();
        }

    }

    private void callSpecialEvents() {
        Toast.makeText(this, "getting ready", Toast.LENGTH_SHORT).show();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bottombanner").document("bottom").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    List<String> link = new ArrayList<>();
                    link = (List<String>) documentSnapshot.get("image");

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(link);
                    editor.putString("bottombanner", json);
                    editor.apply();
                    dialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


    }
}
