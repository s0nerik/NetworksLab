package com.github.sonerik.networkslab.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.sonerik.networkslab.Constants;
import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.adapters.chat_message.ChatMessageAdapter;
import com.github.sonerik.networkslab.adapters.chat_message.ChatMessageItem;
import com.github.sonerik.networkslab.fragments.base.NetworkFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class ChatFragment extends NetworkFragment {

    @Bind(R.id.textInput)
    EditText editText;

    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    private List<ChatMessageItem> messages = new ArrayList<>();
    private ChatMessageAdapter adapter = new ChatMessageAdapter(messages);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected String getServiceName() {
        return Constants.SERVICE_CHAT;
    }

    @Override
    protected int getDefaultServicePort() {
        return Constants.SERVICE_CHAT_DEFAULT_PORT;
    }

    @OnClick(R.id.btnSend)
    public void onSend() {
        Log.d(Constants.LOG_TAG, "onSend: "+editText.getText());
    }
}
