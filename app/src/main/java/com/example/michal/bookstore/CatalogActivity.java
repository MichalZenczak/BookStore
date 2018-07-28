package com.example.michal.bookstore;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.michal.bookstore.data.BookContract.BookEntry;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static String LOG_TAG = CatalogActivity.class.getSimpleName();
    private BookCursorAdapter mBookCursorAdapter;
    private static final int BOOK_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        //TODO: change action on fab click to open new activity. Intent.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView bookListView = findViewById(R.id.content_list);
        TextView emptyTv = findViewById(R.id.empty_text_view);
        emptyTv.setText(R.string.text_empty_text_view);
        bookListView.setEmptyView(emptyTv);

        mBookCursorAdapter = new BookCursorAdapter(this,null);
        bookListView.setAdapter(mBookCursorAdapter);

        //TODO: create onItemClickListener on list items

        getSupportLoaderManager().initLoader(BOOK_LOADER,null,this);
    }

    /**
     * Helper method to insert hardcoded book data into the database.
     */
    private void insertBook(){
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "Fancy Book Title");
        values.put(BookEntry.COLUMN_PRICE, "100");
        values.put(BookEntry.COLUMN_QUANTITY, "8");
        values.put(BookEntry.COLUMN_IN_STOCK, BookEntry.inStock_YES);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, "Joe Doe");
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER,"666-666-666");
        getContentResolver().insert(BookEntry.CONTENT_URI,values);
    }

    private void deleteAllBooks(){
        getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int selectedItemId = item.getItemId();

        switch (selectedItemId){
            case R.id.action_insert_dummy_data:
                insertBook();
                break;
            case R.id.action_delete_all_data:
                deleteAllBooks();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        String[] projection ={
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY
        };
        //This Loader will execute the Content Provider's query method on a background thread
        return new CursorLoader(this, BookEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mBookCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mBookCursorAdapter.swapCursor(null);
    }
}
