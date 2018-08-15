package com.bus.lifecircleeventbus;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LifeCircleEventBus.init(this);
    }
}
