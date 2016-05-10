package com.github.sonerik.networkslab.beans.files;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.github.sonerik.networkslab.Constants;

import java.io.IOException;

import lombok.SneakyThrows;

@JsonObject
public class FileMessage {
    @JsonField
    public String name;
    @JsonField
    public byte[] content;

    @SneakyThrows
    public String toJson() {
        return LoganSquare.serialize(this);
    }

    public static FileMessage fromJson(String json) {
        try {
            return LoganSquare.parse(json, FileMessage.class);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error while parsing FileMessage", e);
            return null;
        }
    }
}
