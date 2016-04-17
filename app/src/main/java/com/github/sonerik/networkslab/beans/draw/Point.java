package com.github.sonerik.networkslab.beans.draw;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonObject
public class Point {
    public enum Type { DOWN, MOVE }

    @JsonField(typeConverter = PointTypeConverter.class)
    public Type type;
    @JsonField
    public float x;
    @JsonField
    public float y;
}