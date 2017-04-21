package com.osselaborde.journal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

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
    }

    static class EntryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView titleTv;
        @BindView(R.id.details) TextView detailsTv;
        @BindView(R.id.address) TextView addressTv;
        @BindView(R.id.item_image) ImageView entryImageView;

        public static int getItemLayoutId() {
            return R.layout.entry_item_layout;
        }

        public EntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(JournalEntry entry) {
            titleTv.setText(entry.address());
            detailsTv.setText(entry.details());
            addressTv.setText(entry.address());
            final String imagePath = entry.imagePath();
            if (!TextUtils.isEmpty(imagePath)) {
                loadImageFromStorage(imagePath, entryImageView);
            }
        }

        private void loadImageFromStorage(String path, ImageView imageView) {
            try {
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}