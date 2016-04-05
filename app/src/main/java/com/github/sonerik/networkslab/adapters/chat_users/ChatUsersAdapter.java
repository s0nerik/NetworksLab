package com.github.sonerik.networkslab.adapters.chat_users;

import android.support.annotation.NonNull;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class ChatUsersAdapter extends FlexibleAdapter<ChatUsersItem> {
    public ChatUsersAdapter(@NonNull List<ChatUsersItem> items) {
        super(items);
    }
}
