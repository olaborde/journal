package com.osselaborde.journal;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.osselaborde.journal.annotation.ApplicationContext;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {

    @NonNull private final Application application;

    public AppModule(@NonNull Application app) {
        this.application = app;
    }

    @Provides
    @Singleton
    @ApplicationContext
    public Context provideApplicationContext() {
        return application.getApplicationContext();
    }
}
