package com.ed.chris.androidpos.admin.admin_adapters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class Add_Stocks_Dialog extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    TextView productname;
    EditText edttxtquantity;
    ImageView confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__stocks__dialog);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        Intent intent = getIntent();

        String productName, productQuantity, productID;

        productName = intent.getStringExtra("pname");
        productQuantity = intent.getStringExtra("pquantity");
        productID = intent.getStringExtra("pid");

        productname = findViewById(R.id.updateProductName);
        edttxtquantity = findViewById(R.id.edttextupdatestocks);
        confirm = findViewById(R.id.okayupdatequantityQ);
        productname.setText(productName);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();

            }
        });


    }

    private void updateItem() {

        String productID, productPrice, currentStocks, productNAME, quantityinput;
        int CurrentStocks, input;

        Intent intent = getIntent();

        productID = intent.getStringExtra("pid");
        productPrice = intent.getStringExtra("pprice");
        currentStocks = intent.getStringExtra("pquantity");
        productNAME = intent.getStringExtra("pname");

        CurrentStocks = Integer.parseInt(currentStocks);

        quantityinput = edttxtquantity.getText().toString();
        input = Integer.parseInt(quantityinput);

        if (quantityinput.isEmpty()) {

            Toast.makeText(this, "Please Input Quantity", Toast.LENGTH_LONG).show();

        } else if (quantityinput.equals("0") || quantityinput.equals("00") || quantityinput.equals("000")
                || quantityinput.equals("0000") || quantityinput.equals("00000") || quantityinput.equals("000000")
                || quantityinput.equals("0000000")) {
            Toast.makeText(this, "Please Input Quantity", Toast.LENGTH_LONG).show();

        } else {
            String newStocks, updatedStocks, discount;
            Integer intnewStocks, currentQuantity, intnewQuantityProduct;
            newStocks = edttxtquantity.getText().toString();

            intnewStocks = Integer.valueOf(newStocks);
            currentQuantity = Integer.parseInt(currentStocks);
            intnewQuantityProduct = currentQuantity + intnewStocks;
            updatedStocks = String.valueOf(intnewQuantityProduct);

            discount = "0";

            mDatabase.execSQL("UPDATE  " + ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + updatedStocks + "' WHERE PRODUCTCODE='" + productID + "'");

            Toast.makeText(this, "Successfully added stocks to " + productNAME, Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}
