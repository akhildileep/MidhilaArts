package com.wisewolf.midhilaarts.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.takusemba.spotlight.CustomTarget;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.vimeo.networking.model.Video;
import com.wisewolf.midhilaarts.AutoResizeTextView;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.HomeActivity;
import com.wisewolf.midhilaarts.Model.Pack;
import com.wisewolf.midhilaarts.Model_viewPager;
import com.wisewolf.midhilaarts.PackageDetails;
import com.wisewolf.midhilaarts.Package_Adapter;
import com.wisewolf.midhilaarts.PopularPackage_Adapter;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.RetrofitClientInstance;
import com.wisewolf.midhilaarts.ViewPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    int yHeight = 0, i;
    RecyclerView recyclerView,ppular_longList;
    ImageView hand;

    int count=0,servertext_count=0,imageHeight;
    private List<Model_viewPager> model_viewPagers = new ArrayList<>();
    List<String> savisheshata=new ArrayList<>();
    AlertDialog.Builder builder ;
    AlertDialog dialog;
    ViewPager2 pager;
    TabLayout tabLayout;
    Thread t;
    int currentPage = 0;
    Timer timer;

    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        builBuilder();

      //  parseFragment();



        final CardView ViewPager_card = root.findViewById(R.id.viewPager_card);
        final ConstraintLayout total_layout = root.findViewById(R.id.total_layout);

        recyclerView = root.findViewById(R.id.recycler_id);
        ppular_longList = root.findViewById(R.id.ppular_longList);



        i = calcheight(total_layout);
        ViewGroup.LayoutParams params = ViewPager_card.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = i;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        ViewPager_card.setLayoutParams(params);
          pager = root.findViewById(R.id.home_viewPager);
          tabLayout = root.findViewById(R.id.tab_layout);

        topBanner();



        return root;




    }


    private void callSpecialEvents() {


                    List<String> link=new ArrayList<>();


                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    Gson gson = new Gson();
                    String json = sharedPrefs.getString("bottombanner", "");
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> arrayList = gson.fromJson(json, type);
                    link = (ArrayList) arrayList;

                    if (link.size()!=0) {


                        PopularPackage_Adapter popularPackage_adapter = new
                            PopularPackage_Adapter(link, recyclerView, imageHeight);
                        ppular_longList.setAdapter(popularPackage_adapter);
                        LinearLayoutManager popuarpackage_adapterlayoutManager
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        ppular_longList.setLayoutManager(popuarpackage_adapterlayoutManager);
                    }
                  //  dialog.dismiss();



    }



    private void topBanner() {


                    model_viewPagers = new ArrayList<>();
                    List<String> link = new ArrayList<>();


                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    Gson gson = new Gson();
                    String json = sharedPrefs.getString("topbanner", "");
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> arrayList = gson.fromJson(json, type);
                    link = (ArrayList) arrayList;


                    for (int i = 0; i < link.size(); i++) {
                        model_viewPagers.add(new Model_viewPager(link.get(i)));

                    }
                    pager.setAdapter(new ViewPagerAdapter(model_viewPagers, pager));
                    attach();
                    try {
                        /*After setting the adapter use the timer */
                        final Handler handler = new Handler();
                        List<String> finalLink = link;
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == finalLink.size()) {
                                    currentPage = 0;
                                }
                                pager.setCurrentItem(currentPage++, true);
                            }
                        };

                        timer = new Timer(); // This will create a new Thread
                        timer.schedule(new TimerTask() { // task to be scheduled
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, DELAY_MS, PERIOD_MS);

                        callPackage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




    }

    private void attach() {


        new TabLayoutMediator(tabLayout, pager,
            new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                }
            }).attach();
    }

    private void builBuilder() {
        builder  = new AlertDialog.Builder(getContext());
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



    private void callPackage() {


                        List<Pack> listPack=new ArrayList<>();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("packlist", "");
        Type type = new TypeToken<List<Pack>>() {
        }.getType();
        List<Pack> arrayList = gson.fromJson(json, type);
        listPack = (ArrayList) arrayList;

                        GlobalData.packs=listPack;

                     //TODO add data addDb(response.body());
                        Package_Adapter  package_adapter = new
                            Package_Adapter(listPack, recyclerView, new Package_Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Pack item) {
                                GlobalData.packSelected=item;
                                Intent intent=(new Intent(getContext(), PackageDetails.class));

                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(getActivity(),recyclerView , "thumb");
                                startActivity(intent, options.toBundle());

                            }
                        });
                        recyclerView.setAdapter(package_adapter);
                        LinearLayoutManager recent_add_package_adapterlayoutManager
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager layoutManager =
            new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        callSpecialEvents();






    }

   /* private void addDb(List<Pack> body) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            db.collection("product")
                .add(body.get(0))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        }
        catch (Exception e){
            String a="";
        }

    }*/

    private int calcheight(final ConstraintLayout total_layout) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        yHeight = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return (int) (yHeight * 0.3);
    }

}
