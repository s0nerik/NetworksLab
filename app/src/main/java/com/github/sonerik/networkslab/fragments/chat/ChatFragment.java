package com.github.sonerik.networkslab.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import com.github.sonerik.networkslab.adapters.chat_users.ChatUsersAdapter;
import com.github.sonerik.networkslab.adapters.chat_users.ChatUsersItem;
import com.github.sonerik.networkslab.beans.ChatMessage;
import com.github.sonerik.networkslab.beans.DeviceStatusChangedMessage;
import com.github.sonerik.networkslab.events.ChatUserClickedEvent;
import com.github.sonerik.networkslab.fragments.base.NetworkFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.val;

public abstract class ChatFragment extends NetworkFragment {

    @Bind(R.id.textInput)
    EditText editText;

    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    @Bind(R.id.recycler_users)
    RecyclerView usersRecycler;

    private List<ChatMessageItem> messages = new ArrayList<>();
    private ChatMessageAdapter adapter = new ChatMessageAdapter(messages);

    protected List<ChatUsersItem> users = new ArrayList<>();
    protected ChatUsersAdapter usersAdapter = new ChatUsersAdapter(users);

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
        ((LinearLayoutManager) recyclerView.getLayoutManager()).setStackFromEnd(true);

        usersRecycler.setAdapter(usersAdapter);

        users.add(new ChatUsersItem(null));
        usersAdapter.notifyDataSetChanged();
    }

    @Override
    protected String getServiceName() {
        return Constants.SERVICE_CHAT;
    }

    @Override
    protected int getDefaultServicePort() {
        return Constants.SERVICE_CHAT_DEFAULT_PORT;
    }

    @Override
    public void onDataReceived(Object o) {
        super.onDataReceived(o);

        val str = String.valueOf(o);
        val msg = ChatMessage.fromJson(str);
        if (msg != null) {
            switch (msg.nestedType) {
                case NOT_NESTED:
                    messages.add(new ChatMessageItem(msg));
                    adapter.notifyDataSetChanged();
                    break;
                case DEVICE_STATUS_CHANGED:
                    val deviceStatusMsg = DeviceStatusChangedMessage.fromJson(msg.text);
                    if (deviceStatusMsg != null
                            && !deviceStatusMsg.device.readableName.equals(network.thisDevice.readableName)
                            && !deviceStatusMsg.device.deviceName.equals(network.thisDevice.deviceName)) {
                        if (deviceStatusMsg.isConnected) {
                            Log.d(Constants.LOG_TAG, "User "+deviceStatusMsg.device.readableName+" has connected!");
                            users.add(new ChatUsersItem(deviceStatusMsg.device));
                            usersAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(Constants.LOG_TAG, "User "+deviceStatusMsg.device.readableName+" has disconnected!");

                            int deviceIndex = users.indexOf(new ChatUsersItem(deviceStatusMsg.device));

                            if (deviceIndex >= 0) {
                                users.remove(deviceIndex);
                                usersAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.btnSend)
    public void onSend() {
        Log.d(Constants.LOG_TAG, "onSend: "+editText.getText());

        // TODO: recipient chooser
        val msg = new ChatMessage();
        msg.nestedType = ChatMessage.NestedType.NOT_NESTED;
        msg.text = editText.getText().toString();
        msg.author = network.thisDevice;
        msg.recipient = null;

        send(msg);

        messages.add(new ChatMessageItem(msg));
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(ChatUserClickedEvent e) {
        Log.d(Constants.LOG_TAG, "ChatUserClickedEvent: "+e.device);
    }

    protected abstract void send(ChatMessage msg);
}
