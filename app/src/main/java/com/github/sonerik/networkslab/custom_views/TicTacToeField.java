package com.github.sonerik.networkslab.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sonerik.networkslab.R;

public class TicTacToeField extends LinearLayout {
    public enum CellValue { EMPTY, X, O }

    public interface CellValueChangedListener {
        void onCellValueChanged(int x, int y, CellValue value);
    }

    private TextView[][] cells = new TextView[3][3];
    private CellValue playerCellValue = CellValue.X;
    private CellValue enemyCellValue = CellValue.O;
    private CellValue lastCellValue = CellValue.EMPTY;
    private CellValueChangedListener valueChangedListener;

    private boolean test = false;

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
        cells[0][0] = (TextView) view.findViewById(R.id.cell11);
        cells[0][1] = (TextView) view.findViewById(R.id.cell12);
        cells[0][2] = (TextView) view.findViewById(R.id.cell13);
        cells[1][0] = (TextView) view.findViewById(R.id.cell21);
        cells[1][1] = (TextView) view.findViewById(R.id.cell22);
        cells[1][2] = (TextView) view.findViewById(R.id.cell23);
        cells[2][0] = (TextView) view.findViewById(R.id.cell31);
        cells[2][1] = (TextView) view.findViewById(R.id.cell32);
        cells[2][2] = (TextView) view.findViewById(R.id.cell33);

        for (TextView[] row : cells) {
            for (TextView cell : row) {
                cell.setOnClickListener(clickListener);
            }
        }

        addView(view);
    }

    private View.OnClickListener clickListener = v -> {
        int x = Character.getNumericValue(v.getTag().toString().charAt(0));
        int y = Character.getNumericValue(v.getTag().toString().charAt(1));
        boolean isEmpty = cells[x][y].getText().length() == 0;

        if (isEmpty) {
            if (test) {
                if (lastCellValue == playerCellValue) {
                    set(x, y, enemyCellValue);
                } else {
                    set(x, y, playerCellValue);
                }
                return;
            }

            if (lastCellValue == enemyCellValue) {
                set(x, y, playerCellValue);
            }
        }
    };

    public void set(int x, int y, CellValue value) {
        cells[x][y].setText(value == CellValue.EMPTY ? "" : value.toString());

        if (valueChangedListener != null)
            valueChangedListener.onCellValueChanged(x, y, value);

        lastCellValue = value;
    }

    public void setPlayerCellValue(CellValue playerCellValue) {
        this.playerCellValue = playerCellValue;
        enemyCellValue = playerCellValue == CellValue.X ? CellValue.O : CellValue.X;
    }

    public void setCellValueChangedListener(CellValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public void clearAll() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                set(i, j, CellValue.EMPTY);
            }
        }
    }

}
