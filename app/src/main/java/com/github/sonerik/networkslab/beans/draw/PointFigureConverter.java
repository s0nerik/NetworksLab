package com.github.sonerik.networkslab.beans.draw;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;

public class PointFigureConverter extends StringBasedTypeConverter<Point.Figure> {
    @Override
    public Point.Figure getFromString(String string) {
        return Point.Figure.valueOf(string);
    }

    @Override
    public String convertToString(Point.Figure object) {
        return object.toString();
    }
}
