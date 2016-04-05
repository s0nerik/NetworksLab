package com.github.sonerik.networkslab.adapters.chat_message;

import android.view.View;
import android.widget.TextView;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.ChatMessage;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class ChatMessageViewHolder extends FlexibleViewHolder {

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.msg)
    TextView text;

    public ChatMessageViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        ButterKnife.bind(this, view);
    }

    public void setMessage(ChatMessage msg) {
        name.setText(msg.author.readableName);
        text.setText(msg.text);
    }

}
