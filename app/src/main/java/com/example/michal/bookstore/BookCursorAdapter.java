package com.example.michal.bookstore;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.michal.bookstore.data.BookContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor cursor){
        super(context, cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.content_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView productNameTv = view.findViewById(R.id.product_name);
        TextView priceTv = view.findViewById(R.id.price);
        TextView quantityTv = view.findViewById(R.id.quantity);

        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        String price =cursor.getString(priceColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);

        productNameTv.setText(productName);
        priceTv.setText(R.string.text_price);
        priceTv.append(price);

        quantityTv.setText(R.string.text_quantity);
        if (Integer.parseInt(quantity) != 0){
            quantityTv.append(quantity);
        }else {
            quantityTv.append("0");
        }



    }
}
