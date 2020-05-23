package com.ed.chris.androidpos.POS;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.CartContract;
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceContract;
import com.ed.chris.androidpos.admin.admin_adapters.SalesContract;
import com.ed.chris.androidpos.admin.admin_adapters.SalesItemsContract;
import com.ed.chris.androidpos.admin.admin_adapters.SalesSummaryContract;
import com.ed.chris.androidpos.database.DatabaseHelper;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PaymentDialog extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    private TextView txtsub, txtdis,txttotal, txtreceiptnumber;
    private EditText edttextcash;
    private ImageView buttoncharge, qrImage, buttondiscount;

    String inputValue;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;

    private ByteArrayOutputStream a_thumbnail = new ByteArrayOutputStream();

    Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, buttonClear;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_dialog);

        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        button0 = (Button) findViewById(R.id.button_0);
        button1 = (Button) findViewById(R.id.button_1);
        button2 = (Button) findViewById(R.id.button_2);
        button3 = (Button) findViewById(R.id.button_3);
        button4 = (Button) findViewById(R.id.button_4);
        button5 = (Button) findViewById(R.id.button_5);
        button6 = (Button) findViewById(R.id.button_6);
        button7 = (Button) findViewById(R.id.button_7);
        button8 = (Button) findViewById(R.id.button_8);
        button9 = (Button) findViewById(R.id.button_9);
        buttonClear = (Button) findViewById(R.id.button_delete) ;

        // keypad();

        txtsub = findViewById(R.id.txtsubtotalpayment);
        txtdis = findViewById(R.id.txtdiscountpayment);
        txttotal = findViewById(R.id.txttotalpayment);
        qrImage = findViewById(R.id.qrHolder);
        edttextcash = findViewById(R.id.edttxtCash);
        buttoncharge = findViewById(R.id.buttoncharge);
        //edttextcash.setEnabled(false);

        //initialized receipt number
        txtreceiptnumber = findViewById(R.id.txtreceiptno);

        Intent intent = getIntent();

        final String finalreceipt =  intent.getStringExtra("receipt");
        final String finalSUBTOTAL =  intent.getStringExtra("subtotal");
        final String finalDISCOUNT =  intent.getStringExtra("discount");
        final String finalTOTALPRICE =  intent.getStringExtra("totalprice");

        final String[] productID = intent.getStringArrayExtra("productID");
        final String[] productcurrentQuantity = intent.getStringArrayExtra("productQuantity");






        StringBuilder builder = new StringBuilder();
        for(String i : productID)
        {
            builder.append("" + i + " ");
        }
        Toast.makeText(this, builder, Toast.LENGTH_LONG).show();
        StringBuilder builder1 = new StringBuilder();
        for(String i : productcurrentQuantity)
        {
            builder1.append("" + i + " ");
        }
        Toast.makeText(this, builder1, Toast.LENGTH_LONG).show();

        final String ReceiptHeader = "                      GreatPOS Mobile " +
                "\n                            Pampanga" +
                "\n                            TESTING\n\n";

        final String ReceiptFooter = "                  \nThank You!! \n No data in database";


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

                final String remfinalSUBTOTAL,remfinalDISCOUNT,remfinalTOTALPRICE;
                final String remfinalSUBTOTAL1,remfinalDISCOUNT1,remfinalTOTALPRICE1;
                final Double convfinalSUBTOTAL, convfinalDISCOUNT,convfinalTOTALPRICE;

                remfinalSUBTOTAL = finalSUBTOTAL.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalDISCOUNT = finalDISCOUNT.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalTOTALPRICE = finalTOTALPRICE.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");

                remfinalSUBTOTAL1 = remfinalSUBTOTAL.replace(",", "");
                remfinalDISCOUNT1 = remfinalDISCOUNT.replace(",", "");
                remfinalTOTALPRICE1 = remfinalTOTALPRICE.replace(",", "");

                convfinalSUBTOTAL = Double.valueOf(remfinalSUBTOTAL1);
                convfinalDISCOUNT = Double.valueOf(remfinalDISCOUNT1);
                convfinalTOTALPRICE = Double.valueOf(remfinalTOTALPRICE1);

                txttotal.setText(format.format(convfinalTOTALPRICE));
                txtdis.setText(format.format(convfinalDISCOUNT));
                txtsub.setText(format.format(convfinalSUBTOTAL));

                String subt = format.format(convfinalSUBTOTAL);
                String subd = format.format(convfinalDISCOUNT);
                String pinakaTOTAL = format.format(convfinalTOTALPRICE);

                final String Detailssub = "\n\nSubtotal: "+subt ;
                final String DetailsDiscount = "\nDiscount: "+subd ;
                final String DetailsTotalPrice = "\nTotal Price: "+pinakaTOTAL ;

                String receiptsettings = "SELECT * FROM receiptsettings";
                Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
                receiptcursor.moveToFirst();
                int counter = receiptcursor.getCount();

                if(counter >0) {
                    String count1 = "SELECT * FROM receiptsettings WHERE _ID = 1";
                    Cursor mcursor2 = mDatabase.rawQuery(count1, null);
                    mcursor2.moveToFirst();
                    int icount1 = mcursor2.getInt(0);
                    String res = String.valueOf(icount1);

                    String APP_NAME = mcursor2.getString(1);
                    String COMPANY_ADDRESS = mcursor2.getString(2);
                    String COMPANY_TELEPHONE = mcursor2.getString(3);
                    String FOOTER_1 = mcursor2.getString(4);
                    String FOOTER_2 = mcursor2.getString(5);
                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  APP_NAME+" "+COMPANY_ADDRESS+" "+COMPANY_TELEPHONE+ " "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+FOOTER_1+" " +FOOTER_2;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }else {

                    String paddedFinalReceipt = "\n\n" +finalreceipt;
                    inputValue =  ReceiptHeader+" "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+ReceiptFooter;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }


                buttoncharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String a = edttextcash.getText().toString();

                        if (a.isEmpty()) {
                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                        }else if (a.equals(".")){

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                        }
                        else {

                            Double edttxtcashIn, Change, res;
                            String cashIn = edttextcash.getText().toString();
                            edttxtcashIn = Double.valueOf(cashIn);
                            res = edttxtcashIn - convfinalTOTALPRICE;
                            String result = String.valueOf(res);

                            if (edttxtcashIn < convfinalTOTALPRICE) {

                                Toast.makeText(PaymentDialog.this,"Insufficient Amount", Toast.LENGTH_LONG).show();

                            } else {

                                String count = "SELECT count(*) FROM salesitems";
                                Cursor mcursor = mDatabase.rawQuery(count, null);
                                mcursor.moveToFirst();
                                int icount = mcursor.getInt(0);

                                if(icount>0){

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    String countID = "SELECT _ID FROM salesitems ORDER BY _ID DESC";
                                    Cursor mcursorID = mDatabase.rawQuery(countID, null);
                                    mcursorID.moveToFirst();
                                    int ID = mcursorID.getInt(0);

                                    int newID = ID + 1;

                                    //copies cart to sales items
                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);


                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);

                                    //update Stocks here

                                    //new 9/4/2019
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());
                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from " + CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }else {

                                    String receiptnumber = txtreceiptnumber.getText().toString();

                                    int defaultID = 1;

                                    //copies cart to sales items
                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);


                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);


                                    mDatabase.execSQL("delete from "+ CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", convfinalTOTALPRICE);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }
                                Toast.makeText(PaymentDialog.this, res.toString(), Toast.LENGTH_LONG).show();

                            }
                        }

                    }
                });
            }else if (thisCurrency.equals("Canadian Dollars")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CANADA));

                final String remfinalSUBTOTAL,remfinalDISCOUNT,remfinalTOTALPRICE;
                final String remfinalSUBTOTAL1,remfinalDISCOUNT1,remfinalTOTALPRICE1;
                final Double convfinalSUBTOTAL, convfinalDISCOUNT,convfinalTOTALPRICE;

                remfinalSUBTOTAL = finalSUBTOTAL.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalDISCOUNT = finalDISCOUNT.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalTOTALPRICE = finalTOTALPRICE.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");

                remfinalSUBTOTAL1 = remfinalSUBTOTAL.replace(",", "");
                remfinalDISCOUNT1 = remfinalDISCOUNT.replace(",", "");
                remfinalTOTALPRICE1 = remfinalTOTALPRICE.replace(",", "");

                convfinalSUBTOTAL = Double.valueOf(remfinalSUBTOTAL1);
                convfinalDISCOUNT = Double.valueOf(remfinalDISCOUNT1);
                convfinalTOTALPRICE = Double.valueOf(remfinalTOTALPRICE1);

                txttotal.setText(format.format(convfinalTOTALPRICE));
                txtdis.setText(format.format(convfinalDISCOUNT));
                txtsub.setText(format.format(convfinalSUBTOTAL));

                String subt = format.format(convfinalSUBTOTAL);
                String subd = format.format(convfinalDISCOUNT);
                String pinakaTOTAL = format.format(convfinalTOTALPRICE);

                final String Detailssub = "\n\nSubtotal: "+subt ;
                final String DetailsDiscount = "\nDiscount: "+subd ;
                final String DetailsTotalPrice = "\nTotal Price: "+pinakaTOTAL ;


                //query if found generate new layout for receipt

                String receiptsettings = "SELECT * FROM receiptsettings";
                Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
                receiptcursor.moveToFirst();

                int counter = receiptcursor.getCount();

                if(counter >0) {
                    String count1 = "SELECT * FROM receiptsettings WHERE _ID = 1";
                    Cursor mcursor2 = mDatabase.rawQuery(count1, null);
                    mcursor2.moveToFirst();
                    int icount1 = mcursor2.getInt(0);
                    String res = String.valueOf(icount1);

                    String APP_NAME = mcursor2.getString(1);
                    String COMPANY_ADDRESS = mcursor2.getString(2);
                    String COMPANY_TELEPHONE = mcursor2.getString(3);
                    String FOOTER_1 = mcursor2.getString(4);
                    String FOOTER_2 = mcursor2.getString(5);


                    String paddedFinalReceipt = "\n\n" +finalreceipt;
                    //for QR Code Receipt


                    //change layout in settings
                    inputValue =  APP_NAME+" "+COMPANY_ADDRESS+" "+COMPANY_TELEPHONE+ " "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+FOOTER_1+" " +FOOTER_2;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }else {

                    String paddedFinalReceipt = "\n\n" +finalreceipt;
                    inputValue =  ReceiptHeader+" "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+ReceiptFooter;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }
                buttoncharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String a = edttextcash.getText().toString();
                        if (a.isEmpty()) {

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();

                        }else if (a.equals(".")){

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                        }else {

                            Double edttxtcashIn, Change, res;
                            String cashIn = edttextcash.getText().toString();
                            edttxtcashIn = Double.valueOf(cashIn);
                            res = edttxtcashIn - convfinalTOTALPRICE;
                            String result = String.valueOf(res);

                            if (edttxtcashIn < convfinalTOTALPRICE) {

                                Toast.makeText(PaymentDialog.this,"Insufficient Amount", Toast.LENGTH_LONG).show();

                            } else {

                                String count = "SELECT count(*) FROM salesitems";
                                Cursor mcursor = mDatabase.rawQuery(count, null);
                                mcursor.moveToFirst();
                                int icount = mcursor.getInt(0);

                                if(icount>0){

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    String countID = "SELECT _ID FROM salesitems ORDER BY _ID DESC";
                                    Cursor mcursorID = mDatabase.rawQuery(countID, null);
                                    mcursorID.moveToFirst();
                                    int ID = mcursorID.getInt(0);
                                    int newID = ID + 1;

                                    //copies cart to sales items
                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from " + CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }else {

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    int defaultID = 1;
                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);
                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from "+ CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", convfinalTOTALPRICE);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }

                                Toast.makeText(PaymentDialog.this, res.toString(), Toast.LENGTH_LONG).show();

                            }
                        }

                    }
                });

            }else if (thisCurrency.equals("Japanese Yen")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.JAPAN));
                final String remfinalSUBTOTAL,remfinalDISCOUNT,remfinalTOTALPRICE;
                final String remfinalSUBTOTAL1,remfinalDISCOUNT1,remfinalTOTALPRICE1;
                final Double convfinalSUBTOTAL, convfinalDISCOUNT,convfinalTOTALPRICE;

                remfinalSUBTOTAL = finalSUBTOTAL.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalDISCOUNT = finalDISCOUNT.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalTOTALPRICE = finalTOTALPRICE.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");

                remfinalSUBTOTAL1 = remfinalSUBTOTAL.replace(",", "");
                remfinalDISCOUNT1 = remfinalDISCOUNT.replace(",", "");
                remfinalTOTALPRICE1 = remfinalTOTALPRICE.replace(",", "");

                convfinalSUBTOTAL = Double.valueOf(remfinalSUBTOTAL1);
                convfinalDISCOUNT = Double.valueOf(remfinalDISCOUNT1);
                convfinalTOTALPRICE = Double.valueOf(remfinalTOTALPRICE1);

                txttotal.setText(format.format(convfinalTOTALPRICE));
                txtdis.setText(format.format(convfinalDISCOUNT));
                txtsub.setText(format.format(convfinalSUBTOTAL));

                String subt = format.format(convfinalSUBTOTAL);
                String subd = format.format(convfinalDISCOUNT);
                String pinakaTOTAL = format.format(convfinalTOTALPRICE);

                final String Detailssub = "\n\nSubtotal: "+subt ;
                final String DetailsDiscount = "\nDiscount: "+subd ;
                final String DetailsTotalPrice = "\nTotal Price: "+pinakaTOTAL ;


                //query if found generate new layout for receipt

                String receiptsettings = "SELECT * FROM receiptsettings";
                Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
                receiptcursor.moveToFirst();

                int counter = receiptcursor.getCount();

                if(counter >0) {
                    String count1 = "SELECT * FROM receiptsettings WHERE _ID = 1";
                    Cursor mcursor2 = mDatabase.rawQuery(count1, null);
                    mcursor2.moveToFirst();
                    int icount1 = mcursor2.getInt(0);
                    String res = String.valueOf(icount1);

                    String APP_NAME = mcursor2.getString(1);
                    String COMPANY_ADDRESS = mcursor2.getString(2);
                    String COMPANY_TELEPHONE = mcursor2.getString(3);
                    String FOOTER_1 = mcursor2.getString(4);
                    String FOOTER_2 = mcursor2.getString(5);

                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  APP_NAME+" "+COMPANY_ADDRESS+" "+COMPANY_TELEPHONE+ " "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+FOOTER_1+" " +FOOTER_2;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }else {

                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  ReceiptHeader+" "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+ReceiptFooter;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {

                    }

                }

                buttoncharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String a = edttextcash.getText().toString();

                        if (a.isEmpty()) {

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();

                        }else if (a.equals(".")){

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                        }else {

                            Double edttxtcashIn, Change, res;

                            String cashIn = edttextcash.getText().toString();
                            edttxtcashIn = Double.valueOf(cashIn);

                            res = edttxtcashIn - convfinalTOTALPRICE;

                            String result = String.valueOf(res);

                            if (edttxtcashIn < convfinalTOTALPRICE) {

                                Toast.makeText(PaymentDialog.this,"Insufficient Amount", Toast.LENGTH_LONG).show();

                            } else {

                                String count = "SELECT count(*) FROM salesitems";
                                Cursor mcursor = mDatabase.rawQuery(count, null);
                                mcursor.moveToFirst();
                                int icount = mcursor.getInt(0);

                                if(icount>0){

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    String countID = "SELECT _ID FROM salesitems ORDER BY _ID DESC";
                                    Cursor mcursorID = mDatabase.rawQuery(countID, null);
                                    mcursorID.moveToFirst();
                                    int ID = mcursorID.getInt(0);
                                    int newID = ID + 1;

                                    //copies cart to sales items
                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);

                                    mDatabase.execSQL("delete from " + CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }else {
                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    int defaultID = 1;

                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from "+ CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", convfinalTOTALPRICE);
                                    intent.putExtra("change", result);
                                    startActivity(intent);
                                    finish();

                                }

                                Toast.makeText(PaymentDialog.this, res.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });

            }else if (thisCurrency.equals("US Dollars")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.US));

                final String remfinalSUBTOTAL,remfinalDISCOUNT,remfinalTOTALPRICE;
                final String remfinalSUBTOTAL1,remfinalDISCOUNT1,remfinalTOTALPRICE1;
                final Double convfinalSUBTOTAL, convfinalDISCOUNT,convfinalTOTALPRICE;

                remfinalSUBTOTAL = finalSUBTOTAL.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalDISCOUNT = finalDISCOUNT.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalTOTALPRICE = finalTOTALPRICE.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");

                remfinalSUBTOTAL1 = remfinalSUBTOTAL.replace(",", "");
                remfinalDISCOUNT1 = remfinalDISCOUNT.replace(",", "");
                remfinalTOTALPRICE1 = remfinalTOTALPRICE.replace(",", "");

                convfinalSUBTOTAL = Double.valueOf(remfinalSUBTOTAL1);
                convfinalDISCOUNT = Double.valueOf(remfinalDISCOUNT1);
                convfinalTOTALPRICE = Double.valueOf(remfinalTOTALPRICE1);

                txttotal.setText(format.format(convfinalTOTALPRICE));
                txtdis.setText(format.format(convfinalDISCOUNT));
                txtsub.setText(format.format(convfinalSUBTOTAL));

                String subt = format.format(convfinalSUBTOTAL);
                String subd = format.format(convfinalDISCOUNT);
                String pinakaTOTAL = format.format(convfinalTOTALPRICE);

                final String Detailssub = "\n\nSubtotal: "+subt ;
                final String DetailsDiscount = "\nDiscount: "+subd ;
                final String DetailsTotalPrice = "\nTotal Price: "+pinakaTOTAL ;

                //query if found generate new layout for receipt
                String receiptsettings = "SELECT * FROM receiptsettings";
                Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
                receiptcursor.moveToFirst();

                int counter = receiptcursor.getCount();

                if(counter >0) {
                    String count1 = "SELECT * FROM receiptsettings WHERE _ID = 1";
                    Cursor mcursor2 = mDatabase.rawQuery(count1, null);
                    mcursor2.moveToFirst();
                    int icount1 = mcursor2.getInt(0);
                    String res = String.valueOf(icount1);

                    String APP_NAME = mcursor2.getString(1);
                    String COMPANY_ADDRESS = mcursor2.getString(2);
                    String COMPANY_TELEPHONE = mcursor2.getString(3);
                    String FOOTER_1 = mcursor2.getString(4);
                    String FOOTER_2 = mcursor2.getString(5);
                    String paddedFinalReceipt = "\n\n" +finalreceipt;
                    //change layout in settings
                    inputValue =  APP_NAME+" "+COMPANY_ADDRESS+" "+COMPANY_TELEPHONE+ " "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+FOOTER_1+" " +FOOTER_2;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }else {

                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  ReceiptHeader+" "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+ReceiptFooter;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {

                    }

                }

                buttoncharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String a = edttextcash.getText().toString();
                        if (a.isEmpty()) {

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();

                        }else if (a.equals(".")){

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                        }else {

                            Double edttxtcashIn, Change, res;
                            String cashIn = edttextcash.getText().toString();
                            edttxtcashIn = Double.valueOf(cashIn);
                            res = edttxtcashIn - convfinalTOTALPRICE;
                            String result = String.valueOf(res);

                            if (edttxtcashIn < convfinalTOTALPRICE) {

                                Toast.makeText(PaymentDialog.this,"Insufficient Amount", Toast.LENGTH_LONG).show();

                            } else {

                                String count = "SELECT count(*) FROM salesitems";
                                Cursor mcursor = mDatabase.rawQuery(count, null);
                                mcursor.moveToFirst();
                                int icount = mcursor.getInt(0);

                                if(icount>0){

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    String countID = "SELECT _ID FROM salesitems ORDER BY _ID DESC";
                                    Cursor mcursorID = mDatabase.rawQuery(countID, null);
                                    mcursorID.moveToFirst();
                                    int ID = mcursorID.getInt(0);
                                    int newID = ID + 1;

                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from " + CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                    intent.putExtra("change", result);
                                    startActivity(intent);
                                    finish();

                                }else {

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    int defaultID = 1;
                                    //copies cart to sales items
                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from "+ CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", convfinalTOTALPRICE);
                                    intent.putExtra("change", result);
                                    startActivity(intent);
                                    finish();

                                }

                                Toast.makeText(PaymentDialog.this, res.toString(), Toast.LENGTH_LONG).show();

                            }
                        }

                    }
                });

            }else if (thisCurrency.equals("Chinese Yuan")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CHINA));

                final String remfinalSUBTOTAL,remfinalDISCOUNT,remfinalTOTALPRICE;
                final String remfinalSUBTOTAL1,remfinalDISCOUNT1,remfinalTOTALPRICE1;
                final Double convfinalSUBTOTAL, convfinalDISCOUNT,convfinalTOTALPRICE;

                remfinalSUBTOTAL = finalSUBTOTAL.replace("CN¥","").replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","");

                remfinalDISCOUNT = finalDISCOUNT.replace("CN¥","").replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                ;
                remfinalTOTALPRICE = finalTOTALPRICE.replace("CN¥","").replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                ;

                remfinalSUBTOTAL1 = remfinalSUBTOTAL.replace(",", "");
                remfinalDISCOUNT1 = remfinalDISCOUNT.replace(",", "");
                remfinalTOTALPRICE1 = remfinalTOTALPRICE.replace(",", "");

                convfinalSUBTOTAL = Double.valueOf(remfinalSUBTOTAL1);
                convfinalDISCOUNT = Double.valueOf(remfinalDISCOUNT1);
                convfinalTOTALPRICE = Double.valueOf(remfinalTOTALPRICE1);

                txttotal.setText(format.format(convfinalTOTALPRICE));
                txtdis.setText(format.format(convfinalDISCOUNT));
                txtsub.setText(format.format(convfinalSUBTOTAL));

                String subt = format.format(convfinalSUBTOTAL);
                String subd = format.format(convfinalDISCOUNT);
                String pinakaTOTAL = format.format(convfinalTOTALPRICE);

                final String Detailssub = "\n\nSubtotal: "+subt ;
                final String DetailsDiscount = "\nDiscount: "+subd ;
                final String DetailsTotalPrice = "\nTotal Price: "+pinakaTOTAL ;

                String receiptsettings = "SELECT * FROM receiptsettings";
                Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
                receiptcursor.moveToFirst();

                int counter = receiptcursor.getCount();

                if(counter >0) {
                    String count1 = "SELECT * FROM receiptsettings WHERE _ID = 1";
                    Cursor mcursor2 = mDatabase.rawQuery(count1, null);
                    mcursor2.moveToFirst();
                    int icount1 = mcursor2.getInt(0);
                    String res = String.valueOf(icount1);

                    String APP_NAME = mcursor2.getString(1);
                    String COMPANY_ADDRESS = mcursor2.getString(2);
                    String COMPANY_TELEPHONE = mcursor2.getString(3);
                    String FOOTER_1 = mcursor2.getString(4);
                    String FOOTER_2 = mcursor2.getString(5);
                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  APP_NAME+" "+COMPANY_ADDRESS+" "+COMPANY_TELEPHONE+ " "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+FOOTER_1+" " +FOOTER_2;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }else {

                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  ReceiptHeader+" "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+ReceiptFooter;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }

                buttoncharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String a = edttextcash.getText().toString();

                        if (a.isEmpty()) {

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();

                        }else if (a.equals(".")){

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                        }else {

                            Double edttxtcashIn, Change, res;
                            String cashIn = edttextcash.getText().toString();
                            edttxtcashIn = Double.valueOf(cashIn);
                            res = edttxtcashIn - convfinalTOTALPRICE;
                            String result = String.valueOf(res);

                            if (edttxtcashIn < convfinalTOTALPRICE) {

                                Toast.makeText(PaymentDialog.this,"Insufficient Amount", Toast.LENGTH_LONG).show();

                            } else {

                                String count = "SELECT count(*) FROM salesitems";
                                Cursor mcursor = mDatabase.rawQuery(count, null);
                                mcursor.moveToFirst();
                                int icount = mcursor.getInt(0);

                                if(icount>0){

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    String countID = "SELECT _ID FROM salesitems ORDER BY _ID DESC";
                                    Cursor mcursorID = mDatabase.rawQuery(countID, null);
                                    mcursorID.moveToFirst();
                                    int ID = mcursorID.getInt(0);
                                    int newID = ID + 1;

                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from " + CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                    intent.putExtra("change", result);
                                    startActivity(intent);
                                    finish();

                                }else {

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    int defaultID = 1;

                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from "+ CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", convfinalTOTALPRICE);
                                    intent.putExtra("change", result);
                                    startActivity(intent);
                                    finish();

                                }

                                Toast.makeText(PaymentDialog.this, res.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });

            }else if (thisCurrency.equals("France Euro")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.FRANCE));

                final String remfinalSUBTOTAL,remfinalDISCOUNT,remfinalTOTALPRICE;
                final String remfinalSUBTOTAL1,remfinalDISCOUNT1,remfinalTOTALPRICE1;
                final Double convfinalSUBTOTAL, convfinalDISCOUNT,convfinalTOTALPRICE;

                remfinalSUBTOTAL = finalSUBTOTAL.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalDISCOUNT = finalDISCOUNT.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalTOTALPRICE = finalTOTALPRICE.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");

                remfinalSUBTOTAL1 = remfinalSUBTOTAL.replace(",", "");
                remfinalDISCOUNT1 = remfinalDISCOUNT.replace(",", "");
                remfinalTOTALPRICE1 = remfinalTOTALPRICE.replace(",", "");

                convfinalSUBTOTAL = Double.valueOf(remfinalSUBTOTAL1);
                convfinalDISCOUNT = Double.valueOf(remfinalDISCOUNT1);
                convfinalTOTALPRICE = Double.valueOf(remfinalTOTALPRICE1);

                txttotal.setText(format.format(convfinalTOTALPRICE));
                txtdis.setText(format.format(convfinalDISCOUNT));
                txtsub.setText(format.format(convfinalSUBTOTAL));

                String subt = format.format(convfinalSUBTOTAL);
                String subd = format.format(convfinalDISCOUNT);
                String pinakaTOTAL = format.format(convfinalTOTALPRICE);

                final String Detailssub = "\n\nSubtotal: "+subt ;
                final String DetailsDiscount = "\nDiscount: "+subd ;
                final String DetailsTotalPrice = "\nTotal Price: "+pinakaTOTAL ;


                //query if found generate new layout for receipt

                String receiptsettings = "SELECT * FROM receiptsettings";
                Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
                receiptcursor.moveToFirst();

                int counter = receiptcursor.getCount();

                if(counter >0) {
                    String count1 = "SELECT * FROM receiptsettings WHERE _ID = 1";
                    Cursor mcursor2 = mDatabase.rawQuery(count1, null);
                    mcursor2.moveToFirst();
                    int icount1 = mcursor2.getInt(0);
                    String res = String.valueOf(icount1);

                    String APP_NAME = mcursor2.getString(1);
                    String COMPANY_ADDRESS = mcursor2.getString(2);
                    String COMPANY_TELEPHONE = mcursor2.getString(3);
                    String FOOTER_1 = mcursor2.getString(4);
                    String FOOTER_2 = mcursor2.getString(5);

                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  APP_NAME+" "+COMPANY_ADDRESS+" "+COMPANY_TELEPHONE+ " "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+FOOTER_1+" " +FOOTER_2;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }



                }else {

                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  ReceiptHeader+" "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+ReceiptFooter;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }

                buttoncharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String a = edttextcash.getText().toString();

                        if (a.isEmpty()) {

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();

                        }else if (a.equals(".")){

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                        }else {

                            Double edttxtcashIn, Change, res;
                            String cashIn = edttextcash.getText().toString();
                            edttxtcashIn = Double.valueOf(cashIn);
                            res = edttxtcashIn - convfinalTOTALPRICE;
                            String result = String.valueOf(res);

                            if (edttxtcashIn < convfinalTOTALPRICE) {

                                Toast.makeText(PaymentDialog.this,"Insufficient Amount", Toast.LENGTH_LONG).show();

                            } else {

                                String count = "SELECT count(*) FROM salesitems";
                                Cursor mcursor = mDatabase.rawQuery(count, null);
                                mcursor.moveToFirst();
                                int icount = mcursor.getInt(0);

                                if(icount>0){

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    String countID = "SELECT _ID FROM salesitems ORDER BY _ID DESC";
                                    Cursor mcursorID = mDatabase.rawQuery(countID, null);
                                    mcursorID.moveToFirst();
                                    int ID = mcursorID.getInt(0);
                                    int newID = ID + 1;

                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from " + CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }else {

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    int defaultID = 1;

                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from "+ CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", convfinalTOTALPRICE);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }
                                // Toast.makeText(PaymentDialog.this, res.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });

            }else if (thisCurrency.equals("Korea Won")){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.KOREA));

                final String remfinalSUBTOTAL,remfinalDISCOUNT,remfinalTOTALPRICE;
                final String remfinalSUBTOTAL1,remfinalDISCOUNT1,remfinalTOTALPRICE1;
                final Double convfinalSUBTOTAL, convfinalDISCOUNT,convfinalTOTALPRICE;

                remfinalSUBTOTAL = finalSUBTOTAL.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalDISCOUNT = finalDISCOUNT.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");
                remfinalTOTALPRICE = finalTOTALPRICE.replace("₱", "").replace("₩", "")
                        .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                        .replace("CN¥","");

                remfinalSUBTOTAL1 = remfinalSUBTOTAL.replace(",", "");
                remfinalDISCOUNT1 = remfinalDISCOUNT.replace(",", "");
                remfinalTOTALPRICE1 = remfinalTOTALPRICE.replace(",", "");

                convfinalSUBTOTAL = Double.valueOf(remfinalSUBTOTAL1);
                convfinalDISCOUNT = Double.valueOf(remfinalDISCOUNT1);
                convfinalTOTALPRICE = Double.valueOf(remfinalTOTALPRICE1);

                txttotal.setText(format.format(convfinalTOTALPRICE));
                txtdis.setText(format.format(convfinalDISCOUNT));
                txtsub.setText(format.format(convfinalSUBTOTAL));

                String subt = format.format(convfinalSUBTOTAL);
                String subd = format.format(convfinalDISCOUNT);
                String pinakaTOTAL = format.format(convfinalTOTALPRICE);

                final String Detailssub = "\n\nSubtotal: "+subt ;
                final String DetailsDiscount = "\nDiscount: "+subd ;
                final String DetailsTotalPrice = "\nTotal Price: "+pinakaTOTAL ;

                String receiptsettings = "SELECT * FROM receiptsettings";
                Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
                receiptcursor.moveToFirst();

                int counter = receiptcursor.getCount();

                if(counter >0) {
                    String count1 = "SELECT * FROM receiptsettings WHERE _ID = 1";
                    Cursor mcursor2 = mDatabase.rawQuery(count1, null);
                    mcursor2.moveToFirst();
                    int icount1 = mcursor2.getInt(0);
                    String res = String.valueOf(icount1);

                    String APP_NAME = mcursor2.getString(1);
                    String COMPANY_ADDRESS = mcursor2.getString(2);
                    String COMPANY_TELEPHONE = mcursor2.getString(3);
                    String FOOTER_1 = mcursor2.getString(4);
                    String FOOTER_2 = mcursor2.getString(5);

                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  APP_NAME+" "+COMPANY_ADDRESS+" "+COMPANY_TELEPHONE+ " "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+FOOTER_1+" " +FOOTER_2;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }


                }else {

                    String paddedFinalReceipt = "\n\n" +finalreceipt;

                    inputValue =  ReceiptHeader+" "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+ReceiptFooter;

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                    } catch (WriterException e) {


                    }

                }

                buttoncharge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String a = edttextcash.getText().toString();

                        if (a.isEmpty()) {

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();

                        }else if (a.equals(".")){

                            Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                        }else {

                            Double edttxtcashIn, Change, res;
                            String cashIn = edttextcash.getText().toString();
                            edttxtcashIn = Double.valueOf(cashIn);
                            res = edttxtcashIn - convfinalTOTALPRICE;
                            String result = String.valueOf(res);

                            if (edttxtcashIn < convfinalTOTALPRICE) {

                                Toast.makeText(PaymentDialog.this,"Insufficient Amount", Toast.LENGTH_LONG).show();

                            } else {

                                //Increment Value from database
                                String count = "SELECT count(*) FROM salesitems";
                                Cursor mcursor = mDatabase.rawQuery(count, null);
                                mcursor.moveToFirst();
                                int icount = mcursor.getInt(0);

                                if(icount>0){

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    String countID = "SELECT _ID FROM salesitems ORDER BY _ID DESC";
                                    Cursor mcursorID = mDatabase.rawQuery(countID, null);
                                    mcursorID.moveToFirst();
                                    int ID = mcursorID.getInt(0);
                                    int newID = ID + 1;

                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);


                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from " + CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }else {

                                    String receiptnumber = txtreceiptnumber.getText().toString();
                                    int defaultID = 1;

                                    String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql1);

                                    String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                            CartContract.CartEntry.TABLE_NAME;
                                    mDatabase.execSQL(sql2);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    ContentValues cv = new ContentValues();
                                    cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                    cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                    cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                    cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                    cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                    mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                    mDatabase.execSQL("delete from "+ CartContract.CartEntry.TABLE_NAME);

                                    Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                    intent.putExtra("totalprice", convfinalTOTALPRICE);
                                    intent.putExtra("change", result);
                                    startActivity(intent);

                                    finish();

                                }
                                Toast.makeText(PaymentDialog.this, res.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });

            }

        }else{

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

            final String remfinalSUBTOTAL,remfinalDISCOUNT,remfinalTOTALPRICE;
            final String remfinalSUBTOTAL1,remfinalDISCOUNT1,remfinalTOTALPRICE1;
            final Double convfinalSUBTOTAL, convfinalDISCOUNT,convfinalTOTALPRICE;

            remfinalSUBTOTAL = finalSUBTOTAL.replace("₱", "").replace("₩", "")
                    .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                    .replace("CN¥","");
            remfinalDISCOUNT = finalDISCOUNT.replace("₱", "").replace("₩", "")
                    .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                    .replace("CN¥","");
            remfinalTOTALPRICE = finalTOTALPRICE.replace("₱", "").replace("₩", "")
                    .replace("CA$","").replace("¥","").replace("$","").replace("€","")
                    .replace("CN¥","");

            remfinalSUBTOTAL1 = remfinalSUBTOTAL.replace(",", "");
            remfinalDISCOUNT1 = remfinalDISCOUNT.replace(",", "");
            remfinalTOTALPRICE1 = remfinalTOTALPRICE.replace(",", "");

            convfinalSUBTOTAL = Double.valueOf(remfinalSUBTOTAL1);
            convfinalDISCOUNT = Double.valueOf(remfinalDISCOUNT1);
            convfinalTOTALPRICE = Double.valueOf(remfinalTOTALPRICE1);

            txttotal.setText(format.format(convfinalTOTALPRICE));
            txtdis.setText(format.format(convfinalDISCOUNT));
            txtsub.setText(format.format(convfinalSUBTOTAL));

            String subt = format.format(convfinalSUBTOTAL);
            String subd = format.format(convfinalDISCOUNT);
            String pinakaTOTAL = format.format(convfinalTOTALPRICE);

            final String Detailssub = "\n\nSubtotal: "+subt ;
            final String DetailsDiscount = "\nDiscount: "+subd ;
            final String DetailsTotalPrice = "\nTotal Price: "+pinakaTOTAL ;


            String receiptsettings = "SELECT * FROM receiptsettings";
            Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
            receiptcursor.moveToFirst();

            int counter = receiptcursor.getCount();

            if(counter >0) {
                String count1 = "SELECT * FROM receiptsettings WHERE _ID = 1";
                Cursor mcursor2 = mDatabase.rawQuery(count1, null);
                mcursor2.moveToFirst();
                int icount1 = mcursor2.getInt(0);
                String res = String.valueOf(icount1);

                String APP_NAME = mcursor2.getString(1);
                String COMPANY_ADDRESS = mcursor2.getString(2);
                String COMPANY_TELEPHONE = mcursor2.getString(3);
                String FOOTER_1 = mcursor2.getString(4);
                String FOOTER_2 = mcursor2.getString(5);
                String paddedFinalReceipt = "\n\n" +finalreceipt;

                inputValue =  APP_NAME+" "+COMPANY_ADDRESS+" "+COMPANY_TELEPHONE+ " "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+FOOTER_1+" " +FOOTER_2;

                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;

                qrgEncoder = new QRGEncoder(
                        inputValue, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                try {
                    bitmap = qrgEncoder.encodeAsBitmap();
                    qrImage.setImageBitmap(bitmap);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                } catch (WriterException e) {


                }



            }else {

                String paddedFinalReceipt = "\n\n" +finalreceipt;

                inputValue =  ReceiptHeader+" "+paddedFinalReceipt+""+Detailssub+" "+DetailsDiscount+" "+DetailsTotalPrice+" "+ReceiptFooter;

                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;

                qrgEncoder = new QRGEncoder(
                        inputValue, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                try {
                    bitmap = qrgEncoder.encodeAsBitmap();
                    qrImage.setImageBitmap(bitmap);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);

                } catch (WriterException e) {


                }

            }


            buttoncharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    String a = edttextcash.getText().toString();

                    if (a.isEmpty()) {

                        Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();


                    }else if (a.equals(".")){

                        Toast.makeText(PaymentDialog.this, "Input Value", Toast.LENGTH_LONG).show();
                    }else {

                        Double edttxtcashIn, Change, res;
                        String cashIn = edttextcash.getText().toString();
                        edttxtcashIn = Double.valueOf(cashIn);
                        res = edttxtcashIn - convfinalTOTALPRICE;
                        String result = String.valueOf(res);

                        if (edttxtcashIn < convfinalTOTALPRICE) {

                            Toast.makeText(PaymentDialog.this,"Insufficient Amount", Toast.LENGTH_LONG).show();

                        } else {

                            String count = "SELECT count(*) FROM salesitems";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            if(icount>0){

                                String receiptnumber = txtreceiptnumber.getText().toString();
                                String countID = "SELECT _ID FROM salesitems ORDER BY _ID DESC";
                                Cursor mcursorID = mDatabase.rawQuery(countID, null);
                                mcursorID.moveToFirst();
                                int ID = mcursorID.getInt(0);

                                int newID = ID + 1;

                                String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                        CartContract.CartEntry.TABLE_NAME;
                                mDatabase.execSQL(sql1);

                                String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                        CartContract.CartEntry.TABLE_NAME;
                                mDatabase.execSQL(sql2);

                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                String formattedDate = df.format(c);

                                ContentValues cv = new ContentValues();
                                cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                mDatabase.execSQL("delete from " + CartContract.CartEntry.TABLE_NAME);

                                Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                intent.putExtra("change", result);
                                startActivity(intent);

                                finish();

                            }else if (icount == 0){

                                String receiptnumber = txtreceiptnumber.getText().toString();
                                int defaultID = 1;

                                String sql1 = "INSERT INTO " + SalesItemsContract.SalesItemsEntry.TABLE_NAME + " SELECT * FROM " +
                                        CartContract.CartEntry.TABLE_NAME;
                                mDatabase.execSQL(sql1);

                                String sql2 = "INSERT INTO " + SalesSummaryContract.SalesSummaryEntry.TABLE_NAME + " SELECT * FROM " +
                                        CartContract.CartEntry.TABLE_NAME;
                                mDatabase.execSQL(sql2);

                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                String formattedDate = df.format(c);

                                ContentValues cv = new ContentValues();
                                cv.put(SalesContract.SalesEntry.KEY_RECEIPTNO, receiptnumber);
                                cv.put(SalesContract.SalesEntry.KEY_DATE, formattedDate);
                                cv.put(SalesContract.SalesEntry.KEY_SUBTOTAL, convfinalSUBTOTAL);
                                cv.put(SalesContract.SalesEntry.KEY_TOTALDISCOUNT, convfinalDISCOUNT);
                                cv.put(SalesContract.SalesEntry.KEY_TOTALPRICE, convfinalTOTALPRICE);
                                cv.put(SalesContract.SalesEntry.KEY_QRCODE, a_thumbnail.toByteArray());

                                mDatabase.insert(SalesContract.SalesEntry.TABLE_NAME, null, cv);
                                mDatabase.execSQL("delete from "+ CartContract.CartEntry.TABLE_NAME);

                                Intent intent = new Intent(PaymentDialog.this, EndOfTransactionActivity.class);
                                intent.putExtra("totalprice", remfinalTOTALPRICE1);
                                intent.putExtra("change", result);
                                startActivity(intent);

                                finish();



                            }


                            Toast.makeText(PaymentDialog.this, res.toString(), Toast.LENGTH_LONG).show();

                        }
                    }

                }
            });

        }

        String countsales = "SELECT count(*) FROM sales";
        Cursor mcursorsales = mDatabase.rawQuery(countsales, null);
        mcursorsales.moveToFirst();
        int icountsales = mcursorsales.getInt(0);

        if(icountsales>0){

            String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
            Cursor mcursor1 = mDatabase.rawQuery(count1, null);
            mcursor1.moveToFirst();
            int icount1 = mcursor1.getInt(0);
            int newID = icount1 + 1;
            String res = String.valueOf(icount1);

            txtreceiptnumber.setText("R-"+newID);

        }else {

            int defaultID = icount + 1;
            txtreceiptnumber.setText("R-"+defaultID);
        }

    }


    public String[] quantityProducts(String tablename, String[] purchasedProductID) {


            try {
                SQLiteDatabase db = myDb.getReadableDatabase();
                Cursor x = db.rawQuery("SELECT * FROM " + tablename + " WHERE PRODUCTCODE = " + purchasedProductID, null);
                int n = x.getCount();
                x.moveToFirst();
                String[] a = new String[n];
                int i = 0;
                do {
                    a[i] = x.getString(x.getColumnIndex("PRODUCTQUANTITY"));
                    i++;
                } while (x.moveToNext());

                x.close();
                return a;
            } catch (Exception e) {
                return null;
            }

    }


    public void updateStocks(String[] productIDs) {
        SQLiteDatabase db = myDb.getWritableDatabase();
        if (db != null) {
            db.beginTransaction();
            try {
                for(String id : productIDs){
                    ContentValues data = new ContentValues();
                    mDatabase.execSQL("UPDATE  " + ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + id + "'" +
                            " WHERE PRODUCTCODE='" + id + "'");
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            db.close();
        }
    }

    public String[] getCurrentPQuantity(String tablename, String[] productCode)
    {

        try {
            SQLiteDatabase db = myDb.getReadableDatabase();

            String[] pCodes = productCode;
            String inClause = pCodes.toString();

            inClause = inClause.replace("[","(");
            inClause = inClause.replace("]",")");

            Cursor x = db.rawQuery("SELECT * FROM "+tablename+" WHERE PRODUCTCODE = "+productCode+"" , null);
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

    @Override
    protected void onResume() {
        super.onResume();

    }


   /* public void keypad(){

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "1");

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "2");

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "3");

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "4");

            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "5");

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "6");

            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "7");

            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "8");

            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "9");

            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText(edttextcash.getText() + "0");

            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edttextcash.setText("");


            }
        });

    }


    */



}
