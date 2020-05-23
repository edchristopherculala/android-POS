package com.ed.chris.androidpos.admin.updateclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.SupplierCreateContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class SupplierCreate extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private EmployeeTypeAdapter mAdapter;
    DatabaseHelper myDb;


    TextView txtviewSupplierCode;
    EditText editTextSupplierName,editTextSupplierAddress,editTextSupplierEmail,editTextSupplierTelephone;
    Button buttonAddSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_create);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();


        txtviewSupplierCode = findViewById(R.id.edittxtsupplierCode);

        editTextSupplierName = findViewById(R.id.edittxtsupplierName);
        editTextSupplierAddress = findViewById(R.id.edittxtsupplierAddress);
        editTextSupplierEmail = findViewById(R.id.edittxtsupplierEmail);
        editTextSupplierTelephone = findViewById(R.id.edittxtsupplierTelephone);

        buttonAddSupplier = findViewById(R.id.btnAddSUPPLIER);

        buttonAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });


        //Increment Value from database

        String count = "SELECT count(*) FROM supplier";
        Cursor mcursor = mDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        String a = String.valueOf(icount);

       // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

        if(icount>0){

            //select max may DATA

           // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

            String count1 = "SELECT _ID FROM supplier ORDER BY _ID DESC";
            Cursor mcursor1 = mDatabase.rawQuery(count1, null);
            mcursor1.moveToFirst();
            int icount1 = mcursor1.getInt(0);

           // Toast.makeText(this,"MAY DATA" +icount1,Toast.LENGTH_LONG).show();

            int newID = icount1 + 1;
            String res = String.valueOf(icount1);

           //Toast.makeText(this,res,Toast.LENGTH_LONG).show();

            txtviewSupplierCode.setText("S-" +newID);



        }else {


            //WORKING CODE
           // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

            int defaultID = icount + 1;

            txtviewSupplierCode.setText("S-" +defaultID);





        }





    }

    private void addItem(){


        String SUPPLIERCODE = txtviewSupplierCode.getText().toString().toUpperCase();
        String SUPPLIERNAME = editTextSupplierName.getText().toString().toUpperCase();
        String SUPPLIERADDRESS = editTextSupplierAddress.getText().toString();
        String SUPPLIEREMAIL = editTextSupplierEmail.getText().toString().trim();
        String SUPPLIERTELEPHONE = editTextSupplierTelephone.getText().toString().trim();




        if (SUPPLIERNAME.isEmpty() && SUPPLIERADDRESS.isEmpty() && SUPPLIEREMAIL.isEmpty()
                && SUPPLIERTELEPHONE.isEmpty()){

            Toast.makeText(this,"Please Input Supplier Data!",Toast.LENGTH_LONG).show();

        }else if (SUPPLIERNAME.isEmpty() ){

            Toast.makeText(this,"Please Input Supplier Name!",Toast.LENGTH_LONG).show();

        }else if (SUPPLIERADDRESS.isEmpty() ){

            Toast.makeText(this,"Please Input Supplier Address!",Toast.LENGTH_LONG).show();
        }else if (SUPPLIEREMAIL.isEmpty() ){

            Toast.makeText(this,"Please Input Supplier Email!",Toast.LENGTH_LONG).show();
        }else if (SUPPLIERTELEPHONE.isEmpty() ){

            Toast.makeText(this,"Please Input Supplier Telephone!",Toast.LENGTH_LONG).show();
        }
        else {

            String typeSupplierCode = txtviewSupplierCode.getText().toString();
            String typeSupplierName = editTextSupplierName.getText().toString();
            String typeSupplierAddress = editTextSupplierAddress.getText().toString();
            String typeSupplierEmail = editTextSupplierEmail.getText().toString();
            String typeSupplierTelephone = editTextSupplierTelephone.getText().toString().trim();


            String querySNAME = "Select * From supplier where SUPPLIERNAME = '" + typeSupplierName + "'";
            String querySEMAIL = "Select * From supplier where SUPPLIEREMAIL = '" + typeSupplierEmail + "'";
            if (mDatabase.rawQuery(querySNAME, null).getCount() > 0  && mDatabase.rawQuery(querySEMAIL,
                    null).getCount() > 0 ) {
                Toast.makeText(this, "" + typeSupplierName + "  is already registered in the system!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues cv = new ContentValues();
                cv.put(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_CODE, SUPPLIERCODE);
                cv.put(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_NAME, SUPPLIERNAME);
                cv.put(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_ADDRESS, SUPPLIERADDRESS);
                cv.put(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_EMAIL, SUPPLIEREMAIL);
                cv.put(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_TELEPHONE, SUPPLIERTELEPHONE);



                mDatabase.insert(SupplierCreateContract.SupplierAccountEntry.TABLE_NAME, null, cv);
                // mAdapter.swapCursor(getAllItems());


                Toast.makeText(this, "" + typeSupplierName + " is now Registered as your Supplier!", Toast.LENGTH_SHORT).show();


                editTextSupplierName.getText().clear();
                editTextSupplierTelephone.getText().clear();
                editTextSupplierEmail.getText().clear();
                editTextSupplierAddress.getText().clear();



                String count = "SELECT count(*) FROM supplier";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);
                if(icount>0) {

                    //select max may DATA

                    // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                    String count1 = "SELECT _ID FROM supplier ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);
                    String res = String.valueOf(icount1);


                    int newID = icount1 + 1;



                    //Toast.makeText(this,res+""+newID,Toast.LENGTH_LONG).show();




                   // Toast.makeText(this, res, Toast.LENGTH_LONG).show();

                    txtviewSupplierCode.setText("S-" + newID);


                    finish();
                }

            }


        }


    }

}
