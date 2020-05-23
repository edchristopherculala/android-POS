package com.ed.chris.androidpos.POS;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.CartAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.CartContract;
import com.ed.chris.androidpos.admin.admin_adapters.Pos_Product_Adapter;
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceContract;
import com.ed.chris.androidpos.database.DatabaseHelper;
import com.steelkiwi.library.view.BadgeHolderLayout;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PosMain extends Fragment {

    private SQLiteDatabase mDatabase;
    private Pos_Product_Adapter mAdapter;
    private CartAdapter mCartAdapter;


    private BadgeHolderLayout badgeCart;
    DatabaseHelper myDb;
    ;

    List<String> ProductCategoryList = new ArrayList<>();
    ArrayAdapter<String> Categoryadapter;
    TextView txtproductCategory, txtsubtotal, txtdiscount, txttotalprice;
    Spinner allProductCategory;
    ImageView allProducts, cart, transactionalDiscount;

    RelativeLayout ItemsHolder;
    LinearLayout transactionalDiscountHolder;

    private ImageView discard, payment;


    //transactional layout codes

    TextView txtviewDiscountType;
    Spinner SpinnerDiscountType;
    TextView edttxtDiscount;
    ImageView confirmDiscount;

    Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, buttonClear;


    boolean isTransactionalDiscount;

    public PosMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_pos_main, container, false);

        myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();


        isTransactionalDiscount = false;
        allProductCategory = v.findViewById(R.id.spinnerPRODUCTCATEGORY);
        txtproductCategory = v.findViewById(R.id.txtviewproductCategory);
        discard = v.findViewById(R.id.discardcart);
        payment = v.findViewById(R.id.paymentcart);

        transactionalDiscountHolder = v.findViewById(R.id.transactionaldiscountHolder);
        ItemsHolder = v.findViewById(R.id.firstHoldeR);

        SpinnerDiscountType = v.findViewById(R.id.spinnerDiscountType1);
        txtviewDiscountType = v.findViewById(R.id.txtviewdiscountTYPE1);
        edttxtDiscount = v.findViewById(R.id.edttextdiscount1);
        confirmDiscount = v.findViewById(R.id.okaydiscount1);

        button0 = (Button) v.findViewById(R.id.button0);
        button1 = (Button) v.findViewById(R.id.button1);
        button2 = (Button) v.findViewById(R.id.button2);
        button3 = (Button) v.findViewById(R.id.button3);
        button4 = (Button) v.findViewById(R.id.button4);
        button5 = (Button) v.findViewById(R.id.button5);
        button6 = (Button) v.findViewById(R.id.button6);
        button7 = (Button) v.findViewById(R.id.button7);
        button8 = (Button) v.findViewById(R.id.button8);
        button9 = (Button) v.findViewById(R.id.button9);
        buttonClear = (Button) v.findViewById(R.id.buttondelete);

        //keypad();

        txtsubtotal = v.findViewById(R.id.txtsubtotalPOS);
        txtdiscount = v.findViewById(R.id.txtdiscountPOS);
        txttotalprice = v.findViewById(R.id.txttotalpricePOS);

        cart = v.findViewById(R.id.buttoncart);


        //for cart badge , subtotal,discount and Total
        final RecyclerView recyclerview = v.findViewById(R.id.rvMain);

        // recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new Pos_Product_Adapter(getActivity(), getAllItems());

        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerview.setAdapter(mAdapter);


        badgeCart = v.findViewById(R.id.cartbadge);


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String countP = "SELECT count(*) FROM currencysettings";
                Cursor mcursorP = mDatabase.rawQuery(countP, null);
                mcursorP.moveToFirst();
                int icountP = mcursorP.getInt(0);


                if (icountP > 0) {

                    String currency1 = "SELECT CURRENCY FROM currencysettings";
                    Cursor mcursor1 = mDatabase.rawQuery(currency1, null);
                    mcursor1.moveToFirst();
                    String thisCurrency = mcursor1.getString(0);

                    if (thisCurrency.equals("Philippine Peso")) {
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));
                        //for total
                        String count = "SELECT sum(PRODUCTQUANTITY) FROM cart";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);
                        String a = String.valueOf(icount);

                        badgeCart.setCount(icount);

                      /*  //Subtotal

                        String countSubtotal = "SELECT sum(PRODUCTTOTALPRICE) FROM cart";
                        Cursor cursorsubtotal = mDatabase.rawQuery(countSubtotal, null);
                        cursorsubtotal.moveToFirst();
                        int countsubtotal = cursorsubtotal.getInt(0);

                        String subtotal11 = String.valueOf(countsubtotal);


                        //Discount

                        String countDiscount = "SELECT sum(TOTALDISCOUNT) FROM cart";
                        Cursor cursorDiscount = mDatabase.rawQuery(countDiscount, null);
                        cursorDiscount.moveToFirst();
                        int countDiscount1 = cursorDiscount.getInt(0);

                        String Discount11 = String.valueOf(countDiscount1);

                        //add validation if discount is zero
                        txtdiscount.setText(format.format(countDiscount1));

                        //Totalprice

                        String countTotalprice = "SELECT sum(PRODUCTTOTALPRICE) FROM cart";
                        Cursor cursorTotalprice = mDatabase.rawQuery(countTotalprice, null);
                        cursorTotalprice.moveToFirst();
                        int countTotalprice1 = cursorTotalprice.getInt(0);

                        String Totalprice11 = String.valueOf(countTotalprice1);

                        //add validation if discount is zero
                        txttotalprice.setText(format.format(countTotalprice1));


                        //forsubtotal

                        Double totalprice1, totaldiscount1, totalsubtotal;

                        totalprice1 = Double.valueOf(Totalprice11);
                        totaldiscount1 = Double.valueOf(Discount11);

                        totalsubtotal = totalprice1 + totaldiscount1;

                        final String converttotalsubtotal = String.valueOf(totalsubtotal);

                        txtsubtotal.setText(format.format(totalsubtotal));  */

                        //for total
                        String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                        mcursor2.moveToFirst();
                        Double icount2 = mcursor2.getDouble(0);

                        final String con = String.valueOf(icount2);
                        txttotalprice.setText(format.format(icount2));

                        //for discount
                        String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        Double icount3 = mcursor3.getDouble(0);

                        final String con1 = String.valueOf(icount3);
                        txtdiscount.setText(format.format(icount3));


                        //for subtotal

                        Double totalprice1, totaldiscount1, totalsubtotal;

                        totalprice1 = Double.valueOf(con);
                        totaldiscount1 = Double.valueOf(con1);

                        totalsubtotal = totalprice1 + totaldiscount1;

                        final String converttotalsubtotal = String.valueOf(totalsubtotal);

                        txtsubtotal.setText(format.format(totalsubtotal));


                    } else if (thisCurrency.equals("Canadian Dollars")) {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        format.setCurrency(Currency.getInstance(Locale.CANADA));

                        String count = "SELECT sum(PRODUCTQUANTITY) FROM cart";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);
                        String a = String.valueOf(icount);

                        badgeCart.setCount(icount);

                        //for total
                        String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                        mcursor2.moveToFirst();
                        Double icount2 = mcursor2.getDouble(0);

                        final String con = String.valueOf(icount2);
                        txttotalprice.setText(format.format(icount2));

                        //for discount
                        String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        Double icount3 = mcursor3.getDouble(0);

                        final String con1 = String.valueOf(icount3);
                        txtdiscount.setText(format.format(icount3));


                        //for subtotal

                        Double totalprice1, totaldiscount1, totalsubtotal;

                        totalprice1 = Double.valueOf(con);
                        totaldiscount1 = Double.valueOf(con1);

                        totalsubtotal = totalprice1 + totaldiscount1;

                        final String converttotalsubtotal = String.valueOf(totalsubtotal);

                        txtsubtotal.setText(format.format(totalsubtotal));


                    } else if (thisCurrency.equals("Japanese Yen")) {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        format.setCurrency(Currency.getInstance(Locale.JAPAN));
                        String count = "SELECT sum(PRODUCTQUANTITY) FROM cart";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);
                        String a = String.valueOf(icount);

                        badgeCart.setCount(icount);

                        //for total
                        String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                        mcursor2.moveToFirst();
                        Double icount2 = mcursor2.getDouble(0);

                        final String con = String.valueOf(icount2);
                        txttotalprice.setText(format.format(icount2));

                        //for discount
                        String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        Double icount3 = mcursor3.getDouble(0);

                        final String con1 = String.valueOf(icount3);
                        txtdiscount.setText(format.format(icount3));


                        //for subtotal

                        Double totalprice1, totaldiscount1, totalsubtotal;

                        totalprice1 = Double.valueOf(con);
                        totaldiscount1 = Double.valueOf(con1);

                        totalsubtotal = totalprice1 + totaldiscount1;

                        final String converttotalsubtotal = String.valueOf(totalsubtotal);

                        txtsubtotal.setText(format.format(totalsubtotal));


                    } else if (thisCurrency.equals("US Dollars")) {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        format.setCurrency(Currency.getInstance(Locale.US));
                        String count = "SELECT sum(PRODUCTQUANTITY) FROM cart";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);
                        String a = String.valueOf(icount);

                        badgeCart.setCount(icount);

                        //for total
                        String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                        mcursor2.moveToFirst();
                        Double icount2 = mcursor2.getDouble(0);

                        final String con = String.valueOf(icount2);
                        txttotalprice.setText(format.format(icount2));

                        //for discount
                        String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        Double icount3 = mcursor3.getDouble(0);

                        final String con1 = String.valueOf(icount3);
                        txtdiscount.setText(format.format(icount3));


                        //for subtotal

                        Double totalprice1, totaldiscount1, totalsubtotal;

                        totalprice1 = Double.valueOf(con);
                        totaldiscount1 = Double.valueOf(con1);

                        totalsubtotal = totalprice1 + totaldiscount1;

                        final String converttotalsubtotal = String.valueOf(totalsubtotal);

                        txtsubtotal.setText(format.format(totalsubtotal));


                    } else if (thisCurrency.equals("Chinese Yuan")) {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        format.setCurrency(Currency.getInstance(Locale.CHINA));
                        String count = "SELECT sum(PRODUCTQUANTITY) FROM cart";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);
                        String a = String.valueOf(icount);

                        badgeCart.setCount(icount);

                        //for total
                        String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                        mcursor2.moveToFirst();
                        Double icount2 = mcursor2.getDouble(0);

                        final String con = String.valueOf(icount2);
                        txttotalprice.setText(format.format(icount2));

                        //for discount
                        String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        Double icount3 = mcursor3.getDouble(0);

                        final String con1 = String.valueOf(icount3);
                        txtdiscount.setText(format.format(icount3));


                        //for subtotal

                        Double totalprice1, totaldiscount1, totalsubtotal;

                        totalprice1 = Double.valueOf(con);
                        totaldiscount1 = Double.valueOf(con1);

                        totalsubtotal = totalprice1 + totaldiscount1;

                        final String converttotalsubtotal = String.valueOf(totalsubtotal);

                        txtsubtotal.setText(format.format(totalsubtotal));


                    } else if (thisCurrency.equals("France Euro")) {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        format.setCurrency(Currency.getInstance(Locale.FRANCE));
                        String count = "SELECT sum(PRODUCTQUANTITY) FROM cart";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);
                        String a = String.valueOf(icount);

                        badgeCart.setCount(icount);

                        //for total
                        String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                        mcursor2.moveToFirst();
                        Double icount2 = mcursor2.getDouble(0);

                        final String con = String.valueOf(icount2);
                        txttotalprice.setText(format.format(icount2));

                        //for discount
                        String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        Double icount3 = mcursor3.getDouble(0);

                        final String con1 = String.valueOf(icount3);
                        txtdiscount.setText(format.format(icount3));


                        //for subtotal

                        Double totalprice1, totaldiscount1, totalsubtotal;

                        totalprice1 = Double.valueOf(con);
                        totaldiscount1 = Double.valueOf(con1);

                        totalsubtotal = totalprice1 + totaldiscount1;

                        final String converttotalsubtotal = String.valueOf(totalsubtotal);

                        txtsubtotal.setText(format.format(totalsubtotal));

                    } else if (thisCurrency.equals("Korea Won")) {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        format.setCurrency(Currency.getInstance(Locale.KOREA));
                        String count = "SELECT sum(PRODUCTQUANTITY) FROM cart";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);
                        String a = String.valueOf(icount);

                        badgeCart.setCount(icount);

                        //for total
                        String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                        mcursor2.moveToFirst();
                        Double icount2 = mcursor2.getDouble(0);

                        final String con = String.valueOf(icount2);
                        txttotalprice.setText(format.format(icount2));

                        //for discount
                        String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        Double icount3 = mcursor3.getDouble(0);

                        final String con1 = String.valueOf(icount3);
                        txtdiscount.setText(format.format(icount3));


                        //for subtotal

                        Double totalprice1, totaldiscount1, totalsubtotal;

                        totalprice1 = Double.valueOf(con);
                        totaldiscount1 = Double.valueOf(con1);

                        totalsubtotal = totalprice1 + totaldiscount1;

                        final String converttotalsubtotal = String.valueOf(totalsubtotal);

                        txtsubtotal.setText(format.format(totalsubtotal));


                    }

                } else if (icountP == 0) {

                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                    String count = "SELECT sum(PRODUCTQUANTITY) FROM cart";
                    Cursor mcursor = mDatabase.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);
                    String a = String.valueOf(icount);

                    badgeCart.setCount(icount);

                    //for total
                    String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                    Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                    mcursor2.moveToFirst();
                    Double icount2 = mcursor2.getDouble(0);

                    final String con = String.valueOf(icount2);
                    txttotalprice.setText(format.format(icount2));

                    //for discount
                    String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                    Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                    mcursor3.moveToFirst();
                    Double icount3 = mcursor3.getDouble(0);

                    final String con1 = String.valueOf(icount3);
                    txtdiscount.setText(format.format(icount3));


                    //for subtotal

                    Double totalprice1, totaldiscount1, totalsubtotal;

                    totalprice1 = Double.valueOf(con);
                    totaldiscount1 = Double.valueOf(con1);

                    totalsubtotal = totalprice1 + totaldiscount1;

                    final String converttotalsubtotal = String.valueOf(totalsubtotal);

                    txtsubtotal.setText(format.format(totalsubtotal));


                }

                handler.postDelayed(this, 500);


            }
        };

        handler.postDelayed(runnable, 500);


        prepareDataProductCategory();

        //recyclerview


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CartItemActivity.class);

                startActivity(intent);

            }
        });


        txtproductCategory.setText("");

        allProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String productCATEGORY = parent.getItemAtPosition(position).toString();

                txtproductCategory.setText(productCATEGORY);

                String whatProduct = txtproductCategory.getText().toString();

                if (whatProduct.equals("ALL")) {

                    mAdapter.swapCursor(getAllItems());

                } else {

                    String count3 = "Select * From products where PRODUCTCATEGORY = '" + whatProduct + "'";

                    if (mDatabase.rawQuery(count3, null).getCount() == 0) {
                        Toast.makeText(getActivity(), "No Data!", Toast.LENGTH_SHORT).show();

                        recyclerview.setVisibility(View.GONE);

                    } else {

                        recyclerview.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(), "Data Found!", Toast.LENGTH_SHORT).show();

                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        String icount3 = mcursor3.getString(3);


                        RecyclerView recyclerview = v.findViewById(R.id.rvMain);
                        mAdapter = new Pos_Product_Adapter(getActivity(), mcursor3);
                        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                        recyclerview.setAdapter(mAdapter);
                        mAdapter.swapCursor(mcursor3);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return v;
    }

    public void prepareDataProductCategory() {
        ProductCategoryList = myDb.getAllProductCategorywithALL();
        //adapter for spinner
        Categoryadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, ProductCategoryList);
        //attach adapter to spinner
        allProductCategory.setAdapter(Categoryadapter);
    }


    private Cursor getAllItems() {
        return mDatabase.query(
                ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProductMaintenanceContract.ProductMaintenanceEntry._ID + " DESC"
        );
    }

    private void giveDiscount() {

        String dtype = txtviewDiscountType.getText().toString();
        String discount = edttxtDiscount.getText().toString().trim();

        if (dtype.equals("Value Discount")) {


            if (discount.isEmpty()) {

                Toast.makeText(getActivity(), "Input value first", Toast.LENGTH_LONG).show();

            } else {

                //for total
                String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                mcursor2.moveToFirst();
                Double totalPrice = mcursor2.getDouble(0);

                //for discount
                String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                Double icount3 = mcursor3.getDouble(0);

                //for subtotal

                Double totalsubtotal = totalPrice + icount3;


                String finalTotalprice = txttotalprice.getText().toString();
                String finaltotalDiscount = txtdiscount.getText().toString();
                String FinalSubtotal = txtsubtotal.getText().toString();


                Double inputtedDiscount;

                inputtedDiscount = Double.valueOf(discount);


                if (inputtedDiscount > totalPrice) {


                    Toast.makeText(getActivity(), "Inputted value must not be greater than total price..", Toast.LENGTH_LONG).show();


                } else if (inputtedDiscount.equals(0.0)) {

                    Toast.makeText(getActivity(), "Input value first", Toast.LENGTH_LONG).show();


                } else {

                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));


                    Double newTotalPrice;

                    newTotalPrice = totalPrice - inputtedDiscount;

                    String convertednewTotalPrice;

                    convertednewTotalPrice = String.valueOf(newTotalPrice);


                    txtdiscount.setText(format.format(inputtedDiscount));
                    txttotalprice.setText(format.format(newTotalPrice));

                    transactionalDiscountHolder.setVisibility(View.GONE);
                    ItemsHolder.setVisibility(View.VISIBLE);


                }
            }

        } else if (dtype.equals("Percentage Discount")) {

            if (discount.isEmpty()) {

                Toast.makeText(getActivity(), "Input value first", Toast.LENGTH_LONG).show();

            } else {

                Integer Idiscount = Integer.valueOf(discount);

                if (Idiscount > 100 || Idiscount.equals(0)) {

                    Toast.makeText(getActivity(), "Input a percentage value.", Toast.LENGTH_LONG).show();
                } else {

                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));


                    Double inputtedDiscount;
                    inputtedDiscount = Double.valueOf(discount);

                    //for total
                    String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
                    Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                    mcursor2.moveToFirst();
                    Double totalPrice = mcursor2.getDouble(0);


                    double percentageinputteddiscount = inputtedDiscount / 100;
                    double finaldiscount = percentageinputteddiscount * totalPrice;
                    double finaltotalprice = totalPrice - finaldiscount;

                    txtdiscount.setText(format.format(finaldiscount));
                    txttotalprice.setText(format.format(finaltotalprice));

                    transactionalDiscountHolder.setVisibility(View.GONE);
                    ItemsHolder.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    public void transactionaldiscounttopayment() {

    }

    @Override
    public void onStart() {
        super.onStart();

        //  mCartAdapter.swapCursor(getCartItems());
    }

    @Override
    public void onResume() {
        super.onResume();

        mAdapter.swapCursor(getAllItems());


    }
}
