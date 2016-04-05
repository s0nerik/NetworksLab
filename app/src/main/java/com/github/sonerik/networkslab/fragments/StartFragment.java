package com.github.sonerik.networkslab.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.fragments.chat.ChatClientFragment;
import com.github.sonerik.networkslab.fragments.chat.ChatHostFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btnChatClient)
    public void onStartChatClient() {
        getActivity().getSupportFragmentManager()
                     .beginTransaction()
                     .replace(R.id.content, new ChatClientFragment())
                     .addToBackStack(null)
                     .commit();
    }

    @OnClick(R.id.btnChatHost)
    public void onStartChatHost() {
        getActivity().getSupportFragmentManager()
                     .beginTransaction()
                     .replace(R.id.content, new ChatHostFragment())
                     .addToBackStack(null)
                     .commit();
    }

}
