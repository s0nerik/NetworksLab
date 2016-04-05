package com.github.sonerik.networkslab.adapters.device_chooser;

import android.view.View;
import android.widget.TextView;

import com.github.sonerik.networkslab.R;
import com.peak.salut.SalutDevice;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class DeviceChooserViewHolder extends FlexibleViewHolder {

    @Bind(R.id.title)
    TextView title;

    private SalutDevice device;

    public DeviceChooserViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        ButterKnife.bind(this, view);
    }

    public void setDevice(SalutDevice device) {
        this.device = device;
        title.setText(device.readableName);
    }
}
