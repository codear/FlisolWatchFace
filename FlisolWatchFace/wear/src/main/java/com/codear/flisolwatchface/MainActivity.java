package com.codear.flisolwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends WatchFaceActivity {

    private TextView txtTime, txtBattery, txtCountry;
    private Typeface tpRegular, tpLight;

    private final static IntentFilter INTENT_FILTER;

    static {
        INTENT_FILTER = new IntentFilter();
        INTENT_FILTER.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER.addAction(Intent.ACTION_TIME_CHANGED);
    }

    static String TIME_FORMAT_DISPLAYED = "KK:mm a";

    private BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            txtTime.setText(new SimpleDateFormat(TIME_FORMAT_DISPLAYED).format(Calendar.getInstance().getTime()));
        }
    };

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            txtBattery.setText(String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "%"));
        }
    };


    @Override
    public void onScreenDim() {
        txtTime.setTextColor(Color.WHITE);
        txtBattery.setTextColor(Color.WHITE);
    }

    @Override
    public void onScreenAwake() {
        txtTime.setTextColor(getResources().getColor(R.color.primary_text));
        txtBattery.setTextColor(getResources().getColor(R.color.primary_text));
        txtCountry.setTextColor(getResources().getColor(R.color.secondary_text));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tpRegular = Typeface.createFromAsset(getAssets(), "Comfortaa-Regular.ttf");
        tpLight = Typeface.createFromAsset(getAssets(), "Comfortaa-Light.ttf");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                txtTime = (TextView) stub.findViewById(R.id.watch_time);
                txtBattery = (TextView) stub.findViewById(R.id.watch_battery);
                txtCountry = (TextView) stub.findViewById(R.id.watch_country);
                txtTime.setTypeface(tpRegular);
                txtBattery.setTypeface(tpLight);
                txtCountry.setTypeface(tpLight);
                txtTime.setTextColor(getResources().getColor(R.color.primary_text));
                txtBattery.setTextColor(getResources().getColor(R.color.primary_text));
                txtCountry.setTextColor(getResources().getColor(R.color.secondary_text));
                txtCountry.setText(getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry());
                mTimeInfoReceiver.onReceive(MainActivity.this, null);
                registerReceiver(mTimeInfoReceiver, INTENT_FILTER);
                registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeInfoReceiver);
        unregisterReceiver(mBatInfoReceiver);
    }

}
