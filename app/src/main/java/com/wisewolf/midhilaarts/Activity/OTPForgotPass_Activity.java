package com.wisewolf.midhilaarts.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.LoginActivity;
import com.wisewolf.midhilaarts.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OTPForgotPass_Activity extends AppCompatActivity {
    TextView verify,resend;
    @BindView(R.id.otp1)
    EditText otp1;
    @BindView(R.id.otp2)
    EditText otp2;
    @BindView(R.id.otp3)
    EditText otp3;
    @BindView(R.id.otp4)
    EditText otp4;
    @BindView(R.id.otp5)
    EditText otp5;
    @BindView(R.id.otp6)
    EditText otp6;
    private EditText[] editTexts;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    String phone,Ophone;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        init();
        builBuilder();
        Intent intent=getIntent();
        phone=intent.getStringExtra("p");
        Ophone=intent.getStringExtra("pO");
        GlobalData.phone=Ophone;

        try {
            sendOtp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        verify = findViewById(R.id.verifyId);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OTPForgotPass_Activity.this, "resent", Toast.LENGTH_SHORT).show();
                try {
                    sendOtp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otp1.getText().toString() +
                    otp2.getText().toString() +
                    otp3.getText().toString() +
                    otp4.getText().toString() +
                    otp5.getText().toString() +
                    otp6.getText().toString();

                if (otp.replaceAll("\\s+","").length() == 6) {
                    dialog.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(OTPForgotPass_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.cancel();
                                if (task.isSuccessful()) {

                                    Toast.makeText(OTPForgotPass_Activity.this, "Verification Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(OTPForgotPass_Activity.this, ConfirmPass.class));
                                    finish();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(OTPForgotPass_Activity.this, "Verification Failed, Invalid credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                } else {
                    Toast.makeText(OTPForgotPass_Activity.this, "fill all OTP fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editTexts = new EditText[]{otp1, otp2, otp3, otp4, otp5, otp6};
        otp1.addTextChangedListener(new PinTextWatcher(0));
        otp2.addTextChangedListener(new PinTextWatcher(1));
        otp3.addTextChangedListener(new PinTextWatcher(2));
        otp4.addTextChangedListener(new PinTextWatcher(3));
        otp5.addTextChangedListener(new PinTextWatcher(4));
        otp6.addTextChangedListener(new PinTextWatcher(5));


        otp1.setOnKeyListener(new PinOnKeyListener(0));
        otp2.setOnKeyListener(new PinOnKeyListener(1));
        otp3.setOnKeyListener(new PinOnKeyListener(2));
        otp4.setOnKeyListener(new PinOnKeyListener(3));
        otp5.setOnKeyListener(new PinOnKeyListener(4));
        otp6.setOnKeyListener(new PinOnKeyListener(5));
    }

    private void sendOtp() throws Exception{
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
              phone,        // Phone number to verify
            1,                 // Timeout duration
            TimeUnit.MINUTES,   // Unit of timeout
            this,               // Activity (for callback binding)
            mCallbacks);
    }
    private void builBuilder() {
        builder = new  AlertDialog.Builder(OTPForgotPass_Activity.this);
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

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        resend=findViewById(R.id.resendPass);

///Add this method below auth initialization in the onCreate method.

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(OTPForgotPass_Activity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
            }
        };

    }

    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0));

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);

            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1) {
                moveToNext();
                editTexts[currentIndex].setBackgroundResource(R.drawable.rect_shape);
            } else if (text.length() == 0) {
                moveToPrevious();
                editTexts[currentIndex].setBackgroundResource(R.drawable.rect_unfilled);
            }
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }


    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }
}
