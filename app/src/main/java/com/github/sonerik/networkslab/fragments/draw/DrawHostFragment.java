package com.github.sonerik.networkslab.fragments.draw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.draw.DrawMessage;
import com.github.sonerik.networkslab.beans.draw.Point;
import com.peak.salut.SalutDevice;

import java.util.List;

import butterknife.Bind;

public class DrawHostFragment extends DrawFragment {

    @Bind(R.id.waiting)
    View waiting;

    private SalutDevice otherDevice;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waiting.setVisibility(View.VISIBLE);
        drawArea.setVisibility(View.GONE);
        network.startNetworkService(salutDevice -> {
            waiting.setVisibility(View.GONE);
            drawArea.setVisibility(View.VISIBLE);

            otherDevice = salutDevice;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        network.stopNetworkService(true);
    }

    @Override
    protected void onNewPointsBufferAvailable(List<Point> buffer) {
        network.sendToDevice(otherDevice,
                             new DrawMessage(DrawMessage.Type.NEW_POINTS, buffer),
                             () -> Log.e(Constants.LOG_TAG, "Can't send new points to another device"));
    }

    @Override
    protected void onCanvasCleared() {
        network.sendToDevice(otherDevice,
                             new DrawMessage(DrawMessage.Type.CLEAR, null),
                             () -> Log.e(Constants.LOG_TAG, "Can't send clear event to another device"));
    }
}
