package com.github.sonerik.networkslab.beans;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ChatMessage {

    @JsonField
    public String author;

    @JsonField
    public String message;

}
