package com.github.sonerik.networkslab.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.beans.ChatMessage;

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

    @Override
    protected void send(ChatMessage msg) {
        if (msg.recipient != null) {
            network.sendToDevice(msg.recipient, msg, () -> Log.e(Constants.LOG_TAG, "Failed to send text!"));
        } else {
            network.sendToAllDevices(msg, () -> Log.e(Constants.LOG_TAG, "Failed to send text!"));
        }
    }
}
