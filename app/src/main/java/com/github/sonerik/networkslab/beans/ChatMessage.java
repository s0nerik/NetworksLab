package com.github.sonerik.networkslab.beans;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@JsonObject
public class ChatMessage {

    @JsonField
    public String author;

    @JsonField
    public String message;

}
