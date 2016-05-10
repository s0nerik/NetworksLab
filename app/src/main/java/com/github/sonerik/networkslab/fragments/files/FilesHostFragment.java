package com.github.sonerik.networkslab.fragments.files;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.github.sonerik.networkslab.R;
import com.github.sonerik.networkslab.beans.files.FileMessage;
import com.peak.salut.SalutDevice;

import butterknife.Bind;

public class FilesHostFragment extends FilesFragment {

    @Bind(R.id.waiting)
    View waiting;

    private SalutDevice otherDevice;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        waiting.setVisibility(View.VISIBLE);
        btnChooseFile.setVisibility(View.GONE);
        network.startNetworkService(salutDevice -> {
            waiting.setVisibility(View.GONE);
            btnChooseFile.setVisibility(View.VISIBLE);

            otherDevice = salutDevice;
        });
    }

    @Override
    protected void send(FileMessage msg) {
        network.sendToAllDevices(msg, () -> Log.e(getTag(), "Can't send a file message"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        network.stopNetworkService(true);
    }
}
