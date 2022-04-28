package com.example.tung_hoang_bmusic.ui.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.StorageUtil;
import com.example.tung_hoang_bmusic.auth.AuthActivity;
import com.example.tung_hoang_bmusic.ui.chat.ChatActivity;

public class SettingFragment extends Fragment {

    public static final String THEME_NIGHT = "theme_night";
    public static boolean mNight = false;
    private Context context;

    public SettingFragment() {}

    private StorageUtil mStorageUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_layout, container, false);
        context = container.getContext();
        mStorageUtil = new StorageUtil(context);
        LinearLayout managerAcc = view.findViewById(R.id.line4);
        managerAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HoangLD: nếu đang nhập rồi thì hiển thị giao diện quản lý tài khoản,
                // còn chưa thì yêu cầu đăng nhập
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
            }
        });
        //chat
        LinearLayout managerChat = view.findViewById(R.id.line5);
        managerChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HoangLD:
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchNight = view.findViewById(R.id.switch_night);
        switchNight.setChecked(getThemeNightMode());
        switchNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mNight = true;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    mNight = false;
                }
                mStorageUtil.storeThemeColor(isChecked);
                saveThemeNightMode(isChecked);
            }
        });

        LinearLayout nightMode = view.findViewById(R.id.line);
        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchNight.performClick();
            }
        });
        return view;
    }



    private void saveThemeNightMode(boolean isNight) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(THEME_NIGHT, isNight);
        editor.apply();
    }

    private boolean getThemeNightMode() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(THEME_NIGHT, false);
    }

}
