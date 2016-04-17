package com.github.sonerik.networkslab.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sonerik.networkslab.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class TicTacToeField extends LinearLayout {
    public enum CellValue { EMPTY, X, O }

    public interface CellValueChangedListener {
        void onCellValueChanged(int x, int y, CellValue value);
    }

    public interface GameWinnerListener {
        void onGameWon(CellValue value);
    }

    private TextView[][] cells = new TextView[3][3];
    private CellValue[][] cellVals = new CellValue[3][3];

    private CellValue playerCellValue = CellValue.X;
    private CellValue enemyCellValue = CellValue.O;
    private CellValue lastCellValue = CellValue.EMPTY;

    private CellValueChangedListener valueChangedListener;
    private GameWinnerListener gameWinnerListener;

    private int winnerNotifyDelay = 0;

    private boolean test = false;
    private boolean autoClearOnWin = false;

    private boolean waitingForWinnerNotification = false;

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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cellVals[i][j] = CellValue.EMPTY;
            }
        }

        addView(view);
    }

    private View.OnClickListener clickListener = v -> {
        int x = Character.getNumericValue(v.getTag().toString().charAt(0));
        int y = Character.getNumericValue(v.getTag().toString().charAt(1));
        boolean isEmpty = cells[x][y].getText().length() == 0;

        if (isEmpty && !waitingForWinnerNotification) {
            if (test) {
                if (lastCellValue == playerCellValue) {
                    set(x, y, enemyCellValue);
                } else {
                    set(x, y, playerCellValue);
                }
                return;
            }

            if (lastCellValue != playerCellValue) {
                set(x, y, playerCellValue);
            }
        }
    };

    private CellValue getWinner() {
        for (int i = 0; i < 3; i++) {
            boolean allSameHor =   cellVals[i][0] == cellVals[i][1]
                                && cellVals[i][1] == cellVals[i][2];

            if (allSameHor && cellVals[i][0] != CellValue.EMPTY) {
                return cellVals[i][0];
            }

            boolean allSameVert =  cellVals[0][i] == cellVals[1][i]
                                && cellVals[1][i] == cellVals[2][i];

            if (allSameVert && cellVals[0][i] != CellValue.EMPTY) {
                return cellVals[0][i];
            }
        }

        boolean allSameDiag1 = cellVals[0][0] == cellVals[1][1]
                            && cellVals[1][1] == cellVals[2][2];

        if (allSameDiag1 && cellVals[0][0] != CellValue.EMPTY) {
            return cellVals[0][0];
        }

        boolean allSameDiag2 = cellVals[2][0] == cellVals[1][1]
                            && cellVals[0][2] == cellVals[1][1];

        if (allSameDiag2 && cellVals[2][0] != CellValue.EMPTY) {
            return cellVals[2][0];
        }

        return CellValue.EMPTY;
    }

    public void set(int x, int y, CellValue value) {
        set(x, y, value, true, true);
    }

    private void set(int x, int y, CellValue value, boolean notifyValueChanged, boolean notifyWinner) {
        cellVals[x][y] = value;
        cells[x][y].setText(value == CellValue.EMPTY ? "" : value.toString());

        if (valueChangedListener != null && notifyValueChanged)
            valueChangedListener.onCellValueChanged(x, y, value);

        CellValue winner = getWinner();
        if (winner != CellValue.EMPTY) {
            if (notifyWinner) {
                if (gameWinnerListener != null) {
                    Observable.timer(winnerNotifyDelay, TimeUnit.MILLISECONDS)
                              .doOnSubscribe(() -> waitingForWinnerNotification = true)
                              .doOnNext(aLong -> waitingForWinnerNotification = false)
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(aLong -> {
                                  if (autoClearOnWin) clearAll();
                                  gameWinnerListener.onGameWon(winner);
                              });
                } else {
                    if (autoClearOnWin) clearAll();
                }
            }
        }

        lastCellValue = value;
    }

    public void setPlayerCellValue(CellValue playerCellValue) {
        this.playerCellValue = playerCellValue;
        enemyCellValue = playerCellValue == CellValue.X ? CellValue.O : CellValue.X;
    }

    public void setCellValueChangedListener(CellValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    public void setGameWinnerListener(GameWinnerListener gameWinnerListener) {
        this.gameWinnerListener = gameWinnerListener;
    }

    public void setWinnerNotifyDelay(int winnerNotifyDelay) {
        this.winnerNotifyDelay = winnerNotifyDelay;
    }

    public void setAutoClearOnWin(boolean autoClearOnWin) {
        this.autoClearOnWin = autoClearOnWin;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public void clearAll() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                set(i, j, CellValue.EMPTY, true, false);
            }
        }
    }

}
