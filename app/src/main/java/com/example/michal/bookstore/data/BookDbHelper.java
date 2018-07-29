package com.example.michal.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.michal.bookstore.data.BookContract.BookEntry;


/**
 * Database helper for Book Store app. Manages database creation and version management.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    private final static String LOG_TAG = BookDbHelper.class.getSimpleName();

    /** Name of the database file */
    private final static String DATABASE_NAME = "bookstore.db";

    /**
     * Database version
     */
    private final static int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BookDbHelper}.
     * @param context of the app
     */
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_PRICE + " INTEGER DEFAULT 0, "
                + BookEntry.COLUMN_QUANTITY + " INTEGER DEFAULT 0, "
                + BookEntry.COLUMN_SUPPLIER_NAME + " TEXT, "
                + BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT);";

        Log.i(LOG_TAG, SQL_CREATE_BOOKS_TABLE);
        db.execSQL(SQL_CREATE_BOOKS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}