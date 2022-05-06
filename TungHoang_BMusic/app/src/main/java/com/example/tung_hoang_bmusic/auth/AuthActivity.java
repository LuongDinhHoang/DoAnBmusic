package com.example.tung_hoang_bmusic.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auth);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // Check if user is signed in (non-null) and update UI accordingly.
        if (user == null) {
            Intent intent = new Intent(this, HomeAuthActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void onSignOut(View view) {
        mAuth.signOut();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void getUserProfile(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thông tin người dùng : ");
            sb.append("\n");
            sb.append("\n");
            sb.append("Tên người dùng : ");
            sb.append(user.getDisplayName());
            sb.append("\n");
            sb.append("\n");
            sb.append("Email người dùng :  ");
            sb.append(user.getEmail());
            sb.append("\n");
            sb.append("\n");
            sb.append("Xác thực :  ");
            sb.append(user.isEmailVerified());
            sb.append("\n");
            sb.append("\n");
            sb.append("ID :  ");
            sb.append(user.getUid());
            sb.append("\n");
            sb.append("\n");
            sb.append("photo url :  ");
            sb.append(user.getPhotoUrl());
            sb.append("\n");

            TextView textUserInfo = findViewById(R.id.text_user_info);
            textUserInfo.setText(sb.toString());
        }
    }

    public void sendVerifyEmail(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AuthActivity.this, " Đã gửi đi email xác thực 驗證Email已寄出", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AuthActivity.this, "Không gửi được email xác thực 驗證Email寄送失敗", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void updateUserInfo(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            final View layout = LayoutInflater.from(this).inflate(R.layout.dlg_update_name, null);
            new AlertDialog.Builder(this)
                    .setView(layout)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TextInputLayout tl_username = layout.findViewById(R.id.tl_updatename);

                            String email = tl_username.getEditText().getText().toString();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(email)
                                    .setPhotoUri(Uri.parse("https://avatars1.githubusercontent.com/u/10694648?s=460&v=4"))
                                    .build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AuthActivity.this, "User information updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AuthActivity.this, "User information update failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            dialog.dismiss();
                        }
                    })
                    .show();



        }
    }

    public void reauthenticate(View view) {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            final View layout = LayoutInflater.from(this).inflate(R.layout.dlg_user_account, null);
            new AlertDialog.Builder(this)
                    .setView(layout)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TextInputLayout tl_username = layout.findViewById(R.id.tl_username);
                            TextInputLayout tl_password = layout.findViewById(R.id.tl_password);

                            String email = tl_username.getEditText().getText().toString();
                            String password = tl_password.getEditText().getText().toString();

                            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AuthActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                        // add code to update Email/Password

                                    } else {
                                        Toast.makeText(AuthActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
