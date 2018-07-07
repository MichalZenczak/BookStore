package com.example.michal.bookstore.data;

import android.provider.BaseColumns;

/**
 * API Contract for the Book Store app
 */

public final class BookContract {

    private BookContract(){}

    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single book.
     */
    public final static class BookEntry implements BaseColumns {

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
        public final static String COLUMN_PRODUCT_NAME = "product name";

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
        public final static String COLUMN_IN_STOCK = "in stock";

        /**
         * Name of the supplier.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier name";

        /**
         * Phone number of the supplier.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier phone number";

        /**
         * Possible values for the in stock column.
         */
        public final static int inStock_NO = 0;
        public final static int inStock_YES = 1;
    }


}
