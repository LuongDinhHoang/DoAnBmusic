package com.example.tung_hoang_bmusic.ui.splashscreen;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.util.NetWorkChangeReceiver;

public class SplashScreenFragment extends Fragment {


    LottieAnimationView mLottieAnimationView;
    BroadcastReceiver mBroadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_screen, container, false);
        mLottieAnimationView = view.findViewById(R.id.animationView);
        mBroadcastReceiver = new NetWorkChangeReceiver();
        registerNetworkBroadcastReceiver();
        return view;
    }

    private void registerNetworkBroadcastReceiver(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getActivity().registerReceiver(mBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getActivity().registerReceiver(mBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


    private void unregisterNetwork(){
        try{
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }
}
