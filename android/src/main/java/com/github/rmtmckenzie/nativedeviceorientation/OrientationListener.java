package com.github.rmtmckenzie.nativedeviceorientation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

import io.flutter.plugin.common.EventChannel;

public class OrientationListener {

    private static final IntentFilter orientationIntentFilter = new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED);

    private final OrientationReader reader;
    private final Context context;
    private final OrientationCallback callback;
    private BroadcastReceiver broadcastReceiver;

    interface OrientationCallback {
        void receive(OrientationReader.Orientation orientation);
    }

    public OrientationListener(OrientationReader reader, Context context, OrientationCallback callback) {
        this.reader = reader;
        this.context = context;
        this.callback = callback;
    }

    public void startOrientationListener() {
        if (broadcastReceiver != null) return;

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                callback.receive(reader.getOrientation());
            }
        };

        context.registerReceiver(broadcastReceiver, orientationIntentFilter);
    }

    public void stopOrientationListener() {
        if (broadcastReceiver == null) return;
        context.unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
    }

}