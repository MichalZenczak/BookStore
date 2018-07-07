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
         * Unique ID number for the pet (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet.
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product name";

        /**
         * Name of the pet.
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Name of the pet.
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Name of the pet.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier name";

        /**
         * Name of the pet.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier phone number";
    }


}
