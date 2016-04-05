package com.github.sonerik.networkslab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TicTacToeField extends LinearLayout {
    private TextView cell11;
    private TextView cell12;
    private TextView cell13;
    private TextView cell21;
    private TextView cell22;
    private TextView cell23;
    private TextView cell31;
    private TextView cell32;
    private TextView cell33;

    public TicTacToeField(Context context) {
        super(context);
        init();
    }

    public TicTacToeField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TicTacToeField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        View view = inflate(getContext(), R.layout.tictactoe_field, null);
        this.cell11 = (TextView) view.findViewById(R.id.cell11);
        this.cell12 = (TextView) view.findViewById(R.id.cell12);
        this.cell13 = (TextView) view.findViewById(R.id.cell13);
        this.cell21 = (TextView) view.findViewById(R.id.cell21);
        this.cell22 = (TextView) view.findViewById(R.id.cell22);
        this.cell23 = (TextView) view.findViewById(R.id.cell23);
        this.cell31 = (TextView) view.findViewById(R.id.cell31);
        this.cell32 = (TextView) view.findViewById(R.id.cell32);
        this.cell33 = (TextView) view.findViewById(R.id.cell33);
        addView(view);
    }

    public void set(int x, int y, boolean value) {
        String cell = x + "" + y;
        String xo = value ? "O" : "X";
        switch (cell) {
            case "11":
                cell11.setText(xo);
                break;
            case "12":
                cell12.setText(xo);
                break;
            case "13":
                cell13.setText(xo);
                break;
            case "21":
                cell21.setText(xo);
                break;
            case "22":
                cell22.setText(xo);
                break;
            case "23":
                cell23.setText(xo);
                break;
            case "31":
                cell31.setText(xo);
                break;
            case "32":
                cell32.setText(xo);
                break;
            case "33":
                cell33.setText(xo);
                break;
            default:
        }
    }
}
