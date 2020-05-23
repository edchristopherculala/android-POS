package com.ed.chris.androidpos.admin.admin_adapters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EmployeeCreate extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private EmployeeAdapter mAdapter;
    DatabaseHelper myDb;

    EditText editTextEmployeeFN, editTextEmployeeLN, editTextEmployeeMobile,
            editTextEmployeeEmail, editTextEmployeeAddress;
    Button buttonAddEmployee;
    TextView editTextEmployeeType;
    Spinner allEmployeeTypes;
    List<String> employeetypesList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_create);

        allEmployeeTypes = findViewById(R.id.spinnerEmployeeType);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        //prepare for spinner
        prepareData();


        editTextEmployeeFN = findViewById(R.id.edittxtemployeeFirstName);
        editTextEmployeeLN = findViewById(R.id.edittxtemployeeLastName);
        editTextEmployeeMobile = findViewById(R.id.edittxtemployeeMobile);
        editTextEmployeeEmail = findViewById(R.id.edittxtemployeeEmail);
        editTextEmployeeType = findViewById(R.id.edittxtemployeeEmployeeType);
        editTextEmployeeAddress = findViewById(R.id.edittxtemployeeAddress);
        buttonAddEmployee = findViewById(R.id.btnAddEmployee);

        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });


        allEmployeeTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String employeeTYPE;

                employeeTYPE = parent.getItemAtPosition(position).toString();
                editTextEmployeeType.setText(employeeTYPE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addItem() {

        String FN, LN, MOBILE, EMAIL, EMPLOYEETYPE, ADDRESS;

        FN = editTextEmployeeFN.getText().toString().trim().toUpperCase();
        LN = editTextEmployeeLN.getText().toString().trim().toUpperCase();
        MOBILE = editTextEmployeeMobile.getText().toString().trim();
        EMAIL = editTextEmployeeEmail.getText().toString().trim();
        EMPLOYEETYPE = editTextEmployeeType.getText().toString().trim();
        ADDRESS = editTextEmployeeAddress.getText().toString().trim();


        if (FN.isEmpty() && LN.isEmpty() && MOBILE.isEmpty() && EMAIL.isEmpty() && EMPLOYEETYPE.isEmpty() && ADDRESS.isEmpty()) {

            Toast.makeText(this, "Please Input Data", Toast.LENGTH_LONG).show();

        } else if (FN.isEmpty()) {

            Toast.makeText(this, "Please Input First Name", Toast.LENGTH_LONG).show();

        } else if (LN.isEmpty()) {

            Toast.makeText(this, "Please Input Last Name", Toast.LENGTH_LONG).show();
        } else if (MOBILE.isEmpty()) {

            Toast.makeText(this, "Please Input Mobile Number", Toast.LENGTH_LONG).show();
        } else if (EMAIL.isEmpty()) {

            Toast.makeText(this, "Please Input Email Address", Toast.LENGTH_LONG).show();
        } else if (EMPLOYEETYPE.isEmpty()) {

            Toast.makeText(this, "Please Select Employee Type", Toast.LENGTH_LONG).show();
        } else if (ADDRESS.isEmpty()) {

            Toast.makeText(this, "Please Input Address", Toast.LENGTH_LONG).show();
        } else {

            String typeFN, typeLN, queryFN, queryLN;
            typeFN = editTextEmployeeFN.getText().toString().trim().toUpperCase();
            typeLN = editTextEmployeeLN.getText().toString().trim().toUpperCase();
            queryFN = "Select * From employee where FIRSTNAME = '" + typeFN + "'";
            queryLN = "Select * From employee where LASTNAME = '" + typeLN + "'";

            if (mDatabase.rawQuery(queryFN, null).getCount() > 0 && mDatabase.rawQuery(queryLN,
                    null).getCount() > 0) {
                Toast.makeText(this, "" + typeFN + "  " + typeLN + " already Exist!", Toast.LENGTH_SHORT).show();
            } else {
                String count2,ba;
                Cursor mcursor2;
                int icount2;

                count2 = "Select _ID From employeetype where EMPLOYEETYPE = '" + EMPLOYEETYPE + "'";
                mcursor2 = mDatabase.rawQuery(count2, null);
                mcursor2.moveToFirst();
                icount2 = mcursor2.getInt(0);

                 ba = String.valueOf(icount2);

                ContentValues cv = new ContentValues();
                cv.put(EmployeeContract.EmployeeEntry.KEY_FIRTSNAME, FN);
                cv.put(EmployeeContract.EmployeeEntry.KEY_LASTNAME, LN);
                cv.put(EmployeeContract.EmployeeEntry.KEY_MOBILE, MOBILE);
                cv.put(EmployeeContract.EmployeeEntry.KEY_EMAIL, EMAIL);
                cv.put(EmployeeContract.EmployeeEntry.KEY_EMPLOYEETYPE, EMPLOYEETYPE);
                cv.put(EmployeeContract.EmployeeEntry.KEY_ADDRESS, ADDRESS);
                cv.put(EmployeeContract.EmployeeEntry.KEY_EMPLOYEETYPE_ID, ba);

                mDatabase.insert(EmployeeContract.EmployeeEntry.TABLE_NAME, null, cv);

                Toast.makeText(this, "" + typeFN + "  " + typeLN + " was Successfully Added!", Toast.LENGTH_SHORT).show();
                editTextEmployeeFN.getText().clear();
                editTextEmployeeLN.getText().clear();
                editTextEmployeeMobile.getText().clear();
                editTextEmployeeEmail.getText().clear();
                editTextEmployeeAddress.getText().clear();

                finish();

            }

        }

    }

    //for swipe deleting
    private void removeItem(long id) {
        mDatabase.delete(EmployeeContract.EmployeeEntry.TABLE_NAME,
                EmployeeContract.EmployeeEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }

    private Cursor getAllItems() {
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
    public void prepareData() {
        employeetypesList = myDb.getAllEmployeeTypes();
        //adapter for spinner
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, employeetypesList);
        //attach adapter to spinner
        allEmployeeTypes.setAdapter(adapter);
    }
}
