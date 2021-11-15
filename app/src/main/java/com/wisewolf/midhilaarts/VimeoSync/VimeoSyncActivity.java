package com.wisewolf.midhilaarts.VimeoSync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.HomeActivity;
import com.wisewolf.midhilaarts.Model.Course;
import com.wisewolf.midhilaarts.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class VimeoSyncActivity extends AppCompatActivity {
    Button tiny,oil,wc1,wc2,demo;
    String uri="",name="";
    ProgressDialog mProgressDialog;
    int flagNext=0;
    Course courseModel=new Course();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo_sync3);
        tiny=findViewById(R.id.tinytots);
        oil=findViewById(R.id.oilpack);
        wc1=findViewById(R.id.wc1);
        wc2=findViewById(R.id.wc2);
        demo=findViewById(R.id.demo);

        vimeoInit();

        tiny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = "/me/projects/2074244/videos";
                name="2074244";
                callVimeo();
            }
        });
        oil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = "/me/projects/3835008/videos";
                name="3835008";
                callVimeo();
            }
        });
        wc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = "/me/projects/3835540/videos";
                name="3835540";
                callVimeo();
            }
        });
        wc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = "/me/projects/3835545/videos";
                name="3835545";
                callVimeo();
            }
        });
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = "/me/projects/2241147/videos";
                name="2241147";
                callVimeo();
            }
        });
    }
    private  void  vimeoInit(){
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
    }

    private void callVimeo() {
        mProgressDialog = new ProgressDialog(VimeoSyncActivity.this);
        mProgressDialog.setMessage(" Please wait . . .");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);


        download();
    }

    private void download() {
        VimeoSyncActivity.Downback DB = new VimeoSyncActivity.Downback();
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
                        else {
                            mProgressDialog.cancel();
                            addvideos();
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
    private void addvideos() {
        ArrayList demovideos=GlobalData.demovideos;
        ArrayList<Course> courseArrayList=new ArrayList<>();
        for (int i=0;i<demovideos.size();i++
             ) {
            courseModel=new Course();
            Video video = (Video)demovideos.get(i);
            courseModel.setDesc(video.description);

            int j = video.pictures.sizes.size();
            if (j < 3) {
                courseModel.setThumb(video.pictures.sizes.get(2).link);
            } else if (j < 6) {
                courseModel.setThumb(video.pictures.sizes.get(5).link);
            } else if (j < 9) {
                courseModel.setThumb(video.pictures.sizes.get(5).link);
            } else if (j < 12) {
                courseModel.setThumb(video.pictures.sizes.get(5).link);
            } else {
                courseModel.setThumb(video.pictures.sizes.get(j - 1).link);
            }

            for (int p = 0; p < video.files.size(); p++) {
                if (video.files.get(p).height == 540) {

                    courseModel.setLink(video.files.get(p).link);
                }
                else  if (video.files.get(p).height == 240) {

                    courseModel.setLink(video.files.get(p).link);
                }
                else {
                    courseModel.setLink(video.files.get(video.files.size()-1).link);
                }
            }


            courseArrayList.add(courseModel);

        }
        firebaseAdd(courseArrayList);
    }
    private void firebaseAdd(ArrayList courseArrayList) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {

            db.collection("Courses").document(name)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Map<String, Object> data = new HashMap<>();
                        data.put("Course", courseArrayList );


                        db.collection("Courses").document(name)
                            .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                String a="";
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
                    public void onFailure(@NonNull Exception e) {

                    }
                });



        } catch (Exception e) {
            String a = "";
        }
    }
}