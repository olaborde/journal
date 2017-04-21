package com.osselaborde.journal;

import com.osselaborde.journal.data.JournalEntry;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import rx.Observable;

public class EntriesManager {

    private final BriteDatabase db;

    public EntriesManager(BriteDatabase db) {
        this.db = db;
    }

    long saveEntryInDb(final JournalEntry entry) {
        return entry.insertInto(db);
    }

    Observable<List<JournalEntry>> getEntries() {
        return db.createQuery(JournalEntry.TABLE, JournalEntry.GET_ENTRIES)
            .mapToList(JournalEntry.MAPPER);
    }
}
