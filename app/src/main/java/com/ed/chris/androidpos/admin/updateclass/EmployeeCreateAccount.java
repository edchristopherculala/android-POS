package com.ed.chris.androidpos.admin.updateclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeCreateAccountContract;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeAdapter;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.util.ArrayList;

public class EmployeeCreateAccount extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private EmployeeTypeAdapter mAdapter;
    DatabaseHelper myDb;

    EditText editTextUsername,editTextPassword,editTextConfirmPassword;
    Button buttonAddAccountRole;

    TextView firstname,lastname,editTextRole;

    Spinner spinnerEmployeeRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_create_account);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();


        editTextUsername = findViewById(R.id.edittxtemployeeUsername);
        editTextPassword = findViewById(R.id.edittxtemployeePassword);
        editTextConfirmPassword = findViewById(R.id.edittxtemployeeConfirmPassword);
        editTextRole = findViewById(R.id.edittxtemployeeEmployeeRole);

        buttonAddAccountRole = findViewById(R.id.btnAddAccountRole);

        spinnerEmployeeRole = findViewById(R.id.spinnerEmployeeRole);

        firstname = findViewById(R.id.FN);
        lastname = findViewById(R.id.LN);



        //Add Spinner Items

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Admin");
        arrayList.add("User");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,                         android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployeeRole.setAdapter(arrayAdapter);

        //spinner onChoose

        spinnerEmployeeRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  String employeeRole = parent.getItemAtPosition(position).toString();
                  editTextRole.setText(employeeRole);
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {
              }
            });

        Intent intent = getIntent();
        String EmployeeFirstName = intent.getStringExtra("employeeFirstName");
        String EmployeeLastName = intent.getStringExtra("employeeLastName");
        final String EmployeeID = intent.getStringExtra("employeeID");

        firstname.setText(EmployeeFirstName);
        lastname.setText(EmployeeLastName);


        //editTextUsername.setText(EmployeeFirstName);

        buttonAddAccountRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });









    }

    private void addItem(){
        Intent intent = getIntent();
        String EmployeeFirstName = intent.getStringExtra("employeeFirstName");
        String EmployeeLastName = intent.getStringExtra("employeeLastName");
        final String EmployeeID = intent.getStringExtra("employeeID");


        String FN = firstname.getText().toString().trim();
        String LN = lastname.getText().toString().trim();
        String USERNAME = editTextUsername.getText().toString().trim();
        String PASSWORD = editTextPassword.getText().toString().trim();
        String CONFIRMPASSWORD = editTextConfirmPassword.getText().toString().trim();
        String ROLE = editTextRole.getText().toString().trim();



        if (USERNAME.isEmpty() && PASSWORD.isEmpty() && CONFIRMPASSWORD.isEmpty() && ROLE.isEmpty()){

            Toast.makeText(this,"Please Input Data",Toast.LENGTH_LONG).show();

        }else if (USERNAME.isEmpty() ){

            Toast.makeText(this,"Please Input Username",Toast.LENGTH_LONG).show();

        }else if (PASSWORD.isEmpty() ){

            Toast.makeText(this,"Please Input Password",Toast.LENGTH_LONG).show();
        }else if (CONFIRMPASSWORD.isEmpty() ){

            Toast.makeText(this,"Please Input Confirm Password",Toast.LENGTH_LONG).show();
        }else if (ROLE.isEmpty() ){

            Toast.makeText(this,"Please Choose Account Role",Toast.LENGTH_LONG).show();
        }else if (!PASSWORD.matches(CONFIRMPASSWORD) ){

            Toast.makeText(this,"Password does not Match. \n Please Input Password Again.",
                    Toast.LENGTH_LONG).show();

            editTextPassword.getText().clear();
            editTextConfirmPassword.getText().clear();
        }else if (PASSWORD.length() < 6 && CONFIRMPASSWORD.length() < 6 ){

            Toast.makeText(this,"Password too short. \n Please Exceed your Password to 6 characters.",
                    Toast.LENGTH_LONG).show();

        }

        else {

            /*ContentValues cv = new ContentValues();
            cv.put(EmployeeTypeContract.EmployeeTypeEntry.KEY_EMPLOYEETYPE, employeeType);
            cv.put(EmployeeTypeContract.EmployeeTypeEntry.KEY_DESCRIPTION, employeeTypeDescription);

            mDatabase.insert(EmployeeTypeContract.EmployeeTypeEntry.TABLE_NAME, null, cv);
            mAdapter.swapCursor(getAllItems());



            editTextEmployeeType.getText().clear();
            editTextDescription.getText().clear();  */

            //NEW

            // String type = editTextEmployeeType.getText().toString().trim();
            String typeFN = firstname.getText().toString().trim();
            String typeLN = lastname.getText().toString().trim();
            String typeUsername = editTextUsername.getText().toString().trim();
            String typePassword = editTextPassword.getText().toString().trim();
            String typeConfirmPassword = editTextConfirmPassword.getText().toString().trim();
            String typeRole = editTextRole.getText().toString().trim();


            String queryFN = "Select * From employeeaccounts where FIRSTNAME = '" + typeFN + "'";
            String queryLN = "Select * From employeeaccounts where LASTNAME = '" + typeLN + "'";

            String checkifUsernameExists = "Select USERNAME From employeeaccounts where USERNAME = '" +typeUsername+ "' ";
            if (mDatabase.rawQuery(queryFN, null).getCount() > 0  && mDatabase.rawQuery(queryLN,
                    null).getCount() > 0 ) {
                Toast.makeText(this, "" + typeFN + "  "+ typeLN + " already has an active account!", Toast.LENGTH_SHORT).show();
            }else if (mDatabase.rawQuery(checkifUsernameExists, null).getCount() > 0 ) {
                Toast.makeText(this, "You cannot use this username.\n Username is already in use.", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues cv = new ContentValues();
                cv.put(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_FIRTSNAME, FN);
                cv.put(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_LASTNAME, LN);
                cv.put(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_USERNAME, USERNAME);
                cv.put(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_PASSWORD, PASSWORD);
                cv.put(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_ROLE, ROLE);
                cv.put(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_ID, EmployeeID );


                mDatabase.insert(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.TABLE_NAME, null, cv);
               // mAdapter.swapCursor(getAllItems());


                Toast.makeText(this, "" + typeFN + "  "+ typeLN + " was Successfully Created an Account!", Toast.LENGTH_SHORT).show();


                editTextUsername.getText().clear();
                editTextPassword.getText().clear();
                editTextConfirmPassword.getText().clear();

                finish();



            }


        }


    }

    private Cursor getAllItems(){
        return mDatabase.query(
                EmployeeCreateAccountContract.EmployeeCreateAccountEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                EmployeeCreateAccountContract.EmployeeCreateAccountEntry._ID + " DESC"
        );

    }


}
