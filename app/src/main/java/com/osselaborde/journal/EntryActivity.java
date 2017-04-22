package com.osselaborde.journal;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.osselaborde.journal.data.EntriesManager;
import com.osselaborde.journal.data.JournalEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;

/**
 * Add entry screen.
 */
public class EntryActivity extends AppCompatActivity
    implements DatePickerFragment.DateSetListener {

    private static final String TAG = "EntryActivity";
    private static final int RESULT_LOAD_IMAGE = 1;
    public static final String ENTRY_EXTRA = "entry_extra";

    @BindView(R.id.title_input) TextInputEditText titleInput;
    @BindView(R.id.details_input) TextInputEditText detailsInput;
    @BindView(R.id.address_input) TextInputEditText addressInput;
    @BindView(R.id.entry_button) Button entryButton;
    @BindView(R.id.journal_image) ImageView journalImage;
    @BindView(R.id.date) TextView dateTv;
    @Inject EntriesManager entriesManager;
    private String journalImagePath;
    private String dayOfWeekOfEntry;
    private int dayDateNumber;
    private String entryDateStr;
    private Uri uriForFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_entry);
        ButterKnife.bind(this);
        JournalApplication.getAppComponent().inject(this);

        if(getIntent().hasExtra(ENTRY_EXTRA)) {
            display(getIntent().getParcelableExtra(ENTRY_EXTRA));
        }
    }

    private void display(JournalEntry entry) {
        titleInput.setText(entry.title());
        detailsInput.setText(entry.details());
        addressInput.setText(entry.address());
    }

    @OnClick(R.id.entry_button)
    void onEntrySave() {
        if (TextUtils.isEmpty(titleInput.getText()) || TextUtils.isEmpty(detailsInput.getText())) {
            Snackbar.make(entryButton, R.string.fill_required_entry, Snackbar.LENGTH_LONG).show();
            return;
        }
        final long entryId = entriesManager.saveEntryInDb(
            JournalEntry.create(-1, titleInput.getText().toString(),
                detailsInput.getText().toString(), addressInput.getText().toString(),
                journalImagePath, dayOfWeekOfEntry, dayDateNumber, entryDateStr));
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.add_image_button)
    void onAddImage() {
        final File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageName = "img" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageName, ".jpg", externalFilesDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        uriForFile = FileProvider.getUriForFile(this, "com.osselaborde.journal", imageFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, RESULT_LOAD_IMAGE);
        }
    }

    @OnClick(R.id.date)
    public void onDatePick() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        newFragment.setDateSetListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (uriForFile != null) {
                final Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriForFile);
                    journalImagePath = saveToInternalStorage(bitmap);
                    journalImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String imageName = "image_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        File imagePath = new File(directory, imageName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/"+ imageName;
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        dayDateNumber = day;
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy/MM/dd");
            entryDateStr = year + "/" + month + "/" + day;
            dateTv.setText(entryDateStr);
            Date date = inFormat.parse(entryDateStr);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            dayOfWeekOfEntry = outFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
