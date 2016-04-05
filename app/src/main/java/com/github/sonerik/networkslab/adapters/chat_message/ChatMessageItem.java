package com.github.sonerik.networkslab.adapters.chat_message;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.sonerik.networkslab.R;
import com.peak.salut.SalutDevice;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatMessageItem extends AbstractFlexibleItem<ChatMessageViewHolder> {

    @Getter
    private final SalutDevice device;

    @Override
    public int getLayoutRes() {
        return R.layout.item_chat;
    }

    @Override
    public ChatMessageViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ChatMessageViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ChatMessageViewHolder holder, int position, List payloads) {
        holder.setDevice(device);
    }

    @Override
    public boolean equals(Object o) {
        return device.equals(o);
    }

}
