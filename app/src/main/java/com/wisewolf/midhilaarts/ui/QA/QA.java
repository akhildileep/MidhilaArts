package com.wisewolf.midhilaarts.ui.QA;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.wisewolf.midhilaarts.Adapters.QAReply;
import com.wisewolf.midhilaarts.Feedback;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.RetrofitClientInstance;
import com.wisewolf.midhilaarts.qa_model.QAModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QA extends Fragment   {
    RecyclerView transactionList;
    int pageLength=0,pageWidth;
    private com.wisewolf.midhilaarts.ui.QA.QAViewModel qaViewModel;
    int x=12;
    EditText feedbox;
    Button save;
    String formattedDate;
    RecyclerView repyList;
     AlertDialog.Builder builder ;
     AlertDialog dialog;
     TextView wssp,techwssp;

    private LinearLayout bottom_sheet;
    private BottomSheetBehavior sheetBehavior,sheetBehavior_instruct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        qaViewModel =
            ViewModelProviders.of(this).get(QAViewModel.class);
        View root = inflater.inflate(R.layout.activity_qa, container, false);
        bottom_sheet =root. findViewById(R.id.botomSheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
      //  transactionList=root.findViewById(R.id.transactionList);

        builBuilder();

        feedbox = root.findViewById(R.id.feedbox);
        repyList =root. findViewById(R.id.repyList);
        save = root.findViewById(R.id.save);
        wssp = root.findViewById(R.id.whatsapp);
        techwssp = root.findViewById(R.id.tech_whatsapp);
       // getListItems();
        if (GlobalData.logged) {
            getFeedreply();
        }
        else {
            alert();
            save.setVisibility(View.GONE);
        }

        wssp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+919846818960");

                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(sendIntent);
            }
        });

        techwssp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+919207543250");

                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(sendIntent);
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!feedbox.getText().toString().equals("")) {

                                            Date c = Calendar.getInstance().getTime();
                                            System.out.println("Current time => " + c);

                                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZZZZ");
                                            formattedDate = df.format(c);

                                            Feedback feedback = new Feedback();
                                            feedback.setAnswer("");
                                            feedback.setQuestion(feedbox.getText().toString());
                                            feedback.setUser_fid(GlobalData.Logged_user.getId());
                                            feedback.setUser_name(GlobalData.Logged_user.getUserName());
                                            feedback.setTime(formattedDate);

                                            addDb(feedback);


                                         /*   HashMap gm=new HashMap();
                                            gm.put("que",feedbox.getText().toString());
                                            gm.put("ans","wait for reply");
                                            Map map=new HashMap();

                                            map.put(formattedDate,gm);
                                            x++;

                                            Map mapM=new HashMap();
                                            mapM.put("qa",map);

                                            addDb(mapM);*/
                                        } else {
                                            Toast.makeText(getContext(), "Fill your feedback", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });



        qaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
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


    private void addDb(Feedback feedback) {
        dialog.show();
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            db.collection("message")
                .add(feedback)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        feedbox.setText("");
                        dialog.cancel();
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        getFeedreply();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                        dialog.cancel();
                    }
                });
        } catch (Exception e) {
            String a = "";
            dialog.cancel();
        }

    }

    private void getFeedreply() {
        try {

            dialog.show();
            final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
            Call<List<QAModel>> call = service.getQAList("/app/api//message/"+GlobalData.Logged_user.getId());
            call.enqueue(new Callback<List<QAModel>>() {
                @Override
                public void onResponse(Call<List<QAModel>> call, Response<List<QAModel>> response) {

                    dialog.cancel();
                    repyList.setAdapter(new QAReply(response.body(), repyList));
                    LinearLayoutManager added_liste_adapterlayoutManager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    repyList.setLayoutManager(added_liste_adapterlayoutManager);

                }

                @Override
                public void onFailure(Call<List<QAModel>> call, Throwable t) {
                    dialog.cancel();

                }
            });


        }catch (Exception ignored){
            String a="";
        }


    }

        /*    ADD QUE to firebase

        private void addDb(Map feedback) {
                // Access a Cloud Firestore instance from your Activity
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                try {
                    HashMap hm = new HashMap();
                    hm.put("qa", feedback);

                    db.collection("qa").document("vhz@gmail.com")
                        .set(feedback, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

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

               private void getListItems() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("qa").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> aaq=   queryDocumentSnapshots.getDocuments();
                   DocumentSnapshot ne = aaq.get(0);
                 Object a= ne.get("qa");
                 String q=a.toString();
                 String ab="";
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String a="";
                }
            });


    }*/



      /*private void addDb(List<Transactions> body) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            db.collection("transaction")
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
}
