package com.github.sonerik.networkslab.adapters.device_chooser;

import android.support.annotation.NonNull;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class DeviceChooserAdapter extends FlexibleAdapter<DeviceChooserItem> {
    public DeviceChooserAdapter(@NonNull List<DeviceChooserItem> items) {
        super(items);
    }
}
