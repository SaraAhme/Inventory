package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventory.data.flowerContract;
import com.example.android.inventory.data.flowerContract.flowerEntry;
import com.example.android.inventory.data.flowerCursorAdapter;
import com.example.android.inventory.data.flowerDbHelper;

public class CatalogActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PET_LOADER = 0;
    flowerCursorAdapter fCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView flowerListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        flowerListView.setEmptyView(emptyView);
        fCursorAdapter = new flowerCursorAdapter(this, null);
        flowerListView.setAdapter(fCursorAdapter);
        flowerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(flowerEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(PET_LOADER, null, this);

    }
    ContentValues values = new ContentValues();

    private void insertflower() {

        values.put(flowerEntry.COLUMN_FLOWER_NAME, "Tulip");
        values.put(flowerEntry.COLUMN_FLOWER_PRICE, 10);
        values.put(flowerEntry.COLUMN_FLOWER_QUANTITY, 1);
        values.put(flowerEntry.COLUMN_SUPPLIER_PHONE, 01170);
        values.put(flowerEntry.COLUMN_FLOWER_SUPPLIER, "Salwa");
        Uri newUri = getContentResolver().insert(flowerEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_tulip_data:
                insertflower();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllflowers();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllflowers() {
        int rowsDeleted = getContentResolver().delete(flowerEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                flowerEntry._ID,
                flowerEntry.COLUMN_FLOWER_NAME,
                flowerEntry.COLUMN_FLOWER_PRICE,
                flowerEntry.COLUMN_FLOWER_QUANTITY,
                flowerEntry.COLUMN_FLOWER_SUPPLIER,
                flowerEntry.COLUMN_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                flowerEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        fCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        fCursorAdapter.swapCursor(null);
    }

}