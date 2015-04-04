package com.codear.flisolwatchface;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;

/**
 * Created by gorro on 04/04/15.
 */
public abstract class WatchFaceActivity extends Activity implements DisplayManager.DisplayListener {

    public abstract void onScreenDim();

    public abstract void onScreenAwake();

    public void onWatchFaceRemoved() {
    }

    public void onScreenOff() {
    }

    private DisplayManager displayManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(this, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        displayManager.unregisterDisplayListener(this);
    }

    @Override
    public void onDisplayAdded(int displayId) {

    }

    @Override
    public void onDisplayRemoved(int displayId) {
        onWatchFaceRemoved();
    }

    @Override
    public void onDisplayChanged(int displayId) {
        switch (displayManager.getDisplay(displayId).getState()) {
            case Display.STATE_DOZE:
                onScreenDim();
                break;
            case Display.STATE_OFF:
                onScreenOff();
                break;
            default:
                onScreenAwake();
                break;
        }
    }

}
