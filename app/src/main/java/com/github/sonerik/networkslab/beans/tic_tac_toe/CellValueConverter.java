package com.github.sonerik.networkslab.beans.tic_tac_toe;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;

public class CellValueConverter extends StringBasedTypeConverter<TicTacToeField.CellValue> {
    @Override
    public TicTacToeField.CellValue getFromString(String s) {
        return TicTacToeField.CellValue.valueOf(s);
    }

    public String convertToString(TicTacToeField.CellValue object) {
        return object.toString();
    }
}
