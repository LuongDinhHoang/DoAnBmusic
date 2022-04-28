package com.example.tung_hoang_bmusic.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tung_hoang_bmusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    private View mLayout;
    private Button mConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_sign_up);
        mLayout = findViewById(R.id.layout_sign_in);
        mConfirmView = findViewById(R.id.btn_sign_up_accept);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onSignUpAcc(View view) {
        TextInputLayout tlUsername = findViewById(R.id.tl_username);
        TextInputLayout tlPassword = findViewById(R.id.tl_password);

        String email = tlUsername.getEditText().getText().toString();
        String password = tlPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {
            tlUsername.setErrorEnabled(true);
            tlUsername.setError("輸入帳號");
        } else if (TextUtils.isEmpty(password)) {
            tlPassword.setErrorEnabled(true);
            tlPassword.setError("輸入密碼");
        } else {
            tlUsername.setErrorEnabled(false);
            tlPassword.setErrorEnabled(false);

            signup(email, password);
        }
    }

    private void signup(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sent email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "ERR sent email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            mLayout.setVisibility(View.GONE);
                            mConfirmView.setVisibility(View.VISIBLE);
                            mConfirmView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
;
                            mAuth.signOut();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
