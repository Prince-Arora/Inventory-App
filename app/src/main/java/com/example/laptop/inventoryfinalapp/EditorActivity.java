package com.example.laptop.inventoryfinalapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptop.inventoryfinalapp.data.ProductContract;

import java.io.ByteArrayOutputStream;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Uri CurrentProductUri;
    public TextView Quantity_Set;
    public EditText NAME;
    EditText COST;
    Button INCREMENT;
    Button DECREMENT;
    Button SUBMIT;
    private boolean mPetHasChanged = false;
    ImageView mImageView;
    Bitmap imageBitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        NAME = (EditText) findViewById(R.id.name);
        COST = (EditText) findViewById(R.id.cost);//ye kha hai/..? mtlb? ye xml me kha defined hai
        INCREMENT = (Button) findViewById(R.id.increment);
        DECREMENT = (Button) findViewById(R.id.decrement);
        Quantity_Set = (TextView) findViewById(R.id.quantity_set);
        View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPetHasChanged = true;
                return false;
            }
        };
        mImageView = (ImageView) findViewById(R.id.Image);
        Button Gallery = (Button) findViewById(R.id.Gallery);
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImageFromGallery();
            }
        });
        INCREMENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Text = "";
                Text = Quantity_Set.getText().toString().trim();
                int value = Integer.parseInt(Text);
                if (value < 1000) {
                    value = value + 1;

                } else {
                    Toast.makeText(EditorActivity.this, "Out of Range", Toast.LENGTH_SHORT);
                }

                display(value);

            }
        });
        DECREMENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Text = Quantity_Set.getText().toString().trim();
                int value = Integer.parseInt(Text);
                if (value > 0) {
                    value = value - 1;

                } else {
                    Toast.makeText(EditorActivity.this, "Items Empty", Toast.LENGTH_SHORT);
                }

                display(value);

            }

        });
        Intent intent = getIntent();
        CurrentProductUri = intent.getData();
        if (CurrentProductUri == null) {
            setTitle("Add a Product ");

        } else {
            setTitle("Edit Product");
            getLoaderManager().initLoader(0, null, this);

        }
        NAME.setOnTouchListener(mTouchListener);
        COST.setOnTouchListener(mTouchListener);
        Quantity_Set.setOnTouchListener(mTouchListener);

    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    mImageView = (ImageView) findViewById(R.id.Image);
                    mImageView.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void display(int number) {
        Quantity_Set.setText("" + number);
    }

    public void InsertProduct() {
        String mName = NAME.getText().toString();
        String mCost = COST.getText().toString();
        String mQuantity = Quantity_Set.getText().toString();
        byte[] imageByteArray = new byte[0];
        if (mImageView.getDrawable() == null) {
            Toast.makeText(this, "please attach your image", Toast.LENGTH_SHORT).show();//sare msg ikathe arre
        } else if (mName.length() == 0) {
            Toast.makeText(this, "please enter your name", Toast.LENGTH_SHORT).show();

        } else if (mCost.length() == 0) {
            Toast.makeText(this, "please enter cost", Toast.LENGTH_SHORT).show();

        } else if (mQuantity.length() == 0) {
            Toast.makeText(this, "please enter Quantity", Toast.LENGTH_SHORT).show();

        } else {
            imageBitMap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            imageByteArray = bos.toByteArray();

            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.NAME_OF_PRODUCT, mName);
            values.put(ProductContract.ProductEntry.COST_OF_PRODUCT, Integer.parseInt(mCost));
            values.put(ProductContract.ProductEntry.QUANTITY_OF_PRODUCT, mQuantity);
            values.put(ProductContract.ProductEntry.Image, imageByteArray);

            Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

            finish();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.NAME_OF_PRODUCT,
                ProductContract.ProductEntry.COST_OF_PRODUCT,
                ProductContract.ProductEntry.QUANTITY_OF_PRODUCT,
                ProductContract.ProductEntry.Image
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                CurrentProductUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }


    @Override
    public void onBackPressed() {
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.NAME_OF_PRODUCT);
            int costcolumnindex = cursor.getColumnIndex(ProductContract.ProductEntry.COST_OF_PRODUCT);
            int quantitycolumnindex = cursor.getColumnIndex(ProductContract.ProductEntry.QUANTITY_OF_PRODUCT);
            int imageColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.Image);
            String name = cursor.getString(nameColumnIndex);
            String cost = cursor.getString(costcolumnindex);
            String quantity1 = cursor.getString(quantitycolumnindex);
            byte[] imageByteArray = cursor.getBlob(imageColumnIndex);

            NAME.setText(name);
            COST.setText(cost);
            Quantity_Set.setText(quantity1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            mImageView.setImageBitmap(bitmap);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.editoractivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Save_new_data:
                InsertProduct();
                ////is finishk karn hora
                return true;
            case R.id.Update_data:
                saveData();
                return true;
            case R.id.action_delete_Current:
                showDeleteConfirmationDialog();
                return true;
            case R.id.Email_intent:
                EmailIntent();
                return true;
            case android.R.id.home:
                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (CurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_Current);
            MenuItem menuItem1 = menu.findItem(R.id.Update_data);
            MenuItem menuItem2 = menu.findItem(R.id.Email_intent);

            menuItem1.setVisible(false);
            menuItem2.setVisible(false);

            menuItem.setVisible(false);
        }
        return true;
    }

    private void EmailIntent() {
        String name = NAME.getText().toString();
        String cost = COST.getText().toString();
        String quan = Quantity_Set.getText().toString().trim();
        String Msg = "Your Product Name is:" + name + "\nYour Product Cost is:$" + cost + "\nYour Product Quantity is:" + quan;
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Product Detail is:\n");
        intent.putExtra(Intent.EXTRA_TEXT, Msg);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private void deleteProduct() {


        // Only perform the delete if this is an existing pet.
        if (CurrentProductUri != null) {

            int rowsDeleted = getContentResolver().delete(CurrentProductUri, null, null);


            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }

        }
        finish();


    }

    private void saveData() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = NAME.getText().toString().trim();
        String Cost = COST.getText().toString().trim();
        String Quantity = Quantity_Set.getText().toString().trim();
        if (mImageView.getDrawable() == null) {
            Toast.makeText(this, "You must upload an image.", Toast.LENGTH_SHORT).show();
            return;
        } else if (Cost.length() == 0) {
            Toast.makeText(this, "plz Insert Valid Cost.", Toast.LENGTH_SHORT).show();

        } else if (nameString.length() == 0) {
            Toast.makeText(this, "Plz insert Valid name", Toast.LENGTH_SHORT).show();

        } else {
            Bitmap imageBitMap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] imageByteArray = bos.toByteArray();
            if (nameString == null || nameString.equals("")) {
                Toast.makeText(this, "Please fill a Name", Toast.LENGTH_SHORT).show();
                return;

            }
            // Create a ContentValues object where column names are the keys,
            // and pet attributes from the editor are the values.
            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.NAME_OF_PRODUCT, nameString);
            values.put(ProductContract.ProductEntry.COST_OF_PRODUCT, Cost);
            values.put(ProductContract.ProductEntry.QUANTITY_OF_PRODUCT, Quantity);
            values.put(ProductContract.ProductEntry.Image, imageByteArray);
            finish();

            // If the weight is not provided by the user, don't try to parse the string into an
            // integer value. Use 0 by default.


            // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
            if (CurrentProductUri == null) {
                // This is a NEW pet, so insert a new pet into the provider,
                // returning the content URI for the new pet.
                Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

                // Show a toast message depending on whether or not the insertion was successful.
                if (newUri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
                // and pass in the new ContentValues. Pass in null for the selection and selection args
                // because mCurrentPetUri will already identify the correct row in the database that
                // we want to modify.
                int rowsAffected = getContentResolver().update(CurrentProductUri, values, null, null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}


