package com.github.sonerik.networkslab.adapters.device_chooser;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.sonerik.networkslab.R;
import com.peak.salut.SalutDevice;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeviceChooserItem extends AbstractFlexibleItem<DeviceChooserViewHolder> {

    @Getter
    private final SalutDevice device;

    @Override
    public int getLayoutRes() {
        return R.layout.item_device_chooser;
    }

    @Override
    public DeviceChooserViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new DeviceChooserViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DeviceChooserViewHolder holder, int position, List payloads) {
        holder.setDevice(device);
    }

    @Override
    public boolean equals(Object o) {
        return device.equals(o);
    }

}
