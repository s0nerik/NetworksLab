package com.github.sonerik.networkslab.fragments.tic_tac_toe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.tic_tac_toe.TicTacToeMessage;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;
import com.github.sonerik.networkslab.fragments.base.NetworkFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import lombok.val;

public abstract class TicTacToeFragment extends NetworkFragment {
    @Bind(R.id.field)
    TicTacToeField field;

    @Bind(R.id.winner)
    TextView winner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tictactoe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

//        field.setTest(true);
        field.setWinnerNotifyDelay(2000);
        field.setAutoClearOnWin(true);
        field.setPlayerCellValue(getPlayerCellValue());
        field.setCellValueChangedListener(this::onCellValueChanged);
        field.setGameWinnerListener(w -> {
            winner.setText(w.toString());
            onGameWinnerAvailable(w);
        });
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDataReceived(Object o) {
        super.onDataReceived(o);

        val msg = TicTacToeMessage.fromJson((String)o);
        if (msg != null) {
            if (msg.type == TicTacToeMessage.Type.CELL_VALUE) {
                field.set(msg.x, msg.y, msg.value);
            } else if (msg.type == TicTacToeMessage.Type.WINNER) {
                Log.d(Constants.LOG_TAG, "Winner: "+msg.value);
            }
        }
    }

    @Override
    protected String getServiceName() {
        return Constants.SERVICE_TIC_TAC_TOE;
    }

    @Override
    protected int getDefaultServicePort() {
        return Constants.SERVICE_TIC_TAC_TOE_DEFAULT_PORT;
    }

    protected abstract TicTacToeField.CellValue getPlayerCellValue();

    protected abstract void onCellValueChanged(int x, int y, TicTacToeField.CellValue v);

    protected abstract void onGameWinnerAvailable(TicTacToeField.CellValue v);
}
