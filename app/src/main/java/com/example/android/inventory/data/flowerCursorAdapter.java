package com.example.android.inventory.data;

/**
 * Created by alslam on 01/07/2018.
 */
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.inventory.CatalogActivity;
import com.example.android.inventory.R;

import static android.content.ContentValues.TAG;

public class flowerCursorAdapter extends CursorAdapter {

    public static final String LOG_TAG = flowerCursorAdapter.class.getSimpleName();
    private Context mContexts;

    public flowerCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContexts = context;
      //  super(context, c, 0 /* flags */);
    }
    public static class ProductViewHolder {
        public final TextView mProductNameTextView;
        public final TextView mProductPriceTextView;
        public final TextView mProductQuantityTextView;
        public final Button mSaleButtons;

        public ProductViewHolder(View view) {
            mProductNameTextView = (TextView) view.findViewById(R.id.name);
            mProductPriceTextView = (TextView) view.findViewById(R.id.price);
            mProductQuantityTextView = (TextView) view.findViewById(R.id.quantity);
            mSaleButtons = (Button) view.findViewById(R.id.sales);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        view.setTag(productViewHolder);
        return view;
    }
    String quantity;
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        ProductViewHolder productViewHolder = (ProductViewHolder) view.getTag();

        final String name = cursor.getString(cursor.getColumnIndex(flowerContract.flowerEntry.COLUMN_FLOWER_NAME));
        final double priceVal = cursor.getDouble(cursor.getColumnIndex(flowerContract.flowerEntry.COLUMN_FLOWER_PRICE));
        final int quantity = cursor.getInt(cursor.getColumnIndex(flowerContract.flowerEntry.COLUMN_FLOWER_QUANTITY));
        final int id = cursor.getInt(cursor.getColumnIndex(flowerContract.flowerEntry._ID));
        final Cursor cursorVal = cursor;
        String priceStatement = "Price $" + priceVal;
        String quantityStatement = " Quantity " + quantity;
        final Uri uri = flowerContract.flowerEntry.buildProductUri(id);
        productViewHolder.mProductNameTextView.setText(name);
        productViewHolder.mProductPriceTextView.setText(priceStatement);
        productViewHolder.mProductQuantityTextView.setText(quantityStatement);
        productViewHolder.mSaleButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Quantity " + quantity);
                ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues values = new ContentValues();
                if (quantity > 0) {
                    int quantity = cursorVal.getInt(cursorVal.getColumnIndex(flowerContract.flowerEntry.COLUMN_FLOWER_QUANTITY));
                  //  int sold = cursorVal.getInt(cursorVal.getColumnIndex(flowerContract.flowerEntry.COLUMN_SOLD));
                    int quantityValue = quantity;
                  //  int soldValue = sold;
                    values.put(flowerContract.flowerEntry.COLUMN_FLOWER_QUANTITY, --quantityValue);
                    resolver.update(
                            uri,
                            values,
                            null,
                            null);
                    mContexts.getContentResolver().notifyChange(uri, null);
                }
            }
        });
}}