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
        TextView priceTvLabel = view.findViewById(R.id.label_price);
        TextView quantityTvLabel = view.findViewById(R.id.label_quantity);
        TextView priceTvValue = view.findViewById(R.id.value_price);
        TextView quantityTvValue = view.findViewById(R.id.value_quantity);

        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);

        String productName = cursor.getString(nameColumnIndex);
        String price =cursor.getString(priceColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);

        productNameTv.setText(productName);
        priceTvLabel.setText(R.string.text_price);
        priceTvValue.setText(price);

        quantityTvLabel.setText(R.string.text_quantity);
        if (Integer.parseInt(quantity) != 0){
            quantityTvValue.setText(quantity);
        }else {
            quantityTvValue.setText("0");
        }



    }
}
