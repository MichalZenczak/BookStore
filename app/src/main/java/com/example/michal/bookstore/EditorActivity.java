package com.example.michal.bookstore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        mProductNameEditText = findViewById(R.id.value_product_name_editor);
        mPriceEditText = findViewById(R.id.value_price_editor);
        mQuantityEditText = findViewById(R.id.value_quantity_editor);
        mSupplierNameEditText = findViewById(R.id.value_supplier_name_editor);
        mSupplierPhoneEditText = findViewById(R.id.value_supplier_phone_editor);
        Button decreaseButton = findViewById(R.id.editor_decrease_button);
        Button increaseButton = findViewById(R.id.editor_increase_button);
        final Button callSupplierButton = findViewById(R.id.call_supplier_button);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        if (mCurrentBookUri == null){
            setTitle(R.string.editor_activity_title_new_book);
            mQuantityEditText.setText("0");
            invalidateOptionsMenu();
        }else {
            setTitle(R.string.editor_activity_title_edit_book);
            getSupportLoaderManager().initLoader(EDIT_BOOK_LOADER,null,this);
        }

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        callSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();
                Intent callSupplierIntent = new Intent(Intent.ACTION_DIAL);
                callSupplierIntent.setData(Uri.parse("tel:" + supplierPhoneString));

                if (callSupplierIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(callSupplierIntent);
                }
            }
        });

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

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentBookUri == null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
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
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBook(){
        String productNameString = mProductNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        boolean hasEmptyFields = TextUtils.isEmpty(productNameString) ||
                TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) ||
                TextUtils.isEmpty(supplierNameString) ||
                TextUtils.isEmpty(supplierPhoneString);

        if (hasEmptyFields){
            Toast.makeText(this, R.string.editor_activity_missing_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, productNameString);

        // If the price is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int price = 0;
        if (!TextUtils.isEmpty(priceString)){
            price = Integer.parseInt(priceString);
        }
        values.put(BookEntry.COLUMN_PRICE, price);

        // If the quantity is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)){
            quantity = Integer.parseInt(quantityString);
        }
        values.put(BookEntry.COLUMN_QUANTITY, quantity);

        values.put(BookEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneString);

        //check if the user inputs new book or updates an existing one
        if (mCurrentBookUri == null){
            //insert new book
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
            if (newUri != null){
               Toast.makeText(this, R.string.editor_activity_successful_save, Toast.LENGTH_SHORT).show();
            }
        }else {
            //update existing book
            int rowsUpdated = getContentResolver().update(mCurrentBookUri, values,null,null);
            if (rowsUpdated != 0){
                Toast.makeText(this, R.string.editor_activity_successful_save, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteBook(){
        if (mCurrentBookUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri,null,null);
            // check whether the deletion was successful
            if (rowsDeleted == 0){
                //deletion failed
                Toast.makeText(this, R.string.editor_activity_deletion_failed, Toast.LENGTH_SHORT).show();
            }else {
                //deletion was successful
                Toast.makeText(this, R.string.editor_activity_successful_delete, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void decreaseQuantity() {
        int quantity = Integer.parseInt(mQuantityEditText.getText().toString().trim());
        if (quantity != 0) {
            quantity -= 1;
            mQuantityEditText.setText(String.valueOf(quantity));
        }
    }

    private void increaseQuantity(){
        int quantity = Integer.parseInt(mQuantityEditText.getText().toString().trim());
            quantity += 1;
            mQuantityEditText.setText(String.valueOf(quantity));
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.editor_activity_delete_msg);
        builder.setPositiveButton(R.string.delete_dialog_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel_dialog_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
