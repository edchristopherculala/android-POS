package com.ed.chris.androidpos.admin.updateclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class EmployeeTypeUpdate extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private EmployeeTypeAdapter mAdapter;
    DatabaseHelper myDb;

    EditText updateEmployeeType, updateEmployeeTypeDesc;
    TextView displayEmployeeType, displayEmployeeTypeDesc;
    Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_type_update);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        buttonUpdate = findViewById(R.id.btnUpdate);


        updateEmployeeType = findViewById(R.id.edittxtemployeetypeUpdate);
        updateEmployeeTypeDesc = findViewById(R.id.edittxtdescriptionUpdate);

        displayEmployeeType = findViewById(R.id.employeetypeText);
        displayEmployeeTypeDesc = findViewById(R.id.employeetypeDescriptionText);


        Intent intent = getIntent();

        String employeeType = intent.getStringExtra("employeetype");
        String employeeTypeDescription = intent.getStringExtra("employeetypedescription");

        updateEmployeeType.setText(employeeType);
        updateEmployeeTypeDesc.setText(employeeTypeDescription);


        displayEmployeeType.setText(employeeType);
        displayEmployeeTypeDesc.setText(employeeTypeDescription);


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });








    }
    private void updateItem() {


        Intent intent = getIntent();


        String employeeType = intent.getStringExtra("employeetype");
        String employeeTypeDescription = intent.getStringExtra("employeetypedescription");

        String type = updateEmployeeType.getText().toString().trim().toUpperCase();
        String type2 = updateEmployeeTypeDesc.getText().toString().trim();

        String query = "Select * From employeetype where EMPLOYEETYPE = '" + type + "'";
        String query2 = "Select * From employeetype where DESCRIPTION = '" + type2 + "'";

        String a,b;
        a = updateEmployeeType.getText().toString().trim().toUpperCase();
        b = updateEmployeeTypeDesc.getText().toString().trim().toUpperCase();


        if (a.isEmpty() && b.isEmpty()) {

            Toast.makeText(this, "Input Data First!", Toast.LENGTH_LONG).show();

        } else if (a.isEmpty()) {

            Toast.makeText(this, "Please Input Employee Type!", Toast.LENGTH_LONG).show();

        } else if (b.isEmpty()) {
            Toast.makeText(this, "Please Input Description!", Toast.LENGTH_LONG).show();


        }  else if (mDatabase.rawQuery(query, null).getCount() > 0 && mDatabase.rawQuery(query2, null).getCount() > 0) {
                Toast.makeText(EmployeeTypeUpdate.this, "Data already Exist!", Toast.LENGTH_SHORT).show();

        } else {

                String employeeTYPE = updateEmployeeType.getText().toString().trim().toUpperCase();
                String employeeTypeDESC = updateEmployeeTypeDesc.getText().toString().trim();


                mDatabase.execSQL("UPDATE  " + EmployeeTypeContract.EmployeeTypeEntry.TABLE_NAME + " SET EMPLOYEETYPE ='" + employeeTYPE + "'," +
                        " DESCRIPTION ='" + employeeTypeDESC + "'  WHERE EMPLOYEETYPE='" + employeeType + "'");
                Toast.makeText(this, "Employee Type Successfully Updated!", Toast.LENGTH_LONG).show();
                updateEmployeeType.getText().clear();
                updateEmployeeTypeDesc.getText().clear();


                finish();
            }


        }


    }


