package com.github.sonerik.networkslab.events;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnCellClickedEvent {
    public final int x;
    public final int y;
    public final boolean isEmpty;
}
