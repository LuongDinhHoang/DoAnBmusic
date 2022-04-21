package com.example.tung_hoang_bmusic.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onLogin(View view) {
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

            login(email, password);
        }
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user.isEmailVerified()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Tài khoản chưa được xác thực.\nKhông thể đăng nhập!!!", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
