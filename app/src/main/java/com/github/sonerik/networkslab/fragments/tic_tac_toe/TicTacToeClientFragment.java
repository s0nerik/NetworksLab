package com.github.sonerik.networkslab.fragments.tic_tac_toe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.tic_tac_toe.TicTacToeMessage;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;
import com.github.sonerik.networkslab.events.DeviceChosenEvent;
import com.github.sonerik.networkslab.fragments.ChooseDeviceFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class TicTacToeClientFragment extends TicTacToeFragment {

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

    @Subscribe
    public void onEvent(DeviceChosenEvent e) {
        network.registerWithHost(e.device,
                () -> {
                    Log.d(Constants.LOG_TAG, "We're now registered.");
                },
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

    @Override
    protected TicTacToeField.CellValue getPlayerCellValue() {
        return TicTacToeField.CellValue.O;
    }

    @Override
    protected void onCellValueChanged(int x, int y, TicTacToeField.CellValue v) {
        if (v == getPlayerCellValue()) {
            Log.d(Constants.LOG_TAG, "Set new client value");
            network.sendToHost(new TicTacToeMessage(x, y, v, TicTacToeMessage.Type.CELL_VALUE),
                    () -> Log.e(Constants.LOG_TAG, "Can't send a message to the host!"));
        }
    }

    @Override
    protected void onGameWinnerAvailable(TicTacToeField.CellValue v) {
        Log.d(Constants.LOG_TAG, "Game winner: "+v);
        network.sendToHost(new TicTacToeMessage(0, 0, v, TicTacToeMessage.Type.WINNER),
                () -> Log.e(Constants.LOG_TAG, "Can't send a message to the host!"));
    }
}
