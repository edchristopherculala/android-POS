package com.ed.chris.androidpos.POS;

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
import com.ed.chris.androidpos.admin.admin_adapters.CartContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class UpdateQuantityActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    TextView productname;
    EditText edttxtquantity;
    ImageView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_quantity);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        Intent intent = getIntent();

        String productName = intent.getStringExtra("pname");
        String productQuantity = intent.getStringExtra("pquantity");
        String productID = intent.getStringExtra("pid");
        double productPrice = getIntent().getDoubleExtra("pprice", 0);
        String productTotalPrice = intent.getStringExtra("ptotal");
        String currentStocks = intent.getStringExtra("currentStocks");


        productname = findViewById(R.id.updateProductName);
        edttxtquantity = findViewById(R.id.edttextupdatequantity);

        confirm = findViewById(R.id.okayupdatequantity);

        productname.setText(productName);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();

            }
        });


    }

    private void updateItem() {


        Intent intent = getIntent();

        String productName = intent.getStringExtra("pname");
        String productQuantity = intent.getStringExtra("pquantity");
        String productID = intent.getStringExtra("pid");
        double productPrice = getIntent().getDoubleExtra("pprice", 0);
        String productTotalPrice = intent.getStringExtra("ptotal");
        String currentStocks = intent.getStringExtra("currentStocks");

        int CurrentStocks = Integer.parseInt(currentStocks);

        String quantityinput;
        int input;
        quantityinput = edttxtquantity.getText().toString();

        input = Integer.parseInt(quantityinput);


        if (quantityinput.isEmpty()) {

            Toast.makeText(this, "Please Input Quantity", Toast.LENGTH_LONG).show();

        } else if (quantityinput.equals("0") || quantityinput.equals("00") || quantityinput.equals("000")
                || quantityinput.equals("0000") || quantityinput.equals("00000") || quantityinput.equals("000000")
                || quantityinput.equals("0000000")) {
            Toast.makeText(this, "Please Input Quantity", Toast.LENGTH_LONG).show();

        } else if (input > CurrentStocks) {

            Toast.makeText(this, "Current stock of " + productName + " is " + currentStocks +
                    ". Please do not exceed.", Toast.LENGTH_LONG).show();

        } else {

            String newquantity = edttxtquantity.getText().toString();
            Integer intnewquantity = Integer.valueOf(newquantity);
           // double intnewProductPrice = productPrice;


            double intnewTotalPrice = intnewquantity * productPrice;

            String newTotalPrice = String.valueOf(intnewTotalPrice);

            String discount = "0";


            mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + quantityinput + "'," +
                    " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " PRODUCTDISCOUNT = '" + discount + "'  WHERE PRODUCTCODE='" + productID + "'");
            Toast.makeText(this, newTotalPrice, Toast.LENGTH_LONG).show();


            finish();
        }


    }
}
