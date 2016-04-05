package com.github.sonerik.networkslab.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.sonerik.networkslab.Constants;

public class ChatHostFragment extends ChatFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        network.startNetworkService(device -> Log.d(Constants.LOG_TAG, "Device connected: "+device));
    }

    @Override
    public void onDestroy() {
        network.stopNetworkService(true);

        super.onDestroy();
    }
}
