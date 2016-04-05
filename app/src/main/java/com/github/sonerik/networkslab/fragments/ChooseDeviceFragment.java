package com.github.sonerik.networkslab.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.adapters.device_chooser.DeviceChooserAdapter;
import com.github.sonerik.networkslab.adapters.device_chooser.DeviceChooserItem;
import com.github.sonerik.networkslab.events.DeviceChosenEvent;
import com.peak.salut.Salut;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lombok.Setter;

public class ChooseDeviceFragment extends Fragment {
    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    private List<DeviceChooserItem> items = new ArrayList<>();
    private DeviceChooserAdapter adapter = new DeviceChooserAdapter(items);

    @Setter
    private Salut network;

    public ChooseDeviceFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public ChooseDeviceFragment(Salut network) {
        this.network = network;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_chooser, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setAdapter(adapter);

        network.discoverNetworkServices(device -> {
            items.add(new DeviceChooserItem(device));
            adapter.notifyDataSetChanged();
        }, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if( keyCode == KeyEvent.KEYCODE_BACK )
            {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(DeviceChosenEvent e) {
        network.stopServiceDiscovery(false);

        getActivity().getSupportFragmentManager().popBackStack();
    }
}
