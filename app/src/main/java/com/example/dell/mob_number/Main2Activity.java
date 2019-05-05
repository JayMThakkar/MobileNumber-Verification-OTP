package com.example.dell.mob_number;

import android.arch.core.executor.TaskExecutor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    private String VeryficationId;
    private FirebaseAuth mAuth;
    private EditText editText;
    private ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String phone = getIntent().getStringExtra("phone");
        sendVerificationCode(phone);
        p = findViewById(R.id.progressBar2);
        editText = findViewById(R.id.editText3);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editText.getText().toString().trim();
                if(code.isEmpty() || code.length()<6){
                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                p.setVisibility(View.VISIBLE);
                VerifyCode(code);
            }
        });

    }
    private void VerifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VeryficationId,code);
        signinWithCredential(credential);
    }

    private void signinWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Main2Activity.this,"Code match",Toast.LENGTH_LONG).show();

                        }
                        else {
                            Toast.makeText(Main2Activity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            p.setVisibility(View.VISIBLE);
            VeryficationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                VerifyCode(code);
                editText.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Main2Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };
}
