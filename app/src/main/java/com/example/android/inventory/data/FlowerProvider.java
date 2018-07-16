package com.example.android.inventory.data;
import com.example.android.inventory.data.flowerContract.flowerEntry;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


/**
 * Created by alslam on 30/06/2018.
 */

public class FlowerProvider extends ContentProvider {
    private static final int FLOWERS = 100;
    private static final int FLOWER_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(flowerContract.CONTENT_AUTHORITY, flowerContract.PATH_FLOWERS, FLOWERS);
        sUriMatcher.addURI(flowerContract.CONTENT_AUTHORITY, flowerContract.PATH_FLOWERS + "/#", FLOWER_ID);

    }


    public static final String LOG_TAG = FlowerProvider.class.getSimpleName();
    private flowerDbHelper fDbHelper;
    @Override
    public boolean onCreate() {
        fDbHelper=new flowerDbHelper(getContext());
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = fDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case FLOWERS:
                cursor=database.query(flowerContract.flowerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                // TODO: Perform database query on pets table
                break;
            case FLOWER_ID:
                selection = flowerEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(flowerContract.flowerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {


        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FLOWERS:
                return insertFlower(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
        
    }

    private Uri insertFlower(Uri uri, ContentValues  values) {
        String name = values.getAsString(flowerEntry.COLUMN_FLOWER_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Flower requires a name");
        }
        Integer price = values.getAsInteger(flowerEntry.COLUMN_FLOWER_PRICE);
        if (price == null && price < 0) {
            throw new IllegalArgumentException("Flower requires valid price");
        }
        Integer quantity = values.getAsInteger(flowerEntry.COLUMN_FLOWER_QUANTITY);
        if (quantity == null && quantity < 0) {
            throw new IllegalArgumentException("Flower requires valid quantity");
        }
        String supplier = values.getAsString(flowerEntry.COLUMN_FLOWER_SUPPLIER);
        if (supplier == null) {
            throw new IllegalArgumentException("Flower requires a Supplier name");
        }
        Integer phone = values.getAsInteger(flowerEntry.COLUMN_SUPPLIER_PHONE);
        if (phone == null && phone < 0) {
            throw new IllegalArgumentException("Flower requires valid phone");
        }
        SQLiteDatabase database = fDbHelper.getWritableDatabase();
        long id = database.insert(flowerEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FLOWERS:
                return updateflower(uri, contentValues, selection, selectionArgs);
            case FLOWER_ID:
                selection = flowerEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateflower(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateflower(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(flowerEntry.COLUMN_FLOWER_NAME)) {
            String name = values.getAsString(flowerEntry.COLUMN_FLOWER_NAME);
            if (name == null) {
                throw new IllegalArgumentException("flower requires a name");
            }
        }
        if (values.containsKey(flowerEntry.COLUMN_FLOWER_SUPPLIER)) {
            String supplierName = values.getAsString(flowerEntry.COLUMN_FLOWER_SUPPLIER);
            if (supplierName == null) {
                throw new IllegalArgumentException("flower requires a supplierName");
            }
        }

        if (values.containsKey(flowerEntry.COLUMN_FLOWER_PRICE)) {
            Integer price = values.getAsInteger(flowerEntry.COLUMN_FLOWER_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("flower requires valid price");
            }
        }
        if (values.containsKey(flowerEntry.COLUMN_FLOWER_QUANTITY)) {
            Integer quantity = values.getAsInteger(flowerEntry.COLUMN_FLOWER_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("flower requires valid price");
            }
        }
        if (values.containsKey(flowerEntry.COLUMN_SUPPLIER_PHONE)) {
            Integer phone = values.getAsInteger(flowerEntry.COLUMN_SUPPLIER_PHONE);
            if (phone != null && phone < 0) {
                throw new IllegalArgumentException("flower requires valid phone");
            }
        }

        if (values.size() == 0) {
            return 0;

        }
        SQLiteDatabase database = fDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(flowerEntry.TABLE_NAME, values, selection, selectionArgs);

                                      if (rowsUpdated != 0) {
                      getContext().getContentResolver().notifyChange(uri, null);
                  }

                     // Return the number of rows updated
                              return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = fDbHelper.getWritableDatabase();
               int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FLOWERS:
                rowsDeleted = database.delete(flowerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FLOWER_ID:
                // Delete a single row given by the ID in the URI
                selection = flowerEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(flowerEntry.TABLE_NAME, selection, selectionArgs);
                break;
                default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
                     getContext().getContentResolver().notifyChange(uri, null);
                 }
                             return rowsDeleted;
    }
    @Override
    public String getType(Uri uri)
    {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FLOWERS:
                return flowerEntry.CONTENT_LIST_TYPE;
            case FLOWER_ID:
                return flowerEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }    }
}
