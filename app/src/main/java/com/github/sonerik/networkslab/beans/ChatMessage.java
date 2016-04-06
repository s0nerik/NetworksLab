package com.github.sonerik.networkslab.beans;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.github.sonerik.networkslab.Constants;
import com.peak.salut.SalutDevice;

import java.io.IOException;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

@EqualsAndHashCode
@JsonObject
public class ChatMessage {

    public enum NestedType { NOT_NESTED, DEVICE_STATUS_CHANGED, DEVICES_LIST }

    public static class NestedTypeConverter extends StringBasedTypeConverter<NestedType> {
        @Override
        public NestedType getFromString(String s) {
            return NestedType.valueOf(s);
        }

        public String convertToString(NestedType object) {
            return object.toString();
        }
    }

    @JsonField
    public String text;

    @JsonField
    public SalutDevice author;

    @JsonField
    public SalutDevice recipient;

    @JsonField(typeConverter = NestedTypeConverter.class)
    public NestedType nestedType;

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
