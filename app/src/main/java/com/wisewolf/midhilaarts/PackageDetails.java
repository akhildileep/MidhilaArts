package com.wisewolf.midhilaarts;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hosseiniseyro.apprating.AppRatingDialog;
import com.hosseiniseyro.apprating.listener.RatingDialogListener;
import com.squareup.picasso.Picasso;
import com.vimeo.networking.model.Video;
import com.wisewolf.midhilaarts.Activity.FeedbackAct;
import com.wisewolf.midhilaarts.Activity.Fullscreen;
import com.wisewolf.midhilaarts.Adapters.FeeAdapter;
import com.wisewolf.midhilaarts.Adapters.PackSelectAdapter;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.Model.Pack;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageDetails extends AppCompatActivity implements  Player.EventListener , RatingDialogListener {

    ProgressBar progressBar;

    PlayerView exoplayerView;

    private String mVedioUrl;
    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;


    TextView textLength, heading, taught, group, purchaseButton,closesheet;
    ImageView mainPic;
    LinearLayout ratingLayout;

    CardView add_pack,add_packM;
    ScrollView scroll;
    AutoResizeTextView packbuy;

    List<String> priceList = new ArrayList<>();
    List<String> amount = new ArrayList<>();
    int isRated = 0, isRatedValue = 0;

    List<String> duration = new ArrayList<>();
    List<String> price = new ArrayList<>();
    List<String> disc = new ArrayList<>();
    ImageView exo_play,urRate;
    String mediaUrl="";


    String pack_selected_amount,duratn,pack_selected_price,pack_selected_dur,pack_selected_disc, demoLink = "";
    MediaController mediaController;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    int lengthTextview, widthTextview, ratingLayoutLength;
    RecyclerView colorslab, package_gallery, pricelist, feestructRecycl;
    private List<Model_viewPager> model_viewPagers = new ArrayList<>();
    TextView packDetails, packBenifits, rated, purchased, anchor;
    LinearLayout tag;
    int play = 0;
    boolean tagFlag = false;
    boolean packSelected = false;
    List<String> cleanedTools = new ArrayList<>();
    ImageView fullscreenButton;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_details);
        init();

        mediaController = new MediaController(PackageDetails.this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        builBuilder();

        try {
            alreadyUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        actionBar.hide();
        calcHandView();
        try {
            callPackage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        demovideo();


        add_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.logged) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    startActivity(new Intent(PackageDetails.this, SignUp.class));
                }
            }
        });
        add_packM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.logged) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    add_packM.setVisibility(View.GONE);
                } else {
                    startActivity(new Intent(PackageDetails.this, SignUp.class));
                }
            }
        });

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaUrl.equals("")) {
                    long current = player.getCurrentPosition();
                    Intent intent = new Intent(PackageDetails.this, Fullscreen.class);
                    intent.putExtra("url", mediaUrl);
                    intent.putExtra("current", String.valueOf(current));
                    startActivity(intent);
                }
            }
        });


        urRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.logged) {
                    if (!GlobalData.Logged_user.getPlantype().equals("")) {
                        if (GlobalData.planTypeMod.getVimeoId().equals(GlobalData.packSelected.getVimeoId())) {
                            rateFunction();
                        } else {
                            Toast.makeText(PackageDetails.this, "you can rate only purchased pack", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (packSelected == true) {
                    String type="buy";
                    if (packbuy.getText().equals("upgrade")){
                        type="upgrade";
                    }

                    Intent intent = new Intent(PackageDetails.this, PaymentActivity.class);
                    intent.putExtra("name", GlobalData.username);
                    intent.putExtra("desc", GlobalData.packSelected.getCourseName());
                    intent.putExtra("amt", pack_selected_amount);
                    intent.putExtra("price", pack_selected_amount);
                    intent.putExtra("dur", pack_selected_dur);
                    intent.putExtra("disc", pack_selected_disc);
                    intent.putExtra("type", type);

                    startActivity(intent);
                } else {
                    Toast.makeText(PackageDetails.this, "select a pack", Toast.LENGTH_SHORT).show();
                }

            }
        });
        exo_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play = 1;
                player.setPlayWhenReady(true);
                player.getPlaybackState();
            }
        });
        closesheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                add_packM.setVisibility(View.VISIBLE);
            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        add_packM.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            float y = 0;

            @Override
            public void onScrollChanged() {
                if (scroll != null) {
                    int x = scroll.getMaxScrollAmount();
                    if (scroll.getScrollY() <= x * .20) {
                        taginVisible();


                    } else {
                        tagvisible();

                    }


                }
            }
        });


    }

    private void rateFunction() {
        if (isRated == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                new AppRatingDialog.Builder()
                    .setPositiveButtonText("Submit")
                    .setNegativeButtonText("Cancel")
                    .setNeutralButtonText("Later")
                    .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                    .setDefaultRating(isRatedValue)
                    .setThreshold(0)
                    .setTitle(GlobalData.packSelected.getCourseName())
                    .setDescription("your rated happiness.....")
                    .setStarColor(R.color.Basic)
                    .setTitleTextColor(R.color.black)
                    .setDescriptionTextColor(R.color.black)
                    .setDialogBackgroundColor(R.color.white)
                    .setWindowAnimation(R.style.MyDialogFadeAnimation)
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .create(PackageDetails.this)
                    .show();
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                new AppRatingDialog.Builder()
                    .setPositiveButtonText("Submit")
                    .setNegativeButtonText("Cancel")
                    .setNeutralButtonText("Later")
                    .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                    .setDefaultRating(1)
                    .setThreshold(0)
                    .setTitle(GlobalData.packSelected.getCourseName())
                    .setDescription("your rated happiness.....")
                    .setStarColor(R.color.Basic)
                    .setTitleTextColor(R.color.black)
                    .setDescriptionTextColor(R.color.black)
                    .setDialogBackgroundColor(R.color.white)
                    .setWindowAnimation(R.style.MyDialogFadeAnimation)
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .create(PackageDetails.this)
                    .show();
            }
        }
    }

    private void alreadyUser() throws Exception {
        if (GlobalData.logged) {
            if (!GlobalData.Logged_user.getPlantype().equals("")) {
                if (GlobalData.planTypeMod.getVimeoId().equals(GlobalData.packSelected.getVimeoId())) {
                    getPersonalRating();
                    try {
                        packbuy.setText("upgrade");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    add_pack.setVisibility(View.GONE);
                }

            }
        }
    }

    private void init() {
        exoplayerView = findViewById(R.id.exoplayerView_full);
            progressBar = findViewById(R.id.progressBar_ful);
        urRate = findViewById(R.id.urRate);
        textLength = findViewById(R.id.length);
        heading = findViewById(R.id.heading);
        taught = findViewById(R.id.taught);
        mainPic = findViewById(R.id.mainPic);
        tag = findViewById(R.id.tag_id);
        package_gallery = findViewById(R.id.package_gallery);
        colorslab = findViewById(R.id.colorslab);
        ratingLayout = findViewById(R.id.rateing);
        scroll = findViewById(R.id.scrollView2);
        packDetails = findViewById(R.id.packDetails);
        packBenifits = findViewById(R.id.packBenifits);
        add_pack = findViewById(R.id.add_pack);
        add_packM = findViewById(R.id.add_packM);
        group = findViewById(R.id.group);
        bottom_sheet = findViewById(R.id.bottom_sheet_packSelect);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        purchaseButton = findViewById(R.id.purchaseBottom);
        closesheet = findViewById(R.id.closeBottomsheet);
        pricelist = findViewById(R.id.pricelist);
        packbuy = findViewById(R.id.packtext);
        feestructRecycl = findViewById(R.id.feeStruct);
        rated = findViewById(R.id.rateText);
        purchased = findViewById(R.id.purchsed);
        anchor = findViewById(R.id.anchor);
        exo_play = exoplayerView.findViewById(R.id.exo_play);
        fullscreenButton = exoplayerView.findViewById(R.id.exo_fullscreen_icon);
    }



    private void packGallery() {
        dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("productGallery").document(GlobalData.packSelected.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null) {                           
                    List<String> link = new ArrayList<>();
                    link = (List<String>) documentSnapshot.get("url");
                    for (int i = 0; i < link.size(); i++) {
                        model_viewPagers.add(new Model_viewPager(link.get(i)));

                    }
                    packageGallery();
                    dialog.cancel();
                } else {
                    dialog.cancel();
                    findViewById(R.id.gallHead).setVisibility(View.GONE);
                }

            }
        })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    String a = "";
                }
            });


    }

   /* private void alreadyUser() {
        if (GlobalData.logged){
            add_pack.setVisibility(View.VISIBLE);
        }
        else {}
        new AlertDialog.Builder(PackageDetails.this)
            .setTitle("Development....")
            .setMessage("select which u want to see..")

            // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("New User", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    startActivity(new Intent(PackageDetails.this, PaymentActivity.class));
                }
            })

            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("Registered User  ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    add_pack.setVisibility(View.GONE);
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }*/

    private void demovideo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pack_demo").document("demo").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {


                    try {
                        HashMap<String, String> data  = (HashMap<String, String>)documentSnapshot.get(GlobalData.packSelected.getVimeoId());

                        demoLink = data.get("player");

                        mediaPlay(demoLink);

                        packGallery();
                    } catch (Exception e) {
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

    private void mediaPlay(String link) {
        mVedioUrl = link;
        mediaUrl=link;
        setUp();
    }

    private void tagvisible() {
        if (tagFlag == true) {
            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(tag, "scaleX", 0f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(tag, "scaleY", 0f);
            scaleDownX.setDuration(500);
            scaleDownY.setDuration(500);
            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.play(scaleDownX).with(scaleDownY);
            scaleDown.start();

            ObjectAnimator scaleDownX1 = ObjectAnimator.ofFloat(add_pack, "scaleX", 0f);
            ObjectAnimator scaleDownY1 = ObjectAnimator.ofFloat(add_pack, "scaleY", 0f);
            scaleDownX1.setDuration(500);
            scaleDownY1.setDuration(500);
            AnimatorSet scaleDown1 = new AnimatorSet();
            scaleDown1.play(scaleDownX1).with(scaleDownY1);
            scaleDown1.start();
            tagFlag = false;
        }
    }

    private void taginVisible() {
        if (tagFlag == false) {
            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(tag, "scaleX", 1f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(tag, "scaleY", 1f);
            scaleDownX.setDuration(500);
            scaleDownY.setDuration(500);

            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.play(scaleDownX).with(scaleDownY);

            scaleDown.start();

            ObjectAnimator scaleDownX1 = ObjectAnimator.ofFloat(add_pack, "scaleX", 1f);
            ObjectAnimator scaleDownY1 = ObjectAnimator.ofFloat(add_pack, "scaleY", 1f);
            scaleDownX1.setDuration(500);
            scaleDownY1.setDuration(500);
            AnimatorSet scaleDown1 = new AnimatorSet();
            scaleDown1.play(scaleDownX1).with(scaleDownY1);
            scaleDown1.start();
            tagFlag = true;
        }
    }

    private void calcHandView() {
        ViewTreeObserver viewTreeObserver = textLength.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    textLength.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int viewWidth = textLength.getWidth();
                    int viewHeight = textLength.getHeight();
                    lengthTextview = viewHeight;
                    widthTextview = viewWidth;

                    ratingLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ratingLayoutLength = ratingLayout.getHeight();

                    mainPic.setLayoutParams(new LinearLayout.LayoutParams((viewWidth), lengthTextview - (ratingLayoutLength / 2)));

                    LinearLayout.LayoutParams params = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                    params.setMargins((int) getResources().getDimension(R.dimen.left), (int) (ratingLayoutLength / 1.5), 0, 0);
                    heading.setLayoutParams(params);

                    scroll.scrollTo(0, 0);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (lengthTextview * .7));
                    params2.setMargins(20, 10, 20, 10);
                    exoplayerView.setLayoutParams(params2);

                  /*  LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (lengthTextview * .3));
                    params3.setMargins(20, 10, 20, 10);
                    add_packM.setLayoutParams(params3);*/

                }
            });
        }


    }

    private void callPackage() throws Exception {
        setThumb();
        heading.setText("PACKAGE : " + GlobalData.packSelected.getCourseName());
        taught.setText("Lead by faculty " + GlobalData.packSelected.getFaculity().getDetails());
        group.setText(GlobalData.packSelected.getAgeGroup());

        if (GlobalData.packSelected.getRatedBy().equals("0")) {
            rated.setText(GlobalData.packSelected.getRating());
        } else {
            Long rat = Long.valueOf(GlobalData.packSelected.getRating().replaceAll("\\s+", "")) / Long.valueOf(GlobalData.packSelected.getRatedBy().replaceAll("\\s+", ""));
            rated.setText(rat + "/5");
        }


        String[] tools = new String[100];
        int z = 0, x = 0, c = 0;
        for (int i = 0; i < GlobalData.packSelected.getTools().getColors().size(); i++) {
            tools[i] = ("COLOR : " + GlobalData.packSelected.getTools().getColors().get(i));
            cleanedTools.add(tools[i]);
            z = i + 1;
        }
        for (int i = 0; i < GlobalData.packSelected.getTools().getPencil().size(); i++) {
            tools[z + i] = ("Pencils : " + GlobalData.packSelected.getTools().getPencil().get(i));
            cleanedTools.add(tools[z + i]);
            x = z + i + 1;
        }
        for (int i = 0; i < GlobalData.packSelected.getTools().getPalate().size(); i++) {
            tools[x + i] = ("PALATE : " + GlobalData.packSelected.getTools().getPalate().get(i));
            cleanedTools.add(tools[x + i]);
        }


        ColorSlabAdapter colorSlabAdapter = new
            ColorSlabAdapter(cleanedTools, colorslab);
        colorslab.setAdapter(colorSlabAdapter);
        LinearLayoutManager recent_add_package_adapterlayoutManager
            = new LinearLayoutManager(PackageDetails.this, LinearLayoutManager.HORIZONTAL, false);
        colorslab.setLayoutManager(recent_add_package_adapterlayoutManager);
        String benifits = "";
        packDetails.setText(GlobalData.packSelected.getCourseDescription());
        for (int i = 0; i < GlobalData.packSelected.getBenifit().size(); i++) {
            if (i == 0) {
                benifits = (GlobalData.packSelected.getBenifit().get(i));
            } else
                benifits = benifits + "\n\n" + (GlobalData.packSelected.getBenifit().get(i));
        }
        packBenifits.setText(benifits);

        String feestruct = "";
        for (int i = 0; i < GlobalData.packSelected.getPrice().size(); i++) {

            duration.add(GlobalData.packSelected.getPrice().get(i).getDuration());
            price.add(GlobalData.packSelected.getPrice().get(i).getAmount());
            disc.add(GlobalData.packSelected.getPrice().get(i).getDiscount());

            if (i == 0) {

                feestruct = (GlobalData.packSelected.getPrice().get(i).getDuration() + " Rs " +
                    GlobalData.packSelected.getPrice().get(i).getAmount() + ", Discount Rs " +
                    GlobalData.packSelected.getPrice().get(i).getDiscount());
                priceList.add(GlobalData.packSelected.getPrice().get(i).getDuration() + "Month ,  Rs " +
                    GlobalData.packSelected.getPrice().get(i).getDiscount());
                amount.add(GlobalData.packSelected.getPrice().get(i).getDiscount());
            } else {
                feestruct = feestruct + "\n\n" + (GlobalData.packSelected.getPrice().get(i).getDuration() + " Rs " +
                    GlobalData.packSelected.getPrice().get(i).getAmount() + " , Discount Rs " +
                    GlobalData.packSelected.getPrice().get(i).getDiscount());
                priceList.add(GlobalData.packSelected.getPrice().get(i).getDuration() + "Month ,  Rs " +
                    GlobalData.packSelected.getPrice().get(i).getDiscount());
                amount.add(GlobalData.packSelected.getPrice().get(i).getDiscount());

            }
        }
        FeeAdapter feeAdapter = null;
        feeAdapter = new FeeAdapter(duration, price, disc, feestructRecycl);
        feestructRecycl.setAdapter(feeAdapter);
        LinearLayoutManager linearLayoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        feestructRecycl.setLayoutManager(linearLayoutManager);


        PackSelectAdapter packSelectAdapter = null;
        packSelectAdapter = new PackSelectAdapter(priceList, pricelist,price,duration,disc, new PackSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String list,String price,String dur,String disc) {
                packSelected = true;
                pack_selected_amount=price;
                pack_selected_price=price;
                pack_selected_dur=dur;
                pack_selected_disc=disc;
            }
        });
        pricelist.setAdapter(packSelectAdapter);
        LinearLayoutManager subj_manager
            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pricelist.setLayoutManager(subj_manager);


        dialog.cancel();

    /*    final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<List<Pack>> call = service.getPackage();
        try {


            call.enqueue(new Callback<List<Pack>>() {
                @Override
                public void onResponse(Call<List<Pack>> call, Response<List<Pack>> response) {

                    int a =response.code();
                    if (a == 200) {
                        //TODO add data addDb(response.body());
                        ColorSlabAdapter  colorSlabAdapter = new
                            ColorSlabAdapter(response.body().get(0), colorslab);
                        colorslab.setAdapter(colorSlabAdapter);
                        LinearLayoutManager recent_add_package_adapterlayoutManager
                            = new LinearLayoutManager(PackageDetails.this, LinearLayoutManager.HORIZONTAL, false);
                        colorslab.setLayoutManager(recent_add_package_adapterlayoutManager);
                        dialog.cancel();
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
        }*/

    }

    private void setThumb() {
        try {
            Picasso.get().load(GlobalData.packSelected.getThumbnail()).into(mainPic);

        } catch (Exception e) {
        }

    }

    private void packageGallery() {

        try {
            Gallery_Adapter galleryAdapter = new
                Gallery_Adapter(model_viewPagers, package_gallery);
            package_gallery.setAdapter(galleryAdapter);
            LinearLayoutManager recent_add_package_adapterlayoutManager
                = new LinearLayoutManager(PackageDetails.this, LinearLayoutManager.HORIZONTAL, false);
            package_gallery.setLayoutManager(recent_add_package_adapterlayoutManager);
        } catch (Exception e) {
        }

    }

    private void builBuilder() {
        builder = new AlertDialog.Builder(PackageDetails.this);
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

    private void getPersonalRating() {
        dialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("personal_rating").document(GlobalData.Logged_user.getId())
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    dialog.cancel();
                    if (documentSnapshot.exists()) {
                        try {
                            Object a = documentSnapshot.get("rate");
                            purchased.setText(new StringBuilder().append("your rate : ").append(a).toString());
                            assert a != null;
                            isRatedValue = Integer.parseInt(a.toString());
                            isRated = 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        isRated = 0;
                        isRatedValue = 0;
                    }

                }
            })
            .addOnFailureListener(new OnFailureListener() {
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
                ExoPlayerFactory.newSimpleInstance(this, trackSelector);
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
            Util.getUserAgent(this, getString(R.string.app_name)),
            null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true /* allowCrossProtocolRedirects */
        );


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
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
    protected void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resumePlayer();
    }

    @Override
    protected void onDestroy() {
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
                // Activate the force enable
                break;
            case Player.STATE_IDLE:
                break;
            case Player.STATE_READY:
                progressBar.setVisibility(View.GONE);
                if (play == 0) {
                    player.seekTo(1);
                    player.setPlayWhenReady(false);
                }
                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
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


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClickedWithComment(int i, @NotNull String s) {

    }

    @Override
    public void onPositiveButtonClickedWithoutComment(int i) {

        if (isRated == 1) {
            updatePersonalRating(i);
        }
        else {
            addpersonalRating(i);
        }

    }

    private void addpersonalRating(int i) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            HashMap hm = new HashMap();
            hm.put("rate", String.valueOf(i));

            db.collection("personal_rating").document(GlobalData.Logged_user.getId())
                .set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    updateProductMasternew(i);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String a="";
                }
            });



        } catch (Exception e) {
            String a = "";
        }
    }

    private void updateProductMasternew(int i) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int rate = i ;
        rate = Integer.parseInt(GlobalData.packSelected.getRating()) + rate;
        int finalRate = rate;
        db.collection("product").document(GlobalData.packSelected.getId())
            .update("rating", String.valueOf(rate)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                int noOfusers=1;
                noOfusers = Integer.parseInt(GlobalData.packSelected.getRatedBy()) + noOfusers;

                int finalNoOfusers = noOfusers;
                db.collection("product").document(GlobalData.packSelected.getId())
                    .update("ratedBy", String.valueOf(noOfusers)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getPersonalRating();
                        packRatingUpdate(finalRate, finalNoOfusers);
                    }
                });
            }
        });

    }

    private void packRatingUpdate(int finalRate, int finalNoOfusers) {
        GlobalData.packSelected.setRating(String.valueOf(finalRate));
        GlobalData.packSelected.setRatedBy(String.valueOf(finalNoOfusers));
        Long rat = Long.valueOf(GlobalData.packSelected.getRating().replaceAll("\\s+", "")) / Long.valueOf(GlobalData.packSelected.getRatedBy().replaceAll("\\s+", ""));
        rated.setText(rat + "/5");
    }

    private void updatePersonalRating(int i) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            HashMap hm = new HashMap();
            hm.put("rate", String.valueOf(i));

            db.collection("personal_rating").document(GlobalData.Logged_user.getId())
                .update("rate", String.valueOf(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    updateProductMaster(i);
                }
            });




        } catch (Exception e) {
            String a = "";
        }
    }

    private void updateProductMaster(int i) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (i < isRatedValue) {
            try {

                int rate = isRatedValue - i;
                rate = Integer.parseInt(GlobalData.packSelected.getRating()) - rate;

                int finalRate = rate;
                db.collection("product").document(GlobalData.packSelected.getId())
                    .update("rating", String.valueOf(rate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getPersonalRating();
                        packRatingUpdate(finalRate);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {

                int rate = i - isRatedValue;
                rate = Integer.parseInt(GlobalData.packSelected.getRating()) + rate;

                int finalRate = rate;
                db.collection("product").document(GlobalData.packSelected.getId())
                    .update("rating", String.valueOf(rate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getPersonalRating();
                        packRatingUpdate(finalRate);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void packRatingUpdate(int finalRate) {
        GlobalData.packSelected.setRating(String.valueOf(finalRate));
        Long rat = null;
        if (GlobalData.packSelected.getRatedBy().replaceAll("\\s+", "").equals("0")){
            rat = Long.valueOf(GlobalData.packSelected.getRating().replaceAll("\\s+", "")) / Long.valueOf(1);

        }
        else {
              rat = Long.valueOf(GlobalData.packSelected.getRating().replaceAll("\\s+", "")) / Long.valueOf(GlobalData.packSelected.getRatedBy().replaceAll("\\s+", ""));
        }
        rated.setText(rat + "/5");
    }


}
