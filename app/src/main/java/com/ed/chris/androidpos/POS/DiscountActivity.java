package com.ed.chris.androidpos.POS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.CartContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

public class DiscountActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    TextView txtdiscounttype;
    EditText edttxtdiscount;
    ImageView confirm;
    Spinner spinnerDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        spinnerDiscount = findViewById(R.id.spinnerDiscountType);
        txtdiscounttype = findViewById(R.id.txtviewdiscountTYPE);
        edttxtdiscount = findViewById(R.id.edttextdiscount);
        confirm = findViewById(R.id.okaydiscount);

        Intent intent = getIntent();
        String productName = intent.getStringExtra("pname");
        String productQuantity =intent.getStringExtra("pquantity");
        String productID =intent.getStringExtra("pid");
        double productPrice = getIntent().getDoubleExtra("pprice", 0);
        double productTotalPrice = getIntent().getDoubleExtra("ptotal", 0);

        int priceLength, realLength, lengthoftprice;
        String pricefromDouble,p;
        pricefromDouble = String.valueOf(productPrice);


        BigDecimal bd = new BigDecimal(productTotalPrice);
        p = String.valueOf(bd);

        priceLength = p.length() ;
        realLength = priceLength + 3;


        edttxtdiscount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(realLength) });

        Toast.makeText(this, p,Toast.LENGTH_LONG).show();
        //Add Spinner Items

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Value Discount");
        arrayList.add("Percentage Discount");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiscount.setAdapter(arrayAdapter);

        spinnerDiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // clicked item will be shown as spinner
                String productCATEGORY = parent.getItemAtPosition(position).toString();

                txtdiscounttype.setText(productCATEGORY);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveDiscount();
            }
        });

    }

    private void giveDiscount() {

        Intent intent = getIntent();
        String productName = intent.getStringExtra("pname");
        String productQuantity =intent.getStringExtra("pquantity");
        String productID =intent.getStringExtra("pid");
        double productPrice = getIntent().getDoubleExtra("pprice", 0);
        String productTotalPrice =intent.getStringExtra("ptotal");
        String productDiscount =intent.getStringExtra("pdiscount");

        String dtype = txtdiscounttype.getText().toString();
        String discount = edttxtdiscount.getText().toString().trim();

        if (dtype.equals("Value Discount")){

            if (discount.isEmpty()){

                double multipliedAns, mQuant,mPrice;
                mQuant = Double.parseDouble(productQuantity);
                mPrice = productPrice;
                multipliedAns = mQuant * mPrice;
                String defaultval = "0";

                mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + defaultval + "'," +
                        " PRODUCTTOTALPRICE ='" + multipliedAns + "'," + " TOTALDISCOUNT ='" + defaultval + "'  WHERE PRODUCTCODE='" + productID + "'");
                Toast.makeText(this, "Discounted", Toast.LENGTH_LONG).show();

                finish();
                Toast.makeText(DiscountActivity.this,"empty",Toast.LENGTH_LONG).show();
            }else{

                double Idiscount = Double.parseDouble(discount);
                int  quantity1,  discountAnswer1;
                double price1,multiplied1;
                price1 = productPrice;
                quantity1 = Integer.parseInt(productQuantity);
                multiplied1 = price1 * quantity1;

                if (Idiscount > multiplied1){

                    Toast.makeText(this, "Inputted value must not be greater than total price..", Toast.LENGTH_LONG).show();

                }else if(Idiscount == 0){

                    //check muna to

                    double multipliedAns, mQuant,mPrice;
                    mQuant = Double.parseDouble(productQuantity);
                    mPrice = productPrice;
                    multipliedAns = mQuant * mPrice;
                    String defaultval = "0";

                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + defaultval + "'," +
                            " PRODUCTTOTALPRICE ='" + multipliedAns + "'," + " TOTALDISCOUNT ='" + defaultval + "'  WHERE PRODUCTCODE='" + productID + "'");
                    Toast.makeText(this, "Discounted", Toast.LENGTH_LONG).show();

                    finish();
                } else{
                    //get the real total price from quantity * price
                    int  quantity;
                    double price, multiplied,discountAnswer,discountprice;
                    price = productPrice;
                    quantity = Integer.parseInt(productQuantity);
                    multiplied = price * quantity;
                    int totalprice, answerminus;

                    discountprice = Double.parseDouble(discount);
                    //totalprice = Integer.parseInt(productTotalPrice);

                    discountAnswer = multiplied - discountprice;
                    String discountedValue = String.valueOf(discountAnswer);
                    String totaldiscounted = String.valueOf(discountprice);
                    String result1 = ("-" +discount );

                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + result1 + "'," +
                            " PRODUCTTOTALPRICE ='" + discountedValue + "'," + " TOTALDISCOUNT ='" + totaldiscounted + "'  WHERE PRODUCTCODE='" + productID + "'");
                    Toast.makeText(this, "Discounted", Toast.LENGTH_LONG).show();

                    finish();

                }


            }

        }else if (dtype.equals("Percentage Discount")){

            if (discount.isEmpty()){

                double multipliedAns, mQuant,mPrice;
                mQuant = Double.parseDouble(productQuantity);
                mPrice = productPrice;
                multipliedAns = mQuant * mPrice;
                String defaultval = "0";

                mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + defaultval + "'," +
                        " PRODUCTTOTALPRICE ='" + multipliedAns + "'," + " TOTALDISCOUNT ='" + defaultval + "'  WHERE PRODUCTCODE='" + productID + "'");
                Toast.makeText(this, "Discounted", Toast.LENGTH_LONG).show();

                finish();

                Toast.makeText(DiscountActivity.this,"empty",Toast.LENGTH_LONG).show();
            }else {

                double Idiscount = Double.parseDouble(discount);
                if(Idiscount > 100){
                    Toast.makeText(this, "Input a percentage value.", Toast.LENGTH_LONG).show();
                }else if (Idiscount == 0){

                    double multipliedAns, mQuant,mPrice;
                    mQuant = Double.parseDouble(productQuantity);
                    mPrice = productPrice;
                    multipliedAns = mQuant * mPrice;
                    String defaultval = "0";

                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + defaultval + "'," +
                            " PRODUCTTOTALPRICE ='" + multipliedAns + "'," + " TOTALDISCOUNT ='" + defaultval + "'  WHERE PRODUCTCODE='" + productID + "'");
                    Toast.makeText(this, "Discounted", Toast.LENGTH_LONG).show();

                    finish();
                }
                else{

                    String discountinput = edttxtdiscount.getText().toString().trim();
                    double inputtedtoint = Double.parseDouble(discountinput);
                    double percent = 100 / inputtedtoint;
                    int quantity;
                    double price, multiplyanswer;
                    price = productPrice;
                    quantity = Integer.parseInt(productQuantity);
                    multiplyanswer = price * quantity;
                    double totaldeduction = multiplyanswer * percent;
                    double realanswer = multiplyanswer - totaldeduction;
                    String answer = String.valueOf(percent);
                    String answerr = ("%" +discount);
                    double amount = Double.parseDouble(edttxtdiscount.getText().toString());
                    double res = (amount / 100.0f);

                    double totaldeduction1 = res * multiplyanswer;

                    double answer5 = multiplyanswer - totaldeduction1;

                    String show = String.valueOf(totaldeduction1);

                    String finaltotal = String.valueOf(answer5);
                    String amountpercentage = ("%" +amount);

                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + amountpercentage + "'," +
                            " PRODUCTTOTALPRICE ='" + finaltotal + "'," + " TOTALDISCOUNT ='" + show + "'  WHERE PRODUCTCODE='" + productID + "'");
                    Toast.makeText(this, show, Toast.LENGTH_LONG).show();

                    finish();

                }

            }

        }

    }
}
