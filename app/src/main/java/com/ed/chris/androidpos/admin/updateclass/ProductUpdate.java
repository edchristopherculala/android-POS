package com.ed.chris.androidpos.admin.updateclass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProductUpdate extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;


    TextView txtproductCategory,txtsupplierName;
    EditText edttxtproductDescription,edttxtdateofValidity,edttxtdateofExpiry,edttxtbuyPrice,edttxtsellPrice;
    Button buttonUpdateProducts;

    Spinner allProductCategory, allProductSupplierName ;

    List<String> ProductCategoryList =new ArrayList<>();
    ArrayAdapter<String> Categoryadapter;

    List<String> ProductSupplierList =new ArrayList<>();
    ArrayAdapter<String> Supplieradapter;


    ImageView previewImage;
    Button imageChooser;

    final Calendar myCalendar = Calendar.getInstance();

    private int REQUEST_CODE = 1;

    private ByteArrayOutputStream a_thumbnail = new ByteArrayOutputStream();

    boolean clicked=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);




        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        allProductCategory = findViewById(R.id.spinnerProductCategoryUPDATE);
        allProductSupplierName = findViewById(R.id.spinnerSupplierUPDATE);



        txtproductCategory = findViewById(R.id.txtviewPRODUCTCATEGORYupdate);
        txtsupplierName = findViewById(R.id.txtviewSUPPLIERupdate);

        edttxtproductDescription = findViewById(R.id.edttxtPRODUCTNAMEupdate);
        edttxtdateofValidity = findViewById(R.id.edttxtDATEOFVALIDITYupdate);
        edttxtdateofExpiry = findViewById(R.id.edttxtDATEOFEXPIRYupdate);
        edttxtbuyPrice = findViewById(R.id.edttxtBUYPRICEupdate);
        edttxtsellPrice = findViewById(R.id.edttxtSELLPRICEupdate);

        buttonUpdateProducts = findViewById(R.id.buttonUPDATEPRODUCT);

        previewImage = findViewById(R.id.buttonCameraUpdate);
        imageChooser = findViewById(R.id.pickImageUpdate);


        prepareDataProductCategory();
        prepareDataProductSupplierName();


        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked = true;

                selectImage(ProductUpdate.this);
            }
        });



        imageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked=true;

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
            }
        });



        Intent intent = getIntent();

        String ProductNAME = intent.getStringExtra("productname");
        final String ProductCATEGORY = intent.getStringExtra("productcategory");
        final String ProductSUPPLIER = intent.getStringExtra("productsuppliername");
        String DATEOFVALIDITY = intent.getStringExtra("dateofvalidity");
        String DATEOFEXPIRY = intent.getStringExtra("dateofexpiry");
        String BUYPRICE = intent.getStringExtra("buyprice");
        String SELLPRICE = intent.getStringExtra("sellprice");
        String UNIQUEID = intent.getStringExtra("ID");






        String count3 = "Select * From products where _ID = '" + UNIQUEID + "'";


        Cursor cursor = mDatabase.rawQuery(count3, new String[] {});

        cursor.moveToFirst();
        byte[] blob = cursor.getBlob(cursor.getColumnIndex("PRODUCTIMAGE"));
        Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);




        edttxtproductDescription.setText(ProductNAME);
        txtproductCategory.setText(ProductCATEGORY);
        txtsupplierName.setText(ProductSUPPLIER);
        edttxtdateofValidity.setText(DATEOFVALIDITY);
        edttxtdateofExpiry.setText(DATEOFEXPIRY);
        edttxtbuyPrice.setText(BUYPRICE);
        edttxtsellPrice.setText(SELLPRICE);

        edttxtproductDescription.setEnabled(false);

        previewImage.setImageBitmap(bmp);




        //DateDialog for Validity

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

                new DatePickerDialog(ProductUpdate.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        edttxtdateofExpiry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(ProductUpdate.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonUpdateProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });



        allProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // clicked item will be shown as spinner
                String productCATEGORY = parent.getItemAtPosition(position).toString();



                txtproductCategory.setText(ProductCATEGORY);
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


                txtsupplierName.setText(ProductSUPPLIER);
                // Toast.makeText(getActivity(),""+parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




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


    private void updateItem(){



        Intent intent = getIntent();

        String ProductNAME = intent.getStringExtra("productname");
        String ProductCATEGORY = intent.getStringExtra("productcategory");
        String ProductSUPPLIER = intent.getStringExtra("productsuppliername");
        String DATEOFVALIDITY = intent.getStringExtra("dateofvalidity");
        String DATEOFEXPIRY = intent.getStringExtra("dateofexpiry");
        String BUYPRICE = intent.getStringExtra("buyprice");
        String SELLPRICE = intent.getStringExtra("sellprice");
        String UNIQUEID = intent.getStringExtra("ID");

        txtproductCategory = findViewById(R.id.txtviewPRODUCTCATEGORYupdate);
        txtsupplierName = findViewById(R.id.txtviewSUPPLIERupdate);

        edttxtproductDescription = findViewById(R.id.edttxtPRODUCTNAMEupdate);
        edttxtdateofValidity = findViewById(R.id.edttxtDATEOFVALIDITYupdate);
        edttxtdateofExpiry = findViewById(R.id.edttxtDATEOFEXPIRYupdate);
        edttxtbuyPrice = findViewById(R.id.edttxtBUYPRICEupdate);
        edttxtsellPrice = findViewById(R.id.edttxtSELLPRICEupdate);



        String PRODUCTCATEGORY = txtproductCategory.getText().toString();
        String PRODUCTSUPPLIERNAME = txtsupplierName.getText().toString();

        String PRODUCTDESCRIPTION = edttxtproductDescription.getText().toString().toUpperCase();
        String PRODUCTDATEOFVALIDITY = edttxtdateofValidity.getText().toString();
        String PRODUCTDATEOFEXPIRY = edttxtdateofExpiry.getText().toString();
        String PRODUCTBUYPRICE = edttxtbuyPrice.getText().toString().trim();
        String PRODUCTSELLPRICE = edttxtsellPrice.getText().toString().trim();


        if (PRODUCTSUPPLIERNAME.isEmpty() && PRODUCTDESCRIPTION.isEmpty() && PRODUCTDATEOFVALIDITY.isEmpty() && PRODUCTDATEOFEXPIRY.isEmpty()
                && PRODUCTBUYPRICE.isEmpty() && PRODUCTSELLPRICE.isEmpty()){

            Toast.makeText(this,"Please Input Product Data!",Toast.LENGTH_LONG).show();

        }else if (PRODUCTCATEGORY.isEmpty() ){

            Toast.makeText(this,"Please Choose Product Category!",Toast.LENGTH_LONG).show();

        }else if (PRODUCTSUPPLIERNAME.isEmpty() ){

            Toast.makeText(this,"Please Choose Supplier Name!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTDESCRIPTION.isEmpty() ){

            Toast.makeText(this,"Please Input Product Description!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTDATEOFVALIDITY.isEmpty() ){

            Toast.makeText(this,"Please Select Product Date of Validity!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTDATEOFEXPIRY.isEmpty() ){

            Toast.makeText(this,"Please Select Product Date of Expiry!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTBUYPRICE.isEmpty() ){

            Toast.makeText(this,"Please Input Product Buy Price!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTSELLPRICE.isEmpty() ){

            Toast.makeText(this,"Please Input Product Sell Price!",Toast.LENGTH_LONG).show();
        }else if (PRODUCTBUYPRICE.equals(".")){

            Toast.makeText(this,"Please enter a valid buy price!",Toast.LENGTH_LONG).show();
        }
        else if (PRODUCTSELLPRICE.equals(".") ){

            Toast.makeText(this,"Please enter a valid sell price!",Toast.LENGTH_LONG).show();
        }
        else {

            String pname = edttxtproductDescription.getText().toString().trim().toUpperCase();
            String pcategory = txtproductCategory.getText().toString().trim();
            String psupplier = txtsupplierName.getText().toString().trim();
            String pdateofvalidity = edttxtdateofValidity.getText().toString().trim();
            String pdateofexpiry = edttxtdateofExpiry.getText().toString().trim();
            String buyprice = edttxtbuyPrice.getText().toString();
            String sellprice = edttxtsellPrice.getText().toString();


            String where = "_ID" + "=" + UNIQUEID;


            mDatabase.execSQL("UPDATE  " + ProductMaintenanceContract.ProductMaintenanceEntry.TABLE_NAME + " SET PRODUCTDESCRIPTION ='" + pname + "'," +
                    " PRODUCTCATEGORY ='" + pcategory + "',"+" PRODUCTSUPPLIERNAME = '"+psupplier+"',"+
                    " PRODUCTDATEOFVALIDITY = '"+pdateofvalidity+"',"+" PRODUCTDATEOFEXPIRY = '"+pdateofexpiry+"',"+
                    " PRODUCTBUYPRICE = '"+buyprice+"',"+" PRODUCTSELLPRICE = '"+sellprice+"' WHERE _ID='" + UNIQUEID + "'");


            if(clicked)
            {
                ContentValues cv = new ContentValues();
                cv.put(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_IMAGE, a_thumbnail.toByteArray());
                mDatabase.update("products", cv,where , null);

            }


            Toast.makeText(this, "Product Successfully Updated!", Toast.LENGTH_LONG).show();

            clicked = false;


            finish();
            Intent newIntent = new Intent(this, ProductMain.class);
            startActivity(newIntent);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null ) {


                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        selectedImage = getResizedBitmap(selectedImage, 400);// 400 is for example, replace with desired size
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 80, a_thumbnail);
                        previewImage.setImageBitmap(selectedImage);



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
                                previewImage.setImageBitmap(selectedImage);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();
        Intent newIntent = new Intent(this, ProductMain.class);
        startActivity(newIntent);

    }
}



