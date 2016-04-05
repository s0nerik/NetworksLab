package com.github.sonerik.networkslab.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;
import com.github.sonerik.networkslab.events.OnCellClickedEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TicTacToeFragment extends Fragment {

    @Bind(R.id.field)
    TicTacToeField field;

    @Bind(R.id.cell11)
    TextView cell11;
    @Bind(R.id.cell12)
    TextView cell12;
    @Bind(R.id.cell13)
    TextView cell13;
    @Bind(R.id.cell21)
    TextView cell21;
    @Bind(R.id.cell22)
    TextView cell22;
    @Bind(R.id.cell23)
    TextView cell23;
    @Bind(R.id.cell31)
    TextView cell31;
    @Bind(R.id.cell32)
    TextView cell32;
    @Bind(R.id.cell33)
    TextView cell33;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tictactoe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        cell11.setOnClickListener(clickListener);
        cell12.setOnClickListener(clickListener);
        cell13.setOnClickListener(clickListener);
        cell21.setOnClickListener(clickListener);
        cell22.setOnClickListener(clickListener);
        cell23.setOnClickListener(clickListener);
        cell31.setOnClickListener(clickListener);
        cell32.setOnClickListener(clickListener);
        cell33.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int x = 0;
            int y = 0;
            boolean isEmpty = true;
            switch (v.getId()) {
                case R.id.cell11:
                    x = 1;
                    y = 1;
                    isEmpty = cell11.getText().equals("");
                    break;
                case R.id.cell12:
                    x = 1;
                    y = 2;
                    isEmpty = cell12.getText().equals("");
                    break;
                case R.id.cell13:
                    x = 1;
                    y = 3;
                    isEmpty = cell13.getText().equals("");
                    break;
                case R.id.cell21:
                    x = 2;
                    y = 1;
                    isEmpty = cell21.getText().equals("");
                    break;
                case R.id.cell22:
                    x = 2;
                    y = 2;
                    isEmpty = cell22.getText().equals("");
                    break;
                case R.id.cell23:
                    x = 2;
                    y = 3;
                    isEmpty = cell23.getText().equals("");
                    break;
                case R.id.cell31:
                    x = 3;
                    y = 1;
                    isEmpty = cell31.getText().equals("");
                    break;
                case R.id.cell32:
                    x = 3;
                    y = 2;
                    isEmpty = cell32.getText().equals("");
                    break;
                case R.id.cell33:
                    x = 3;
                    y = 3;
                    isEmpty = cell33.getText().equals("");
                    break;
            }
            EventBus.getDefault().post(new OnCellClickedEvent(x, y, isEmpty));
        }
    };

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

}
