package com.github.sonerik.networkslab.beans.draw;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;

public class DrawMsgTypeConverter extends StringBasedTypeConverter<DrawMessage.Type> {
    @Override
    public DrawMessage.Type getFromString(String string) {
        return DrawMessage.Type.valueOf(string);
    }

    @Override
    public String convertToString(DrawMessage.Type object) {
        return object.toString();
    }
}
