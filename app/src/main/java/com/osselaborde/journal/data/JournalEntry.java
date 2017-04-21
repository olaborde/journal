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

    public static final String DAY_OF_WEEK = "day_of_week";
    public static final String DAY_DATE_NUMBER = "day_date_number";
    public static final String CREATION_DATE = "creation_date";

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
        + " TEXT, "
        + DAY_OF_WEEK
        + " TEXT, "
        + DAY_DATE_NUMBER
        + " INTEGER, "
        + CREATION_DATE
        + " TEXT )";

    public static final String GET_ENTRIES = "SELECT * FROM " + JournalEntry.TABLE;

    public static Func1<Cursor, JournalEntry> MAPPER = cursor -> create(new OkCursor(cursor));

    public static JournalEntry create(long id, String title, String details,
        @NonNull String address, String imagePath, String dayOfWeekOfEntry, int dayDateNumber,
        String creationDate) {
        return new AutoParcel_JournalEntry(id, title, details, address, imagePath, dayOfWeekOfEntry,
            dayDateNumber, creationDate);
    }

    public abstract long id();

    public abstract String title();

    public abstract String details();

    public abstract String address();

    public abstract String imagePath();

    public abstract String dayOfWeek();

    public abstract int dayDateNumber();

    public abstract String creationDate();

    public static JournalEntry create(OkCursor okCursor) {
        long id = okCursor.getLong(JournalEntry.ID);
        String title = okCursor.getString(JournalEntry.TITLE);
        String details = okCursor.getString(JournalEntry.DETAILS);
        String address = okCursor.getString(JournalEntry.ADDRESS);
        String imagePath = okCursor.getString(JournalEntry.IMAGE_PATH);
        String dayOfWeek = okCursor.getString(JournalEntry.DAY_OF_WEEK);
        int dayDateNumber = okCursor.getInt(JournalEntry.DAY_DATE_NUMBER);
        String creationDate = okCursor.getString(JournalEntry.CREATION_DATE);
        return JournalEntry.create(id, title, details, address, imagePath, dayOfWeek, dayDateNumber,
            creationDate);
    }

    public long insertInto(BriteDatabase db) {
        return db.insert(JournalEntry.TABLE, toContentValues());
    }

    private ContentValues toContentValues() {
        Builder builder = new Builder();
        if (id() > 0) {
            builder = builder.id(id());
        }
        return builder.title(title())
            .details(details())
            .address(address())
            .imagePath(imagePath())
            .dayOfWeekOfEntry(dayOfWeek())
            .dayDateNumber(dayDateNumber())
            .creationDate(creationDate())
            .build();
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

        public Builder dayOfWeekOfEntry(String dayOfWeekOfEntry) {
            values.put(DAY_OF_WEEK, dayOfWeekOfEntry);
            return this;
        }

        public Builder dayDateNumber(long dayDateNumber) {
            values.put(DAY_DATE_NUMBER, dayDateNumber);
            return this;
        }

        public Builder creationDate(String creationDate) {
            values.put(CREATION_DATE, creationDate);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
