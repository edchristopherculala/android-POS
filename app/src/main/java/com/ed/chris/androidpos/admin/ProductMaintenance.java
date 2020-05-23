package com.ed.chris.androidpos.admin;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceContract;
import com.ed.chris.androidpos.admin.admin_adapters.SalesItemsContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductMaintenance extends Fragment {

    private SQLiteDatabase mDatabase;
    private ProductMaintenanceAdapter mAdapter;

    public ProductMaintenance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_maintenance, container, false);

        DatabaseHelper myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();

        RecyclerView recyclerview = v.findViewById(R.id.recyclerviewProduct);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProductMaintenanceAdapter(getActivity(), getAllItems(), getSoldQuantity());
        recyclerview.setAdapter(mAdapter);

        //for swipe deleting
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                final String productID = viewHolder.itemView.getTag().toString();


                String count3 = "Select * From products where _ID = '" + productID + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                int icount3 = mcursor3.getInt(11);
                String PQTY = String.valueOf(icount3);

                if (icount3 > 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("UNABLE TO DELETE ");
                    builder.setMessage("Product cannot be deleted. \nProduct still has some stocks.");
                    builder.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    mAdapter.swapCursor(getAllItems());
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                } else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("DELETE");
                    builder.setMessage("Are you sure you want to delete this Product?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    removeItem((long) viewHolder.itemView.getTag());
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mAdapter.swapCursor(getAllItems());
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }

            }
        }).attachToRecyclerView(recyclerview);

        // this method expects a ScrollDirectionListener as second arg
        // so use this for that parameter
        // fabAdd.attachToRecyclerView(recyclerview);


        return v;
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProductMaintenanceContract.ProductMaintenanceEntry._ID + " DESC"
        );

    }


    private Cursor getSoldQuantity() {
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

    private void removeItem(long id) {
        mDatabase.delete(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME,
                ProductMaintenanceContract.ProductMaintenanceEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter.swapCursor(getAllItems());
    }

    @Override
    public void onResume() {
        super.onResume();

        mAdapter.swapCursor(getAllItems());
    }
}
