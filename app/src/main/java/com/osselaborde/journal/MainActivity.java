package com.osselaborde.journal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osselaborde.journal.model.Entry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.entriesRv)
    RecyclerView entriesRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        entriesRv.setLayoutManager(new LinearLayoutManager(this));

        //temp dummy data
        List<Entry> items = new ArrayList<>();
        items.add(new Entry("TEST"));
        EntryAdapter entryAdapter = new EntryAdapter(items);
        entriesRv.setAdapter(entryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        return super.onOptionsItemSelected(item);
    }

    class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {

        List<Entry> items;

        public EntryAdapter(List<Entry> items) {
            this.items = items;
        }

        @Override
        public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(EntryViewHolder.getItemLayoutId(), parent, false);
            return new EntryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EntryViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    static class EntryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.address)
        TextView addressTv;

        public static int getItemLayoutId() {
            return R.layout.entry_item_layout;
        }

        public EntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Entry entry) {
            addressTv.setText(entry.getAddress());
        }
    }

}
