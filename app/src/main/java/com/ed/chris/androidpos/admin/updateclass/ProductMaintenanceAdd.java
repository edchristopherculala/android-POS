package com.ed.chris.androidpos.admin.updateclass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.ProductMain;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceContract;
import com.ed.chris.androidpos.database.DatabaseHelper;
import com.ed.chris.androidpos.login.LoginForm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;



public class ProductMaintenanceAdd extends AppCompatActivity implements View.OnClickListener{

    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;


    Bitmap thumbnail;

    private SQLiteDatabase mDatabase;
    private EmployeeTypeAdapter mAdapter;
    DatabaseHelper myDb;


    TextView txtproductCode,txtproductCategory,txtsupplierName;
    EditText edttxtproductDescription,edttxtdateofValidity,edttxtdateofExpiry,edttxtbuyPrice,edttxtsellPrice,edttxtqty;
    Button buttonAddProducts;

    ImageView cameraChoose;
    Button pickIMAGE;

    Spinner allProductCategory, allProductSupplierName ;

    List<String> ProductCategoryList =new ArrayList<>();
    ArrayAdapter<String> Categoryadapter;

    List<String> ProductSupplierList =new ArrayList<>();
    ArrayAdapter<String> Supplieradapter;

    final Calendar myCalendar = Calendar.getInstance();

    private int REQUEST_CODE = 1;

    private ByteArrayOutputStream a_thumbnail = new ByteArrayOutputStream();

