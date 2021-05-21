package com.wisewolf.midhilaarts;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.Model.LoginMod;
import com.wisewolf.midhilaarts.Model.PlanTypeMod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    RelativeLayout contentView;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ArrayList allVideos=new ArrayList();
    ProgressDialog mProgressDialog;
    ImageView profilepic,gallery,camera;
    TextView name,packDetails;
    FirebaseStorage storage;
    StorageReference storageRef;
    private LinearLayout bottom_sheet;
    private BottomSheetBehavior sheetBehavior;
    DrawerLayout drawer;
    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    boolean doubleBackToExitPressedOnce = false;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String uri="";
    int flagNext=0;

     AlertDialog.Builder builder ;
     AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
      //toolbar.setNavigationIcon(R.drawable.ic_burger);
        setSupportActionBar(toolbar);
        bottomSheet();
        builBuilder();

        navigationView = findViewById(R.id.nav_view);
        FloatingActionButton fab = findViewById(R.id.fab);
        contentView = findViewById(R.id.content_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            }
        });
          drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


   //     Picasso.get().load("https://fontmeme.com/permalink/200823/07532fc03890c200da4f3661bfa89b8c.png").into(calligraphy);


        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawer, float slideOffset) {


                                     /*    contentView.setX(navigationView.getWidth() * slideOffset);



                                         DrawerLayout.LayoutParams lp =
                                             (DrawerLayout.LayoutParams) contentView.getLayoutParams();
                                         lp.height = drawer.getHeight() -
                                             (int) (drawer.getHeight() * slideOffset * 0.3f);
                                         lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                         contentView.setLayoutParams(lp);*/

                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_gallery,R.id.nav_courses,R.id.nav_trans, R.id.nav_slideshow,R.id.nav_settings,R.id.nav_qa,R.id.nav_aboutus)
            .setDrawerLayout(drawer)
            .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    /*    Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burger);*/

        View headerView = getLayoutInflater().inflate(R.layout.nav_header_home, navigationView, false);
        navigationView.addHeaderView(headerView);

        profilepic=headerView.findViewById(R.id.profile_pic);
        name=headerView.findViewById(R.id.name);
        packDetails=headerView.findViewById(R.id.pack_expiry);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        String saved = sharedPrefs.getString("walk", "");
        if (saved != null && saved.equals("")) {
            walkAround(headerView);
        }


        gallery=findViewById(R.id.bottomSheet_gallery);
        camera=findViewById(R.id.bottomSheet_camera);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImgGallery();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraopen();
            }
        });

        if (GlobalData.logged){
            name.setText(GlobalData.Logged_user.getUserName());
            packDetails.setText("");
       //     getprofilePic();

            String phone = sharedPrefs.getString("phone", "");
            String pass = sharedPrefs.getString("pass", "");
            if (phone != null) {
                if (phone.equals("")){
                    finishFunct();
                }
                else {
                    getUserEnableData(phone,pass);
                }
            }

        }
        else {
            name.setText("Midhila App");
            packDetails.setText("");

        }



      /*  profilepic.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
          alertforchangeprofile();
    }
   });*/


        if (GlobalData.logged){
            if (!GlobalData.Logged_user.getPlantype().equals("")){

                getPlanId();

            }
            else {
                uri = "/me/projects/2241147/videos";
                callVimeo();
            }
        }
        else {
            uri = "/me/projects/2241147/videos";
            callVimeo();
        }
    }

    private void finishFunct() {
        new AlertDialog.Builder(HomeActivity.this)
            .setTitle("Midhila Arts")
            .setMessage("App needs to sync , Please login again")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();

                    editor.putString("username", "json");
                    editor.putString("saved", "0");
                    editor.putString("logged_user", "0");
                    editor.apply();
                  startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                  finish();
                }
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("Close ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAffinity();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void getUserEnableData(String phone, String pass) {
        try {

            dialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<LoginMod>> call = service.loginCheck("/app/api/login/" + phone + "/" +pass);
            call.enqueue(new Callback<List<LoginMod>>() {
                @Override
                public void onResponse(Call<List<LoginMod>> call, Response<List<LoginMod>> response) {
                    String a = "";
                    if (response.body() != null) {
                        if (response.body().size() != 0) {
                            dialog.cancel();
                            GlobalData.logged = true;
                            GlobalData.username = response.body().get(0).getUserName();
                            GlobalData.Logged_user = response.body().get(0);

                        } else {
                            Toast.makeText(HomeActivity.this, "login error", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString("saved", "0");

                            editor.apply();
                            finish();

                        }
                    }


                }

                @Override
                public void onFailure(Call<List<LoginMod>> call, Throwable t) {
                    String a = "";

                }
            });


        } catch (Exception ignored) {
            String a = "";
        }
    }

    private void cameraopen() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private void takeImgGallery() {
        Intent intent = new Intent();
        //******call android default gallery
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //******code for crop image
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        try {
            intent.putExtra("return-data", true);
            startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                GALLERY_ACTIVITY_CODE);
        }
        catch (Exception e){}
    }

    private void alertforchangeprofile() {
        {
            new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Midhila Arts")
                .setMessage("Do you want to change profile..")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        drawer.closeDrawers();
                      ImagepicfromGallery();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Cancel ", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }
    }

    private void ImagepicfromGallery() {

        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void builBuilder() {
        builder  = new  AlertDialog.Builder(HomeActivity.this);
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

    private void getprofilePic() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        String ref="profileImages/"+GlobalData.Logged_user.getUserName()+".jpg" ;


        StorageReference islandRef = storageRef.child(ref);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                profilepic.setImageBitmap(bmp);
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               /* alertforchangeprofile();
                String a="";*/
            }
        });
    }

    private void callVimeo() {
        mProgressDialog = new ProgressDialog(HomeActivity.this);
        mProgressDialog.setMessage(" Please wait . . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

       // 9622e623337e621648be4f5d9bf45da5
      //  7da2733e3c625c3cd20558b450429ade
        final Configuration.Builder configBuilder =
            new Configuration.Builder("7da2733e3c625c3cd20558b450429ade")
                .setCacheDirectory(this.getCacheDir());
        VimeoClient.initialize(configBuilder.build());


        final VimeoClient mApiClient = VimeoClient.getInstance();
        // ---- Client Credentials Auth ----
        if (mApiClient.getVimeoAccount().getAccessToken() == null) {
            VimeoClient.getInstance().authorizeWithClientCredentialsGrant(new AuthCallback() {
                @Override
                public void success() {
                    String accessToken = VimeoClient.getInstance().getVimeoAccount().getAccessToken();
                    Configuration.Builder configBuilder =
                        new Configuration.Builder(accessToken);
                    VimeoClient.initialize(configBuilder.build());


                }

                @Override
                public void failure(VimeoError error) {
                    String errorMessage = error.getDeveloperMessage();

                }
            });
        }

        if (mApiClient.getVimeoAccount().getAccessToken() == null) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        download();
    }

    private void bottomSheet() {
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // click event for show-dismiss bottom sheet

// callback for do something


    }

    private void download() {
        Downback DB = new Downback();
        DB.execute("");
    }

    private class Downback extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressDialog.cancel();

        }

        @Override
        protected String doInBackground(String... strings) {



                VimeoClient.getInstance().fetchNetworkContent(uri, new ModelCallback<VideoList>(VideoList.class) {
                    @Override
                    public void success(VideoList videoList) {


                        if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {

                            if (flagNext == 0) {
                                ArrayList allVideoList = new ArrayList();
                                allVideoList = (videoList.data);
                                GlobalData.demovideos = (videoList.data);
                            }
                            else {
                                GlobalData.demovideos .addAll (videoList.data);
                            }

                            if (videoList.paging.next!=null){
                                flagNext=1;
                                uri=videoList.paging.next;
                                download();
                            }


                        }
                    }


                    @Override
                    public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                        String a="";
                        super.onResponse(call, response);
                    }

                    @Override
                    public void failure(VimeoError error) {
                        String a = String.valueOf(error);
                    }
                });

                return null;



        }


    }

    private void getPlanId() {

        try {


            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<PlanTypeMod>> call = service.planType("/app/api/userplan/"+GlobalData.Logged_user.getId());
            call.enqueue(new Callback<List<PlanTypeMod>>() {
                @Override
                public void onResponse(Call<List<PlanTypeMod>> call, Response<List<PlanTypeMod>> response) {
                    if (response.body() != null && response.body().size() != 0) {
                        GlobalData.planTypeMod = response.body().get(0);
                        uri = "/me/projects/" + GlobalData.planTypeMod.getVimeoId() + "/videos";
                        packDetails.setText("Pack Start Date " + GlobalData.planTypeMod.getStartDate());
                      //  callVimeo();
                    }

                }

                @Override
                public void onFailure(Call<List<PlanTypeMod>> call, Throwable t) {
                    String a = "";

                }
            });


        }
        catch (Exception ignored){
            String a="";
        }

    }

    private void walkAround(View headerView) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("walk", "1");
        editor.apply();


        SimpleTarget thirdTarget =
            new SimpleTarget.Builder(HomeActivity.this).setPoint(headerView.findViewById(R.id.profile_pic))
                .setRadius(200f)
                .setTitle("Find Your Videos")

                .setDescription("Once a pack is purchased go here to find 'myCourse' to find your videos")
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {

                    }

                    @Override
                    public void onEnded(SimpleTarget target) {

                    }
                })
                .build();



        profilepic.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                profilepic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Spotlight.with(HomeActivity.this)
                    .setClosedOnTouchedOutside(true)
                    .setOverlayColor(ContextCompat.getColor(HomeActivity.this, R.color.background)) // background overlay color
                    .setDuration(1000L) // duration of Spotlight emerging and disappearing in ms
                    .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
                    .setTargets(thirdTarget) // set targets. see below for more info
                    .setClosedOnTouchedOutside(true) // set if target is closed when touched outside
                    .setOnSpotlightStartedListener(new OnSpotlightStartedListener() { // callback when Spotlight starts
                        @Override
                        public void onStarted() {
                              Toast.makeText(HomeActivity.this, "spotlight is started", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
            || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {

        if (reqCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            profilepic.setImageBitmap(photo);
            UploadprofilePic(photo);
        }

       else if (reqCode == GALLERY_ACTIVITY_CODE) {
            try {
                if (data.getData()!=null){
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                if (imageUri != null) {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    profilepic.setImageBitmap(selectedImage);
                    UploadprofilePic(selectedImage);
                }
                else {
                    Toast.makeText(this, "camera permision not given", Toast.LENGTH_SHORT).show();
                }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
       else {

            super.onActivityResult(reqCode, resultCode, data);
        }
    }

    private void UploadprofilePic(Bitmap selectedImage) {
        String ref="profileImages/"+GlobalData.Logged_user.getUserName()+".jpg" ;
        StorageReference photoRef = storageRef.child(ref);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                try {
                    uploadNew(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                try {
                    uploadNew(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void uploadNew(Bitmap selectedImage) throws Exception{
        String ref = "profileImages/" + GlobalData.Logged_user.getUserName() + ".jpg";
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        StorageReference imageRef = storageReferenceProfilePic.child(ref);
        imageRef.putBytes(byteconvert(selectedImage))

            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //if the upload is successful
                    //hiding the progress dialog
                    //and displaying a success toast
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    mProgressDialog.cancel();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //if the upload is not successful
                    //hiding the progress dialog
                    mProgressDialog.cancel();
                    //and displaying error message
                    Toast.makeText(HomeActivity.this, exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //calculating progress percentage
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        //displaying percentage in progress dialog
//                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    mProgressDialog.setMessage("Uploading " + ((int) progress) + "%...");
                }
            });

    }

    private byte[] byteconvert(Bitmap selectedImage) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
       // selectedImage.recycle();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
