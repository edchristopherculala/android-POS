package com.ed.chris.androidpos.admin.updateclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.SupplierCreateContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class SupplierUpdate extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    EditText updateSupplierADDRESS, updateSupplierEMAIL
            , updateSupplierTELEPHONE, suppliername;

    Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_update);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        buttonUpdate = findViewById(R.id.buttonUPDATESupplier);

        suppliername = findViewById(R.id.edttxtSuppliernameUpdate);
        updateSupplierADDRESS = findViewById(R.id.edttxtSupplierAddressupdate);
        updateSupplierEMAIL = findViewById(R.id.edttxtSupplierEmailupdate);
        updateSupplierTELEPHONE = findViewById(R.id.edttxtSupplierTelephoneupdate);



        Intent intent = getIntent();

        String sname = intent.getStringExtra("suppliername");
        String saddress = intent.getStringExtra("supplieraddress");
        String semail = intent.getStringExtra("supplieremail");
        String stelephone = intent.getStringExtra("suppliertelephone");
        String UNIQUEID = intent.getStringExtra("employeeID");


        suppliername.setText(sname);
        updateSupplierADDRESS.setText(saddress);
        updateSupplierEMAIL.setText(semail);
        updateSupplierTELEPHONE.setText(stelephone);

        suppliername.setEnabled(false);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });



    }

    private void updateItem() {

        Intent intent = getIntent();

        String sname = intent.getStringExtra("suppliername");
        String saddress = intent.getStringExtra("supplieraddress");
        String semail = intent.getStringExtra("supplieremail");
        String stelephone = intent.getStringExtra("suppliertelephone");
        String UNIQUEID = intent.getStringExtra("employeeID");




        String SUPPLIERNAME = suppliername.getText().toString();
        String SUPPLIERADDRESS = updateSupplierADDRESS.getText().toString();
        String SUPPLIEREMAIL = updateSupplierEMAIL.getText().toString().toUpperCase();
        String SUPPLIERTELEPHONE = updateSupplierTELEPHONE.getText().toString();


        if (SUPPLIERADDRESS.isEmpty() && SUPPLIEREMAIL.isEmpty() && SUPPLIERTELEPHONE.isEmpty()) {

            Toast.makeText(this, "Input Data First!", Toast.LENGTH_LONG).show();

        } else if (SUPPLIERADDRESS.isEmpty()) {

            Toast.makeText(this, "Please Input Supplier's Address.", Toast.LENGTH_LONG).show();

        } else if (SUPPLIEREMAIL.isEmpty()) {
            Toast.makeText(this, "Please Input Supplier's Email..", Toast.LENGTH_LONG).show();


        }  else if (SUPPLIERTELEPHONE.isEmpty()) {
            Toast.makeText(this, "Please Input Supplier's Contact Details..", Toast.LENGTH_LONG).show();


        }
        else {



            String suname = suppliername.getText().toString().trim().toUpperCase();
            String suaddress = updateSupplierADDRESS.getText().toString();
            String suemail = updateSupplierEMAIL.getText().toString().trim();
            String sutelephone = updateSupplierTELEPHONE.getText().toString();


            mDatabase.execSQL("UPDATE  " + SupplierCreateContract.SupplierAccountEntry.TABLE_NAME + " SET SUPPLIERADDRESS ='" + suaddress + "'," +
                    " SUPPLIEREMAIL ='" + suemail + "',"+" SUPPLIERTELEPHONE = '"+sutelephone+"' WHERE _ID='" + UNIQUEID + "'");
            Toast.makeText(this, "Supplier Successfully Updated!", Toast.LENGTH_LONG).show();


            finish();

        }


    }
}
