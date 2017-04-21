package com.osselaborde.journal;

import com.osselaborde.journal.data.DataModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = { AppModule.class, DataModule.class })
public interface AppComponent {

    void inject(JournalApplication journalApplication);
    void inject(MainActivity mainActivity);
    void inject(AddEntryActivity addEntryActivity);

    final class Initializer {
        private Initializer() {
        }

        static AppComponent init(JournalApplication app) {
            return DaggerAppComponent.builder().appModule(new AppModule(app)).build();
        }
    }

}
