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
import com.github.sonerik.networkslab.beans.DevicesListMessage;
import com.github.sonerik.networkslab.events.ChatUserClickedEvent;
import com.github.sonerik.networkslab.fragments.base.NetworkFragment;
import com.peak.salut.SalutDevice;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private SalutDevice lastSelectedUser = null;

    private List<ChatMessageItem> messages = new ArrayList<>();
    private List<ChatMessageItem> displayedMessages = new ArrayList<>();
    private ChatMessageAdapter adapter = new ChatMessageAdapter(displayedMessages);
    private Map<SalutDevice, List<ChatMessageItem>> privateMessages = new HashMap<>();

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
                    if (!msg.author.equals(network.thisDevice)) {
                        addMessage(msg);
                    }
                    break;
                case DEVICE_STATUS_CHANGED:
                    val deviceStatusMsg = DeviceStatusChangedMessage.fromJson(msg.text);
                    if (deviceStatusMsg != null && !deviceStatusMsg.device.equals(network.thisDevice)) {
                        if (deviceStatusMsg.isConnected) {
                            Log.d(Constants.LOG_TAG, "User "+deviceStatusMsg.device.readableName+" has connected!");
                            addUser(deviceStatusMsg.device);
                        } else {
                            Log.d(Constants.LOG_TAG, "User "+deviceStatusMsg.device.readableName+" has disconnected!");
                            removeUser(deviceStatusMsg.device);
                        }
                    }
                    break;
                case DEVICES_LIST:
                    val devicesListMsg = DevicesListMessage.fromJson(msg.text);
                    if (devicesListMsg != null && devicesListMsg.devices != null) {
                        initUsersList(devicesListMsg.devices);
                    }
                    break;
            }

            onChatMessageReceived(msg);
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
        msg.recipient = lastSelectedUser;

        send(msg);

        addMessage(msg);
    }

    @Subscribe
    public void onEvent(ChatUserClickedEvent e) {
        Log.d(Constants.LOG_TAG, "ChatUserClickedEvent: "+e.device);
        lastSelectedUser = e.device;

        displayUserMessages(lastSelectedUser);
    }

    protected void initUsersList(List<SalutDevice> devices) {
        for (SalutDevice device : devices) {
            if (device != null && !device.equals(network.thisDevice)) {
                users.add(new ChatUsersItem(device));
                privateMessages.put(device, new ArrayList<>());
            }
        }
        usersAdapter.notifyDataSetChanged();
    }

    protected void addUser(SalutDevice device) {
        users.add(new ChatUsersItem(device));
        usersAdapter.notifyDataSetChanged();

        privateMessages.put(device, new ArrayList<>());
    }

    protected void removeUser(SalutDevice device) {
        int deviceIndex = users.indexOf(new ChatUsersItem(device));

        if (deviceIndex >= 0) {
            users.remove(deviceIndex);
            usersAdapter.notifyDataSetChanged();

            privateMessages.remove(device);
        }
    }

    protected void addMessage(ChatMessage msg) {
        if (msg.recipient == null) { // Someone is sending a message to everyone (public chat)
            messages.add(new ChatMessageItem(msg));

            displayMessageIfUserSelected(null, msg);
        } else if (msg.recipient.equals(network.thisDevice)) { // Someone is sending a message to us (private chat)
            List<ChatMessageItem> deviceMessages = privateMessages.get(msg.author);
            if (deviceMessages == null) {
                deviceMessages = new ArrayList<>();
                privateMessages.put(msg.author, deviceMessages);
            }
            deviceMessages.add(new ChatMessageItem(msg));

            displayMessageIfUserSelected(msg.author, msg);
        } else if (msg.author.equals(network.thisDevice) && msg.recipient != null) { // We are sending a message to someone (private chat)
            List<ChatMessageItem> deviceMessages = privateMessages.get(msg.recipient);
            if (deviceMessages == null) {
                deviceMessages = new ArrayList<>();
                privateMessages.put(msg.recipient, deviceMessages);
            }
            deviceMessages.add(new ChatMessageItem(msg));

            displayMessageIfUserSelected(msg.recipient, msg);
        }
    }

    protected void displayMessageIfUserSelected(SalutDevice user, ChatMessage msg) {
        if (lastSelectedUser != null && lastSelectedUser.equals(user) || lastSelectedUser == null && user == null) {
            displayedMessages.add(new ChatMessageItem(msg));
            adapter.notifyDataSetChanged();
        }
    }

    protected void displayUserMessages(SalutDevice user) {
        displayedMessages.clear();

        if (user == null) {
            displayedMessages.addAll(messages);
        } else {
            val userMessages = privateMessages.get(user);
            if (userMessages != null) {
                displayedMessages.addAll(userMessages);
            }
        }

        adapter.notifyDataSetChanged();
    }

    protected abstract void send(ChatMessage msg);

    protected void onChatMessageReceived(ChatMessage msg) {}
}
