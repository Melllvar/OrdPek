package se.henkan.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import se.henkan.ordpek.ImageEntry;

/**
 * Created by Henkan on 2014-10-03.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "imageEntryManager";

    // Images entries table name
    private static final String TABLE_IMAGES = "imageEntry";

    // Images Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FILE_PATH = "filePath";
    private static final String KEY_NAME = "name";
    private static final String KEY_FIRST_LETTER = "firstLetter";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FILE_PATH + " TEXT," + KEY_NAME + " TEXT,"
                + KEY_FIRST_LETTER + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

        // Create tables again
        onCreate(db);
    }

    // Add new ImageEntry
    public void addImageEntry(ImageEntry imageEntry){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILE_PATH, imageEntry.get_filePath());
        values.put(KEY_NAME, imageEntry.get_name());
        values.put(KEY_FIRST_LETTER, imageEntry.get_firstLetter());

        db.insert(TABLE_IMAGES, null, values);
        db.close(); // Closing database connection

    }

    // Get ImageEntry
    public ImageEntry getImageEntry(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID, KEY_FILE_PATH,
                        KEY_NAME, KEY_FIRST_LETTER }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ImageEntry imageEntry = new ImageEntry(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return imageEntry
        return imageEntry;
    }

    // Getting All Image entries
    public List<ImageEntry> getAllImageEntries() {
        List<ImageEntry> imageList = new ArrayList<ImageEntry>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ImageEntry imageEntry = new ImageEntry();
                imageEntry.set_id(Integer.parseInt(cursor.getString(0)));
                imageEntry.set_filePath(cursor.getString(1));
                imageEntry.set_name(cursor.getString(2));
                imageEntry.set_firstLetter(cursor.getString(3));
                // Adding imageEntry to list
                imageList.add(imageEntry);
            } while (cursor.moveToNext());
        }

        // return contact list
        return imageList;
    }

    // Get number of ImagesEntry in database
    public int getImagesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_IMAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single ImageEntry
    public int updateImageEntry(ImageEntry imageEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILE_PATH, imageEntry.get_filePath());
        values.put(KEY_NAME, imageEntry.get_name());
        values.put(KEY_FIRST_LETTER, imageEntry.get_firstLetter());

        // updating row
        return db.update(TABLE_IMAGES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(imageEntry.get_id()) });
    }

    // Delete single ImageEntry
    public void deleteImageEntry(ImageEntry imageEntry){
        SQLiteDatabase db = this.getWritableDatabase();
        // ToDo: Delete file form filesystem?
        db.delete(TABLE_IMAGES, KEY_ID + " = ?",
                new String[] { String.valueOf(imageEntry.get_id()) });
        db.close();
    }

    // Return a List of all the ID's
    public List<Integer> getAllIDs(){
        List<Integer> IDs = new ArrayList<Integer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                IDs.add(Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        return IDs;
    }

}
