package com.github.sonerik.networkslab.fragments.tic_tac_toe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.tic_tac_toe.TicTacToeMessage;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;
import com.peak.salut.SalutDevice;

import butterknife.Bind;

public class TicTacToeHostFragment extends TicTacToeFragment {

    @Bind(R.id.waiting)
    View waiting;

    private SalutDevice otherPlayer;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waiting.setVisibility(View.VISIBLE);
        field.setVisibility(View.GONE);
        network.startNetworkService(salutDevice -> {
            waiting.setVisibility(View.GONE);
            field.setVisibility(View.VISIBLE);

            otherPlayer = salutDevice;
        });
    }

    @Override
    protected TicTacToeField.CellValue getPlayerCellValue() {
        return TicTacToeField.CellValue.X;
    }

    @Override
    protected void onCellValueChanged(int x, int y, TicTacToeField.CellValue v) {
        if (v == getPlayerCellValue()) {
            Log.d(Constants.LOG_TAG, "Set new host value");
            network.sendToDevice(otherPlayer,
                    new TicTacToeMessage(x, y, v, TicTacToeMessage.Type.CELL_VALUE),
                    () -> Log.e(Constants.LOG_TAG, "Can't send a message to the host!"));

        }
    }

    @Override
    protected void onGameWinnerAvailable(TicTacToeField.CellValue v) {
        Log.d(Constants.LOG_TAG, "Game winner: "+v);
        network.sendToDevice(otherPlayer,
                new TicTacToeMessage(0, 0, v, TicTacToeMessage.Type.WINNER),
                () -> Log.e(Constants.LOG_TAG, "Can't send a message to the host!"));
    }
}
