package com.osselaborde.journal;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.osselaborde.journal.data.JournalEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;

public class AddEntryActivity extends AppCompatActivity
    implements DatePickerFragment.DateSetListener {

    private static final String TAG = "AddEntryActivity";
    private static final int RESULT_LOAD_IMAGE = 1;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_entry);
        ButterKnife.bind(this);
        JournalApplication.getAppComponent().inject(this);
    }

    @OnClick(R.id.entry_button)
    void onEntrySave() {
        final long entryId = entriesManager.saveEntryInDb(
            JournalEntry.create(-1, titleInput.getText().toString(),
                detailsInput.getText().toString(), addressInput.getText().toString(),
                journalImagePath, dayOfWeekOfEntry, dayDateNumber, entryDateStr));
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.add_image_button)
    void onAddImage() {
        Intent i = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
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
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor =
                getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            final Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
            journalImagePath = saveToInternalStorage(imageBitmap);
            journalImage.setImageBitmap(imageBitmap);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String imageName =
            "image_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mypath = new File(directory, imageName + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
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
        return directory.getAbsolutePath();
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
