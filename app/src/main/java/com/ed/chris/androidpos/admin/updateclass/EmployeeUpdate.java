package com.ed.chris.androidpos.admin.updateclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EmployeeUpdate extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    EditText updateEmployeeFIRSTNAME, updateEmployeeLASTNAME
            , updateEmployeeMOBILE, updateEmployeeEMAIL
            ,updateEmployeeADDRESS;
    TextView  updateEmployeeTYPE;
    Button buttonUpdate;

    Spinner employeeTYPES;

    List<String> employeetypesList =new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_update);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();




        buttonUpdate = findViewById(R.id.buttonUPDATEEMPLOYEE);

        updateEmployeeFIRSTNAME = findViewById(R.id.edttxtFNupdate);
        updateEmployeeLASTNAME = findViewById(R.id.edttxtLNupdate);
        updateEmployeeMOBILE = findViewById(R.id.edttxtMOBILEupdate);
        updateEmployeeEMAIL = findViewById(R.id.edttxtEMAILupdate);
        updateEmployeeADDRESS = findViewById(R.id.edttxtADDRESSupdate);

        updateEmployeeTYPE = findViewById(R.id.txtviewEMPLOYEETYPEupdate);

        employeeTYPES = findViewById(R.id.spinnerEmployeeTypeUPDATE);


        prepareData();

        Intent intent = getIntent();

        String fn = intent.getStringExtra("fn");
        String ln = intent.getStringExtra("ln");
        String mobile = intent.getStringExtra("mobile");
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");
        String employeetype = intent.getStringExtra("employeetype");

        updateEmployeeFIRSTNAME.setText(fn);
        updateEmployeeLASTNAME.setText(ln);
        updateEmployeeMOBILE.setText(mobile);
        updateEmployeeEMAIL.setText(email);
        updateEmployeeADDRESS.setText(address);

        updateEmployeeTYPE.setText(employeetype);


        updateEmployeeFIRSTNAME.setEnabled(false);
        updateEmployeeLASTNAME.setEnabled(false);



        employeeTYPES.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // clicked item will be shown as spinner
                String employeeTYPE = parent.getItemAtPosition(position).toString();

                updateEmployeeTYPE.setText(employeeTYPE);
                // Toast.makeText(getActivity(),""+parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });





    }

    //Prepare data for Spinner
    public void prepareData()
    {
        employeetypesList=myDb.getAllEmployeeTypes();
        //adapter for spinner
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,employeetypesList);
        //attach adapter to spinner
        employeeTYPES.setAdapter(adapter);
    }



    private void updateItem() {

        Intent intent = getIntent();

        String fn = intent.getStringExtra("fn");
        String ln = intent.getStringExtra("ln");
        String mobile = intent.getStringExtra("mobile");
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");
        String employeetype = intent.getStringExtra("employeetype");
        String uniqueID = intent.getStringExtra("employeeID");


        String type = updateEmployeeFIRSTNAME.getText().toString().trim().toUpperCase();
        String type2 = updateEmployeeLASTNAME.getText().toString().trim();

        String query = "Select * From employee where FIRSTNAME = '" + type + "'";
        String query2 = "Select * From employee where LASTNAME = '" + type2 + "'";



       String a = updateEmployeeFIRSTNAME.getText().toString();
        String b = updateEmployeeLASTNAME.getText().toString();
        String c = updateEmployeeMOBILE.getText().toString();
        String d = updateEmployeeEMAIL.getText().toString();
        String e = updateEmployeeADDRESS.getText().toString();



        String queryFN = "Select * From employee where FIRSTNAME = '" + a + "'";
        String queryLN = "Select * From employee where LASTNAME = '" + b + "'";

        if (c.isEmpty() && d.isEmpty() && e.isEmpty()) {

            Toast.makeText(this, "Input Data First!", Toast.LENGTH_LONG).show();

        } else if (a.isEmpty()) {

            Toast.makeText(this, "Please Input First Name.", Toast.LENGTH_LONG).show();

        } else if (b.isEmpty()) {
            Toast.makeText(this, "Please Input Last Name.", Toast.LENGTH_LONG).show();


        }  else if (c.isEmpty()) {
            Toast.makeText(this, "Please Input Mobile Number.", Toast.LENGTH_LONG).show();


        }  else if (d.isEmpty()) {
            Toast.makeText(this, "Please Input Email Address.", Toast.LENGTH_LONG).show();


        }  else if (e.isEmpty()) {
            Toast.makeText(this, "Please Input Address.", Toast.LENGTH_LONG).show();


        }
        else {


            String employeeFIRSTNAME = updateEmployeeFIRSTNAME.getText().toString().trim().toUpperCase();
            String employeeLASTNAME = updateEmployeeLASTNAME.getText().toString().trim().toUpperCase();
            String employeeMOBILE = updateEmployeeMOBILE.getText().toString().trim();
            String employeeEMAIL = updateEmployeeEMAIL.getText().toString().trim();
            String employeeTYPE = updateEmployeeTYPE.getText().toString().trim();
            String employeeADDRESS = updateEmployeeADDRESS.getText().toString();


            mDatabase.execSQL("UPDATE  " + EmployeeContract.EmployeeEntry.TABLE_NAME + " SET FIRSTNAME ='" + employeeFIRSTNAME + "'," +
                    " LASTNAME ='" + employeeLASTNAME + "',"+" MOBILE = '"+employeeMOBILE+"',"+
                    " EMAIL = '"+employeeEMAIL+"',"+" EMPLOYEETYPE = '"+employeeTYPE+"',"+
                    " ADDRESS = '"+employeeADDRESS+"'WHERE _ID='" + uniqueID + "'");
            Toast.makeText(this, "Employee Successfully Updated!", Toast.LENGTH_LONG).show();


           finish();

        }


    }

}
