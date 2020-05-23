package com.ed.chris.androidpos.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeCreateAccountContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.ProductCategoryContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.SupplierCreateContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.ProductMaintenanceContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeLoginLogoutContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.StocksContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.CartContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.SalesItemsContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.SalesContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.SalesSummaryContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.ReceiptLayoutContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.TenLoginTrialContract.*;
import com.ed.chris.androidpos.admin.admin_adapters.CurrencySettingsContract.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

  /*  public static final String TAG = "DatabaseHelper";

    public static final String DATABASE_NAME = "greatPOS.db";

    public static final String TABLE_NAME = "employeetypetbl";
    public static final String COL1 = "ID";
    public static final String COL2 = "employeetype";
    public static final String COL3 = "description";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL("create table employeetypetbl (ID integer primary key autoincrement,employeetype text,description text)");

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+COL1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL2+" VARCHAR, "+COL3+" VARCHAR)";
        db.execSQL(CREATE_TABLE);



    }   */


    private static final int DB_VERSION = 31;

    //WORKING
    private static final String DB_NAME = "androidPOS.db";
    /* private static final String TABLE_NAME = "employeetype";
     private static final String KEY_ID = "id";
     private static final String KEY_EMPLOYEETYPE = "EMPLOYEETYPE";
     private static final String KEY_DESCRIPTION = "DESCRIPTION";  */
    private Context mContext;

    public DatabaseHelper(Context context){
        super(context,DB_NAME, null, DB_VERSION);
        mContext =context;
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        //WORKING

      /* String sql = "CREATE TABLE "+TABLE_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_EMPLOYEETYPE+" TEXT, "+KEY_DESCRIPTION+" TEXT)";
       db.execSQL(sql);  */



        //EMPLOYEE TYPE TABLE  -- EmployeeTypeEntry

        final String SQL_CREATE_EMPLOYEETYPE_TABLE = "CREATE TABLE " +
                EmployeeTypeEntry.TABLE_NAME + " (" +
                EmployeeTypeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EmployeeTypeEntry.KEY_EMPLOYEETYPE + " TEXT NOT NULL, " +
                EmployeeTypeEntry.KEY_DESCRIPTION + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_EMPLOYEETYPE_TABLE);

        //Employee Table -- EmployeeEntry

        final String SQL_CREATE_EMPLOYEE_TABLE = "CREATE TABLE " +
                EmployeeEntry.TABLE_NAME + " (" +
                EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EmployeeEntry.KEY_FIRTSNAME + " TEXT NOT NULL, " +
                EmployeeEntry.KEY_LASTNAME + " TEXT NOT NULL, " +
                EmployeeEntry.KEY_MOBILE + " TEXT NOT NULL, " +
                EmployeeEntry.KEY_EMAIL + " TEXT NOT NULL, " +
                EmployeeEntry.KEY_EMPLOYEETYPE + " TEXT NOT NULL, " +
                EmployeeEntry.KEY_ADDRESS + " TEXT NOT NULL," +
                EmployeeEntry.KEY_EMPLOYEETYPE_ID + " TEXT NOT NULL" +

                ");";
        db.execSQL(SQL_CREATE_EMPLOYEE_TABLE);


        //Employee ACcount Table -- EmployeeCreateAccount

        final String SQL_CREATE_EMPLOYEE_ACCOUNTS_TABLE = "CREATE TABLE " +
                EmployeeCreateAccountEntry.TABLE_NAME + " (" +
                EmployeeCreateAccountEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EmployeeCreateAccountEntry.KEY_FIRTSNAME + " TEXT NOT NULL, " +
                EmployeeCreateAccountEntry.KEY_LASTNAME + " TEXT NOT NULL, " +
                EmployeeCreateAccountEntry.KEY_USERNAME + " TEXT NOT NULL, " +
                EmployeeCreateAccountEntry.KEY_PASSWORD + " TEXT NOT NULL, " +
                EmployeeCreateAccountEntry.KEY_ROLE + " TEXT NOT NULL, " +
                EmployeeCreateAccountEntry.KEY_ID + " TEXT NOT NULL " +
                ");";
        db.execSQL(SQL_CREATE_EMPLOYEE_ACCOUNTS_TABLE);


        //Product Category Table -- Product Categories

        final String SQL_CREATE_PRODUCT_CATEGORY_TABLE = "CREATE TABLE " +
                ProductCategoryEntry.TABLE_NAME + " (" +
                ProductCategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductCategoryEntry.KEY_PRODUCT_CATEGORY + " TEXT NOT NULL " +
                ");";
        db.execSQL(SQL_CREATE_PRODUCT_CATEGORY_TABLE);


        //Supplier Table -- SupplierCreate

        final String SQL_SUPPLIER_TABLE = "CREATE TABLE " +
                SupplierAccountEntry.TABLE_NAME + " (" +
                SupplierAccountEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SupplierAccountEntry.KEY_SUPPLIER_CODE + " TEXT NOT NULL, " +
                SupplierAccountEntry.KEY_SUPPLIER_NAME+ " TEXT NOT NULL, " +
                SupplierAccountEntry.KEY_SUPPLIER_ADDRESS+ " TEXT NOT NULL, " +
                SupplierAccountEntry.KEY_SUPPLIER_EMAIL + " TEXT NOT NULL, " +
                SupplierAccountEntry.KEY_SUPPLIER_TELEPHONE + " TEXT NOT NULL " +
                ");";
        db.execSQL(SQL_SUPPLIER_TABLE);


        //Products Table --

        final String SQL_PRODUCTS_TABLE = "CREATE TABLE " +
                ProductMaintenanceEntry.TABLE_NAME + " (" +
                ProductMaintenanceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductMaintenanceEntry.KEY_PRODUCT_CODE + " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY+ " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION+ " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_NAME+ " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_DATEOFVALIDITY + " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY + " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_BUY_PRICE + " REAL NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE + " REAL NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_ID + " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY_ID + " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY + " TEXT NOT NULL, " +
                ProductMaintenanceEntry.KEY_PRODUCT_IMAGE + " BLOB NOT NULL " +
                ");";
        db.execSQL(SQL_PRODUCTS_TABLE);

        //Employee Time-in Time-out Table --

        final String SQL_TIMEINTIMEOUT_TABLE = "CREATE TABLE " +
                EmployeeLoginLogoutEntry.TABLE_NAME + " (" +
                EmployeeLoginLogoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EmployeeLoginLogoutEntry.KEY_EMPLOYEEFIRSTNAME + " TEXT NOT NULL, " +
                EmployeeLoginLogoutEntry.KEY_EMPLOYEELASTNAME+ " TEXT NOT NULL, " +
                EmployeeLoginLogoutEntry.KEY_EMPLOYEEROLE+ " TEXT NOT NULL, " +
                EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEIN+ " TEXT NOT NULL, " +
                EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEOUT + " TEXT NOT NULL " +

                ");";
        db.execSQL(SQL_TIMEINTIMEOUT_TABLE);


        //Stocks Table --

        final String SQL_STOCKS_TABLE = "CREATE TABLE " +
                StocksEntry.TABLE_NAME + " (" +
                StocksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StocksEntry.KEY_PRODUCTNAME + " TEXT NOT NULL, " +
                StocksEntry.KEY_PRODUCTCATEGORY+ " TEXT NOT NULL, " +
                StocksEntry.KEY_PRODUCTSUPPLIERNAME+ " TEXT NOT NULL, " +
                StocksEntry.KEY_PRODUCTQUANTITY+ " INTEGER NOT NULL, " +
                StocksEntry.KEY_PRODUCTID + " TEXT NOT NULL " +
                ");";
        db.execSQL(SQL_STOCKS_TABLE);




        //CART Table --

        final String SQL_CART_TABLE = "CREATE TABLE " +
                CartEntry.TABLE_NAME + " (" +
                CartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CartEntry.KEY_PRODUCTCODE + " TEXT NOT NULL, " +
                CartEntry.KEY_PRODUCTNAME+ " TEXT NOT NULL, " +
                CartEntry.KEY_PRODUCTQUANTITY+ " TEXT NOT NULL, " +
                CartEntry.KEY_PRODUCTSELLPRICE+ " REAL NOT NULL, " +
                CartEntry.KEY_PRODUCTTOTALPRICE + " REAL NOT NULL, " +
                CartEntry.KEY_PRODUCTDISCOUNT + " TEXT NOT NULL, " +
                CartEntry.KEY_TOTALDISCOUNT + " TEXT NOT NULL,  " +
                CartEntry.KEY_RECEIPTNO + " TEXT NOT NULL,  " +
                CartEntry.KEY_DATE+ " TEXT NOT NULL " +
                ");";
        db.execSQL(SQL_CART_TABLE);

        //Sales Items Table --

        final String SQL_SALES_ITEMS_TABLE = "CREATE TABLE " +
                SalesItemsEntry.TABLE_NAME + " (" +
                SalesItemsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SalesItemsEntry.KEY_PRODUCTCODE + " TEXT NOT NULL, " +
                SalesItemsEntry.KEY_PRODUCTNAME+ " TEXT NOT NULL, " +
                SalesItemsEntry.KEY_PRODUCTQUANTITY+ " TEXT NOT NULL, " +
                SalesItemsEntry.KEY_PRODUCTSELLPRICE+ " REAL NOT NULL, " +
                SalesItemsEntry.KEY_PRODUCTTOTALPRICE + " REAL NOT NULL, " +
                SalesItemsEntry.KEY_PRODUCTDISCOUNT + " TEXT NOT NULL, " +
                SalesItemsEntry.KEY_TOTALDISCOUNT + " TEXT NOT NULL,  " +
                SalesItemsEntry.KEY_RECEIPTNO + " TEXT NOT NULL, " +
                SalesItemsEntry.KEY_DATE+ " TEXT NOT NULL " +

                //copy from cart table then update this table to add the receipt number.
                ");";
        db.execSQL(SQL_SALES_ITEMS_TABLE);


        //Sales Table --

        final String SQL_SALES_TABLE = "CREATE TABLE " +
                SalesEntry.TABLE_NAME + " (" +
                SalesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SalesEntry.KEY_RECEIPTNO + " TEXT NOT NULL, " +
                SalesEntry.KEY_DATE+ " TEXT NOT NULL, " +
                SalesEntry.KEY_SUBTOTAL+ " REAL NOT NULL, " +
                SalesEntry.KEY_TOTALDISCOUNT+ " REAL NOT NULL, " +
                SalesEntry.KEY_TOTALPRICE + " REAL NOT NULL ," +
                SalesEntry.KEY_QRCODE + " BLOB NOT NULL " +

                ");";
        db.execSQL(SQL_SALES_TABLE);


        //Sales Summary Table --

        final String SQL_SALES_Summary_TABLE = "CREATE TABLE " +
                SalesSummaryEntry.TABLE_NAME + " (" +
                SalesSummaryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SalesSummaryEntry.KEY_PRODUCTCODE + " TEXT NOT NULL, " +
                SalesSummaryEntry.KEY_PRODUCTNAME+ " TEXT NOT NULL, " +
                SalesSummaryEntry.KEY_PRODUCTQUANTITY+ " TEXT NOT NULL, " +
                SalesSummaryEntry.KEY_PRODUCTSELLPRICE+ " REAL NOT NULL, " +
                SalesSummaryEntry.KEY_PRODUCTTOTALPRICE + " REAL NOT NULL, " +
                SalesSummaryEntry.KEY_PRODUCTDISCOUNT + " TEXT NOT NULL, " +
                SalesSummaryEntry.KEY_TOTALDISCOUNT + " TEXT NOT NULL,  " +
                SalesSummaryEntry.KEY_RECEIPTNO + " TEXT NOT NULL, " +
                SalesSummaryEntry.KEY_DATE+ " TEXT NOT NULL " +

                //copy from cart table then update this table to add the receipt number.
                ");";
        db.execSQL(SQL_SALES_Summary_TABLE);




        //for Receipt Settings


        final String SQL_RECEIPT_SETTINGS_TABLE = "CREATE TABLE " +
                ReceiptLayoutEntry.TABLE_NAME + " (" +
                ReceiptLayoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReceiptLayoutEntry.KEY_APP_NAME + " TEXT NOT NULL, " +
                ReceiptLayoutEntry.KEY_COMPANY_ADDRESS+ " TEXT NOT NULL, " +
                ReceiptLayoutEntry.KEY_COMPANY_TELEPHONE+ " TEXT NOT NULL, " +
                ReceiptLayoutEntry.KEY_FOOTER1+ " TEXT NOT NULL, " +
                ReceiptLayoutEntry.KEY_FOOTER2 + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_RECEIPT_SETTINGS_TABLE);


        //for Currency Settings


        final String SQL_CURRENCY_SETTINGS_TABLE = "CREATE TABLE " +
                CurrencySettingsEntry.TABLE_NAME + " (" +
                CurrencySettingsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CurrencySettingsEntry.KEY_CURRENCY + " TEXT NOT NULL " +
                ");";
        db.execSQL(SQL_CURRENCY_SETTINGS_TABLE);







        //for Trial


        final String SQL_TRIAL_TABLE = "CREATE TABLE " +
                TenLoginTrialEntry.TABLE_NAME + " (" +
                TenLoginTrialEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TenLoginTrialEntry.KEY_LOGIN+ " TEXT NOT NULL " +
                ");";
        db.execSQL(SQL_TRIAL_TABLE);




    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +EmployeeTypeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +EmployeeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +EmployeeCreateAccountEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +ProductCategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +SupplierAccountEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +ProductMaintenanceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +StocksEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +CartEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +SalesItemsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +SalesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +SalesSummaryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +ReceiptLayoutEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +TenLoginTrialEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +CurrencySettingsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +EmployeeLoginLogoutEntry.TABLE_NAME);


        onCreate(db);
    }






    //Get all EmployeeTypes for Spinner

    public List<String> getAllEmployeeTypes()
    {
        List<String> employeetypes=new ArrayList<>();
        //get readable database
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT EMPLOYEETYPE FROM employeetype",null);
        if(cursor.moveToFirst())
        {
            do {
                employeetypes.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        //close the cursor
        cursor.close();
        //close the database

        //below is a bug hehe
        // db.close();
        return employeetypes;
    }




    //Get all Product Category for Spinner

    public List<String> getAllProductCategory()
    {
        List<String> productcategory=new ArrayList<>();
        productcategory.add("N/A");
        //get readable database
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT CATEGORY FROM productcategory",null);
        if(cursor.moveToFirst())
        {
            do {

                productcategory.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        //close the cursor
        cursor.close();



        //below is a bug hehe
        // db.close();
        return productcategory;
    }

    //Get all Product Category for Spinner with ALL for POS

    public List<String> getAllProductCategorywithALL()
    {
        List<String> productcategory=new ArrayList<>();
        productcategory.add("ALL");
        productcategory.add("N/A");
        //get readable database
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT CATEGORY FROM productcategory",null);
        if(cursor.moveToFirst())
        {
            do {

                productcategory.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        //close the cursor
        cursor.close();



        //below is a bug hehe
        // db.close();
        return productcategory;
    }


    //Get all Supplier Name for Spinner

    public List<String> getAllSupplierName()
    {
        List<String> suppliername=new ArrayList<>();
        suppliername.add("N/A");
        //get readable database
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT SUPPLIERNAME FROM supplier",null);
        if(cursor.moveToFirst())
        {
            do {
                suppliername.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        //close the cursor
        cursor.close();
        //close the database

        //below is a bug hehe
        // db.close();
        return suppliername;
    }


    public void backup(String outFileName) {

        //database path

        final String inFileName = mContext.getDatabasePath(DB_NAME).toString();

        try {


            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {


        final String outFileName = mContext.getDatabasePath(DB_NAME).toString();


        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(mContext, outFileName, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    //ALL WORKING!

    /*public boolean insertData(String employeetype,String description){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EMPLOYEETYPE,employeetype);
        contentValues.put(KEY_DESCRIPTION,description);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }  */

   /* public Cursor getEmployeeTypeData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +TABLE_NAME,null);
        return res;
    }  */


    @Override
    public void onOpen(SQLiteDatabase db) {
        db.disableWriteAheadLogging();
        super.onOpen(db);
    }
}
