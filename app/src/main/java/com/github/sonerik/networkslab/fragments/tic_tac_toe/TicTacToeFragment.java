package com.github.sonerik.networkslab.fragments.tic_tac_toe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;
import com.github.sonerik.networkslab.fragments.base.NetworkFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class TicTacToeFragment extends NetworkFragment {
    @Bind(R.id.field)
    TicTacToeField field;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tictactoe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        field.setPlayerCellValue(getPlayerCellValue());
        field.setCellValueChangedListener(this::onCellValueChanged);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
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
}
