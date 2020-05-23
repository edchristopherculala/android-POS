package com.ed.chris.androidpos.POS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.AdminMain;
import com.ed.chris.androidpos.database.DatabaseHelper;
import com.ed.chris.androidpos.user.User_Main;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class EndOfTransactionActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;


    private TextView totalprice,change;
    private ImageView buttonNewTransaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_transaction);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();


        String count = "SELECT count(*) FROM currencysettings";
        Cursor mcursor = mDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        String a = String.valueOf(icount);
        if(icount>0) {


            String currency1 = "SELECT CURRENCY FROM currencysettings";
            Cursor mcursor1 = mDatabase.rawQuery(currency1, null);
            mcursor1.moveToFirst();
            String thisCurrency = mcursor1.getString(0);

            if (thisCurrency.equals("Philippine Peso")){
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                String totalPRICE,totalCHANGE;
                Double converttotalPRICE,converttotalCHANGE;

                Intent intent = getIntent();
                totalPRICE = intent.getStringExtra("totalprice");
                totalCHANGE = intent.getStringExtra("change");

                converttotalPRICE = Double.valueOf(totalPRICE);
                converttotalCHANGE = Double.valueOf(totalCHANGE);

                totalprice = findViewById(R.id.txttotalamount);
                change = findViewById(R.id.txttotalchange);

                buttonNewTransaction = findViewById(R.id.buttonnewtransaction);

                totalprice.setText(format.format(converttotalPRICE));
                change.setText(format.format(converttotalCHANGE));

                buttonNewTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String count = "SELECT count(*) FROM employeeloginlogout";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);


                        if (icount > 0) {

                            String s = "Select * From employeeloginlogout";
                            Cursor ms = mDatabase.rawQuery(s, null);
                            ms.moveToLast();
                            String whatRole = ms.getString(3);
                            Toast.makeText(EndOfTransactionActivity.this, whatRole.toString(),Toast.LENGTH_LONG).show();

                            ms.close();

                            if (whatRole.equals("ADMIN")){

                                Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                                startActivity(intent);
                                finish();

                            }else if (whatRole.equals("USER")){

                                Intent intent = new Intent(EndOfTransactionActivity.this, User_Main.class);
                                startActivity(intent);
                                finish();

                            }

                        } else if (icount == 0){

                            Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });


            }else if (thisCurrency.equals("Canadian Dollars")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CANADA));

                String totalPRICE,totalCHANGE;
                Double converttotalPRICE,converttotalCHANGE;

                Intent intent = getIntent();
                totalPRICE = intent.getStringExtra("totalprice");
                totalCHANGE = intent.getStringExtra("change");

                converttotalPRICE = Double.valueOf(totalPRICE);
                converttotalCHANGE = Double.valueOf(totalCHANGE);

                totalprice = findViewById(R.id.txttotalamount);
                change = findViewById(R.id.txttotalchange);

                buttonNewTransaction = findViewById(R.id.buttonnewtransaction);

                totalprice.setText(format.format(converttotalPRICE));
                change.setText(format.format(converttotalCHANGE));

                buttonNewTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String count = "SELECT count(*) FROM employeeloginlogout";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);


                        if (icount > 0) {

                            String s = "Select * From employeeloginlogout";
                            Cursor ms = mDatabase.rawQuery(s, null);
                            ms.moveToLast();
                            String whatRole = ms.getString(3);
                            Toast.makeText(EndOfTransactionActivity.this, whatRole.toString(),Toast.LENGTH_LONG).show();

                            ms.close();

                            if (whatRole.equals("ADMIN")){

                                Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                                startActivity(intent);
                                finish();

                            }else if (whatRole.equals("USER")){

                                Intent intent = new Intent(EndOfTransactionActivity.this, User_Main.class);
                                startActivity(intent);
                                finish();

                            }

                        } else if (icount == 0){

                            Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });



            }else if (thisCurrency.equals("Japanese Yen")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.JAPAN));

                String totalPRICE,totalCHANGE;
                Double converttotalPRICE,converttotalCHANGE;

                Intent intent = getIntent();
                totalPRICE = intent.getStringExtra("totalprice");
                totalCHANGE = intent.getStringExtra("change");

                converttotalPRICE = Double.valueOf(totalPRICE);
                converttotalCHANGE = Double.valueOf(totalCHANGE);

                totalprice = findViewById(R.id.txttotalamount);
                change = findViewById(R.id.txttotalchange);

                buttonNewTransaction = findViewById(R.id.buttonnewtransaction);

                totalprice.setText(format.format(converttotalPRICE));
                change.setText(format.format(converttotalCHANGE));

                buttonNewTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String count = "SELECT count(*) FROM employeeloginlogout";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);


                        if (icount > 0) {

                            String s = "Select * From employeeloginlogout";
                            Cursor ms = mDatabase.rawQuery(s, null);
                            ms.moveToLast();
                            String whatRole = ms.getString(3);
                            Toast.makeText(EndOfTransactionActivity.this, whatRole.toString(),Toast.LENGTH_LONG).show();

                            ms.close();

                            if (whatRole.equals("ADMIN")){

                                Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                                startActivity(intent);
                                finish();

                            }else if (whatRole.equals("USER")){

                                Intent intent = new Intent(EndOfTransactionActivity.this, User_Main.class);
                                startActivity(intent);
                                finish();

                            }

                        } else if (icount == 0){

                            Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });



            }else if (thisCurrency.equals("US Dollars")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.US));

                String totalPRICE,totalCHANGE;
                Double converttotalPRICE,converttotalCHANGE;

                Intent intent = getIntent();
                totalPRICE = intent.getStringExtra("totalprice");
                totalCHANGE = intent.getStringExtra("change");

                converttotalPRICE = Double.valueOf(totalPRICE);
                converttotalCHANGE = Double.valueOf(totalCHANGE);

                totalprice = findViewById(R.id.txttotalamount);
                change = findViewById(R.id.txttotalchange);

                buttonNewTransaction = findViewById(R.id.buttonnewtransaction);

                totalprice.setText(format.format(converttotalPRICE));
                change.setText(format.format(converttotalCHANGE));

                buttonNewTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String count = "SELECT count(*) FROM employeeloginlogout";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);


                        if (icount > 0) {

                            String s = "Select * From employeeloginlogout";
                            Cursor ms = mDatabase.rawQuery(s, null);
                            ms.moveToLast();
                            String whatRole = ms.getString(3);
                            Toast.makeText(EndOfTransactionActivity.this, whatRole.toString(),Toast.LENGTH_LONG).show();

                            ms.close();

                            if (whatRole.equals("ADMIN")){

                                Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                                startActivity(intent);
                                finish();

                            }else if (whatRole.equals("USER")){

                                Intent intent = new Intent(EndOfTransactionActivity.this, User_Main.class);
                                startActivity(intent);
                                finish();

                            }

                        } else if (icount == 0){

                            Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });


            }else if (thisCurrency.equals("Chinese Yuan")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CHINA));

                String totalPRICE,totalCHANGE;
                Double converttotalPRICE,converttotalCHANGE;

                Intent intent = getIntent();
                totalPRICE = intent.getStringExtra("totalprice");
                totalCHANGE = intent.getStringExtra("change");

                converttotalPRICE = Double.valueOf(totalPRICE);
                converttotalCHANGE = Double.valueOf(totalCHANGE);

                totalprice = findViewById(R.id.txttotalamount);
                change = findViewById(R.id.txttotalchange);

                buttonNewTransaction = findViewById(R.id.buttonnewtransaction);

                totalprice.setText(format.format(converttotalPRICE));
                change.setText(format.format(converttotalCHANGE));

                buttonNewTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String count = "SELECT count(*) FROM employeeloginlogout";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);


                        if (icount > 0) {

                            String s = "Select * From employeeloginlogout";
                            Cursor ms = mDatabase.rawQuery(s, null);
                            ms.moveToLast();
                            String whatRole = ms.getString(3);
                            Toast.makeText(EndOfTransactionActivity.this, whatRole.toString(),Toast.LENGTH_LONG).show();

                            ms.close();

                            if (whatRole.equals("ADMIN")){

                                Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                                startActivity(intent);
                                finish();

                            }else if (whatRole.equals("USER")){

                                Intent intent = new Intent(EndOfTransactionActivity.this, User_Main.class);
                                startActivity(intent);
                                finish();

                            }

                        } else if (icount == 0){

                            Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

            }else if (thisCurrency.equals("France Euro")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.FRANCE));

                String totalPRICE,totalCHANGE;
                Double converttotalPRICE,converttotalCHANGE;

                Intent intent = getIntent();
                totalPRICE = intent.getStringExtra("totalprice");
                totalCHANGE = intent.getStringExtra("change");

                converttotalPRICE = Double.valueOf(totalPRICE);
                converttotalCHANGE = Double.valueOf(totalCHANGE);

                totalprice = findViewById(R.id.txttotalamount);
                change = findViewById(R.id.txttotalchange);

                buttonNewTransaction = findViewById(R.id.buttonnewtransaction);

                totalprice.setText(format.format(converttotalPRICE));
                change.setText(format.format(converttotalCHANGE));

                buttonNewTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String count = "SELECT count(*) FROM employeeloginlogout";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);


                        if (icount > 0) {

                            String s = "Select * From employeeloginlogout";
                            Cursor ms = mDatabase.rawQuery(s, null);
                            ms.moveToLast();
                            String whatRole = ms.getString(3);
                            Toast.makeText(EndOfTransactionActivity.this, whatRole.toString(),Toast.LENGTH_LONG).show();

                            ms.close();

                            if (whatRole.equals("ADMIN")){

                                Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                                startActivity(intent);
                                finish();

                            }else if (whatRole.equals("USER")){

                                Intent intent = new Intent(EndOfTransactionActivity.this, User_Main.class);
                                startActivity(intent);
                                finish();

                            }

                        } else if (icount == 0){

                            Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

            }else if (thisCurrency.equals("Korea Won")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.KOREA));

                String totalPRICE,totalCHANGE;
                Double converttotalPRICE,converttotalCHANGE;

                Intent intent = getIntent();
                totalPRICE = intent.getStringExtra("totalprice");
                totalCHANGE = intent.getStringExtra("change");

                converttotalPRICE = Double.valueOf(totalPRICE);
                converttotalCHANGE = Double.valueOf(totalCHANGE);

                totalprice = findViewById(R.id.txttotalamount);
                change = findViewById(R.id.txttotalchange);

                buttonNewTransaction = findViewById(R.id.buttonnewtransaction);

                totalprice.setText(format.format(converttotalPRICE));
                change.setText(format.format(converttotalCHANGE));

                buttonNewTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String count = "SELECT count(*) FROM employeeloginlogout";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);


                        if (icount > 0) {

                            String s = "Select * From employeeloginlogout";
                            Cursor ms = mDatabase.rawQuery(s, null);
                            ms.moveToLast();
                            String whatRole = ms.getString(3);
                            Toast.makeText(EndOfTransactionActivity.this, whatRole.toString(),Toast.LENGTH_LONG).show();

                            ms.close();

                            if (whatRole.equals("ADMIN")){

                                Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                                startActivity(intent);
                                finish();

                            }else if (whatRole.equals("USER")){

                                Intent intent = new Intent(EndOfTransactionActivity.this, User_Main.class);
                                startActivity(intent);
                                finish();

                            }

                        } else if (icount == 0){

                            Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });




            }

        }else{

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

            String totalPRICE,totalCHANGE;
            Double converttotalPRICE,converttotalCHANGE;

            Intent intent = getIntent();
            totalPRICE = intent.getStringExtra("totalprice");
            totalCHANGE = intent.getStringExtra("change");

            converttotalPRICE = Double.valueOf(totalPRICE);
            converttotalCHANGE = Double.valueOf(totalCHANGE);

            totalprice = findViewById(R.id.txttotalamount);
            change = findViewById(R.id.txttotalchange);

            buttonNewTransaction = findViewById(R.id.buttonnewtransaction);

            totalprice.setText(format.format(converttotalPRICE));
            change.setText(format.format(converttotalCHANGE));

            buttonNewTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String count = "SELECT count(*) FROM employeeloginlogout";
                    Cursor mcursor = mDatabase.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);


                    if (icount > 0) {

                        String s = "Select * From employeeloginlogout";
                        Cursor ms = mDatabase.rawQuery(s, null);
                        ms.moveToLast();
                        String whatRole = ms.getString(3);
                        Toast.makeText(EndOfTransactionActivity.this, whatRole.toString(),Toast.LENGTH_LONG).show();

                        ms.close();

                        if (whatRole.equals("ADMIN")){

                            Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                            startActivity(intent);
                            finish();

                        }else if (whatRole.equals("USER")){

                            Intent intent = new Intent(EndOfTransactionActivity.this, User_Main.class);
                            startActivity(intent);
                            finish();

                        }

                    } else if (icount == 0){

                        Intent intent = new Intent(EndOfTransactionActivity.this,AdminMain.class);
                        startActivity(intent);
                        finish();
                    }




                }
            });


        }



    }
}
