package com.osselaborde.journal.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import auto.parcel.AutoParcel;
import com.squareup.sqlbrite.BriteDatabase;
import rx.functions.Func1;

/**
 * Created by osselaborde on 4/17/17.
 */
@AutoParcel public abstract class JournalEntry {

    public static final String TABLE = "entry";
    public static final String ID = "entry_id";

    public static final String TITLE = "title";
    public static final String DETAILS = "details";
    public static final String ADDRESS = "address";
    public static final String IMAGE_PATH = "image_path";

    public static final String CREATE_ENTRY = ""
        + "CREATE TABLE "
        + TABLE
        + "("
        + ID
        + " INTEGER NOT NULL PRIMARY KEY,"
        + TITLE
        + " TEXT NOT NULL UNIQUE, "
        + DETAILS
        + " TEXT, "
        + ADDRESS
        + " TEXT, "
        + IMAGE_PATH
        + " TEXT)";

    public static final String GET_ENTRIES = "SELECT * FROM " + JournalEntry.TABLE;

    public static Func1<Cursor, JournalEntry> MAPPER = cursor -> create(new OkCursor(cursor));

    public static JournalEntry create(long id, String title, String details,
        @NonNull String address, String imagePath) {
        return new AutoParcel_JournalEntry(id, title, details, address, imagePath);
    }

    public abstract long id();
    public abstract String title();
    public abstract String details();
    public abstract String address();
    public abstract String imagePath();

    public static JournalEntry create(OkCursor okCursor) {
        long id = okCursor.getLong(JournalEntry.ID);
        String title = okCursor.getString(JournalEntry.TITLE);
        String details = okCursor.getString(JournalEntry.DETAILS);
        String address = okCursor.getString(JournalEntry.ADDRESS);
        String imagePath = okCursor.getString(JournalEntry.IMAGE_PATH);
        return JournalEntry.create(id, title, details, address, imagePath);
    }

    public long insertInto(BriteDatabase db) {
        return db.insert(JournalEntry.TABLE, toContentValues());
    }

    private ContentValues toContentValues() {
        Builder builder = new Builder();
        if (id() > 0) {
            builder = builder.id(id());
        }
        return builder.title(title()).details(details()).address(address()).imagePath(imagePath()).build();
    }

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(ID, id);
            return this;
        }

        public Builder title(String title) {
            values.put(TITLE, title);
            return this;
        }

        public Builder details(String details) {
            values.put(DETAILS, details);
            return this;
        }

        public Builder address(String address) {
            values.put(ADDRESS, address);
            return this;
        }

        public Builder imagePath(String imagePath) {
            values.put(IMAGE_PATH, imagePath);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
