package com.osselaborde.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.osselaborde.journal.data.EntriesManager;
import com.osselaborde.journal.ui.recyclerview.ClickItemTouchListener;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Main screen that displays the list of journal entries.
 */
public class MainActivity extends AppCompatActivity {

    private static final int ADD_ENTRY_REQUEST_CODE = 2;

    @BindView(R.id.toolbar_bottom) Toolbar bottomToolbar;
    @BindView(R.id.entriesRv) RecyclerView entriesRv;
    @BindView(R.id.add) ImageView addView;

    @Inject EntriesManager entriesManager;

    CompositeSubscription compositeSubscription = new CompositeSubscription();
    private EntryAdapter entryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        JournalApplication.getAppComponent().inject(this);

        bottomToolbar.inflateMenu(R.menu.bottom_menu);
        bottomToolbar.setTitle(null);
        bottomToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_more:

                    return true;
                case R.id.menu_gallery:

                    return true;
                case R.id.menu_add:

                    return true;
                case R.id.menu_calendar:

                    return true;
            }
            return false;
        });

        entriesRv.setLayoutManager(new LinearLayoutManager(this));
        entryAdapter = new EntryAdapter();
        entriesRv.setAdapter(entryAdapter);
        fetchEntries();

        entriesRv.addOnItemTouchListener(new ClickItemTouchListener(entriesRv) {
            @Override
            protected boolean performItemClick(RecyclerView parent, View view, int position,
                long id) {
                Intent intent = new Intent(MainActivity.this, EntryActivity.class);
                intent.putExtra(EntryActivity.ENTRY_EXTRA,
                    entryAdapter.getItemByPosition(position));
                startActivity(intent);
                return true;
            }

            @Override
            public boolean performItemLongClick(RecyclerView parent, View view, int position,
                long id) {
                //NO-OP
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                //NO-OP
            }
        });
    }

    /**
     * Fetches the saved entries.
     */
    private void fetchEntries() {
        compositeSubscription.add(entriesManager.getEntries()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(entryAdapter));
    }

    @Override
    protected void onDestroy() {
        compositeSubscription.clear();
        super.onDestroy();
    }

    @OnClick({ R.id.add, R.id.image })
    void onAdd() {
        startActivityForResult(new Intent(this, EntryActivity.class), ADD_ENTRY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fetchEntries();
    }
}
