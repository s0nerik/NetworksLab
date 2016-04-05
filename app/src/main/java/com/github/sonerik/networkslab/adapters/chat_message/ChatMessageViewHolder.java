package com.github.sonerik.networkslab.adapters.chat_message;

import android.view.View;
import android.widget.TextView;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.ChatMessage;
import com.peak.salut.SalutDevice;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class ChatMessageViewHolder extends FlexibleViewHolder {

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.msg)
    TextView msg;

    private SalutDevice device;

    public ChatMessageViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        ButterKnife.bind(this, view);
    }

//    @OnClick(R.id.item)
//    public void onItemClicked() {
////        EventBus.getDefault().post();
////        bus.post(new MpGameSelectedEvent(device));
//    }

    public void setMessage(ChatMessage msg) {
//        this.device = device;
//
//        if (device.txtRecord != null) {
//            val name = device.txtRecord.get("name");
//            if (name != null) {
//                title.setText(name);
//            } else {
//                title.setText(device.readableName);
//            }
//
//            val players = device.txtRecord.get("players");
//            if (players != null)
//                subtitle.setText(players+" players");
//        } else {
//            title.setText(device.readableName);
//        }
    }

}
