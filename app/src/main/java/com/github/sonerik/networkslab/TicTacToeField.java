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
        addView(view);
    }

//    private void init() {
//        inflate(getContext(), R.layout.tictactoe_field, this);
//        this.cell11 = (TextView) findViewById(R.id.cell11);
//        this.cell12 = (TextView) findViewById(R.id.cell12);
//        this.cell13 = (TextView) findViewById(R.id.cell13);
//        this.cell21 = (TextView) findViewById(R.id.cell21);
//        this.cell22 = (TextView) findViewById(R.id.cell22);
//        this.cell23 = (TextView) findViewById(R.id.cell23);
//        this.cell31 = (TextView) findViewById(R.id.cell31);
//        this.cell32 = (TextView) findViewById(R.id.cell32);
//        this.cell33 = (TextView) findViewById(R.id.cell33);
//    }
}
