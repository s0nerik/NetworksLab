package com.github.sonerik.networkslab.beans.tic_tac_toe;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.github.sonerik.networkslab.custom_views.TicTacToeField;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonObject
public class TicTacToeMessage {
    public enum Type { CELL_VALUE, WINNER }

    public static class MsgTypeConverter extends StringBasedTypeConverter<Type> {
        @Override
        public Type getFromString(String string) {
            return Type.valueOf(string);
        }

        @Override
        public String convertToString(Type object) {
            return object.toString();
        }
    }

    @JsonField
    public int x;
    @JsonField
    public int y;
    @JsonField(typeConverter = CellValueConverter.class)
    public TicTacToeField.CellValue value;
    @JsonField(typeConverter = MsgTypeConverter.class)
    public Type type;

    public static TicTacToeMessage fromJson(String json) {
        try {
            return LoganSquare.parse(json, TicTacToeMessage.class);
        } catch (IOException e) {
            return null;
        }
    }
}
