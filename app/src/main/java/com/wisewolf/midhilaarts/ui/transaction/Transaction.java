package com.wisewolf.midhilaarts.ui.transaction;

import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.Model.Transactions;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.RetrofitClientInstance;
import com.wisewolf.midhilaarts.TransactionAdapter;
import com.wisewolf.midhilaarts.ui.gallery.TransactionViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transaction extends Fragment   {
    RecyclerView transactionList;
    int pageLength=0,pageWidth;
    AlertDialog.Builder builder ;
    AlertDialog dialog;
    private com.wisewolf.midhilaarts.ui.gallery.TransactionViewModel transactionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionViewModel =
            ViewModelProviders.of(this).get(TransactionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaction, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
        transactionList=root.findViewById(R.id.transactionList);
        builBuilder();

        calcHandView();

        if (GlobalData.logged) {
            callPackage();
        }
        else {
            alert();

        }




        transactionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
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

    private void callPackage() {
        dialog.show();
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<List<Transactions>> call = service.getTransaction("/app/api/transaction/"+GlobalData.Logged_user.getId());
        try {


            call.enqueue(new Callback<List<Transactions>>() {
                @Override
                public void onResponse(Call<List<Transactions>> call, Response<List<Transactions>> response) {

                    int a =response.code();
                    if (a == 200) {

                        //TODO add data addDb(response.body());
                        if (response.body().size()!=0) {
                            TransactionAdapter transactionAdapter = new TransactionAdapter(pageLength, pageLength, response.body(), transactionList, getActivity(), new TransactionAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String s, int position) {
                                }

                            });

                            transactionList.setAdapter(transactionAdapter);
                            LinearLayoutManager galleryHeadlayoutManager
                                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            transactionList.setLayoutManager(galleryHeadlayoutManager);

                        }
                        else {
                            Toast.makeText(getContext(), "No transaction available", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<List<Transactions>> call, Throwable t) {
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
    }

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
