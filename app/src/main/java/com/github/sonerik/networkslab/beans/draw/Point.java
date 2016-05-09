package com.github.sonerik.networkslab.beans.draw;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonObject
public class Point {
    public enum Type { DOWN, MOVE }
    public enum Figure { LINE, CIRCLE, CUBE }

    @JsonField(typeConverter = PointTypeConverter.class)
    public Type type;
    @JsonField(typeConverter = PointFigureConverter.class)
    public Figure figure;
    @JsonField
    public int index;
    @JsonField
    public float x;
    @JsonField
    public float y;
    @JsonField
    public int color;
    @JsonField
    public float thickness;
}