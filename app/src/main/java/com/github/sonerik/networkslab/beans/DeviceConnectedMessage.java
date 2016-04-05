package com.github.sonerik.networkslab.beans;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.github.sonerik.networkslab.Constants;
import com.peak.salut.SalutDevice;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
@AllArgsConstructor
@JsonObject
public class DeviceConnectedMessage {
    @JsonField
    public SalutDevice device;

    @SneakyThrows
    public String toJson() {
        return LoganSquare.serialize(this);
    }

    public static DeviceConnectedMessage fromJson(String json) {
        try {
            return LoganSquare.parse(json, DeviceConnectedMessage.class);
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error while parsing ChatMessage", e);
            return null;
        }
    }
}
