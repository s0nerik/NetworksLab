package com.github.sonerik.networkslab.fragments.tic_tac_toe;

import android.util.Log;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;

public class TicTacToeHostFragment extends TicTacToeFragment {
    @Override
    protected TicTacToeField.CellValue getPlayerCellValue() {
        return TicTacToeField.CellValue.X;
    }

    @Override
    protected void onCellValueChanged(int x, int y, TicTacToeField.CellValue v) {
        if (v == getPlayerCellValue()) {
            Log.d(Constants.LOG_TAG, "Set new host value");
        }
    }

    @Override
    protected void onGameWinnerAvailable(TicTacToeField.CellValue v) {
        Log.d(Constants.LOG_TAG, "Game winner: "+v);
    }
}
