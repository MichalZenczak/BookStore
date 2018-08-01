package com.example.michal.bookstore;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView productNameTv = view.findViewById(R.id.product_name);
        TextView priceTvLabel = view.findViewById(R.id.label_price);
        TextView quantityTvLabel = view.findViewById(R.id.label_quantity);
        TextView priceTvValue = view.findViewById(R.id.value_price);
        TextView quantityTvValue = view.findViewById(R.id.value_quantity);
        Button sellButton = view.findViewById(R.id.sellButton);

        int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);

        final String id = cursor.getString(idColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        String price =cursor.getString(priceColumnIndex);
        final String quantity = cursor.getString(quantityColumnIndex);

        productNameTv.setText(productName);
        priceTvLabel.setText(R.string.text_price);
        priceTvValue.setText(price);

        quantityTvLabel.setText(R.string.text_quantity);
        quantityTvValue.setText(quantity);

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogActivity catalogActivity = (CatalogActivity) context;
                catalogActivity.decreaseQuantity(Integer.parseInt(id), Integer.parseInt(quantity));
            }
        });
    }
}
