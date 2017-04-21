package com.osselaborde.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_ENTRY_REQUEST_CODE = 2;

    @BindView(R.id.toolbar_bottom) Toolbar bottomToolbar;
    @BindView(R.id.entriesRv) RecyclerView entriesRv;
    @BindView(R.id.add) ImageView addView;

    @Inject EntriesManager entriesManager;

    CompositeSubscription compositeSubscription = new CompositeSubscription();

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

        EntryAdapter entryAdapter = new EntryAdapter();
        compositeSubscription.add(entriesManager.getEntries()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(entryAdapter));
        entriesRv.setAdapter(entryAdapter);
    }

    @OnClick(R.id.add)
    void onAdd() {
        startActivityForResult(new Intent(this, AddEntryActivity.class), ADD_ENTRY_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        compositeSubscription.clear();
        super.onDestroy();
    }
}
