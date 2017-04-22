package com.osselaborde.journal.data;

import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import rx.Observable;

/**
 * Contains the model logic for entries.
 */
public class EntriesManager {

    private final BriteDatabase db;

    public EntriesManager(BriteDatabase db) {
        this.db = db;
    }

    public long saveEntryInDb(final JournalEntry entry) {
        return entry.insertInto(db);
    }

    public Observable<List<JournalEntry>> getEntries() {
        return db.createQuery(JournalEntry.TABLE, JournalEntry.GET_ENTRIES)
            .mapToList(JournalEntry.MAPPER);
    }
}
