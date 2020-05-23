package com.ed.chris.androidpos.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.SalesContract;
import com.ed.chris.androidpos.admin.admin_adapters.SalesDetailsAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.SalesItemsAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.SalesItemsContract;
import com.ed.chris.androidpos.database.DatabaseHelper;
//import com.steelkiwi.library.view.BadgeHolderLayout;

public class ReceiptInformation extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private SalesItemsAdapter mAdapter;
    private SalesDetailsAdapter mAdapter1;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_information);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        Intent intent = getIntent();
        String sampledata = intent.getStringExtra("receiptnumber");
        String count3 = "Select * From salesitems where RECEIPTNO = '" + sampledata + "'";

        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
        mcursor3.moveToFirst();
        String icount3 = mcursor3.getString(3);

        //for items
        RecyclerView recyclerview = findViewById(R.id.recyclerviewItemSales);
        mAdapter = new SalesItemsAdapter(this, mcursor3);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(mAdapter);

        mAdapter.swapCursor(mcursor3);
        String count4 = "Select * From sales where RECEIPTNO = '" + sampledata + "'";

        Cursor mcursor4 = mDatabase.rawQuery(count4, null);
        mcursor4.moveToFirst();
        String icount4 = mcursor4.getString(3);

        //for details
        RecyclerView recyclerview1 = findViewById(R.id.recyclerviewSalesData);
        mAdapter1 = new SalesDetailsAdapter(this, mcursor4);
        recyclerview1.setLayoutManager(new LinearLayoutManager(this));
        recyclerview1.setAdapter(mAdapter1);

        mAdapter1.swapCursor(mcursor4);

    }


    private Cursor getAllItems() {
        return mDatabase.query(
                SalesItemsContract.SalesItemsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SalesItemsContract.SalesItemsEntry._ID + " DESC"
        );
    }

    private Cursor getAllReceiptDetails() {
        return mDatabase.query(
                SalesContract.SalesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SalesItemsContract.SalesItemsEntry._ID + " DESC"
        );
    }
}
