package com.github.sonerik.networkslab.beans.draw;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.IOException;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonObject
public class DrawMessage {

    public enum Type { NEW_POINTS, CLEAR }

    @JsonField(typeConverter = DrawMsgTypeConverter.class)
    public Type type;

    @JsonField
    public List<Point> points;

    public static DrawMessage fromJson(String json) {
        try {
            return LoganSquare.parse(json, DrawMessage.class);
        } catch (IOException e) {
            return null;
        }
    }
}
