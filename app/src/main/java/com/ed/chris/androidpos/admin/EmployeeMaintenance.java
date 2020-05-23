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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeContract;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeCreateAccountContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeMaintenance extends Fragment {

    private SQLiteDatabase mDatabase;
    private EmployeeAdapter mAdapter;
    DatabaseHelper myDb;

    private EditText editTextEmployeeFN, editTextEmployeeLN,editTextEmployeeMobile,
            editTextEmployeeEmail,editTextEmployeeType,editTextEmployeeAddress;

    private Spinner allEmployeeTypes;
    Animation animation, animation1;
    SwipeRefreshLayout swipeRefreshLayout;
    private List<String> employeetypesList =new ArrayList<>();
    private ArrayAdapter<String> adapter;
    Spinner spinnerEmployee;
    private EmployeeType employeeTypeFragment;
    private EmployeeMaintenance employeeMaintenanceFragment;


    public EmployeeMaintenance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_employee_maintenance, container, false);

        allEmployeeTypes = v.findViewById(R.id.spinnerEmployeeType);

        myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();

        RecyclerView recyclerview = v.findViewById(R.id.recyclerviewEmployee);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new EmployeeAdapter(getActivity(), getAllItems());
        recyclerview.setAdapter(mAdapter);

        //initialize delete here first
        //for swipe deleting
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( 0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("DELETE");
                builder.setMessage("Are you sure you want to DELETE?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final String userID = viewHolder.itemView.getTag().toString();

                                removeItem((long) viewHolder.itemView.getTag());


                                String queryAccounts = "Select * From employeeaccounts where USERID = '" + userID + "'";

                                if (mDatabase.rawQuery(queryAccounts, null).getCount() == 0 ) {
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                } else {

                                    //new alertDialog

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setCancelable(false);
                                    builder.setTitle("Delete Account");
                                    builder.setMessage("The user you deleted has an active account. Delete?");
                                    builder.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    mDatabase.execSQL("DELETE FROM " + EmployeeCreateAccountContract.EmployeeCreateAccountEntry.TABLE_NAME + " WHERE USERID ='" + userID + "'");
                                                    Toast.makeText(getActivity(), "User Account also Deleted!", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });

                                    AlertDialog dialog1 = builder.create();
                                    dialog1.setCanceledOnTouchOutside(false);
                                    dialog1.show();
                                }
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
        }).attachToRecyclerView(recyclerview);

        editTextEmployeeFN = v.findViewById(R.id.edittxtemployeeFirstName);
        editTextEmployeeLN = v.findViewById(R.id.edittxtemployeeLastName);
        editTextEmployeeMobile = v.findViewById(R.id.edittxtemployeeMobile);
        editTextEmployeeEmail = v.findViewById(R.id.edittxtemployeeEmail);
        editTextEmployeeType = v.findViewById(R.id.edittxtemployeeEmployeeType);
        editTextEmployeeAddress = v.findViewById(R.id.edittxtemployeeAddress);

        return v;
    }

    private void addItem(){

        String FN = editTextEmployeeFN.getText().toString().trim().toUpperCase();
        String LN = editTextEmployeeLN.getText().toString().trim().toUpperCase();
        String MOBILE = editTextEmployeeMobile.getText().toString().trim();
        String EMAIL = editTextEmployeeEmail.getText().toString().trim();
        String EMPLOYEETYPE = editTextEmployeeType.getText().toString().trim();
        String ADDRESS = editTextEmployeeAddress.getText().toString().trim();


        if (FN.isEmpty() && LN.isEmpty() && MOBILE.isEmpty() && EMAIL.isEmpty() && EMPLOYEETYPE.isEmpty() && ADDRESS.isEmpty()){

            Toast.makeText(getActivity(),"Please Input Data",Toast.LENGTH_LONG).show();

        }else if (FN.isEmpty() ){

            Toast.makeText(getActivity(),"Please Input First Name",Toast.LENGTH_LONG).show();

        }else if (LN.isEmpty() ){

            Toast.makeText(getActivity(),"Please Input Last Name",Toast.LENGTH_LONG).show();
        }else if (MOBILE.isEmpty() ){

            Toast.makeText(getActivity(),"Please Input Mobile Number",Toast.LENGTH_LONG).show();
        }else if (EMAIL.isEmpty() ){

            Toast.makeText(getActivity(),"Please Input Email Address",Toast.LENGTH_LONG).show();
        }else if (EMPLOYEETYPE.isEmpty() ){

            Toast.makeText(getActivity(),"Please Select Employee Type",Toast.LENGTH_LONG).show();
        }else if (ADDRESS.isEmpty() ){

            Toast.makeText(getActivity(),"Please Input Address",Toast.LENGTH_LONG).show();
        }else {

            //NEW

            String typeFN = editTextEmployeeFN.getText().toString().trim().toUpperCase();
            String typeLN = editTextEmployeeLN.getText().toString().trim().toUpperCase();


            String queryFN = "Select * From employee where FIRSTNAME = '" + typeFN + "'";
            String queryLN = "Select * From employee where LASTNAME = '" + typeLN + "'";
            if (mDatabase.rawQuery(queryFN, null).getCount() > 0  && mDatabase.rawQuery(queryLN,
                    null).getCount() > 0 ) {
                Toast.makeText(getActivity(), "" + typeFN + "  "+ typeLN + " already Exist!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues cv = new ContentValues();
                cv.put(EmployeeContract.EmployeeEntry.KEY_FIRTSNAME, FN);
                cv.put(EmployeeContract.EmployeeEntry.KEY_LASTNAME, LN);
                cv.put(EmployeeContract.EmployeeEntry.KEY_MOBILE, MOBILE);
                cv.put(EmployeeContract.EmployeeEntry.KEY_EMAIL, EMAIL);
                cv.put(EmployeeContract.EmployeeEntry.KEY_EMPLOYEETYPE, EMPLOYEETYPE);
                cv.put(EmployeeContract.EmployeeEntry.KEY_ADDRESS, ADDRESS);

                mDatabase.insert(EmployeeContract.EmployeeEntry.TABLE_NAME, null, cv);
                mAdapter.swapCursor(getAllItems());


                editTextEmployeeFN.getText().clear();
                editTextEmployeeLN.getText().clear();
                editTextEmployeeMobile.getText().clear();
                editTextEmployeeEmail.getText().clear();
                editTextEmployeeType.getText().clear();
                editTextEmployeeAddress.getText().clear();


            }


        }


    }

    //for swipe deleting
    private void removeItem(long id){
        mDatabase.delete(EmployeeContract.EmployeeEntry.TABLE_NAME,
                EmployeeContract.EmployeeEntry._ID + "=" + id, null);


        mAdapter.swapCursor(getAllItems());

    }

    //for swipe deleting other data
    private void removeData(String fn){

        mDatabase.delete(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.TABLE_NAME,
                EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_FIRTSNAME + "=" +fn,null);

        mAdapter.swapCursor(getAllItems());

    }


    //for updating
    private void updateItem(long id){




    }

    private Cursor getAllItems(){
        return mDatabase.query(
                EmployeeContract.EmployeeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                EmployeeContract.EmployeeEntry._ID + " DESC"
        );

    }

    //Prepare data for Spinner
    public void prepareData()
    {
        employeetypesList=myDb.getAllEmployeeTypes();
        //adapter for spinner
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,employeetypesList);
        //attach adapter to spinner
        allEmployeeTypes.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter.swapCursor(getAllItems());
    }

}
