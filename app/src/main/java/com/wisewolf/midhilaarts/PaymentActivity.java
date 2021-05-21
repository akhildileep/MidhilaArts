package com.wisewolf.midhilaarts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.Global.KillNotificationService;
import com.wisewolf.midhilaarts.Model.LoginMod;
import com.wisewolf.midhilaarts.firebaseModel.TransactionModel;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends Activity implements PaymentResultListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();

    private LinearLayout bottom_sheet;
    private BottomSheetBehavior sheetBehavior;
    ImageView payment;
    TextView close,amountText,details,privacy,apply;
    TextInputEditText referalcode;
    String amount,  billNo,   date,expDate, packName,  packHead, paymentMode, status,  userId,  razor_id,offerp="",referenceCode;
    TransactionModel transactionModel=new TransactionModel();
    String pack_selected_amount,duratn,pack_selected_price,pack_selected_dur,pack_selected_disc, type,video_no="0";

    AlertDialog.Builder builder;
    AlertDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builBuilder();
        setContentView(R.layout.activity_payment);
        bottom_sheet = findViewById(R.id.bottom_sheet_payment);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        payment=findViewById(R.id.paymentsucess);
        close=findViewById(R.id.paymentsheet_close);
        amountText=findViewById(R.id.amount);
        details=findViewById(R.id.details);
        privacy=findViewById(R.id.txt_privacy_policy);
        referalcode=findViewById(R.id.refCode);
        apply=findViewById(R.id.apply);
        dntcloseApp();
        Intent intent=getIntent();

        Date c = Calendar.getInstance().getTime();
              SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date = df.format(c);



        packName=intent.getStringExtra("desc");
        amount=intent.getStringExtra("amt");
        packHead=intent.getStringExtra("name");
        type=intent.getStringExtra("type");
        pack_selected_amount=intent.getStringExtra("price");
        pack_selected_dur=intent.getStringExtra("dur");
        duratn=pack_selected_dur;
        pack_selected_disc=intent.getStringExtra("disc");

        if (type.equals("upgrade")){
            int dur=Integer.parseInt(GlobalData.planTypeMod.getPackPeriod());
            dur=dur+Integer.parseInt(pack_selected_dur);
            pack_selected_dur=String.valueOf(dur);
            video_no=GlobalData.planTypeMod.getVideoNo();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateExp = null,today=null;
            try {
                dateExp = format.parse(GlobalData.planTypeMod.getExpiry());
                today = format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (today != null) {
                if (today.before(dateExp)) {
                    Calendar calendar = Calendar.getInstance();
                    if (dateExp != null) {
                        calendar.setTime(dateExp);
                        calendar.add(Calendar.MONTH, Integer.parseInt(duratn));

                    }
                    c=calendar.getTime();
                    df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    expDate=df.format(c);
                }
                else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH, Integer.parseInt(duratn));
                    c=calendar.getTime();
                    df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    expDate=df.format(c);
                }
            }
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, Integer.parseInt(duratn));
            c=calendar.getTime();
            df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            expDate=df.format(c);
        }

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apply.getText().equals("Apply")) {
                    String refCode = referalcode.getText().toString();
                    if (refCode.isEmpty()) {
                        Toast.makeText(PaymentActivity.this, "enter referal code", Toast.LENGTH_SHORT).show();
                    } else {
                        validateRefCode(refCode);
                    }
                }
                else {
                    Toast.makeText(PaymentActivity.this, "Referal Removed", Toast.LENGTH_SHORT).show();
                    referalcode.setText("");
                    apply.setText("Apply");
                    offerp="";
                    referenceCode="";
                    details.setText("You are purchasing Midhila Art's pack -"+packName+" amounting to Rs "+pack_selected_amount+" which ineffective cost to Rs "
                        +pack_selected_disc+" after all discount "+"For a duration of "+pack_selected_dur+" Months -until "+expDate+" .");
                    amountText.setText("INR "+pack_selected_disc);
                }
            }
        });



        details.setText("You are purchasing Midhila Art's pack -"+packName+" amounting to Rs "+pack_selected_amount+" which ineffective cost to Rs "
        +pack_selected_disc+" after all discount "+"For a duration of "+pack_selected_dur+" Months -until "+expDate+" .");
        amountText.setText("INR "+pack_selected_disc);
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        expDate=df.format(c);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPrivacy();
            }
        });
        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        // Payment button created by you in XML layout
        Button button = findViewById(R.id.btn_pay);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startPayment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


    }

    private void builBuilder() {
        builder = new AlertDialog.Builder(PaymentActivity.this);
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

    private void validateRefCode(String refCode) {
dialog.show();
    /*    FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("PromoCode").document(refCode).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {




                    try {

                        String percent;
                        percent = String.valueOf(documentSnapshot.get("percent"));
                       if (percent==null){
                           dialog.cancel();
                           Toast.makeText(PaymentActivity.this, "Invalid Referal Code", Toast.LENGTH_SHORT).show();
                       }
                       else {
                           dialog.cancel();
                           Toast.makeText(PaymentActivity.this, "Referal Code Applied", Toast.LENGTH_SHORT).show();
                           apply.setText("Remove");
                           referenceCode=refCode;
                           Double offer=Double.parseDouble(percent);
                           offer=Double.parseDouble(pack_selected_disc)*(offer/100);
                           Double offerPrice=Double.parseDouble(pack_selected_disc)-offer;
                           details.setText("You are purchasing Midhila Art's pack -"+packName+" amounting to Rs "+pack_selected_amount+" which ineffective cost to Rs "
                               +offerPrice.toString()+" after all discount and PROMOCODE "+"For a duration of "+pack_selected_dur+" Months -until "+expDate+" .");
                           amountText.setText("INR "+offerPrice.toString());
                           offerp=offerPrice.toString();
                       }
                    } catch (Exception e) {
                        dialog.cancel();
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.cancel();
                Toast.makeText(PaymentActivity.this, "Invalid referal code", Toast.LENGTH_SHORT).show();
            }
        });*/


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = rootRef.collection("PromoCode").document(refCode);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        try {

                            String percent;
                            percent = String.valueOf(document.get("percent"));
                            if (percent==null){
                                dialog.cancel();
                                Toast.makeText(PaymentActivity.this, "Invalid Referal Code", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                dialog.cancel();
                                Toast.makeText(PaymentActivity.this, "Referal Code Applied", Toast.LENGTH_SHORT).show();
                                apply.setText("Remove");
                                referenceCode=refCode;
                                Double offer=Double.parseDouble(percent);
                                offer=Double.parseDouble(pack_selected_disc)*(offer/100);
                                Double offerPrice=Double.parseDouble(pack_selected_disc)-offer;
                                details.setText("You are purchasing Midhila Art's pack -"+packName+" amounting to Rs "+pack_selected_amount+" which ineffective cost to Rs "
                                    +offerPrice.toString()+" after all discount and PROMOCODE "+"For a duration of "+pack_selected_dur+" Months -until "+expDate+" .");
                                amountText.setText("INR "+offerPrice.toString());
                                offerp=offerPrice.toString();
                            }
                        } catch (Exception e) {
                            dialog.cancel();
                            e.printStackTrace();
                        }
                    } else {
                        dialog.cancel();
                        Toast.makeText(PaymentActivity.this, "Invalid referal code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.cancel();
                    Toast.makeText(PaymentActivity.this, "Invalid referal code", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void startPayment() throws Exception{
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", GlobalData.Logged_user.getUserName());
            options.put("description", "You are purchasing Midhila Art's pack -"+packName);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            int amt=0;
            try {
                if (offerp.equals("")){
                    amt=Integer.valueOf(pack_selected_disc);
                    amt=amt*100;
                }
                else {
                    amt=Integer.valueOf( Double.valueOf(offerp).intValue());
                    amt=amt*100;
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(activity, "issue in payment", Toast.LENGTH_SHORT).show();
                finish();
            }


            options.put("amount", String.valueOf(amt));

            JSONObject preFill = new JSONObject();
            preFill.put("email", GlobalData.Logged_user.getAge());
            preFill.put("contact", GlobalData.Logged_user.getMobileNo());

            options.put("prefill", preFill);
               billNo="";
              paymentMode=""; status="";  userId=GlobalData.Logged_user.getId();  razor_id="";

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */


    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            sucsessParty(razorpayPaymentID);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    private void sucsessParty(String razorpayPaymentID) {
        status = "success";
        razor_id=razorpayPaymentID;
        addDB();
        getBillNo();
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        Glide.with(PaymentActivity.this).asGif().load(R.raw.done).into(payment);
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();


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
                        startActivity(new Intent(PaymentActivity.this,HomeActivity.class));
                        finish();
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
    }

    private void getBillNo() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bill_master").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> aaq=   queryDocumentSnapshots.getDocuments();
                    DocumentSnapshot ne = aaq.get(0);
                    Object a= ne.get("billNo");
                    billNo =a.toString();
                    int bill=Integer.valueOf(billNo)+1;
                    updateBill(String.valueOf(bill));
                    billNo=String.valueOf(bill);
                    transactionModel.setBillNo(billNo);
                    transactionModel.setPaymentMode("RazorPay");
                    addTransaction();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String a="";
                }
            });




    }

    private void addTransaction() {

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            db.collection("transaction")
                .add(transactionModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if (status.equals("success")) {
                            updateUsertable();
                        }
                        String a = "";
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                        String a = "";
                    }
                });
        } catch (Exception e) {
            String a = "";

        }
    }

    private void updateUsertable() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {

            db.collection("users").document(GlobalData.Logged_user.getId())
              .update("plantype",GlobalData.packSelected.getVimeoId());

            //TODO duration change
            HashMap hm = new HashMap();
            hm.put("duration", duratn);
            hm.put("expiry", expDate);
            hm.put("pack_period", pack_selected_dur);
            hm.put("start_date", date);
            hm.put("video_no",video_no);
            hm.put("user_id", GlobalData.Logged_user.getId());
            hm.put("vimeo_id", GlobalData.packSelected.getVimeoId());

            db.collection("plan_type").document(GlobalData.Logged_user.getId())
                .set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
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



        } catch (Exception e) {
            String a = "";
        }
        GlobalData.Logged_user.setPlantype(amount);
        savedetails(GlobalData.username,GlobalData.Logged_user);

    }

    private void savedetails(String username, LoginMod logged_user) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
