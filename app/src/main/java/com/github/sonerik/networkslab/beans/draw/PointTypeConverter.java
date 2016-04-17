package com.github.sonerik.networkslab.beans.draw;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;

public class PointTypeConverter extends StringBasedTypeConverter<Point.Type> {
    @Override
    public Point.Type getFromString(String string) {
        return Point.Type.valueOf(string);
    }

    @Override
    public String convertToString(Point.Type object) {
        return object.toString();
    }
}
