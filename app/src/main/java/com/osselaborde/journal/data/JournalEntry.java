package com.osselaborde.journal.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import auto.parcel.AutoParcel;
import com.squareup.sqlbrite.BriteDatabase;
import rx.functions.Func1;

/**
 * Entry model.
 */
@AutoParcel public abstract class JournalEntry implements Parcelable {

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
        + " TEXT NOT NULL, "
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

    public static JournalEntry create(long id, String title, String details,
        @NonNull String address, String imagePath, String dayOfWeekOfEntry, int dayDateNumber,
        String creationDate) {
        return new AutoParcel_JournalEntry(id, title, details, address, imagePath, dayOfWeekOfEntry,
            dayDateNumber, creationDate);
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

    public abstract long id();

    public abstract String title();

    public abstract String details();

    @Nullable
    public abstract String address();

    @Nullable
    public abstract String imagePath();

    @Nullable
    public abstract String dayOfWeek();

    @Nullable
    public abstract int dayDateNumber();

    @Nullable
    public abstract String creationDate();

    public int deleteInto(BriteDatabase db) {
        return db.delete(TABLE, JournalEntry.ID + "=?", String.valueOf(id()));
    }

    public void updateInto(BriteDatabase db) {
        db.update(TABLE, toContentValues(), ID + "=?", String.valueOf(id()));
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

        public Builder address(@Nullable String address) {
            values.put(ADDRESS, address);
            return this;
        }

        public Builder imagePath(@Nullable String imagePath) {
            values.put(IMAGE_PATH, imagePath);
            return this;
        }

        public Builder dayOfWeekOfEntry(@Nullable String dayOfWeekOfEntry) {
            values.put(DAY_OF_WEEK, dayOfWeekOfEntry);
            return this;
        }

        public Builder dayDateNumber(long dayDateNumber) {
            values.put(DAY_DATE_NUMBER, dayDateNumber);
            return this;
        }

        public Builder creationDate(@Nullable String creationDate) {
            values.put(CREATION_DATE, creationDate);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
