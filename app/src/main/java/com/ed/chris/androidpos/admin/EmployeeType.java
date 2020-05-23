package com.ed.chris.androidpos.admin;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeType extends Fragment {

    private SQLiteDatabase mDatabase;
    private EmployeeTypeAdapter mAdapter;
    private DatabaseHelper myDb;
    private EditText editTextEmployeeType, editTextDescription;
    private Button buttonAdd, buttonViewAll;


    public EmployeeType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_employee_type, container, false);

        myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();

        RecyclerView recyclerview = v.findViewById(R.id.recyclerviewEmployeeType);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new EmployeeTypeAdapter(getActivity(), getAllItems());
        recyclerview.setAdapter(mAdapter);

        //try onclickRecyclerView

        //for swipe deleting
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                final String employeeTYPEID = viewHolder.itemView.getTag().toString();

                //need fix

                String count3 = "Select count(*) From employee where EMPLOYEETYPEID = '" + employeeTYPEID + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                int icount3 = mcursor3.getInt(0);
                String PQTY = String.valueOf(icount3);

                if (icount3 > 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setTitle("UNABLE TO DELETE ");
                    builder.setMessage("Cannot be deleted.. \nEmployee type is still in use.");
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
                    dialog.show();

                }

            }
        }).attachToRecyclerView(recyclerview);

        editTextEmployeeType = v.findViewById(R.id.edittxtemployeetype1);
        editTextDescription = v.findViewById(R.id.edittxtdescription1);
        buttonAdd = v.findViewById(R.id.btnAdd);

        getItemsFromRecyclerView();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        return v;
    }

    private void getItemsFromRecyclerView() {

        Intent intent = getActivity().getIntent();

        String EmployeeType = intent.getStringExtra("EmployeeType");
        String EmployeeTypeDescription = intent.getStringExtra("EmployeeTypeDescription");
        editTextEmployeeType.setText(EmployeeType);
        editTextDescription.setText(EmployeeTypeDescription);
    }

    private void addItem() {

        String employeeType = editTextEmployeeType.getText().toString().trim().toUpperCase();
        String employeeTypeDescription = editTextDescription.getText().toString().trim();


        if (employeeType.isEmpty() && employeeTypeDescription.isEmpty()) {

            Toast.makeText(getActivity(), "Please Input Employee Type and Description", Toast.LENGTH_LONG).show();

        } else if (employeeType.isEmpty()) {

            Toast.makeText(getActivity(), "Please Input Employee Type", Toast.LENGTH_LONG).show();

        } else if (employeeTypeDescription.isEmpty()) {

            Toast.makeText(getActivity(), "Please Input Description", Toast.LENGTH_LONG).show();
        } else {
            //NEW

            String type = editTextEmployeeType.getText().toString().trim().toUpperCase();


            String query = "Select * From employeetype where EMPLOYEETYPE = '" + type + "'";
            if (mDatabase.rawQuery(query, null).getCount() > 0) {
                Toast.makeText(getActivity(), "" + type + " already Exist!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues cv = new ContentValues();
                cv.put(EmployeeTypeContract.EmployeeTypeEntry.KEY_EMPLOYEETYPE, employeeType);
                cv.put(EmployeeTypeContract.EmployeeTypeEntry.KEY_DESCRIPTION, employeeTypeDescription);

                mDatabase.insert(EmployeeTypeContract.EmployeeTypeEntry.TABLE_NAME, null, cv);
                mAdapter.swapCursor(getAllItems());

                editTextEmployeeType.getText().clear();
                editTextDescription.getText().clear();

            }
        }


    }

    //for swipe deleting
    private void removeItem(long id) {
        mDatabase.delete(EmployeeTypeContract.EmployeeTypeEntry.TABLE_NAME,
                EmployeeTypeContract.EmployeeTypeEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }


    //for updating
    private void updateItem(long id) {


    }

    private Cursor getAllItems() {
        return mDatabase.query(
                EmployeeTypeContract.EmployeeTypeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                EmployeeTypeContract.EmployeeTypeEntry._ID + " DESC"
        );

    }

    public void showMessage(String title, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter.swapCursor(getAllItems());
    }
}
