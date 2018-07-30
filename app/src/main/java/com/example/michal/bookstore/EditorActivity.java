package com.example.michal.bookstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michal.bookstore.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_BOOK_LOADER = 0;
    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;
    private Uri mCurrentBookUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        if (mCurrentBookUri == null){
            setTitle(R.string.editor_activity_title_new_book);
        }else {
            setTitle(R.string.editor_activity_title_edit_book);
            getSupportLoaderManager().initLoader(EDIT_BOOK_LOADER,null,this);
        }

        mProductNameEditText = findViewById(R.id.value_product_name_editor);
        mPriceEditText = findViewById(R.id.value_price_editor);
        mQuantityEditText = findViewById(R.id.value_quantity_editor);
        mSupplierNameEditText = findViewById(R.id.value_supplier_name_editor);
        mSupplierPhoneEditText = findViewById(R.id.value_supplier_phone_editor);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };

        return new CursorLoader(this,
                mCurrentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()){
            String productName = data.getString(data.getColumnIndexOrThrow(BookEntry.COLUMN_PRODUCT_NAME));
            int price = data.getInt(data.getColumnIndexOrThrow(BookEntry.COLUMN_PRICE));
            int quantity = data.getInt(data.getColumnIndexOrThrow(BookEntry.COLUMN_QUANTITY));
            String supplierName = data.getString(data.getColumnIndexOrThrow(BookEntry.COLUMN_SUPPLIER_NAME));
            String supplierPhone = data.getString(data.getColumnIndexOrThrow(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER));

            mProductNameEditText.setText(productName);
            mPriceEditText.setText(String.valueOf(price));
            mQuantityEditText.setText(String.valueOf(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mProductNameEditText.setText("");
        mPriceEditText.setText(String.valueOf(0));
        mQuantityEditText.setText(String.valueOf(0));
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        switch (selectedItem){
            case R.id.action_save:
                saveBook();
                finish();
                break;
            case R.id.action_delete:
                deleteBook();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBook(){
        String productNameString = mProductNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(productNameString)){
            Toast.makeText(this, R.string.editor_activity_required_fields_missing, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, productNameString);

        // If the price is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int price = 0;
        if (!TextUtils.isEmpty(priceString)){
            // check if the value is not negative
            if (Integer.parseInt(priceString) > 0){
                price = Integer.parseInt(priceString);
            }else {
                Toast.makeText(this, R.string.editor_activity_no_negative_values,Toast.LENGTH_SHORT).show();
                return;
            }

        }
        values.put(BookEntry.COLUMN_PRICE, price);

        // If the quantity is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)){
            // check if the value is not negative
            if (Integer.parseInt(quantityString) > 0){
                quantity = Integer.parseInt(quantityString);
            }
            else {
                Toast.makeText(this, R.string.editor_activity_no_negative_values,Toast.LENGTH_SHORT).show();
                return;
            }

        }
        values.put(BookEntry.COLUMN_QUANTITY, quantity);

        values.put(BookEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneString);

        //check if the user inputs new book or updates an existing one
        if (mCurrentBookUri == null){
            //insert new book
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
            if (newUri == null){
                Toast.makeText(this, R.string.editor_activity_insert_failed, Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, R.string.editor_activity_insert_successful, Toast.LENGTH_SHORT).show();
            }
        }else {
            //update existing book
            int rowsUpdated = getContentResolver().update(mCurrentBookUri, values,null,null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsUpdated == 0){
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, R.string.editor_activity_update_failed, Toast.LENGTH_SHORT).show();
            }else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, R.string.editor_activity_update_successful, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteBook(){
        //TODO: fill in this method
    }
}