//            did now        String json = gson.toJson(allVideoList);

        String logged_userjson = gson.toJson(logged_user);
        editor.putString("username", username);
        editor.putString("logged_user", logged_userjson);
        editor.putString("saved", "1");
        editor.apply();


    }

    private void addDB() {
if (offerp.equals("")){ transactionModel.setAmount(pack_selected_disc);}
else { transactionModel.setAmount(offerp);}

        transactionModel.setBillNo(billNo);
        transactionModel.setPackName(packName);
        transactionModel.setPackHead(packHead);
        transactionModel.setPaymentMode(paymentMode);
        transactionModel.setStatus(status);
        transactionModel.setUserId(userId);
        transactionModel.setRazor_id(razor_id);
        transactionModel.setRefCode(referenceCode);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        transactionModel.setDate(formattedDate);


    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            status = "fail";
            razor_id=  response;
            addDB();
            getBillNo();
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            Glide.with(PaymentActivity.this).asGif().load(R.raw.fail).into(payment);
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }



    private void updateBill(String billNo) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            HashMap hm = new HashMap();
            hm.put("billNo", billNo);

            db.collection("bill_master").document("billNo")
                .set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
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



        } catch (Exception e) {
            String a = "";
        }

    }

    private void alertPrivacy() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_privacy, null);
        builder.setView(customLayout);
        // add a button
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void dntcloseApp() {
        startService(new Intent(PaymentActivity.this, KillNotificationService.class));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dntclose").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> aaq = queryDocumentSnapshots.getDocuments();

                    try {
                        DocumentSnapshot ne = aaq.get(0);
                        Object a = ne.get("close");
                        if (a != null) {
                            a = a.toString();

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);


                            alertDialogBuilder.setTitle("NOTICE")
                                .setMessage(a.toString())
                                .setNegativeButton("close", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        finish();
                        Toast.makeText(PaymentActivity.this, "Network Down", Toast.LENGTH_SHORT).show();
                    }



                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

    }

}