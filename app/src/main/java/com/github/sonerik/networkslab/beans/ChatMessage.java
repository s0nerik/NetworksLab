package com.github.sonerik.networkslab.beans;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.github.sonerik.networkslab.Constants;
import com.peak.salut.SalutDevice;

import java.io.IOException;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

@EqualsAndHashCode
@JsonObject
public class ChatMessage {

    @JsonField
    public String text;

    @JsonField
    public SalutDevice author;

    @JsonField
    public SalutDevice recipient;

    @SneakyThrows
    public String toJson() {
        return LoganSquare.serialize(this);
    }

    public static ChatMessage fromJson(String json) {
        try {
            return LoganSquare.parse(json, ChatMessage.class);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error while parsing ChatMessage", e);
            return null;
        }
    }

}
