package com.chymtt.reactnativedropdown;

import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;

public class DropdownManager extends SimpleViewManager<Dropdown> {
    public static final String REACT_CLASS = "DropdownAndroid";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected Dropdown createViewInstance(ThemedReactContext context) {
        return new Dropdown(context);
    }

    @UIProp(UIProp.Type.ARRAY)
    public static final String PROP_VALUES = "values";

    @UIProp(UIProp.Type.NUMBER)
    public static final String PROP_SELECTED = "selected";

    @Override
    public void updateView(Dropdown view, CatalystStylesDiffMap props) {
        super.updateView(view, props);

        if (props.hasKey(PROP_VALUES)) {
            view.setValues(props.getArray(PROP_VALUES));
        }

        if (props.hasKey(PROP_SELECTED)) {
            view.setSelected(props.getInt(PROP_SELECTED, 0));
        }
    }
}