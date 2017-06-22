package com.example.laptop.inventoryfinalapp.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptop.inventoryfinalapp.R;

import static android.R.attr.id;

/**
 * Created by LAPTOP on 19-06-2017.
 */

public class ProductAdapter extends CursorAdapter {
    public ProductAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView tv1 = (TextView) view.findViewById(R.id.name);
        TextView tv2 = (TextView) view.findViewById(R.id.cost);
        TextView tv3 = (TextView) view.findViewById(R.id.quantity);
        int namecolumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.NAME_OF_PRODUCT);
        int costcolumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COST_OF_PRODUCT);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.QUANTITY_OF_PRODUCT);
        String ProductName = "Name: " + cursor.getString(namecolumnIndex);
        String ProductCost = "Cost: $" + cursor.getString(costcolumnIndex);
        String ProductQuantity = cursor.getString(quantityColumnIndex);


        tv1.setText(ProductName);
        tv2.setText(ProductCost);
        tv3.setText(ProductQuantity);
        final Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
        final int id = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
        final int quantityItem = Integer.parseInt(ProductQuantity);
   Button btn=(Button)view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                if (quantityItem > 0) {
                    int leftquantity = quantityItem;
                    values.put(ProductContract.ProductEntry.QUANTITY_OF_PRODUCT, --leftquantity);
                    Uri uri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                    context.getContentResolver().update(uri, values, null, null);
                    context.getContentResolver().notifyChange(currentProductUri, null);
                } else {
                    Toast.makeText(context, "Items Finish", Toast.LENGTH_SHORT).show();
                }
                context.getContentResolver().notifyChange(ProductContract.ProductEntry.CONTENT_URI, null);

            }
        });
    }
}

