package com.github.sonerik.networkslab.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.adapters.chat_users.ChatUsersItem;
import com.github.sonerik.networkslab.beans.ChatMessage;
import com.github.sonerik.networkslab.beans.DeviceConnectedMessage;

import org.greenrobot.eventbus.EventBus;

public class ChatHostFragment extends ChatFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        network.startNetworkService(device -> {
            Log.d(Constants.LOG_TAG, "Device connected: "+device);

            network.sendToAllDevices(new DeviceConnectedMessage(device),
                                     () -> Log.e(Constants.LOG_TAG, "Can't notify that new user has connected!"));

            users.add(new ChatUsersItem(device));
            usersAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

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
