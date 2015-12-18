package com.chymtt.reactnativedropdown;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;

import java.util.ArrayList;

public class Dropdown extends AppCompatSpinner {

    private Context mContext;
    private boolean firstEventFired = false;
    private int mSelected = 0;
    private int selected = 0;

    public Dropdown(ThemedReactContext context) {
        super(context, 0);
        mContext = context;
        setOnItemSelectedListener(ON_ITEM_SELECTED_LISTENER);
    }

    public void setValues(ReadableArray values) {
        ArrayList<String> spinnerArray = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            String type = values.getType(i).name();
            if ("String".equals(type)) {
                spinnerArray.add(values.getString(i));
            } else {
                if ("Number".equals(type)) {
                    Double v = values.getDouble(i);
                    if ((v == Math.floor(v)) && !Double.isInfinite(v)) {
                        spinnerArray.add("" + values.getInt(i));
                    } else {
                        spinnerArray.add("" + v);
                    }
                } else if ("Boolean".equals(type)) {
                    spinnerArray.add("" + values.getBoolean(i));
                } else if ("Array".equals(type)) {
                    spinnerArray.add(values.getArray(i).toString());
                } else if ("Map".equals(type)) {
                    spinnerArray.add(values.getMap(i).toString());
                }
            }
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(spinnerArrayAdapter);
        setSelection(mSelected);
    }

    public void setSelected(int selected) {
        if (selected == mSelected && selected == this.selected) {
            return;
        }
        mSelected = selected;
        setSelection(mSelected);
    }

    private final AdapterView.OnItemSelectedListener ON_ITEM_SELECTED_LISTENER =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    selected = pos;
                    // It always fire this event when the component starts, thus we need to surpress
                    // the first event
                    if (!firstEventFired) {
                        firstEventFired = true;
                        return;
                    }
                    ReactContext reactContext = (ReactContext) view.getContext();
                    reactContext
                            .getNativeModule(UIManagerModule.class)
                            .getEventDispatcher().dispatchEvent(
                                new DropdownEvent(
                                    getId(),
                                    SystemClock.uptimeMillis(),
                                    pos,
                                    parent.getSelectedItem().toString()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            };

    private final Runnable mLayoutRunnable = new Runnable() {
        @Override
        public void run() {
            measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(mLayoutRunnable);
    }
}