    private static final int REQUEST_CAPTURE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_maintenance_add);





        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        allProductCategory = findViewById(R.id.spinnerProductCategory);
        allProductSupplierName = findViewById(R.id.spinnerSupplierName);


        txtproductCode = findViewById(R.id.txtviewproductCode);
        txtproductCategory = findViewById(R.id.txtviewproductCategory);
        txtsupplierName = findViewById(R.id.txtviewsupplierName1);

        edttxtproductDescription = findViewById(R.id.edittxtproductDescription);
        edttxtdateofValidity = findViewById(R.id.edittxtdateofValidity);
        edttxtdateofExpiry = findViewById(R.id.edittxtdateofExpiry);
        edttxtbuyPrice = findViewById(R.id.edittxtBuyPrice);
        edttxtsellPrice = findViewById(R.id.edittxtSellPrice);
        edttxtqty = findViewById(R.id.edittxtQTY);

        cameraChoose = findViewById(R.id.buttonCamera);
        pickIMAGE = findViewById(R.id.pickImage);

        buttonAddProducts = findViewById(R.id.btnAddProducts);

        prepareDataProductCategory();
        prepareDataProductSupplierName();


        // For Camera
        cameraChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage(ProductMaintenanceAdd.this);
            }
        });

        //FOR IMAGE

        pickIMAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
            }
        });


        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };


        //DateDialog for Expiry

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        edttxtdateofValidity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(ProductMaintenanceAdd.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        edttxtdateofExpiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(ProductMaintenanceAdd.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        buttonAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Add Item
                addItem();
            }
        });




        allProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // clicked item will be shown as spinner
                String productCATEGORY = parent.getItemAtPosition(position).toString();

                txtproductCategory.setText(productCATEGORY);
                // Toast.makeText(getActivity(),""+parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        allProductSupplierName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // clicked item will be shown as spinner
                String supplierName = parent.getItemAtPosition(position).toString();

                txtsupplierName.setText(supplierName);
                // Toast.makeText(getActivity(),""+parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        //Increment Value from database
        String count = "SELECT count(*) FROM products";
        Cursor mcursor = mDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        String a = String.valueOf(icount);

        // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

        if(icount>0){

            //select max may DATA

            // Toast.makeText(this,a,Toast.LENGTH_LONG).show();

            String count1 = "SELECT _ID FROM products ORDER BY _ID DESC";
            Cursor mcursor1 = mDatabase.rawQuery(count1, null);
            mcursor1.moveToFirst();
            int icount1 = mcursor1.getInt(0);

            int newID = icount1 + 1;
            String res = String.valueOf(icount1);

            // Toast.makeText(this,res,Toast.LENGTH_LONG).show();

            txtproductCode.setText("P-" +newID);

        }else {


            //WORKING CODE
            //Toast.makeText(this,a,Toast.LENGTH_LONG).show();

            int defaultID = icount + 1;

            txtproductCode.setText("P-" +defaultID);





        }



    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edttxtdateofExpiry.setText(sdf.format(myCalendar.getTime()));
    }


    private void updateLabel1() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edttxtdateofValidity.setText(sdf.format(myCalendar.getTime()));
    }


    private void addItem(){

        String PRODUCTCODE, PRODUCTCATEGORY, PRODUCTSUPPLIERNAME, PRODUCTDESCRIPTION, PRODUCTDATEOFVALIDITY,
                PRODUCTDATEOFEXPIRY, PRODUCTBUYPRICE, PRODUCTSELLPRICE, PRODUCTQUANTITY ;

        PRODUCTCODE = txtproductCode.getText().toString();
        PRODUCTCATEGORY = txtproductCategory.getText().toString();
        PRODUCTSUPPLIERNAME = txtsupplierName.getText().toString();

        PRODUCTDESCRIPTION = edttxtproductDescription.getText().toString().toUpperCase();
        PRODUCTDATEOFVALIDITY = edttxtdateofValidity.getText().toString();
        PRODUCTDATEOFEXPIRY = edttxtdateofExpiry.getText().toString();
        PRODUCTBUYPRICE = edttxtbuyPrice.getText().toString().trim();
        PRODUCTSELLPRICE = edttxtsellPrice.getText().toString().trim();

        PRODUCTQUANTITY = edttxtqty.getText().toString();

        //get the id of the supplier

        if (PRODUCTQUANTITY.isEmpty() && PRODUCTCATEGORY.isEmpty() && PRODUCTSUPPLIERNAME.isEmpty() && PRODUCTDESCRIPTION.isEmpty() && PRODUCTDATEOFVALIDITY.isEmpty() && PRODUCTDATEOFEXPIRY.isEmpty()
                && PRODUCTBUYPRICE.isEmpty() && PRODUCTSELLPRICE.isEmpty()){

            Toast.makeText(this,"Please Input Product Data!",Toast.LENGTH_LONG).show();

        }else if (PRODUCTCATEGORY.isEmpty() ){

            Toast.makeText(this,"Please Choose Product Category!",Toast.LENGTH_LONG).show();

        }else if (PRODUCTSUPPLIERNAME.isEmpty() ){

            Toast.makeText(this,"Please Choose Supplier Name!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTDESCRIPTION.isEmpty() ){

            Toast.makeText(this,"Please Input Product Description!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTBUYPRICE.isEmpty() ){

            Toast.makeText(this,"Please Input Product Buy Price!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTSELLPRICE.isEmpty() ){

            Toast.makeText(this,"Please Input Product Sell Price!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTBUYPRICE.equals("0") ){

            Toast.makeText(this,"Please Input Product Sell Price!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTBUYPRICE.equals(".")){

            Toast.makeText(this,"Please enter a valid buy price!",Toast.LENGTH_LONG).show();
        }
        else if (PRODUCTSELLPRICE.equals(".") ){

            Toast.makeText(this,"Please enter a valid sell price!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTQUANTITY.isEmpty() ){

            Toast.makeText(this,"Please enter a product quantity",Toast.LENGTH_LONG).show();
        }
        else if (PRODUCTDATEOFVALIDITY.isEmpty() && PRODUCTDATEOFEXPIRY.isEmpty()) {

            String typeProductDesc, typeProductSupplier, queryProductDesc, queryProductSupplier;

            typeProductDesc = edttxtproductDescription.getText().toString();
            typeProductSupplier = txtsupplierName.getText().toString();

            queryProductDesc = "Select * From products where PRODUCTDESCRIPTION = '" + PRODUCTDESCRIPTION + "'";
            queryProductSupplier = "Select * From products where PRODUCTSUPPLIERNAME = '" + PRODUCTSUPPLIERNAME + "'";

            if (mDatabase.rawQuery(queryProductDesc, null).getCount() > 0  && mDatabase.rawQuery(queryProductSupplier,
                    null).getCount() > 0 ) {
                Toast.makeText(this, "" + typeProductDesc + "  is already registered in the system!", Toast.LENGTH_SHORT).show();
            } else {
                String count2, ba, count3, Pcategory, startvalidity, endvalidity, naCategory, naSupplier;
                Cursor mcursor2, mcursor3;
                int icount2, icount3, dayOfWeek, dayOfMonth;
                Calendar calendar;

                calendar = Calendar.getInstance(TimeZone.getDefault());
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                startvalidity = dayOfMonth+"/"+dayOfWeek+"/2000";
                endvalidity = dayOfMonth+"/"+dayOfWeek+"/3000";

                naCategory = "N/A";
                naSupplier = "N/A";

                if (txtproductCategory.getText().toString().equals("N/A") || txtsupplierName.getText().toString().equals("N/A")) {

                    ContentValues cv = new ContentValues();
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE, PRODUCTCODE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY, PRODUCTCATEGORY);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION, PRODUCTDESCRIPTION);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_NAME, PRODUCTSUPPLIERNAME);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFVALIDITY, startvalidity);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY, endvalidity);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_BUY_PRICE, PRODUCTBUYPRICE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE, PRODUCTSELLPRICE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_ID, naSupplier);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY_ID, naCategory);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY, PRODUCTQUANTITY);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_IMAGE, a_thumbnail.toByteArray());

                    mDatabase.insert(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME, null, cv);

                    Toast.makeText(this, "" + typeProductDesc + " was successfully added!", Toast.LENGTH_SHORT).show();

                    edttxtproductDescription.getText().clear();
                    edttxtdateofValidity.getText().clear();
                    edttxtdateofExpiry.getText().clear();
                    edttxtbuyPrice.getText().clear();
                    edttxtsellPrice.getText().clear();
                    edttxtqty.getText().clear();



                    String count = "SELECT count(*) FROM products";
                    Cursor mcursor = mDatabase.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);

                    String a = String.valueOf(icount);
                    if(icount>0) {


                        String count1 = "SELECT _ID FROM products ORDER BY _ID DESC";
                        Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                        mcursor1.moveToFirst();
                        int icount1 = mcursor1.getInt(0);
                        String res = String.valueOf(icount1);


                        int newID = icount1 + 1;

                        txtproductCode.setText("P-" + newID);


                        Intent intent = new Intent(ProductMaintenanceAdd.this, ProductMain.class);
                        startActivity(intent);
                        finish();

                    }
                }else{



                    count2 = "Select _ID From supplier where SUPPLIERNAME = '" + PRODUCTSUPPLIERNAME + "'";
                mcursor2 = mDatabase.rawQuery(count2, null);
                mcursor2.moveToFirst();
                icount2 = mcursor2.getInt(0);

                ba = String.valueOf(icount2);


                count3 = "Select _ID From productcategory where CATEGORY = '" + PRODUCTCATEGORY + "'";
                mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                icount3 = mcursor3.getInt(0);

                Pcategory = String.valueOf(icount3);
                calendar = Calendar.getInstance(TimeZone.getDefault());
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                startvalidity = dayOfMonth+"/"+dayOfWeek+"/2000";
                endvalidity = dayOfMonth+"/"+dayOfWeek+"/3000";

                ContentValues cv = new ContentValues();
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE, PRODUCTCODE);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY, PRODUCTCATEGORY);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION, PRODUCTDESCRIPTION);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_NAME, PRODUCTSUPPLIERNAME);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFVALIDITY, startvalidity);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY, endvalidity);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_BUY_PRICE, PRODUCTBUYPRICE);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE, PRODUCTSELLPRICE);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_ID, ba);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY_ID, Pcategory);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY, PRODUCTQUANTITY);
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_IMAGE, a_thumbnail.toByteArray());

                mDatabase.insert(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME, null, cv);

                Toast.makeText(this, "" + typeProductDesc + " was successfully added!", Toast.LENGTH_SHORT).show();

                edttxtproductDescription.getText().clear();
                edttxtdateofValidity.getText().clear();
                edttxtdateofExpiry.getText().clear();
                edttxtbuyPrice.getText().clear();
                edttxtsellPrice.getText().clear();
                edttxtqty.getText().clear();



                String count = "SELECT count(*) FROM products";
                Cursor mcursor = mDatabase.rawQuery(count, null);
                mcursor.moveToFirst();
                int icount = mcursor.getInt(0);

                String a = String.valueOf(icount);
                if(icount>0) {


                    String count1 = "SELECT _ID FROM products ORDER BY _ID DESC";
                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                    mcursor1.moveToFirst();
                    int icount1 = mcursor1.getInt(0);
                    String res = String.valueOf(icount1);


                    int newID = icount1 + 1;

                    txtproductCode.setText("P-" + newID);


                    Intent intent = new Intent(ProductMaintenanceAdd.this, ProductMain.class);
                    startActivity(intent);
                    finish();

                }
                }

            }




        }else if(PRODUCTDATEOFEXPIRY.length() > 0 && PRODUCTDATEOFVALIDITY.length() > 0) {



            String typeProductDesc = edttxtproductDescription.getText().toString();
            String typeProductSupplier = txtsupplierName.getText().toString();




            String queryProductDesc = "Select * From products where PRODUCTDESCRIPTION = '" + PRODUCTDESCRIPTION + "'";
            String queryProductSupplier = "Select * From products where PRODUCTSUPPLIERNAME = '" + PRODUCTSUPPLIERNAME + "'";
            if (mDatabase.rawQuery(queryProductDesc, null).getCount() > 0  && mDatabase.rawQuery(queryProductSupplier,
                    null).getCount() > 0 ) {
                Toast.makeText(this, "" + typeProductDesc + "  is already registered in the system!", Toast.LENGTH_SHORT).show();
            } else {

                txtproductCategory = findViewById(R.id.txtviewproductCategory);
                txtsupplierName = findViewById(R.id.txtviewsupplierName1);

                String naCategory, naSupplier;

                naCategory = "N/A";
                naSupplier = "N/A";

                if (txtproductCategory.getText().toString().equals("N/A") || txtsupplierName.getText().toString().equals("N/A")) {


                    ContentValues cv = new ContentValues();
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE, PRODUCTCODE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY, PRODUCTCATEGORY);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION, PRODUCTDESCRIPTION);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_NAME, PRODUCTSUPPLIERNAME);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFVALIDITY, PRODUCTDATEOFVALIDITY);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY, PRODUCTDATEOFEXPIRY);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_BUY_PRICE, PRODUCTBUYPRICE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE, PRODUCTSELLPRICE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_ID, naSupplier);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY_ID, naCategory);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY, PRODUCTQUANTITY);

                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_IMAGE, a_thumbnail.toByteArray());


                    mDatabase.insert(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME, null, cv);
                    // mAdapter.swapCursor(getAllItems());


                    Toast.makeText(this, "" + typeProductDesc + " was successfully added!", Toast.LENGTH_SHORT).show();

                    edttxtproductDescription.getText().clear();
                    edttxtdateofValidity.getText().clear();
                    edttxtdateofExpiry.getText().clear();
                    edttxtbuyPrice.getText().clear();
                    edttxtsellPrice.getText().clear();
                    edttxtqty.getText().clear();
                    a_thumbnail.reset();


                    String count = "SELECT count(*) FROM products";
                    Cursor mcursor = mDatabase.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);

                    String a = String.valueOf(icount);
                    if (icount > 0) {


                        String count1 = "SELECT _ID FROM products ORDER BY _ID DESC";
                        Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                        mcursor1.moveToFirst();
                        int icount1 = mcursor1.getInt(0);
                        String res = String.valueOf(icount1);


                        int newID = icount1 + 1;


                        //Toast.makeText(this,res+""+newID,Toast.LENGTH_LONG).show();


                        // Toast.makeText(this, res, Toast.LENGTH_LONG).show();

                        txtproductCode.setText("P-" + newID);


                        finish();
                    }

                } else {

                    String count2 = "Select _ID From supplier where SUPPLIERNAME = '" + PRODUCTSUPPLIERNAME + "'";
                    Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                    mcursor2.moveToFirst();
                    int icount2 = mcursor2.getInt(0);

                    String ba = String.valueOf(icount2);


                    String count3 = "Select _ID From productcategory where CATEGORY = '" + PRODUCTCATEGORY + "'";
                    Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                    mcursor3.moveToFirst();
                    int icount3 = mcursor3.getInt(0);

                    String Pcategory = String.valueOf(icount3);

                    ContentValues cv = new ContentValues();
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE, PRODUCTCODE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY, PRODUCTCATEGORY);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION, PRODUCTDESCRIPTION);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_NAME, PRODUCTSUPPLIERNAME);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFVALIDITY, PRODUCTDATEOFVALIDITY);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY, PRODUCTDATEOFEXPIRY);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_BUY_PRICE, PRODUCTBUYPRICE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE, PRODUCTSELLPRICE);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_ID, ba);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY_ID, Pcategory);
                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY, PRODUCTQUANTITY);

                    cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_IMAGE, a_thumbnail.toByteArray());


                    mDatabase.insert(ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME, null, cv);
                    // mAdapter.swapCursor(getAllItems());


                    Toast.makeText(this, "" + typeProductDesc + " was successfully added!", Toast.LENGTH_SHORT).show();

                    edttxtproductDescription.getText().clear();
                    edttxtdateofValidity.getText().clear();
                    edttxtdateofExpiry.getText().clear();
                    edttxtbuyPrice.getText().clear();
                    edttxtsellPrice.getText().clear();
                    edttxtqty.getText().clear();


                    String count = "SELECT count(*) FROM products";
                    Cursor mcursor = mDatabase.rawQuery(count, null);
                    mcursor.moveToFirst();
                    int icount = mcursor.getInt(0);

                    String a = String.valueOf(icount);
                    if (icount > 0) {


                        String count1 = "SELECT _ID FROM products ORDER BY _ID DESC";
                        Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                        mcursor1.moveToFirst();
                        int icount1 = mcursor1.getInt(0);
                        String res = String.valueOf(icount1);


                        int newID = icount1 + 1;


                        //Toast.makeText(this,res+""+newID,Toast.LENGTH_LONG).show();


                        // Toast.makeText(this, res, Toast.LENGTH_LONG).show();

                        txtproductCode.setText("P-" + newID);


                        finish();
                    }
                }



            }




        }

        //


    }

    //Prepare data for Spinner
    public void prepareDataProductCategory()
    {
        ProductCategoryList=myDb.getAllProductCategory();
        //adapter for spinner
        Categoryadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,ProductCategoryList);
        //attach adapter to spinner
        allProductCategory.setAdapter(Categoryadapter);
    }

    public void prepareDataProductSupplierName()
    {
        ProductSupplierList=myDb.getAllSupplierName();
        //adapter for spinner
        Supplieradapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,ProductSupplierList);
        //attach adapter to spinner
        allProductSupplierName.setAdapter(Supplieradapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int dataSize=0;

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null ) {


                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);
                        cameraChoose.setImageBitmap(selectedImage);



                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri uri =  data.getData();


                        try {

                            if (uri != null) {
                                InputStream imageStream = getContentResolver().openInputStream(uri);
                                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                                selectedImage.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);
                                cameraChoose.setImageBitmap(selectedImage);

                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                    }
                    break;
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void openCameraIntent(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (pictureIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.bmwaresd.chris.greatposmobile.provider",
                        photoFile);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent,REQUEST_CAPTURE_IMAGE);
            }
        }



    }

    String imageFilePath;


    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        imageFilePath = image.getAbsolutePath();
        return image;
    }


    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
