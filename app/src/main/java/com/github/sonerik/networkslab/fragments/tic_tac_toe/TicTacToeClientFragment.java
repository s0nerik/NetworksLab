package com.github.sonerik.networkslab.fragments.tic_tac_toe;

import android.util.Log;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;

public class TicTacToeClientFragment extends TicTacToeFragment {
    @Override
    protected TicTacToeField.CellValue getPlayerCellValue() {
        return TicTacToeField.CellValue.O;
    }

    @Override
    protected void onCellValueChanged(int x, int y, TicTacToeField.CellValue v) {
        if (v == getPlayerCellValue()) {
            Log.d(Constants.LOG_TAG, "Set new client value");
        }
    }

    @Override
    protected void onGameWinnerAvailable(TicTacToeField.CellValue v) {
        Log.d(Constants.LOG_TAG, "Game winner: "+v);
    }
}
