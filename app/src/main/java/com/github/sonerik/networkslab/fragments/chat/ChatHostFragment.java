package com.github.sonerik.networkslab.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.adapters.chat_users.ChatUsersItem;
import com.github.sonerik.networkslab.beans.ChatMessage;
import com.github.sonerik.networkslab.beans.DeviceStatusChangedMessage;
import com.github.sonerik.networkslab.beans.DevicesListMessage;
import com.peak.salut.SalutDevice;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class ChatHostFragment extends ChatFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        network.startNetworkService(device -> {
            Log.d(Constants.LOG_TAG, "Device connected: "+device);

            ChatMessage msg = new ChatMessage();
            msg.nestedType = ChatMessage.NestedType.DEVICE_STATUS_CHANGED;
            msg.text = new DeviceStatusChangedMessage(device, true).toJson();

            for (SalutDevice client : network.registeredClients) {
                if (!client.equals(device))
                    network.sendToDevice(client,
                                         msg,
                                         () -> Log.e(Constants.LOG_TAG, "Can't notify ("+ client +") that new user has connected!"));
            }

            Observable.timer(1, TimeUnit.SECONDS)
                      .filter(aLong1 -> network.registeredClients.contains(device))
                      .subscribe(aLong -> {
                          ChatMessage devicesMsg = new ChatMessage();
                          devicesMsg.nestedType = ChatMessage.NestedType.DEVICES_LIST;
                          devicesMsg.text = new DevicesListMessage(network.registeredClients).toJson();

                          network.sendToDevice(device,
                                               devicesMsg,
                                               () -> Log.e(Constants.LOG_TAG, "Can't notify new user about existing clients!"));
                      });

            users.add(new ChatUsersItem(device));
            usersAdapter.notifyDataSetChanged();
        });

        network.setOnDeviceUnregisteredCallback(salutDevice -> {
            Log.d(Constants.LOG_TAG, "Device unregistered: "+salutDevice);

            ChatMessage msg = new ChatMessage();
            msg.nestedType = ChatMessage.NestedType.DEVICE_STATUS_CHANGED;
            msg.text = new DeviceStatusChangedMessage(salutDevice, false).toJson();

            network.sendToAllDevices(msg,
                                     () -> Log.e(Constants.LOG_TAG, "Can't notify that new user has disconnected!"));

            int deviceIndex = users.indexOf(new ChatUsersItem(salutDevice));

            Log.d(Constants.LOG_TAG, "Unregistered device index: "+deviceIndex);

            if (deviceIndex >= 0) {
                users.remove(deviceIndex);
                usersAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

        network.stopNetworkService(true);

        super.onDestroy();
    }

    @Override
    protected void onChatMessageReceived(ChatMessage msg) {
        super.onChatMessageReceived(msg);
        send(msg);
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
