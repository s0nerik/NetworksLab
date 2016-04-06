package com.github.sonerik.networkslab.adapters.chat_users;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.sonerik.networkslab.R;
import com.peak.salut.SalutDevice;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class ChatUsersItem extends AbstractFlexibleItem<ChatUsersViewHolder> {

    @Getter
    private final SalutDevice device;

    @Override
    public int getLayoutRes() {
        return R.layout.item_chat_users;
    }

    @Override
    public ChatUsersViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ChatUsersViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ChatUsersViewHolder holder, int position, List payloads) {
        holder.setDevice(device);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatUsersItem) {
            val otherDevice = ((ChatUsersItem) o).getDevice();
            if (otherDevice == null) {
                return device == null;
            } else {
                return device.instanceName.equals(otherDevice.instanceName);
            }
        } else if (o instanceof SalutDevice) {
            val otherDevice = (SalutDevice) o;
            return device.instanceName.equals(otherDevice.instanceName);
        } else {
            return false;
        }
    }

}
