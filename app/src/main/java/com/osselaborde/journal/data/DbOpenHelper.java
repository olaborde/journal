package com.osselaborde.journal.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import static com.osselaborde.journal.data.JournalEntry.CREATE_ENTRY;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3;
    private static final String JOURNAL_DB = "journal.db";

    public DbOpenHelper(Context context) {
        super(context, JOURNAL_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < 16) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
        db.execSQL(CREATE_ENTRY);
    }

    public void recreateDb() {
        recreateDb(getWritableDatabase());
    }

    public void recreateDb(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + JournalEntry.TABLE);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != DB_VERSION) {
            recreateDb(db);
        }
    }

    @TargetApi(16)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

}
