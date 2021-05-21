package com.wisewolf.midhilaarts.ui.myCourse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.midhilaarts.Activity.Fullscreen;
import com.wisewolf.midhilaarts.Adapters.CommentAdapter;
import com.wisewolf.midhilaarts.AutoResizeTextView;
import com.wisewolf.midhilaarts.Gallery;
import com.wisewolf.midhilaarts.GalleryHead;
import com.wisewolf.midhilaarts.GalleryImgAdapter;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.HomeActivity;
import com.wisewolf.midhilaarts.LoginActivity;
import com.wisewolf.midhilaarts.Model.Pack;
import com.wisewolf.midhilaarts.PackageDetails;
import com.wisewolf.midhilaarts.PackageVideoAdapter;
import com.wisewolf.midhilaarts.Package_Adapter;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.RetrofitClientInstance;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCourse extends Fragment implements  Player.EventListener{

    private MyCourseViewModel myCourseViewModel;
    RecyclerView video_list,commentList;
    int pageLength=0,pageWidth;
     AlertDialog.Builder builder ;
     AlertDialog dialog;
    ConstraintLayout layout;
    CardView buttonCard;
    View bottomsheet;

    ImageView preview;
    ImageView fullscreenButton;
    boolean fullscreenFlaf = false;
    TextView commentbtn,daysLeftcard;
    Bitmap commentimage;
    PackageVideoAdapter PackageVideoAdapter;
    int flagNext=0;

    private final int GALLERY_ACTIVITY_CODE=400;

    LinearLayout comment,detailsLinear;
    TextView newVideos,expiryDetails,videowatched,cHead,fullscreen,
    userid,duration,sDate,videosWatch,plandetail;
    int completedvideo=0;
    String thumburl="",mediaUrl;
    int flag=0;
    String   uri="";

    ProgressBar progressBar;
    AlertDialog dialogComment ;

    PlayerView exoplayerView;
    int videoCommntFlag=0;


    private String mVedioUrl;
    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;

    private BottomSheetBehavior bottomSheetBehavior;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myCourseViewModel =
            ViewModelProviders.of(this).get(MyCourseViewModel.class);
        View root = inflater.inflate(R.layout.fragmentmycourse, container, false);

        //requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
   /*     Window window = requireActivity().getWindow();
        WindowManager wm = requireActivity().getWindowManager();
        wm.removeViewImmediate(window.getDecorView());
        wm.addView(window.getDecorView(), window.getAttributes());*/


         setKeepScreenOn(getActivity().getWindow(), true);

        builBuilder();

        bottomsheet = root.findViewById(R.id.botomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        video_list = root.findViewById(R.id.videoListMycourse);
        layout= root.findViewById(R.id.totalLength);
        detailsLinear= root.findViewById(R.id.details_linear);
        commentList= root.findViewById(R.id.commentList);
        buttonCard= root.findViewById(R.id.buttonCard);
        exoplayerView=root.findViewById(R.id.exoplayerView);
        progressBar=root.findViewById(R.id.progressBar);
        commentbtn=root.findViewById(R.id.writecomment);
        fullscreenButton = exoplayerView.findViewById(R.id.exo_fullscreen_icon);



        comment= root.findViewById(R.id.comment);
        newVideos=root.findViewById(R.id.newVideos);
        expiryDetails=root.findViewById(R.id.expiryDetails);
        videowatched=root.findViewById(R.id.v_watched);
        cHead=root.findViewById(R.id.c_head);
        fullscreen=root.findViewById(R.id.fulscreen);
        daysLeftcard=root.findViewById(R.id.daysLeftcard);

        userid=root.findViewById(R.id.userid);
        duration=root.findViewById(R.id.duration);
        sDate=root.findViewById(R.id.startDate);
        videosWatch=root.findViewById(R.id.videosWatched);
        plandetail=root.findViewById(R.id.planName);

        video_list.setVisibility(View.VISIBLE);
        detailsLinear.setVisibility(View.GONE);
        fullscreen.setVisibility(View.GONE);

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long current=player.getCurrentPosition();
                Intent intent=new Intent(getActivity(), Fullscreen.class);
                intent.putExtra("url", mediaUrl);
                intent.putExtra("current", String.valueOf(current));
                startActivity(intent);
            }
            });

        commentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoCommntFlag!=0) {
                    alertComment();
                }else {
                    Toast.makeText(getContext(), "Select a video to post comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (GlobalData.logged){
            if (!GlobalData.Logged_user.getPlantype().equals("")){
                try {
                     getThumb();
                    videowatched.setText("Videos watched "+GlobalData.planTypeMod.getVideoNo());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            else {
                videowatched.setText("");
                cHead.setText("Demo Videos");
            }
       }

        else {
            videowatched.setText("");
            cHead.setText("Demo Videos");
        }


        expiryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expiryDetails.setBackground(getActivity().getDrawable(R.drawable.shape_curve));
                newVideos.setBackgroundColor(getResources().getColor(R.color.white));
                if (flag==1){
                    video_list.setVisibility(View.GONE);
                    detailsLinear.setVisibility(View.VISIBLE);
                }
                else {
                    video_list.setVisibility(View.GONE);
                    detailsLinear.setVisibility(View.VISIBLE);
                    flag=1;
                }
            }
        });

        newVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newVideos.setBackground(getActivity().getDrawable(R.drawable.shape_curve));
                expiryDetails.setBackgroundColor(getResources().getColor(R.color.white));
                if (flag==1){
                    video_list.setVisibility(View.VISIBLE);
                    detailsLinear.setVisibility(View.GONE);
                }
                else {
                    video_list.setVisibility(View.VISIBLE);
                    detailsLinear.setVisibility(View.GONE);
                }
            }
        });

        if (GlobalData.logged) {
            if (GlobalData.Logged_user.getPlantype().equals("")){
                comment.setVisibility(View.GONE);
                newVideos.setText("Free Class");
                expiryDetails.setVisibility(View.GONE);
                try {
                    alertPlan();
                    callPackage();
                    calcExpiry();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = format.format(c);

                Date dateExp = null;
                if (GlobalData.planTypeMod.getExpiry()!=null) {
                    try {

                        dateExp = format.parse(GlobalData.planTypeMod.getExpiry());
                        Date today = format.parse(date);
                        if (today != null) {
                            if (today.before(dateExp)) {
                                try {
                                    if (GlobalData.Logged_user.isActive()) {
                                        dialog.show();
                                      //  myCourses();
                                        uri = "/me/projects/" + GlobalData.planTypeMod.getVimeoId() + "/videos";
                                        Toast.makeText(getContext(), "preparing your classes !!! Please wait", Toast.LENGTH_SHORT).show();

                                        download();
                                    }
                                    //TODO disable user perform
                                /*else {
                                    Toast.makeText(getContext(), "User blocked..contact office", Toast.LENGTH_SHORT).show();
                                    comment.setVisibility(View.GONE);
                                    newVideos.setText("Free Class");
                                    expiryDetails.setVisibility(View.GONE);
                                    try {
                                         callPackage();
                                        calcExpiry();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                comment.setVisibility(View.GONE);
                                newVideos.setText("Free Class");
                                expiryDetails.setVisibility(View.GONE);
                                try {
                                    alertPlanexp();
                                    callPackage();
                                    calcExpiry();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
        else {
            comment.setVisibility(View.GONE);
            newVideos.setText("Free Class");
            expiryDetails.setVisibility(View.GONE);
            alert();
            try {
                callPackage();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        myCourseViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }



    public   void setKeepScreenOn(Window window, boolean isOn) {

        if (window == null) {
            return;
        }
        if (isOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    private void alertComment() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setIcon(R.drawable.login);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_comments, null);

        ImageView cam =  customLayout.findViewById(R.id.camera_comment);
          preview =  customLayout.findViewById(R.id.commentImage);
        Button save =  customLayout.findViewById(R.id.commentSave);
        EditText commnt =  customLayout.findViewById(R.id.commentText);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImgGallery();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmt=commnt.getText().toString();
                dialog.show();
                  uploadNew(commentimage,cmt);
            }
        });


        builder.setView(customLayout);
        // add a button

        // create and show the alert dialog
        dialogComment= builder.create();
        dialogComment.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogComment.show();
    }

    private void calcExpiry() {
        expiryDetails.setVisibility(View.GONE);
    }

    private void builBuilder() {
        builder  = new  AlertDialog.Builder(getContext());
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

    private void getThumb() {
        for (int i=0;i<GlobalData.packs.size();i++){
            Pack pack=GlobalData.packs.get(i);
            if (GlobalData.planTypeMod!=null){
            if (pack.getId().equals(GlobalData.planTypeMod.getVimeoId())){
                thumburl=pack.getThumbnail();

                cHead.setText(pack.getCourseName());

                plandetail.setText("Plan Name "+pack.getCourseName());
            }
            }
        }
        userid.setText("User ID - "+GlobalData.Logged_user.getId());
        if (GlobalData.planTypeMod!=null) {
            duration.setText("Duration - " + GlobalData.planTypeMod.getDuration() + " Month");
            sDate.setText("Start Date - " + GlobalData.planTypeMod.getStartDate());
            videosWatch.setText("Total Watched videos - " + GlobalData.planTypeMod.getVideoNo());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {

                Date datePack = format.parse(GlobalData.planTypeMod.getExpiry());
                Date dateStart = format.parse(GlobalData.planTypeMod.getStartDate());
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String today = df.format(c);

                long startTime = dateStart.getTime();
                long endTime = datePack.getTime();
                long difference = datePack.getTime() - c.getTime();
                float daysBetween = (difference / (1000*60*60*24));
                daysLeftcard.setText(String.valueOf(daysBetween));

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    private void alert() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setIcon(R.drawable.login);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_login, null);
        builder.setView(customLayout);
        // add a button

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertPlan() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setIcon(R.drawable.login);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_plan, null);
        builder.setView(customLayout);
        // add a button

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertPlanexp() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setIcon(R.drawable.login);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_planexp, null);
        builder.setView(customLayout);
        // add a button

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mediaPlay(String link) {
        Intent intent=new Intent(getActivity(), Fullscreen.class);
        intent.putExtra("url", link);
        intent.putExtra("current", String.valueOf(0));
        startActivity(intent);
       /* dialog.show();
        mediaUrl=link;
         mVedioUrl=link;
         setUp();*/


    }

    private void alertComplete() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alertcomplete, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               updatePlan();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updatePlan() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("plan_type").document(GlobalData.Logged_user.getId())
            .update("video_no", String.valueOf(completedvideo));
        GlobalData.planTypeMod.setVideoNo(String.valueOf(completedvideo));

    }

    private void myCourses() {

        calcHandView();
        int i = pageWidth;

        try {
            if (GlobalData.demovideos != null) {

                sortVideos(GlobalData.demovideos);
                 dialog.cancel();
                  PackageVideoAdapter = new
                    PackageVideoAdapter(GlobalData.demovideos, video_list, i, pageLength, new PackageVideoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Video item) throws Exception {
                        String[] videoNum = item.description.split("-");
                        int userPackDuration= Integer.parseInt(GlobalData.planTypeMod.getPackPeriod());
                        int videoPackDuration=Integer.parseInt(videoNum[2]);
                        if (videoPackDuration<=userPackDuration) {
                            int uservideNum = Integer.parseInt(GlobalData.planTypeMod.getVideoNo());
                            int VimeoVideoNum = Integer.parseInt(videoNum[0]);
                            if (VimeoVideoNum - 1 <= uservideNum) {
                                int found = 0;
                                boolean iswify;
                                iswify = checknetworkSpeed();

                                if (iswify) {
                                    for (int i = 0; i < item.files.size(); i++) {
                                        if (item.files.get(i).height == 540) {
                                            completedvideo = VimeoVideoNum;
                                            GlobalData.completed = completedvideo;
                                            String[] parts = item.uri.split("/");
                                            try {
                                                videoCommntFlag = Integer.parseInt(parts[2]);
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                            loadComment(videoCommntFlag);
                                            mediaPlay(item.files.get(i).link);
                                        }
                                    }
                                }
                                else
                                {
                                    for (int i = 0; i < item.files.size(); i++) {
                                        if (item.files.get(i).height == 240) {
                                            completedvideo = VimeoVideoNum;
                                            GlobalData.completed = completedvideo;
                                            String[] parts = item.uri.split("/");
                                            try {
                                                videoCommntFlag = Integer.parseInt(parts[2]);
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                            loadComment(videoCommntFlag);
                                            mediaPlay(item.files.get(i).link);
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), "Complete watching older videos", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "You purchased basic package , please upgrade pack", Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                video_list.setAdapter(PackageVideoAdapter);
                LinearLayoutManager added_liste_adapterlayoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

                video_list.setLayoutManager(added_liste_adapterlayoutManager);
            }
            else {

                download();
                Toast.makeText(getContext(), "loading your content !!! Please wait", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void download() {
        final Configuration.Builder configBuilder =
            new Configuration.Builder("7da2733e3c625c3cd20558b450429ade")
                .setCacheDirectory(getContext().getCacheDir());
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
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        }
         Downback DB = new  Downback();
        DB.execute("");
    }

    private class Downback extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
           progressBar.setVisibility(View.VISIBLE);

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
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            GlobalData.demovideos .addAll (videoList.data);
                            flagNext=0;
                        }

                        if (videoList.paging.next!=null){
                            flagNext=1;
                            uri=videoList.paging.next;
                            download();
                        }
                        else {
                            myCourses();
                        }

                    }
                }

                @Override
                public void failure(VimeoError error) {
                    String a = String.valueOf(error);
                    download();
                }
            });

            return null;



        }


    }

    private void sortVideos(ArrayList demovideos) throws Exception {

        try {
            Collections.sort(demovideos, new Comparator<Video>() {
                @Override
                public int compare(Video lhs, Video rhs) {

                    return Integer.valueOf(lhs.description.split("-")[0]).compareTo(Integer.valueOf(rhs.description.split("-")[0]));
                }
            });
            GlobalData.demovideos = demovideos;

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean checknetworkSpeed() {
        ConnectivityManager cm =
            (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting();
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        return isWiFi;
    }

    private void callPackage() {

        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<List<Pack>> call = service.getPackage();

        try {


            call.enqueue(new Callback<List<Pack>>() {
                @Override
                public void onResponse(Call<List<Pack>> call, Response<List<Pack>> response) {

                    int a =response.code();
                    if (a == 200) {
                        //TODO add data addDb(response.body());
                        Package_Adapter package_adapter = new
                            Package_Adapter(response.body(), video_list, new Package_Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Pack item) {

                                demovideo(item.getVimeoId());

                            }
                        });
                        video_list.setAdapter(package_adapter);
                        LinearLayoutManager recent_add_package_adapterlayoutManager
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        video_list.setLayoutManager(recent_add_package_adapterlayoutManager);


                    }

                }

                @Override
                public void onFailure(Call<List<Pack>> call, Throwable t) {
                    String a;
                }
            });
            String a;
        } catch (Exception e) {
            String a;
        }

    }

    private void calcHandView() {
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

        }

    private void demovideo(String courseName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pack_demo").document("demo").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {


                    try {
                        HashMap<String, String> data  = (HashMap<String, String>)documentSnapshot.get(courseName);

                     String   demoLink = data.get("player");

                        mediaPlay(demoLink);


                    } catch (Exception e) {
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }


    private void setUp() {
        initializePlayer();
        if (mVedioUrl == null) {
            return;
        }
        buildMediaSource(Uri.parse(mVedioUrl));
    }

    private void initializePlayer() {
        if (player == null) {
            // 1. Create a default TrackSelector
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player
            player =
                ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            exoplayerView.setPlayer(player);
        }
    }

    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
       /* DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)), bandwidthMeter);
       */
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
            Util.getUserAgent(getContext(), getString(R.string.app_name)),
            null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true /* allowCrossProtocolRedirects */
        );


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
            null /* listener */,
            httpDataSourceFactory
        );
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mUri);
        // Prepare the player with the source.
        player.prepare(videoSource);

        player.setPlayWhenReady(true);
        player.addListener(this);

    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                progressBar.setVisibility(View.VISIBLE);

                break;
            case Player.STATE_ENDED:
                endedFunct();
                break;
            case Player.STATE_IDLE:
                break;
            case Player.STATE_READY:
                dialog.cancel();
                progressBar.setVisibility(View.GONE);
                fullscreen.setVisibility(View.GONE);

                comment.setVisibility(View.GONE);
                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
        }
    }

    private void endedFunct() {
        fullscreen.setVisibility(View.GONE);
        if (GlobalData.logged){
            if (!GlobalData.Logged_user.getPlantype().equals("")){
                int videoNum=Integer.valueOf(GlobalData.planTypeMod.getVideoNo());
                if (completedvideo>videoNum) {
                    alertComplete();
                    PackageVideoAdapter.notifyDataSetChanged();

                }

            }
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onSeekProcessed() {
    }

    private void takeImgGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_ACTIVITY_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
             startActivityForResult(intent, GALLERY_ACTIVITY_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == GALLERY_ACTIVITY_CODE) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                preview.setImageBitmap(selectedImage);
                preview.setVisibility(View.VISIBLE);
                commentimage=selectedImage;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getContext(), "  haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void uploadNew(Bitmap selectedImage, String cmt) {
        if (selectedImage!=null) {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
            String date = df.format(c);
            String ref = "comment/" + GlobalData.Logged_user.getUserName() + date + ".jpg";

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
            StorageReference imageRef = storageReferenceProfilePic.child(ref);
            imageRef.putBytes(byteconvert(selectedImage))


                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String a=uri.toString();
                                updateComment(cmt, a);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String a="";
                            }
                        });



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //if the upload is not successful
                        //hiding the progress dialog
                        dialog.cancel();
                        //and displaying error message
                    }
                });
        }
        else {
            updateComment(cmt, "");
            dialog.show();
        }

    }

    private void updateComment(String coment, String image) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = df.format(c);

//TODO duration change
            HashMap hm = new HashMap();
            hm.put("image", image);
            hm.put("message", coment);
            hm.put("product_id", GlobalData.planTypeMod.getVimeoId());
            hm.put("product_sub_id", String.valueOf(videoCommntFlag));
            hm.put("user_name", GlobalData.username);
            hm.put("date",date);
            hm.put("from","Android");

            db.collection("comment").add(hm)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    dialog.cancel();
                        dialogComment.cancel();
                        loadComment(videoCommntFlag);

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

    private byte[] byteconvert(Bitmap selectedImage) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        // selectedImage.recycle();
        return byteArray;
    }

    private void loadComment(int videoCommntFlag) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("comment").
            whereEqualTo("product_sub_id",String.valueOf(videoCommntFlag)).
            whereEqualTo("product_id", GlobalData.planTypeMod.getVimeoId())
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> aaq=   queryDocumentSnapshots.getDocuments();
                    List<String> messages=new ArrayList<>();
                    List<String> dates=new ArrayList<>();
                    List<String> Uname=new ArrayList<>();
                    List<String> images=new ArrayList<>();
                    for (int i=0;i<aaq.size();i++){
                        DocumentSnapshot ne = aaq.get(i);
                        Object a= ne.get("message");
                        messages.add(String.valueOf(a));
                          a= ne.get("image");
                        images.add(String.valueOf(a));
                          a= ne.get("user_name");
                        Uname.add(String.valueOf(a));
                          a= ne.get("date");
                        dates.add(String.valueOf(a));
                    }
                     setAdapter(messages,dates,Uname,images);


                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
    }

    private void setAdapter(List<String> messages, List<String> dates, List<String> uname, List<String> images) {
        CommentAdapter commentAdapter = new CommentAdapter(messages,dates,uname,images,commentList,getContext());

        commentList.setAdapter(commentAdapter);
        LinearLayoutManager galleryHeadlayoutManager
            = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        commentList.setLayoutManager(galleryHeadlayoutManager);
    }

}
