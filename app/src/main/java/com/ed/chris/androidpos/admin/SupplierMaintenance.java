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
import com.ed.chris.androidpos.admin.admin_adapters.SupplierCreateAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.SupplierCreateContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupplierMaintenance extends Fragment {

    private SQLiteDatabase mDatabase;
    private SupplierCreateAdapter mAdapter;

    public SupplierMaintenance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_supplier_maintenance, container, false);

        DatabaseHelper myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();

        final RecyclerView recyclerview = v.findViewById(R.id.recyclerviewSupplier);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SupplierCreateAdapter(getActivity(), getAllItems());
        recyclerview.setAdapter(mAdapter);


        //for swipe deleting
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( 0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {



                final String supplierID = viewHolder.itemView.getTag().toString();


                String queryifProduct = "Select PRODUCTSUPPLIERID From products where PRODUCTSUPPLIERID = '" + supplierID + "'";
                if (mDatabase.rawQuery(queryifProduct, null).getCount() > 0 ) {

                 // Toast.makeText(getActivity(),"This Supplier still has products registered in the system!",Toast.LENGTH_LONG).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("UNABLE TO DELETE ");
                    builder.setMessage("Supplier cannot be deleted. \nSupplier still has products registered in the system.");
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

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(true);
                    builder.setTitle("DELETE");
                    builder.setMessage("Are you sure you want to delete this?");
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

        return v;
    }

    private Cursor getAllItems(){
        return mDatabase.query(
                SupplierCreateContract.SupplierAccountEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SupplierCreateContract.SupplierAccountEntry._ID + " DESC"
        );

    }

    private void removeItem(long id){
        mDatabase.delete(SupplierCreateContract.SupplierAccountEntry.TABLE_NAME,
                SupplierCreateContract.SupplierAccountEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.swapCursor(getAllItems());
    }

}
