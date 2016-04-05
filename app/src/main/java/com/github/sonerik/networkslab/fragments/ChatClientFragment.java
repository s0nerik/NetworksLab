package com.github.sonerik.networkslab.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatClientFragment extends Fragment implements SalutDataCallback {

    @Bind(R.id.textInput)
    EditText editText;

    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    private List<ChatMessageItem> messages = new ArrayList<>();
    private ChatMessageAdapter adapter = new ChatMessageAdapter(messages);

    private SalutDataReceiver dataReceiver = new SalutDataReceiver(getActivity(), this);
    private SalutServiceData serviceData = new SalutServiceData(Constants.SERVICE_CHAT,
                                                                Constants.SERVICE_CHAT_DEFAULT_PORT,
                                                                Build.MODEL);

    private Salut network = new Salut(dataReceiver,
                                      serviceData,
                                      () -> Log.e(Constants.LOG_TAG, "Sorry, but this device does not support WiFi Direct."));

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

        getActivity().getSupportFragmentManager()
                     .beginTransaction()
                     .add(R.id.content, new ChooseDeviceFragment(network), null)
                     .commit();
    }

    @OnClick(R.id.btnSend)
    public void onSend() {
        Log.d(Constants.LOG_TAG, "onSend: "+editText.getText());
    }

    @Override
    public void onDataReceived(Object o) {
        Log.d(Constants.LOG_TAG, "onDataReceived: "+o);
    }
}
