package com.github.sonerik.networkslab.fragments.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.sonerik.networkslab.Constants;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;

public abstract class NetworkFragment extends Fragment implements SalutDataCallback {

    protected Salut network;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Salut(new SalutDataReceiver(getActivity(), this),
                            new SalutServiceData(getServiceName(),
                                                 getDefaultServicePort(),
                                                 Build.MODEL),
                            () -> Log.e(Constants.LOG_TAG, "Sorry, but this device does not support WiFi Direct."));
    }

    @Override
    public void onDataReceived(Object o) {
        Log.d(Constants.LOG_TAG, "onDataReceived: "+o);
    }

    protected abstract String getServiceName();
    protected abstract int getDefaultServicePort();
}
