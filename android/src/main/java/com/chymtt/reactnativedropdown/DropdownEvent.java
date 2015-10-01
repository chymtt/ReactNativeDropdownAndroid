package com.chymtt.reactnativedropdown;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class DropdownEvent extends Event<DropdownEvent> {
    public static final String EVENT_NAME = "topChange";

    private final int mPosition;
    private final String mValue;

    public DropdownEvent(int viewId, long timestampMs, int position, String value) {
        super(viewId, timestampMs);
        mPosition = position;
        mValue = value;
    }

    public int getPosition() {
        return mPosition;
    }

    public String getValue() {
        return mValue;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public short getCoalescingKey() {
        return 0;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap eventData = Arguments.createMap();
        eventData.putInt("selected", getPosition());
        eventData.putString("value", getValue());
        return eventData;
    }
}
