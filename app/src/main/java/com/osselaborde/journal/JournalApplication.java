package com.osselaborde.journal;

import android.app.Application;

/**
 * Application object for initialising nqpp state qnd dependencies.
 */
public class JournalApplication extends Application {

    private static JournalApplication instance;
    private AppComponent component;

    public static AppComponent getAppComponent() {
        return instance.getComponent();
    }

    public AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initAppComponent();
        component.inject(this);
    }

    protected void initAppComponent() {
        component = AppComponent.Initializer.init(this);
    }
}
