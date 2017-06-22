package com.example.laptop.inventoryfinalapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by LAPTOP on 19-06-2017.
 */

public class ProductContract {

    private ProductContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryfinalapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCT = "Product";

    public static final class ProductEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);
        public final static String _ID = BaseColumns._ID;

        public final static String TABLE_NAME = "Product";
        public final static String NAME_OF_PRODUCT = "Name";
        public final static String QUANTITY_OF_PRODUCT = "Quantity";
        public final static String COST_OF_PRODUCT = "Cost";
        public final static String Image = "Image";
    }
}
