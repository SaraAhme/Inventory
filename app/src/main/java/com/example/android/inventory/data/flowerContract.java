package com.example.android.inventory.data;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
public final class flowerContract  {
    private flowerContract()  {}


    public static final String CONTENT_AUTHORITY = "com.example.android.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FLOWERS = "flowers";

    public static final class flowerEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLOWERS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLOWERS;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FLOWERS);

        public final static String TABLE_NAME = "flowers";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FLOWER_NAME ="name";
        public final static String COLUMN_FLOWER_PRICE = "price";
        public final static String COLUMN_FLOWER_QUANTITY= "quantity";
        public final static String COLUMN_FLOWER_SUPPLIER = "supplier";
        public final static String COLUMN_SUPPLIER_PHONE = "phone";

        public static Uri buildProductUri(int id) {
                return ContentUris.withAppendedId(CONTENT_URI, id);
            }
        }
    }



