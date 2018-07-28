package com.example.michal.bookstore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.michal.bookstore.data.BookContract.BookEntry;

public class BookProvider extends ContentProvider{

    public static final String LOG_TAG = ContentProvider.class.getSimpleName();

    private BookDbHelper mBookDBHelper;
    private static final int INSERTION_FAILED = -1;
    private static final int BOOKS = 100;
    private static final int BOOK_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH_BOOKS + "/#", BOOK_ID);
    }

    @Override
    public boolean onCreate() {
        mBookDBHelper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = mBookDBHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);

        switch (match){
            case BOOKS:
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null,null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri: " + uri);
        }

        //TODO: set notification change on URI
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = sUriMatcher.match(uri);
        switch (match){
            case BOOK_ID:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported fo uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        //TODO: insert code for user input verification

        int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update not supported for uri: " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values){

        //TODO: insert code for user input verification

        SQLiteDatabase database = mBookDBHelper.getWritableDatabase();
        long id = database.insert(BookEntry.TABLE_NAME, null, values);

        if (id == INSERTION_FAILED){
            Log.e(LOG_TAG, "Failed to insert row for uri: " + uri);
            return null;
        }

        //TODO: set notification change on URI
        return ContentUris.withAppendedId(uri, id);
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        //TODO: insert code for user input verification

        SQLiteDatabase database = mBookDBHelper.getWritableDatabase();
        int updatedRowId = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);

        //TODO: set notification change on URI

        return updatedRowId;
    }
}
