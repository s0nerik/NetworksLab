package com.github.sonerik.networkslab.adapters.chat_users;

import android.view.View;
import android.widget.TextView;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.events.ChatUserClickedEvent;
import com.peak.salut.SalutDevice;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class ChatUsersViewHolder extends FlexibleViewHolder {

    @Bind(R.id.name)
    TextView name;

    private SalutDevice device;

    public ChatUsersViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.name)
    public void onItemClicked() {
        EventBus.getDefault().post(new ChatUserClickedEvent(device));
    }

    public void setDevice(SalutDevice device) {
        this.device = device;

        if (device != null) {
            name.setText(device.readableName);
        } else {
            name.setText("All");
        }
    }

}
