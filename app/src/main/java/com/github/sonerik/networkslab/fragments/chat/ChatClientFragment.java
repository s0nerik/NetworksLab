package com.github.sonerik.networkslab.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.ChatMessage;
import com.github.sonerik.networkslab.events.DeviceChosenEvent;
import com.github.sonerik.networkslab.fragments.ChooseDeviceFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatClientFragment extends ChatFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getSupportFragmentManager()
                     .beginTransaction()
                     .add(R.id.content, new ChooseDeviceFragment(network))
                     .addToBackStack(null)
                     .commit();
    }

    @Override
    protected void send(ChatMessage msg) {
        network.sendToHost(msg, () -> Log.e(Constants.LOG_TAG, "Failed to send text!"));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(DeviceChosenEvent e) {
        network.registerWithHost(e.device,
                                 () -> Log.d(Constants.LOG_TAG, "We're now registered."),
                                 () -> Log.d(Constants.LOG_TAG, "We failed to register."));
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

        network.unregisterClient(() -> Log.d(Constants.LOG_TAG, "Unregistered from server."),
                                 () -> Log.d(Constants.LOG_TAG, "Failed to unregister from server."),
                                 false);

        super.onDestroy();
    }
}
