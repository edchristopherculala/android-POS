package com.ed.chris.androidpos.POS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.util.ArrayList;

public class TransactionalDiscountActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;


    TextView txtviewDiscountType;
    Spinner SpinnerDiscountType;
    EditText edttxtDiscount;
    ImageView confirmDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactional_discount);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        SpinnerDiscountType = findViewById(R.id.spinnerDiscountType1);
        txtviewDiscountType = findViewById(R.id.txtviewdiscountTYPE1);
        edttxtDiscount = findViewById(R.id.edttextdiscount1);
        confirmDiscount = findViewById(R.id.okaydiscount1);


        Intent intent = getIntent();
        String transactionTotalPrice = intent.getStringExtra("mainTotal");

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Value Discount");
        arrayList.add("Percentage Discount");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerDiscountType.setAdapter(arrayAdapter);

        SpinnerDiscountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // clicked item will be shown as spinner
                String productCATEGORY = parent.getItemAtPosition(position).toString();

                txtviewDiscountType.setText(productCATEGORY);



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        confirmDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveDiscount();
            }
        });


    }

    private void giveDiscount() {


        Intent intent = getIntent();
        String transactionTotalPrice = intent.getStringExtra("mainTotal");



        String dtype = txtviewDiscountType.getText().toString();
        String discount = edttxtDiscount.getText().toString().trim();





        if (dtype.equals("Value Discount")){


            if (discount.isEmpty()){



                finish();


            }else{

                Double inputtedDiscount, totalPrice;

                inputtedDiscount = Double.valueOf(discount);
                totalPrice = Double.valueOf(transactionTotalPrice);

               /* Integer Idiscount = Integer.valueOf(discount);
                int price1, quantity1, multiplied1, discountAnswer1;

                price1 = Integer.valueOf(productPrice);
                quantity1 = Integer.valueOf(productQuantity);

                multiplied1 = price1 * quantity1;  */





                if (inputtedDiscount > totalPrice){


                    Toast.makeText(this, "Inputted value must not be greater than total price..", Toast.LENGTH_LONG).show();


                }else if(inputtedDiscount.equals(0.0)){

                       Toast.makeText(this, "Input value first", Toast.LENGTH_LONG).show();



                } else{




                    //get the real total price from quantity * price




                    Double newTotalPrice;

                    newTotalPrice = totalPrice - inputtedDiscount;



                  //  mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + result1 + "'," +
                    //        " PRODUCTTOTALPRICE ='" + resultMinus + "'," + " TOTALDISCOUNT ='" + inputtedDiscount + "'  WHERE PRODUCTCODE='" + productID + "'");


                    Intent intentdiscount = new Intent(TransactionalDiscountActivity.this, PaymentDialog.class);
                    intent.putExtra("newtotalprice", newTotalPrice);
                    intent.putExtra("newdiscount", inputtedDiscount);
                    intent.putExtra("previoustotalprice", totalPrice);
                    startActivity(intentdiscount);

                    Toast.makeText(this, "Discounted", Toast.LENGTH_LONG).show();


                    finish();

                }


            }








        }else if (dtype.equals("Percentage Discount")){

            /*

            if (discount.isEmpty()){

                Double multipliedAns, mQuant,mPrice;

               // mQuant = Double.valueOf(productQuantity);
               // mPrice = Double.valueOf(productPrice);

                //multipliedAns = mQuant * mPrice;

                String defaultval = "0";


                mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + defaultval + "'," +
                        " PRODUCTTOTALPRICE ='" + multipliedAns + "'," + " TOTALDISCOUNT ='" + defaultval + "'  WHERE PRODUCTCODE='" + productID + "'");
                Toast.makeText(this, "Discounted", Toast.LENGTH_LONG).show();


                finish();


                Toast.makeText(DiscountActivity.this,"empty",Toast.LENGTH_LONG).show();
            }else {

                Integer Idiscount = Integer.valueOf(discount);

                if(Idiscount > 100){

                    Toast.makeText(this, "Input a percentage value.", Toast.LENGTH_LONG).show();
                }else if (Idiscount.equals(0)){

                    Double multipliedAns, mQuant,mPrice;

                    mQuant = Double.valueOf(productQuantity);
                    mPrice = Double.valueOf(productPrice);

                    multipliedAns = mQuant * mPrice;

                    String defaultval = "0";


                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTDISCOUNT ='" + defaultval + "'," +
                            " PRODUCTTOTALPRICE ='" + multipliedAns + "'," + " TOTALDISCOUNT ='" + defaultval + "'  WHERE PRODUCTCODE='" + productID + "'");
                    Toast.makeText(this, "Discounted", Toast.LENGTH_LONG).show();


                    finish();


                }
                else{

                    String discountinput = edttxtdiscount.getText().toString().trim();
                    double inputtedtoint = Integer.valueOf(discountinput);
                    double percent = 100 / inputtedtoint;

                    int price, quantity, multiplyanswer;

                    price = Integer.valueOf(productPrice);
                    quantity = Integer.valueOf(productQuantity);

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
 */

        }





    }
}
