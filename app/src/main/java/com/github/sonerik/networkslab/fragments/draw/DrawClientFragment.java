package com.github.sonerik.networkslab.fragments.draw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.draw.DrawMessage;
import com.github.sonerik.networkslab.beans.draw.Point;
import com.github.sonerik.networkslab.events.DeviceChosenEvent;
import com.github.sonerik.networkslab.fragments.ChooseDeviceFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class DrawClientFragment extends DrawFragment {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

        network.unregisterClient(() -> Log.d(Constants.LOG_TAG, "Unregistered from server."),
                                 () -> Log.d(Constants.LOG_TAG, "Failed to unregister from server."),
                                 false);

        super.onDestroy();
    }

    @Subscribe
    public void onEvent(DeviceChosenEvent e) {
        network.registerWithHost(e.device,
                                 () -> Log.d(Constants.LOG_TAG, "We're now registered."),
                                 () -> Log.d(Constants.LOG_TAG, "We failed to register."));
    }

    @Override
    protected void onNewPointsBufferAvailable(List<Point> buffer) {
        network.sendToHost(new DrawMessage(DrawMessage.Type.NEW_POINTS, buffer),
                           () -> Log.e(Constants.LOG_TAG, "Can't send new points to the host"));
    }

    @Override
    protected void onCanvasCleared() {
        network.sendToHost(new DrawMessage(DrawMessage.Type.CLEAR, null),
                           () -> Log.e(Constants.LOG_TAG, "Can't send clear event to the host"));
    }
}
