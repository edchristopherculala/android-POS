package com.ed.chris.androidpos.admin.updateclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceContract;
import com.ed.chris.androidpos.admin.admin_adapters.StocksContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class AddStocks extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;


    EditText edttxtPRODUCTNAME,edttxtPRODUCTCATEGORY,edttxtSUPPLIERNAME,edttxtQUANTITY;
    Button buttonAddSTOCKS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stocks);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();


        edttxtPRODUCTNAME = findViewById(R.id.edittxtPRODUCTNAME);
        edttxtPRODUCTCATEGORY = findViewById(R.id.edittxtPRODUCTCATEGORY);
        edttxtSUPPLIERNAME = findViewById(R.id.edittxtSUPPLIERNAME);
        edttxtQUANTITY = findViewById(R.id.edittxtQUANTITY);

        buttonAddSTOCKS = findViewById(R.id.btnAddProducts);


        Intent intent = getIntent();
        String ProductNAME = intent.getStringExtra("productname");
        String ProductCATEGORY = intent.getStringExtra("productcategory");
        String ProductSUPPLIER = intent.getStringExtra("productsuppliername");

        String UNIQUEID = intent.getStringExtra("ID");


        edttxtPRODUCTNAME.setText(ProductNAME);
        edttxtPRODUCTCATEGORY.setText(ProductCATEGORY);
        edttxtSUPPLIERNAME.setText(ProductSUPPLIER);

        edttxtPRODUCTNAME.setEnabled(false);
        edttxtPRODUCTCATEGORY.setEnabled(false);
        edttxtSUPPLIERNAME.setEnabled(false);




        buttonAddSTOCKS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addStocks();
            }
        });


    }

    private void addStocks(){
        Intent intent = getIntent();

        String ProductNAME = intent.getStringExtra("productname");
        String ProductCATEGORY = intent.getStringExtra("productcategory");
        String ProductSUPPLIER = intent.getStringExtra("productsuppliername");
        String DATEOFVALIDITY = intent.getStringExtra("dateofvalidity");
        String DATEOFEXPIRY = intent.getStringExtra("dateofexpiry");
        String BUYPRICE = intent.getStringExtra("buyprice");
        String SELLPRICE = intent.getStringExtra("sellprice");
        String UNIQUEID = intent.getStringExtra("ID");


        edttxtPRODUCTNAME = findViewById(R.id.edittxtPRODUCTNAME);
        edttxtPRODUCTCATEGORY = findViewById(R.id.edittxtPRODUCTCATEGORY);
        edttxtSUPPLIERNAME = findViewById(R.id.edittxtSUPPLIERNAME);
        edttxtQUANTITY = findViewById(R.id.edittxtQUANTITY);

        String PRODUCTNAME = edttxtPRODUCTNAME.getText().toString();
        String PRODUCTCATEGORY = edttxtPRODUCTCATEGORY.getText().toString();
        String PRODUCTSUPPLIERNAME = edttxtSUPPLIERNAME.getText().toString().toUpperCase();
        String PRODUCTQUANTITY = edttxtQUANTITY.getText().toString();

       if (PRODUCTQUANTITY.isEmpty() ){

            Toast.makeText(this,"Please input product quantity",Toast.LENGTH_LONG).show();
        }else if (PRODUCTQUANTITY.equals("0")){

           Toast.makeText(this,"Please input product quantity ",Toast.LENGTH_LONG).show();

       }
        else {

            String pname = edttxtPRODUCTNAME.getText().toString().trim().toUpperCase();
            String pcategory = edttxtPRODUCTCATEGORY.getText().toString().trim();
            String psupplier = edttxtSUPPLIERNAME.getText().toString().trim();
            String pquantity = edttxtQUANTITY.getText().toString().trim();


           ContentValues cv = new ContentValues();
           cv.put(StocksContract.StocksEntry.KEY_PRODUCTNAME, pname);
           cv.put(StocksContract.StocksEntry.KEY_PRODUCTCATEGORY, pcategory);
           cv.put(StocksContract.StocksEntry.KEY_PRODUCTSUPPLIERNAME, psupplier);
           cv.put(StocksContract.StocksEntry.KEY_PRODUCTQUANTITY, pquantity);
           cv.put(StocksContract.StocksEntry.KEY_PRODUCTID, UNIQUEID);



           mDatabase.insert(StocksContract.StocksEntry.TABLE_NAME, null, cv);


           ContentValues cv1 = new ContentValues();
           cv1.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY,pquantity);




           mDatabase.insert(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME, null, cv1);
           // mAdapter.swapCursor(getAllItems());



           Toast.makeText(this, "" + pquantity + " stocks was added to " +pname, Toast.LENGTH_SHORT).show();

           finish();

        }
    }
}
