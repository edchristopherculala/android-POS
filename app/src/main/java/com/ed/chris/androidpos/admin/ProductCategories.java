package com.ed.chris.androidpos.admin;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.ProductCategoryAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.ProductCategoryContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCategories extends Fragment {

    private SQLiteDatabase mDatabase;
    private ProductCategoryAdapter mAdapter;
    private DatabaseHelper myDb;
    private EditText editTextProductCATEGORY;
    private Button buttonAdd;


    public ProductCategories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_categories, container, false);

        myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();

        final int orientation = this.getResources().getConfiguration().orientation;
        final RecyclerView recyclerview = v.findViewById(R.id.recyclerviewProductCategory);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            mAdapter = new ProductCategoryAdapter(getActivity(), getAllItems());
            recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerview.setAdapter(mAdapter);
        } else {
            mAdapter = new ProductCategoryAdapter(getActivity(), getAllItems());
            recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 6));
            recyclerview.setAdapter(mAdapter);
        }
        //for swipe deleting
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                final String productCategoryID = viewHolder.itemView.getTag().toString();

                String queryifProduct = "Select PRODUCTCATEGORYID From products where PRODUCTCATEGORYID = '" + productCategoryID + "'";
                if (mDatabase.rawQuery(queryifProduct, null).getCount() > 0) {

                    // Toast.makeText(getActivity(),"This Supplier still has products registered in the system!",Toast.LENGTH_LONG).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("UNABLE TO DELETE ");
                    builder.setMessage("Product category cannot be deleted. \nCategory is still being used in one of your products.");
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
                    dialog.onBackPressed();
                    dialog.show();

                }

            }
        }).attachToRecyclerView(recyclerview);

        editTextProductCATEGORY = v.findViewById(R.id.edittxtProductCategory);
        buttonAdd = v.findViewById(R.id.btnAddProductCategory);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
        return v;
    }

    private void addItem() {

        String productCATEGORY = editTextProductCATEGORY.getText().toString().trim().toUpperCase();

        if (productCATEGORY.isEmpty()) {
            Toast.makeText(getActivity(), "Please Input Product Category", Toast.LENGTH_LONG).show();
        } else {
            String type = editTextProductCATEGORY.getText().toString().trim().toUpperCase();

            String query = "Select * From productcategory where CATEGORY = '" + type + "'";
            if (mDatabase.rawQuery(query, null).getCount() > 0) {
                Toast.makeText(getActivity(), "" + type + " already Exist!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues cv = new ContentValues();
                cv.put(ProductCategoryContract.ProductCategoryEntry.KEY_PRODUCT_CATEGORY, productCATEGORY);

                mDatabase.insert(ProductCategoryContract.ProductCategoryEntry.TABLE_NAME, null, cv);
                mAdapter.swapCursor(getAllItems());

                editTextProductCATEGORY.getText().clear();

            }
        }

    }

    private void removeItem(long id) {
        mDatabase.delete(ProductCategoryContract.ProductCategoryEntry.TABLE_NAME,
                ProductCategoryContract.ProductCategoryEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }

    private Cursor getAllItems() {
        return mDatabase.query(
                ProductCategoryContract.ProductCategoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProductCategoryContract.ProductCategoryEntry._ID + " DESC"
        );

    }

}
