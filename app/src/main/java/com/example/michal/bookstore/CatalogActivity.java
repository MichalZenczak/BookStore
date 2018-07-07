package com.example.michal.bookstore;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.michal.bookstore.data.BookContract.BookEntry;
import com.example.michal.bookstore.data.BookDbHelper;

public class CatalogActivity extends AppCompatActivity {

    private final static String LOG_TAG = CatalogActivity.class.getSimpleName();
    private BookDbHelper mBookDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mBookDbHelper = new BookDbHelper(this);
    }

    /**
     * Helper method to insert hardcoded book data into the database.
     */
    private void insertBook(){
        SQLiteDatabase db = mBookDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "Fancy Book Title");
        values.put(BookEntry.COLUMN_PRICE, "100");
        values.put(BookEntry.COLUMN_QUANTITY, "8");
        values.put(BookEntry.COLUMN_IN_STOCK, BookEntry.inStock_YES);
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

        Log.i(LOG_TAG, values.toString());

        Toast toast = Toast.makeText(this, "Dummy Data inserted with ID: " + newRowId, Toast.LENGTH_SHORT);
        toast.show();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_insert_dummy_data) {
            insertBook();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
