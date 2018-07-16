package com.example.android.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.inventory.data.flowerContract.flowerEntry;
import com.example.android.inventory.data.flowerDbHelper;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>   {
    private static final int EXISTING_PET_LOADER = 0;
    private EditText fNameEditText;
    private EditText fQuantityEditText;
    private EditText fPriceEditText;
    private EditText fSupplierEditText;
    private EditText sphoneEditText;
    private Uri CurrentflowerUri;
    private boolean flowerHasChanged = false;
    private Button increase;
    private Button decrease;
    private Button sales;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            flowerHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        CurrentflowerUri = intent.getData();
        if (CurrentflowerUri == null) {
            // This is a new pet, so change the app bar to say "Add a Pet"
            setTitle(getString(R.string.editor_activity_title_new_pet));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_flower));

            getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        }


        fNameEditText = (EditText) findViewById(R.id.edit_flower_name);
        fQuantityEditText = (EditText) findViewById(R.id.edit_flower_quantity);
        fPriceEditText = (EditText) findViewById(R.id.edit_flower_price);
        fSupplierEditText = (EditText) findViewById(R.id.edit_flower_supplier);
        sphoneEditText= (EditText) findViewById(R.id.edit_supplier_phone);
        increase = (Button) findViewById(R.id.button);
        decrease = (Button) findViewById(R.id.button2);
        sales = (Button) findViewById(R.id.button3);
        fNameEditText.setOnTouchListener(mTouchListener);
        fQuantityEditText.setOnTouchListener(mTouchListener);
        fPriceEditText.setOnTouchListener(mTouchListener);
        fSupplierEditText.setOnTouchListener(mTouchListener);
        sphoneEditText.setOnTouchListener(mTouchListener);
    }
    ContentValues values = new ContentValues();
    public void icrease(View v){
        String quantity= fQuantityEditText.getText().toString();
        int icrease=Integer.parseInt(quantity);
        icrease++;

        fQuantityEditText.setText(String.valueOf(icrease));
    }
    public void decrease(View v){
        String quantity= fQuantityEditText.getText().toString();
        int decrease=Integer.parseInt(quantity);
        if (decrease>=1)
        {
            decrease--;
        }
        fQuantityEditText.setText(String.valueOf(decrease));
    }
    public void contact(View v){

        // Use format with "tel:" and phone number to create phoneNumber.
        String phoneNumber = String.format("tel: %s", sphoneEditText.getText().toString());
        // Create the intent.
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        // Set the data for the intent as the phone number.
        dialIntent.setData(Uri.parse(phoneNumber));
        // If package resolves to an app, send intent.
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            // Log.e(TAG, "Can't resolve app for ACTION_DIAL Intent.");


            Toast  toast = Toast.makeText(getApplicationContext(), "Can't resolve app for ACTION_DIAL Intent.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void saveflower() {
        String nameString="",quantityString="",priceString="",supplierString="",phoneString="";
        if(!fNameEditText.getText().toString().trim().equals("")) {
            nameString = fNameEditText.getText().toString().trim();
        }
        else
        {
            fNameEditText.requestFocus();
            fNameEditText.setError("Insert Titel");
            Toast.makeText(this, "Insert Titel", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!fQuantityEditText.getText().toString().trim().equals("")) {
            quantityString = fQuantityEditText.getText().toString().trim();

        }
        else
        {
            fQuantityEditText.requestFocus();
            fQuantityEditText.setError("Insert quentity");
            Toast.makeText(this, "Insert quentity", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!fPriceEditText.getText().toString().trim().equals("")) {
            priceString = fPriceEditText.getText().toString().trim();
        }
        else
        {
            fPriceEditText.requestFocus();
            fPriceEditText.setError("Insert Price");
            Toast.makeText(this, "Insert Price", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!fSupplierEditText.getText().toString().trim().equals("")) {
            supplierString = fSupplierEditText.getText().toString().trim();
        }
        else
        {
            fSupplierEditText.requestFocus();
            fSupplierEditText.setError("Insert supplier");
            Toast.makeText(this, "Insert supplier", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!sphoneEditText.getText().toString().trim().equals("")) {
            phoneString = sphoneEditText.getText().toString().trim();
        }
        else
        {
            sphoneEditText.requestFocus();
            sphoneEditText.setError("Insert phone");
            Toast.makeText(this, "Insert phone", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();

        int price;
        try {
            price = Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            price = 0;
            Toast.makeText(this, "Invalid Price", Toast.LENGTH_LONG).show();
            return;
        }
        int phone;
        try {
           phone = Integer.parseInt(phoneString);
        } catch (NumberFormatException e) {
            phone = 0;
            Toast.makeText(this, "Invalid phone", Toast.LENGTH_LONG).show();
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            quantity = 0;
            Toast.makeText(this, "Invalid quantity", Toast.LENGTH_LONG).show();
            return;
        }
        values.put(flowerEntry.COLUMN_FLOWER_NAME, nameString);
        values.put(flowerEntry.COLUMN_FLOWER_QUANTITY, quantity);
        values.put(flowerEntry.COLUMN_FLOWER_PRICE, price);
        values.put(flowerEntry.COLUMN_SUPPLIER_PHONE, phone);
        values.put(flowerEntry.COLUMN_FLOWER_SUPPLIER, supplierString);
        if (CurrentflowerUri == null) {
            Uri   newUri = getContentResolver().insert(flowerEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_flower_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_flower_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(CurrentflowerUri, values, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_flower_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_flower_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (CurrentflowerUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveflower();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllflowers();
                return true;
            case android.R.id.home:
                if (!flowerHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteAllflowers() {
        int rowsDeleted = getContentResolver().delete(flowerEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from flower database");
    }
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!flowerHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showDeleteConfirmationDialog() {
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
             builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {
                                deletePet();
                          }
     });
              builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
             if (dialog != null) {
              dialog.dismiss();
                }
              }
              });

         AlertDialog alertDialog = builder.create();
            alertDialog.show();
         }
    private void deletePet() {
                if (CurrentflowerUri != null) {

              int rowsDeleted = getContentResolver().delete(CurrentflowerUri, null, null);

                           if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_flower_failed),
                Toast.LENGTH_SHORT).show();
         } else {
                Toast.makeText(this, getString(R.string.editor_delete_flower_successful),
                Toast.LENGTH_SHORT).show();
        }
           }

             finish();
          }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                flowerEntry._ID,
                flowerEntry.COLUMN_FLOWER_NAME,
                flowerEntry.COLUMN_FLOWER_PRICE,
                flowerEntry.COLUMN_FLOWER_QUANTITY,
                flowerEntry.COLUMN_FLOWER_SUPPLIER,
                flowerEntry.COLUMN_SUPPLIER_PHONE};


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                CurrentflowerUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);           }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
    }
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(flowerEntry.COLUMN_FLOWER_NAME);
            int priceColumnIndex = cursor.getColumnIndex(flowerEntry.COLUMN_FLOWER_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(flowerEntry.COLUMN_FLOWER_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(flowerEntry.COLUMN_FLOWER_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(flowerEntry.COLUMN_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int phone = cursor.getInt(phoneColumnIndex);

            // Update the views on the screen with the values from the database
            fNameEditText.setText(name);
            fSupplierEditText.setText(supplier);
            fQuantityEditText.setText(Integer.toString(quantity));
            fPriceEditText.setText(Integer.toString(price));
            sphoneEditText.setText(Integer.toString(phone));


            }

    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        fNameEditText.setText("");
        fSupplierEditText.setText("");
        fQuantityEditText.setText("");
        fPriceEditText.setText("");
        sphoneEditText.setText("");

    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
