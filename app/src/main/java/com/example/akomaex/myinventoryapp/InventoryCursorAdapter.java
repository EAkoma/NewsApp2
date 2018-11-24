package com.example.akomaex.myinventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akomaex.myinventoryapp.data.InventoryContract;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.list_product_name);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.list_quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.list_price);
        TextView phoneTextView = (TextView) view.findViewById(R.id.list_phone);

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        int phoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

        String productName = cursor.getString(nameColumnIndex);
        final String quantity = cursor.getString(quantityColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        String phone = cursor.getString(phoneColumnIndex);

        nameTextView.setText(productName);
        quantityTextView.setText(quantity);
        priceTextView.setText(context.getString(R.string.dollar_sign) + price);
        phoneTextView.setText(phone);
        TextView soldButton = (TextView) view.findViewById(R.id.sale_button);
        final TextView saleTextView = (TextView) view.findViewById(R.id.sale_view);
        int saleColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
        saleTextView.setText(cursor.getString(saleColumnIndex));
        soldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantityCounter = Integer.parseInt(quantityTextView.getText().toString().trim());
                if (quantityCounter > 0) {
                    quantityCounter -= 1;
                    quantityTextView.setText(Integer.toString(quantityCounter));
                    long id_number = Integer.parseInt(saleTextView.getText().toString());
                    Uri productSelected = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id_number);
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantityTextView.getText().toString());
                    int rowsAffected = context.getContentResolver().update(productSelected, values, null, null);
                    if (rowsAffected == 0) {
                        Toast.makeText(context, R.string.error_sale_message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.successful_sale_message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
