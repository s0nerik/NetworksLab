package com.github.sonerik.networkslab.events;

import com.peak.salut.SalutDevice;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatUserClickedEvent {
    public final SalutDevice device;
}
