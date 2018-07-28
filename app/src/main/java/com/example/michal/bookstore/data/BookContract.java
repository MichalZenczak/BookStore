package com.example.michal.bookstore.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Book Store app
 */

public final class BookContract {

    private BookContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.michal.bookstore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "books";


    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single book.
     */
    public final static class BookEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /** Name of database table for books */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book.
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the book.
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product_name";

        /**
         * Price of the book.
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Quantity of the book.
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Is the book in stock or not?
         * Type: INTEGER
         */
        public final static String COLUMN_IN_STOCK = "in_stock";

        /**
         * Name of the supplier.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";

        /**
         * Phone number of the supplier.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        /**
         * Possible values for the in stock column.
         */
        public final static int inStock_NO = 0;
        public final static int inStock_YES = 1;
    }


}
