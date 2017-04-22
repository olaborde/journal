package com.osselaborde.journal.data;

import android.content.Context;
import android.util.Log;
import com.osselaborde.journal.annotation.ApplicationContext;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import rx.schedulers.Schedulers;

/**
 * Dagger module to provide data dependencies in the app.
 */
@Module public class DataModule {

    @Provides
    @Singleton
    DbOpenHelper provideOpenHelper(@ApplicationContext Context applicationContext) {
        return new DbOpenHelper(applicationContext);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return new SqlBrite.Builder().logger(message -> Log.d("TEST", message)).build();
    }

    @Provides
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, DbOpenHelper helper) {
        return sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    }

    @Provides
    @Singleton
    EntriesManager provideEntriesManager(BriteDatabase briteDatabase) {
        return new EntriesManager(briteDatabase);
    }
}
