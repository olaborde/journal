package com.osselaborde.journal;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.osselaborde.journal.data.JournalEntry;
import com.osselaborde.journal.util.ImageHelper;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

/**
 * Adapter to display entries data on a list.
 */
public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder>
    implements Action1<List<JournalEntry>> {

    List<JournalEntry> items = new ArrayList<>();

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

    @Override
    public void call(List<JournalEntry> journalEntries) {
        this.items = journalEntries;
        notifyDataSetChanged();
    }

    public JournalEntry getItemByPosition(int position) {
        return items.get(position);
    }

    /**
     * View holder for each entry item.
     */
    static class EntryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView titleTv;
        @BindView(R.id.details) TextView detailsTv;
        @BindView(R.id.address) TextView addressTv;
        @BindView(R.id.item_image) ImageView entryImageView;
        @BindView(R.id.day) TextView dayTv;
        @BindView(R.id.date_number) TextView dateNumberTv;

        public EntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public static int getItemLayoutId() {
            return R.layout.entry_item_layout;
        }

        void bind(JournalEntry entry) {
            titleTv.setText(entry.title());
            detailsTv.setText(entry.details());
            addressTv.setText(entry.address());
            dayTv.setText(entry.dayOfWeek());
            if (entry.dayDateNumber() > 0) {
                dateNumberTv.setText(String.valueOf(entry.dayDateNumber()));
            }
            final String imagePath = entry.imagePath();
            if (!TextUtils.isEmpty(imagePath)) {
                ImageHelper.loadImageFromStorage(imagePath, entryImageView);
                entryImageView.setVisibility(View.VISIBLE);
            } else {
                entryImageView.setVisibility(View.GONE);
            }
        }
    }
}