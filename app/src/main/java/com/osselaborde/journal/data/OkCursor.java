package com.osselaborde.journal.data;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Helper class for cursors for the results we get from the database.
 */
public final class OkCursor extends CursorWrapper implements Cursor {

  public OkCursor(Cursor cursor) {
    super(cursor);
  }

  public byte[] getBlob(String column) {
    return getBlob(getColumnIndexOrThrow(column));
  }

  public byte[] getBlob(String column, byte[] defaultVal) {
    if (isNull(column)) return defaultVal;
    return getBlob(column);
  }

  public boolean getBoolean(String column) {
    int i = getInt(getColumnIndexOrThrow(column));
    if ((i < 0) || (i > 1)) {
      throw new IllegalStateException("column " + column + " out of range [0,1]: " + i);
    }
    return i == 1;
  }

  public boolean getBoolean(String column, boolean defaultVal) {
    if (isNull(column)) return defaultVal;
    return getBoolean(column);
  }

  public double getDouble(String column) {
    return getDouble(getColumnIndexOrThrow(column));
  }

  public double getDouble(String column, double defaultVal) {
    if (isNull(column)) return defaultVal;
    return getDouble(column);
  }

  public <T extends Enum<T>> T getEnum(String column, Class<T> clazz) {
    return Enum.valueOf(clazz, getString(column));
  }

  public <T extends Enum<T>> T getEnum(String column, Class<T> clazz, T defaultVal) {
    if (isNull(column)) return defaultVal;
    return getEnum(column, clazz);
  }

  public float getFloat(String column) {
    return getFloat(getColumnIndexOrThrow(column));
  }

  public float getFloat(String column, float defaultVal) {
    if (isNull(column)) return defaultVal;
    return getFloat(column);
  }

  public int getInt(String column) {
    return getInt(getColumnIndexOrThrow(column));
  }

  public int getInt(String column, int paramInt) {
    if (isNull(column)) return paramInt;
    return getInt(column);
  }

  public long getLong(String column) {
    return getLong(getColumnIndexOrThrow(column));
  }

  public long getLong(String column, long defaultVal) {
    if (isNull(column)) return defaultVal;
    return getLong(column);
  }

  public String getString(String column) {
    return getString(getColumnIndexOrThrow(column));
  }

  public String getString(String column, String defaultVal) {
    if (isNull(column)) return defaultVal;
    return getString(column);
  }

  public boolean isNull(String column) {
    return (getColumnIndex(column) == -1) || (isNull(getColumnIndex(column)));
  }

  public void close() {
    if (getWrappedCursor() != null) super.close();
  }

  public int getCount() {
    if (getWrappedCursor() == null) return 0;
    return super.getCount();
  }

  public boolean moveToFirst() {
    return (getWrappedCursor() != null) && (super.moveToFirst());
  }

  public int getColumnCount() {
    if (getWrappedCursor() == null) return 0;
    return super.getColumnCount();
  }

  public boolean moveToLast() {
    return (getWrappedCursor() != null) && (super.moveToLast());
  }

  public boolean move(int offset) {
    return (getWrappedCursor() != null) && (super.move(offset));
  }

  public boolean moveToPosition(int position) {
    return (getWrappedCursor() != null) && (super.moveToPosition(position));
  }

  public boolean moveToNext() {
    return (getWrappedCursor() != null) && (super.moveToNext());
  }

  public boolean moveToPrevious() {
    return (getWrappedCursor() != null) && (super.moveToPrevious());
  }
}
