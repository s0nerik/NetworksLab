package com.github.sonerik.networkslab.adapters.chat_message;

import android.support.annotation.NonNull;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class ChatMessageAdapter extends FlexibleAdapter<ChatMessageItem> {
    public ChatMessageAdapter(@NonNull List<ChatMessageItem> items) {
        super(items);
    }
}
