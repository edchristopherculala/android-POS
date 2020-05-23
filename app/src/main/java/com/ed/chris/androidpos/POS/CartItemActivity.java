package com.ed.chris.androidpos.POS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.CartAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.CartContract;
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceContract;
import com.ed.chris.androidpos.database.DatabaseHelper;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class CartItemActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private CartAdapter mAdapter;

    DatabaseHelper myDb;

    Button buttonAddProduct;

    private ImageView discard, payment;

    private TextView txtsubtotal, txtdiscount, txttotalprice, txtreceiptNUMBER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item);

        currencyChecker();

        }

    public String[] quantityPCart(String tablename) {
        try {
            SQLiteDatabase db = myDb.getReadableDatabase();
            Cursor x = db.rawQuery("SELECT * FROM "+tablename, null);
            int n=x.getCount();
            x.moveToFirst();
            String[] a=new String[n];int i=0;
            do
            {
                a[i]=x.getString(x.getColumnIndex("PRODUCTQUANTITY"));
                i++;
            } while(x.moveToNext());

            x.close();
            return a;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public String[] getProductID(String tablename)
    {
        try {
            SQLiteDatabase db = myDb.getReadableDatabase();
            Cursor x = db.rawQuery("SELECT * FROM "+tablename, null);
            int n=x.getCount();
            x.moveToFirst();
            String[] a=new String[n];int i=0;
            do
            {
                a[i]=x.getString(x.getColumnIndex("PRODUCTCODE"));
                i++;
            } while(x.moveToNext());

            x.close();
            return a;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String[] getCurrentPQuantity(String tablename)
    {
        try {
            SQLiteDatabase db = myDb.getReadableDatabase();
            Cursor x = db.rawQuery("SELECT * FROM "+tablename, null);
            int n=x.getCount();
            x.moveToFirst();
            String[] a=new String[n];int i=0;
            do
            {
                a[i]=x.getString(x.getColumnIndex("PRODUCTQUANTITY"));
                i++;
            } while(x.moveToNext());

            x.close();
            return a;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private Cursor getAllItems(){
        return mDatabase.query(
                CartContract.CartEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CartContract.CartEntry._ID + " DESC"
        );

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAdapter.swapCursor(getAllItems());
    }

    private void removeItem(long id){
        mDatabase.delete(CartContract.CartEntry.TABLE_NAME,
                CartContract.CartEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }


    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.swapCursor(getAllItems());

        currencyChecker();


      /*  NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

        //for total
        String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From cart";
        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
        mcursor2.moveToFirst();
        Double icount2 = mcursor2.getDouble(0);

        String con = String.valueOf(icount2);
        txttotalprice.setText(format.format(icount2));

        //for discount
        String count3 = "Select SUM(TOTALDISCOUNT) as Total From cart";
        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
        mcursor3.moveToFirst();
        Double icount3 = mcursor3.getDouble(0);

        String con1 = String.valueOf(icount3);
        txtdiscount.setText(format.format(icount3));


        //for subtotal

        Double totalprice1, totaldiscount1, totalsubtotal;

        totalprice1 = Double.valueOf(con);
        totaldiscount1 = Double.valueOf(con1);

        totalsubtotal = totalprice1 + totaldiscount1;

        String converttotalsubtotal = String.valueOf(totalsubtotal);

        txtsubtotal.setText(format.format(totalsubtotal));  */


    }


    private void currencyChecker() {

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();


        txtsubtotal = findViewById(R.id.txtsubtotal);
        txtdiscount = findViewById(R.id.txtdiscount);
        txttotalprice = findViewById(R.id.txttotalprice);


        RecyclerView recyclerview = findViewById(R.id.cartitem);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CartAdapter(this, getAllItems());
        recyclerview.setAdapter(mAdapter);

        discard = findViewById(R.id.discardcart);
        payment = findViewById(R.id.paymentcart);



        String countP = "SELECT count(*) FROM currencysettings";
        Cursor mcursorP = mDatabase.rawQuery(countP, null);
        mcursorP.moveToFirst();
        int icountP = mcursorP.getInt(0);


        if(icountP>0) {

            String currency1 = "SELECT CURRENCY FROM currencysettings";
            Cursor mcursorC = mDatabase.rawQuery(currency1, null);
            mcursorC.moveToFirst();
            String thisCurrency = mcursorC.getString(0);

            if (thisCurrency.equals("Philippine Peso")) {

                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

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


                //for receipt number

                String count = "SELECT count(*) FROM sales";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);

                // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                if(icount>0){

                    //select max may DATA

                    // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);

                    int newID = icount1 + 1;
                    String res = String.valueOf(icount1);

                }else {
                    int defaultID = icount + 1;

                }

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ITEM");
                        builder.setMessage("Are you sure you want to remove this in Cart?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem((long) viewHolder.itemView.getTag());
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }).attachToRecyclerView(recyclerview);




                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String TABLE_NAME_cart = "cart";


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ALL ITEMS");
                        builder.setMessage("Are you sure you want to remove all items?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase.execSQL("delete from "+ TABLE_NAME_cart);
                                        mAdapter.swapCursor(getAllItems());
                                        finish();

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });

                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fpSELLPRICE, fpTOTALPRICE;
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                        ArrayList<String> listReceipt = new ArrayList<String>();
                        String forReceipt = "SELECT * FROM cart";
                        Cursor cursor = mDatabase.rawQuery(forReceipt, null);
                        if (cursor.moveToFirst()) {

                            do {
                                String productNAME = cursor.getString(2);
                                String productQuantity = cursor.getString(3);
                                Double productSellPrice = cursor.getDouble(4);
                                Double productTotalPrice = cursor.getDouble(5);

                                fpSELLPRICE = format.format(productSellPrice);
                                fpTOTALPRICE = format.format(productTotalPrice);

                                String toPadproductNAME = productNAME;
                                String paddedproductNAME = String.format("%8s", toPadproductNAME);
                                String paddedproductQuantity = String.format("%7s", productQuantity);
                                String paddedproductSellPrice = String.format("%7s", fpSELLPRICE);
                                String paddedproductTotalPrice = String.format("%7s", fpTOTALPRICE);

                                if (paddedproductNAME.length() > 8 ){

                                    String finalproductname = paddedproductNAME.substring(0,6);

                                    String receipt = "" + finalproductname + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }else if (paddedproductQuantity.length() > 7 ){
                                    String finalquantity = paddedproductQuantity.substring(0,6);

                                    String receipt = "" + paddedproductNAME + "          " + finalquantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductSellPrice.length() > 7){
                                    String finalsellprice = paddedproductSellPrice.substring(0,6);


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + finalsellprice + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductTotalPrice.length() > 7){
                                    String finaltotalprice = paddedproductTotalPrice.substring(0,6);
                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + finaltotalprice + "";
                                    listReceipt.add(receipt);

                                }
                                else {


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);
                                }


                            } while (cursor.moveToNext());
                        }

                        int arrayCounter = listReceipt.size();
                        String a = String.valueOf(arrayCounter);

                        String joined = TextUtils.join("\n", listReceipt);
                        Toast.makeText(CartItemActivity.this,joined,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CartItemActivity.this, PaymentDialog.class);
                        intent.putExtra("subtotal",converttotalsubtotal);
                        intent.putExtra("discount",con1);
                        intent.putExtra("totalprice",con);
                        intent.putExtra("receipt",joined);
                        intent.putExtra("productID", getProductID("cart"));
                        intent.putExtra("productQuantity", quantityPCart("cart") );
                        startActivity(intent);

                    }
                });



            } else if (thisCurrency.equals("Canadian Dollars")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CANADA));

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


                //for receipt number

                String count = "SELECT count(*) FROM sales";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);

                // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                if(icount>0){

                    //select max may DATA

                    // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);

                    int newID = icount1 + 1;
                    String res = String.valueOf(icount1);

                }else {
                    int defaultID = icount + 1;

                }

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ITEM");
                        builder.setMessage("Are you sure you want to remove this in Cart?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem((long) viewHolder.itemView.getTag());
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }).attachToRecyclerView(recyclerview);




                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String TABLE_NAME_cart = "cart";


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ALL ITEMS");
                        builder.setMessage("Are you sure you want to remove all items?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase.execSQL("delete from "+ TABLE_NAME_cart);
                                        mAdapter.swapCursor(getAllItems());
                                        finish();

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });

                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fpSELLPRICE, fpTOTALPRICE;
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                        ArrayList<String> listReceipt = new ArrayList<String>();
                        String forReceipt = "SELECT * FROM cart";
                        Cursor cursor = mDatabase.rawQuery(forReceipt, null);
                        if (cursor.moveToFirst()) {

                            do {
                                String productNAME = cursor.getString(2);
                                String productQuantity = cursor.getString(3);
                                Double productSellPrice = cursor.getDouble(4);
                                Double productTotalPrice = cursor.getDouble(5);

                                fpSELLPRICE = format.format(productSellPrice);
                                fpTOTALPRICE = format.format(productTotalPrice);

                                String toPadproductNAME = productNAME;
                                String paddedproductNAME = String.format("%8s", toPadproductNAME);
                                String paddedproductQuantity = String.format("%7s", productQuantity);
                                String paddedproductSellPrice = String.format("%7s", fpSELLPRICE);
                                String paddedproductTotalPrice = String.format("%7s", fpTOTALPRICE);

                                if (paddedproductNAME.length() > 8 ){

                                    String finalproductname = paddedproductNAME.substring(0,6);

                                    String receipt = "" + finalproductname + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }else if (paddedproductQuantity.length() > 7 ){
                                    String finalquantity = paddedproductQuantity.substring(0,6);

                                    String receipt = "" + paddedproductNAME + "          " + finalquantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductSellPrice.length() > 7){
                                    String finalsellprice = paddedproductSellPrice.substring(0,6);


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + finalsellprice + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductTotalPrice.length() > 7){
                                    String finaltotalprice = paddedproductTotalPrice.substring(0,6);
                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + finaltotalprice + "";
                                    listReceipt.add(receipt);

                                }
                                else {


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);
                                }


                            } while (cursor.moveToNext());
                        }

                        int arrayCounter = listReceipt.size();
                        String a = String.valueOf(arrayCounter);

                        String joined = TextUtils.join("\n", listReceipt);
                        Toast.makeText(CartItemActivity.this,joined,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CartItemActivity.this, PaymentDialog.class);
                        intent.putExtra("subtotal",converttotalsubtotal);
                        intent.putExtra("discount",con1);
                        intent.putExtra("totalprice",con);
                        intent.putExtra("receipt",joined);
                        intent.putExtra("productID", getProductID("cart"));
                        intent.putExtra("productQuantity", quantityPCart("cart") );
                        startActivity(intent);

                    }
                });

            } else if (thisCurrency.equals("Japanese Yen")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.JAPAN));

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


                //for receipt number

                String count = "SELECT count(*) FROM sales";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);

                // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                if(icount>0){

                    //select max may DATA

                    // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);

                    int newID = icount1 + 1;
                    String res = String.valueOf(icount1);

                }else {
                    int defaultID = icount + 1;

                }

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ITEM");
                        builder.setMessage("Are you sure you want to remove this in Cart?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem((long) viewHolder.itemView.getTag());
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }).attachToRecyclerView(recyclerview);




                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String TABLE_NAME_cart = "cart";


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ALL ITEMS");
                        builder.setMessage("Are you sure you want to remove all items?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase.execSQL("delete from "+ TABLE_NAME_cart);
                                        mAdapter.swapCursor(getAllItems());
                                        finish();

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });

                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fpSELLPRICE, fpTOTALPRICE;
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                        ArrayList<String> listReceipt = new ArrayList<String>();
                        String forReceipt = "SELECT * FROM cart";
                        Cursor cursor = mDatabase.rawQuery(forReceipt, null);
                        if (cursor.moveToFirst()) {

                            do {
                                String productNAME = cursor.getString(2);
                                String productQuantity = cursor.getString(3);
                                Double productSellPrice = cursor.getDouble(4);
                                Double productTotalPrice = cursor.getDouble(5);

                                fpSELLPRICE = format.format(productSellPrice);
                                fpTOTALPRICE = format.format(productTotalPrice);

                                String toPadproductNAME = productNAME;
                                String paddedproductNAME = String.format("%8s", toPadproductNAME);
                                String paddedproductQuantity = String.format("%7s", productQuantity);
                                String paddedproductSellPrice = String.format("%7s", fpSELLPRICE);
                                String paddedproductTotalPrice = String.format("%7s", fpTOTALPRICE);

                                if (paddedproductNAME.length() > 8 ){

                                    String finalproductname = paddedproductNAME.substring(0,6);

                                    String receipt = "" + finalproductname + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }else if (paddedproductQuantity.length() > 7 ){
                                    String finalquantity = paddedproductQuantity.substring(0,6);

                                    String receipt = "" + paddedproductNAME + "          " + finalquantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductSellPrice.length() > 7){
                                    String finalsellprice = paddedproductSellPrice.substring(0,6);


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + finalsellprice + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductTotalPrice.length() > 7){
                                    String finaltotalprice = paddedproductTotalPrice.substring(0,6);
                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + finaltotalprice + "";
                                    listReceipt.add(receipt);

                                }
                                else {


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);
                                }


                            } while (cursor.moveToNext());
                        }

                        int arrayCounter = listReceipt.size();
                        String a = String.valueOf(arrayCounter);

                        String joined = TextUtils.join("\n", listReceipt);
                        Toast.makeText(CartItemActivity.this,joined,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CartItemActivity.this, PaymentDialog.class);
                        intent.putExtra("subtotal",converttotalsubtotal);
                        intent.putExtra("discount",con1);
                        intent.putExtra("totalprice",con);
                        intent.putExtra("receipt",joined);
                        intent.putExtra("productID", getProductID("cart"));
                        intent.putExtra("productQuantity", quantityPCart("cart") );
                        startActivity(intent);

                    }
                });

            } else if (thisCurrency.equals("US Dollars")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.US));

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


                //for receipt number

                String count = "SELECT count(*) FROM sales";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);

                // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                if(icount>0){

                    //select max may DATA

                    // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);

                    int newID = icount1 + 1;
                    String res = String.valueOf(icount1);

                }else {
                    int defaultID = icount + 1;

                }

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ITEM");
                        builder.setMessage("Are you sure you want to remove this in Cart?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem((long) viewHolder.itemView.getTag());
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }).attachToRecyclerView(recyclerview);




                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String TABLE_NAME_cart = "cart";


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ALL ITEMS");
                        builder.setMessage("Are you sure you want to remove all items?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase.execSQL("delete from "+ TABLE_NAME_cart);
                                        mAdapter.swapCursor(getAllItems());
                                        finish();

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });

                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fpSELLPRICE, fpTOTALPRICE;
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                        ArrayList<String> listReceipt = new ArrayList<String>();
                        String forReceipt = "SELECT * FROM cart";
                        Cursor cursor = mDatabase.rawQuery(forReceipt, null);
                        if (cursor.moveToFirst()) {

                            do {
                                String productNAME = cursor.getString(2);
                                String productQuantity = cursor.getString(3);
                                Double productSellPrice = cursor.getDouble(4);
                                Double productTotalPrice = cursor.getDouble(5);

                                fpSELLPRICE = format.format(productSellPrice);
                                fpTOTALPRICE = format.format(productTotalPrice);

                                String toPadproductNAME = productNAME;
                                String paddedproductNAME = String.format("%8s", toPadproductNAME);
                                String paddedproductQuantity = String.format("%7s", productQuantity);
                                String paddedproductSellPrice = String.format("%7s", fpSELLPRICE);
                                String paddedproductTotalPrice = String.format("%7s", fpTOTALPRICE);

                                if (paddedproductNAME.length() > 8 ){

                                    String finalproductname = paddedproductNAME.substring(0,6);

                                    String receipt = "" + finalproductname + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }else if (paddedproductQuantity.length() > 7 ){
                                    String finalquantity = paddedproductQuantity.substring(0,6);

                                    String receipt = "" + paddedproductNAME + "          " + finalquantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductSellPrice.length() > 7){
                                    String finalsellprice = paddedproductSellPrice.substring(0,6);


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + finalsellprice + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductTotalPrice.length() > 7){
                                    String finaltotalprice = paddedproductTotalPrice.substring(0,6);
                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + finaltotalprice + "";
                                    listReceipt.add(receipt);

                                }
                                else {


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);
                                }


                            } while (cursor.moveToNext());
                        }

                        int arrayCounter = listReceipt.size();
                        String a = String.valueOf(arrayCounter);

                        String joined = TextUtils.join("\n", listReceipt);
                        Toast.makeText(CartItemActivity.this,joined,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CartItemActivity.this, PaymentDialog.class);
                        intent.putExtra("subtotal",converttotalsubtotal);
                        intent.putExtra("discount",con1);
                        intent.putExtra("totalprice",con);
                        intent.putExtra("receipt",joined);
                        intent.putExtra("productID", getProductID("cart"));
                        intent.putExtra("productQuantity", quantityPCart("cart") );
                        startActivity(intent);

                    }
                });

            } else if (thisCurrency.equals("Chinese Yuan")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CHINA));

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


                //for receipt number

                String count = "SELECT count(*) FROM sales";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);

                // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                if(icount>0){

                    //select max may DATA

                    // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);

                    int newID = icount1 + 1;
                    String res = String.valueOf(icount1);

                }else {
                    int defaultID = icount + 1;

                }

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ITEM");
                        builder.setMessage("Are you sure you want to remove this in Cart?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem((long) viewHolder.itemView.getTag());
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }).attachToRecyclerView(recyclerview);




                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String TABLE_NAME_cart = "cart";


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ALL ITEMS");
                        builder.setMessage("Are you sure you want to remove all items?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase.execSQL("delete from "+ TABLE_NAME_cart);
                                        mAdapter.swapCursor(getAllItems());
                                        finish();

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });

                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fpSELLPRICE, fpTOTALPRICE;
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                        ArrayList<String> listReceipt = new ArrayList<String>();
                        String forReceipt = "SELECT * FROM cart";
                        Cursor cursor = mDatabase.rawQuery(forReceipt, null);
                        if (cursor.moveToFirst()) {

                            do {
                                String productNAME = cursor.getString(2);
                                String productQuantity = cursor.getString(3);
                                Double productSellPrice = cursor.getDouble(4);
                                Double productTotalPrice = cursor.getDouble(5);

                                fpSELLPRICE = format.format(productSellPrice);
                                fpTOTALPRICE = format.format(productTotalPrice);

                                String toPadproductNAME = productNAME;
                                String paddedproductNAME = String.format("%8s", toPadproductNAME);
                                String paddedproductQuantity = String.format("%7s", productQuantity);
                                String paddedproductSellPrice = String.format("%7s", fpSELLPRICE);
                                String paddedproductTotalPrice = String.format("%7s", fpTOTALPRICE);

                                if (paddedproductNAME.length() > 8 ){

                                    String finalproductname = paddedproductNAME.substring(0,6);

                                    String receipt = "" + finalproductname + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }else if (paddedproductQuantity.length() > 7 ){
                                    String finalquantity = paddedproductQuantity.substring(0,6);

                                    String receipt = "" + paddedproductNAME + "          " + finalquantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductSellPrice.length() > 7){
                                    String finalsellprice = paddedproductSellPrice.substring(0,6);


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + finalsellprice + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductTotalPrice.length() > 7){
                                    String finaltotalprice = paddedproductTotalPrice.substring(0,6);
                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + finaltotalprice + "";
                                    listReceipt.add(receipt);

                                }
                                else {


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);
                                }


                            } while (cursor.moveToNext());
                        }

                        int arrayCounter = listReceipt.size();
                        String a = String.valueOf(arrayCounter);

                        String joined = TextUtils.join("\n", listReceipt);
                        Toast.makeText(CartItemActivity.this,joined,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CartItemActivity.this, PaymentDialog.class);
                        intent.putExtra("subtotal",converttotalsubtotal);
                        intent.putExtra("discount",con1);
                        intent.putExtra("totalprice",con);
                        intent.putExtra("receipt",joined);
                        intent.putExtra("productID", getProductID("cart"));
                        intent.putExtra("productQuantity", quantityPCart("cart") );
                        startActivity(intent);

                    }
                });

            } else if (thisCurrency.equals("France Euro")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.FRANCE));

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


                //for receipt number

                String count = "SELECT count(*) FROM sales";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);

                // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                if(icount>0){

                    //select max may DATA

                    // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);

                    int newID = icount1 + 1;
                    String res = String.valueOf(icount1);

                }else {
                    int defaultID = icount + 1;

                }

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ITEM");
                        builder.setMessage("Are you sure you want to remove this in Cart?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem((long) viewHolder.itemView.getTag());
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }).attachToRecyclerView(recyclerview);




                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String TABLE_NAME_cart = "cart";


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ALL ITEMS");
                        builder.setMessage("Are you sure you want to remove all items?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase.execSQL("delete from "+ TABLE_NAME_cart);
                                        mAdapter.swapCursor(getAllItems());
                                        finish();

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });

                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fpSELLPRICE, fpTOTALPRICE;
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                        ArrayList<String> listReceipt = new ArrayList<String>();
                        String forReceipt = "SELECT * FROM cart";
                        Cursor cursor = mDatabase.rawQuery(forReceipt, null);
                        if (cursor.moveToFirst()) {

                            do {
                                String productNAME = cursor.getString(2);
                                String productQuantity = cursor.getString(3);
                                Double productSellPrice = cursor.getDouble(4);
                                Double productTotalPrice = cursor.getDouble(5);

                                fpSELLPRICE = format.format(productSellPrice);
                                fpTOTALPRICE = format.format(productTotalPrice);

                                String toPadproductNAME = productNAME;
                                String paddedproductNAME = String.format("%8s", toPadproductNAME);
                                String paddedproductQuantity = String.format("%7s", productQuantity);
                                String paddedproductSellPrice = String.format("%7s", fpSELLPRICE);
                                String paddedproductTotalPrice = String.format("%7s", fpTOTALPRICE);

                                if (paddedproductNAME.length() > 8 ){

                                    String finalproductname = paddedproductNAME.substring(0,6);

                                    String receipt = "" + finalproductname + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }else if (paddedproductQuantity.length() > 7 ){
                                    String finalquantity = paddedproductQuantity.substring(0,6);

                                    String receipt = "" + paddedproductNAME + "          " + finalquantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductSellPrice.length() > 7){
                                    String finalsellprice = paddedproductSellPrice.substring(0,6);


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + finalsellprice + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductTotalPrice.length() > 7){
                                    String finaltotalprice = paddedproductTotalPrice.substring(0,6);
                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + finaltotalprice + "";
                                    listReceipt.add(receipt);

                                }
                                else {


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);
                                }


                            } while (cursor.moveToNext());
                        }

                        int arrayCounter = listReceipt.size();
                        String a = String.valueOf(arrayCounter);

                        String joined = TextUtils.join("\n", listReceipt);
                        Toast.makeText(CartItemActivity.this,joined,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CartItemActivity.this, PaymentDialog.class);
                        intent.putExtra("subtotal",converttotalsubtotal);
                        intent.putExtra("discount",con1);
                        intent.putExtra("totalprice",con);
                        intent.putExtra("receipt",joined);
                        intent.putExtra("productID", getProductID("cart"));
                        intent.putExtra("productQuantity", quantityPCart("cart") );
                        startActivity(intent);

                    }
                });

            } else if (thisCurrency.equals("Korea Won")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.KOREA));

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


                //for receipt number

                String count = "SELECT count(*) FROM sales";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);

                // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                if(icount>0){

                    //select max may DATA

                    // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);

                    int newID = icount1 + 1;
                    String res = String.valueOf(icount1);

                }else {
                    int defaultID = icount + 1;

                }

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ITEM");
                        builder.setMessage("Are you sure you want to remove this in Cart?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem((long) viewHolder.itemView.getTag());
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }).attachToRecyclerView(recyclerview);




                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String TABLE_NAME_cart = "cart";


                        AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("REMOVE ALL ITEMS");
                        builder.setMessage("Are you sure you want to remove all items?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase.execSQL("delete from "+ TABLE_NAME_cart);
                                        mAdapter.swapCursor(getAllItems());
                                        finish();

                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAdapter.swapCursor(getAllItems());
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                    }
                });

                payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fpSELLPRICE, fpTOTALPRICE;
                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                        ArrayList<String> listReceipt = new ArrayList<String>();
                        String forReceipt = "SELECT * FROM cart";
                        Cursor cursor = mDatabase.rawQuery(forReceipt, null);
                        if (cursor.moveToFirst()) {

                            do {
                                String productNAME = cursor.getString(2);
                                String productQuantity = cursor.getString(3);
                                Double productSellPrice = cursor.getDouble(4);
                                Double productTotalPrice = cursor.getDouble(5);

                                fpSELLPRICE = format.format(productSellPrice);
                                fpTOTALPRICE = format.format(productTotalPrice);

                                String toPadproductNAME = productNAME;
                                String paddedproductNAME = String.format("%8s", toPadproductNAME);
                                String paddedproductQuantity = String.format("%7s", productQuantity);
                                String paddedproductSellPrice = String.format("%7s", fpSELLPRICE);
                                String paddedproductTotalPrice = String.format("%7s", fpTOTALPRICE);

                                if (paddedproductNAME.length() > 8 ){

                                    String finalproductname = paddedproductNAME.substring(0,6);

                                    String receipt = "" + finalproductname + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }else if (paddedproductQuantity.length() > 7 ){
                                    String finalquantity = paddedproductQuantity.substring(0,6);

                                    String receipt = "" + paddedproductNAME + "          " + finalquantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductSellPrice.length() > 7){
                                    String finalsellprice = paddedproductSellPrice.substring(0,6);


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + finalsellprice + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);

                                }
                                else if (paddedproductTotalPrice.length() > 7){
                                    String finaltotalprice = paddedproductTotalPrice.substring(0,6);
                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + finaltotalprice + "";
                                    listReceipt.add(receipt);

                                }
                                else {


                                    String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                    listReceipt.add(receipt);
                                }


                            } while (cursor.moveToNext());
                        }

                        int arrayCounter = listReceipt.size();
                        String a = String.valueOf(arrayCounter);

                        String joined = TextUtils.join("\n", listReceipt);
                        Toast.makeText(CartItemActivity.this,joined,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CartItemActivity.this, PaymentDialog.class);
                        intent.putExtra("subtotal",converttotalsubtotal);
                        intent.putExtra("discount",con1);
                        intent.putExtra("totalprice",con);
                        intent.putExtra("receipt",joined);
                        intent.putExtra("productID", getProductID("cart"));
                        intent.putExtra("productQuantity", quantityPCart("cart") );
                        startActivity(intent);

                    }
                });

            }
        } else if (icountP == 0){

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

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


            //for receipt number

            String count = "SELECT count(*) FROM sales";
            Cursor mcursor = mDatabase.rawQuery(count, null);
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);

            String a = String.valueOf(icount);

            // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

            if(icount>0){

                //select max may DATA

                // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

                String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                mcursor1.moveToFirst();
                int icount1 = mcursor1.getInt(0);

                int newID = icount1 + 1;
                String res = String.valueOf(icount1);

            }else {
                int defaultID = icount + 1;

            }

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("REMOVE ITEM");
                    builder.setMessage("Are you sure you want to remove this in Cart?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    removeItem((long) viewHolder.itemView.getTag());
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mAdapter.swapCursor(getAllItems());
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }).attachToRecyclerView(recyclerview);




            discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String TABLE_NAME_cart = "cart";


                    AlertDialog.Builder builder = new AlertDialog.Builder(CartItemActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("REMOVE ALL ITEMS");
                    builder.setMessage("Are you sure you want to remove all items?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    mDatabase.execSQL("delete from "+ TABLE_NAME_cart);
                                    mAdapter.swapCursor(getAllItems());
                                    finish();

                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mAdapter.swapCursor(getAllItems());
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }
            });

            payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String fpSELLPRICE, fpTOTALPRICE;
                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                    ArrayList<String> listReceipt = new ArrayList<String>();
                    String forReceipt = "SELECT * FROM cart";
                    Cursor cursor = mDatabase.rawQuery(forReceipt, null);
                    if (cursor.moveToFirst()) {

                        do {
                            String productNAME = cursor.getString(2);
                            String productQuantity = cursor.getString(3);
                            Double productSellPrice = cursor.getDouble(4);
                            Double productTotalPrice = cursor.getDouble(5);

                            fpSELLPRICE = format.format(productSellPrice);
                            fpTOTALPRICE = format.format(productTotalPrice);

                            String toPadproductNAME = productNAME;
                            String paddedproductNAME = String.format("%8s", toPadproductNAME);
                            String paddedproductQuantity = String.format("%7s", productQuantity);
                            String paddedproductSellPrice = String.format("%7s", fpSELLPRICE);
                            String paddedproductTotalPrice = String.format("%7s", fpTOTALPRICE);

                            if (paddedproductNAME.length() > 8 ){

                                String finalproductname = paddedproductNAME.substring(0,6);

                                String receipt = "" + finalproductname + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                listReceipt.add(receipt);

                            }else if (paddedproductQuantity.length() > 7 ){
                                String finalquantity = paddedproductQuantity.substring(0,6);

                                String receipt = "" + paddedproductNAME + "          " + finalquantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                listReceipt.add(receipt);

                            }
                            else if (paddedproductSellPrice.length() > 7){
                                String finalsellprice = paddedproductSellPrice.substring(0,6);


                                String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + finalsellprice + "          " + fpTOTALPRICE + "";
                                listReceipt.add(receipt);

                            }
                            else if (paddedproductTotalPrice.length() > 7){
                                String finaltotalprice = paddedproductTotalPrice.substring(0,6);
                                String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + finaltotalprice + "";
                                listReceipt.add(receipt);

                            }
                            else {


                                String receipt = "" + paddedproductNAME + "          " + productQuantity + "          " + fpSELLPRICE + "          " + fpTOTALPRICE + "";
                                listReceipt.add(receipt);
                            }


                        } while (cursor.moveToNext());
                    }

                    int arrayCounter = listReceipt.size();
                    String a = String.valueOf(arrayCounter);

                    String joined = TextUtils.join("\n", listReceipt);
                    Toast.makeText(CartItemActivity.this,joined,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(CartItemActivity.this, PaymentDialog.class);
                    intent.putExtra("subtotal",converttotalsubtotal);
                    intent.putExtra("discount",con1);
                    intent.putExtra("totalprice",con);
                    intent.putExtra("receipt",joined);
                    intent.putExtra("productID", getProductID("cart"));
                    intent.putExtra("productQuantity", quantityPCart("cart") );
                    startActivity(intent);

                }
            });



        }
    }
}
