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

import butterknife.Bind;
import butterknife.ButterKnife;

public class TicTacToeFragment extends Fragment {
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

        field.setPlayerCellValue(TicTacToeField.CellValue.O);
        field.setCellValueChangedListener((x, y, value) -> Log.d(Constants.LOG_TAG, "Value changed: ("+x+","+y+"), "+value));
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

}
