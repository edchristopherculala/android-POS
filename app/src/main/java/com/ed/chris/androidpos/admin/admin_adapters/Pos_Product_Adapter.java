package com.ed.chris.androidpos.admin.admin_adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class Pos_Product_Adapter extends RecyclerView.Adapter<Pos_Product_Adapter.PosProductViewHolder> {


    private Context mContext;
    private Cursor mCursor;


    private SQLiteDatabase mDatabase;


    DatabaseHelper myDb;

    Animation animation, animation1;
    private int currentSelectedPosition = RecyclerView.NO_POSITION;


    public Pos_Product_Adapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;

    }

    public class PosProductViewHolder extends RecyclerView.ViewHolder {


        public TextView txtProductCode;
        public TextView txtProductName;
        public TextView txtProductSellPrice;


        public CardView select_layout;

        public ImageView product_thumb;


        public PosProductViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProductCode = itemView.findViewById(R.id.txtposproductCode);
            txtProductName = itemView.findViewById(R.id.txtposproductName);
            txtProductSellPrice = itemView.findViewById(R.id.txtposproductSell);

            product_thumb = itemView.findViewById(R.id.product_thumbnail);

            select_layout = itemView.findViewById(R.id.selectCard);
        }
    }

    @NonNull
    @Override
    public PosProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cardview_product, parent, false);
        return new Pos_Product_Adapter.PosProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PosProductViewHolder holder, int position) {

        myDb = new DatabaseHelper(mContext);
        mDatabase = myDb.getWritableDatabase();


        String count = "SELECT count(*) FROM currencysettings";
        Cursor mcursor = mDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        String a = String.valueOf(icount);
        if (icount > 0) {

            //if you have a saved currency in settings

            String currency1 = "SELECT CURRENCY FROM currencysettings";
            Cursor mcursor1 = mDatabase.rawQuery(currency1, null);
            mcursor1.moveToFirst();
            String thisCurrency = mcursor1.getString(0);

            if (thisCurrency.equals("Philippine Peso")) {

                //if your current currency is set to ph peso
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
                final String productNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
                final String productSELL = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
                final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


                long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
                double price = Double.valueOf(productSELL);
                byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                holder.txtProductCode.setText(productCODE);
                holder.txtProductName.setText(productNAME);
                holder.txtProductSellPrice.setText(format.format(price));
                holder.product_thumb.setImageBitmap(bmp);


                final String quantity = "1";
                holder.itemView.setTag(id);

                final String idString = String.valueOf(id);

                //code for adding to cart
                holder.select_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if product already exist; if exists. add 1

                        String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {
                            //condition if the product is already inside the cart
                            //then below code will run
                            //need to minus 1 the == totalremaningstock

                            //STOCK CHECKER
                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart, exactRemainingstock;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //need to check the quantity inside the cart for validation
                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            String SqCart = String.valueOf(quantityinCart);


                            //sum of productquantity inside the sales
                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);
                            currentQuantity = Integer.parseInt(productQUANTITY);


                            //validator for total stocks remaining
                            totalRemainingStocks = currentQuantity - soldSales;
                            exactRemainingstock = totalRemainingStocks - 1;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //add 1 if exists
                            String count2 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                            mcursor2.moveToFirst();
                            //icount2 == productquantity from cart
                            String icount2 = mcursor2.getString(3);

                            String count3 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                            mcursor3.moveToFirst();
                            //icount3 == sellprice from cart
                            double sellpriceI = mcursor3.getDouble(4);


                            int quantityI = Integer.parseInt(icount2);
                            //Integer sellpriceI = Integer.valueOf(icount3);
                            int newQuantity = quantityI + 1;
                            double newTotalPrice = newQuantity * sellpriceI;

                            String zero = "0";

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);

                            ///////////////////////////////////////////////////////////////


                            if (icount > 0) {

                                //select max may DATA

                                if (quantityinCart <= exactRemainingstock) {

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);


                                    String receiptlah = ("R-" + newID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");
                                    // Toast.makeText(mContext, "Product Already Exists. Added 1 ", Toast.LENGTH_LONG).show();

                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_LONG).show();


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (quantityinCart <= exactRemainingstock) {

                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }

                            }

                        } else {

                            //this condition == product is not yet inside the cart


                            //STOCK CHECKER
                            // final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));

                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);

                            currentQuantity = Integer.parseInt(productQUANTITY);

                            totalRemainingStocks = currentQuantity - soldSales;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);
                            String asd = String.valueOf(currentQuantity);


                            if (icount > 0) {

                                if (totalRemainingStocks <= 0) {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    int as;
                                    as = 1;

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    String receiptlah = ("R-" + newID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);


                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (totalRemainingStocks <= 0) {
                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);
                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);

                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();


                                }


                            }


                        }

                    }
                });


            } else if (thisCurrency.equals("Canadian Dollars")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CANADA));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
                final String productNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
                final String productSELL = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
                final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


                long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
                double price = Double.valueOf(productSELL);
                byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                holder.txtProductCode.setText(productCODE);
                holder.txtProductName.setText(productNAME);
                holder.txtProductSellPrice.setText(format.format(price));
                holder.product_thumb.setImageBitmap(bmp);


                final String quantity = "1";
                holder.itemView.setTag(id);

                final String idString = String.valueOf(id);

                //code for adding to cart
                holder.select_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if product already exist; if exists. add 1

                        String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {
                            //condition if the product is already inside the cart
                            //then below code will run
                            //need to minus 1 the == totalremaningstock

                            //STOCK CHECKER
                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart, exactRemainingstock;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //need to check the quantity inside the cart for validation
                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            String SqCart = String.valueOf(quantityinCart);


                            //sum of productquantity inside the sales
                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);
                            currentQuantity = Integer.parseInt(productQUANTITY);


                            //validator for total stocks remaining
                            totalRemainingStocks = currentQuantity - soldSales;
                            exactRemainingstock = totalRemainingStocks - 1;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            String count2 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                            mcursor2.moveToFirst();
                            //icount2 == productquantity from cart
                            String icount2 = mcursor2.getString(3);

                            String count3 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                            mcursor3.moveToFirst();
                            //icount3 == sellprice from cart
                            double sellpriceI = mcursor3.getDouble(4);


                            int quantityI = Integer.parseInt(icount2);
                            //Integer sellpriceI = Integer.valueOf(icount3);
                            int newQuantity = quantityI + 1;
                            double newTotalPrice = newQuantity * sellpriceI;


                            String zero = "0";

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);

                            ///////////////////////////////////////////////////////////////


                            if (icount > 0) {

                                //select max may DATA

                                if (quantityinCart <= exactRemainingstock) {

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    String receiptlah = ("R-" + newID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");

                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_LONG).show();


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (quantityinCart <= exactRemainingstock) {

                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }

                            }

                        } else {

                            //this condition == product is not yet inside the cart


                            //STOCK CHECKER
                            // final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));

                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);

                            currentQuantity = Integer.parseInt(productQUANTITY);

                            totalRemainingStocks = currentQuantity - soldSales;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);
                            String asd = String.valueOf(currentQuantity);


                            if (icount > 0) {

                                if (totalRemainingStocks <= 0) {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    int as;
                                    as = 1;

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    String receiptlah = ("R-" + newID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);


                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (totalRemainingStocks <= 0) {
                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);
                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);

                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();


                                }


                            }


                        }

                    }
                });


            } else if (thisCurrency.equals("Japanese Yen")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.JAPAN));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
                final String productNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
                final String productSELL = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
                final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


                long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
                double price = Double.valueOf(productSELL);
                byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                holder.txtProductCode.setText(productCODE);
                holder.txtProductName.setText(productNAME);
                holder.txtProductSellPrice.setText(format.format(price));
                holder.product_thumb.setImageBitmap(bmp);


                final String quantity = "1";
                holder.itemView.setTag(id);

                final String idString = String.valueOf(id);

                //code for adding to cart
                holder.select_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if product already exist; if exists. add 1

                        String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {
                            //condition if the product is already inside the cart
                            //then below code will run
                            //need to minus 1 the == totalremaningstock

                            //STOCK CHECKER
                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart, exactRemainingstock;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //need to check the quantity inside the cart for validation
                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            String SqCart = String.valueOf(quantityinCart);


                            //sum of productquantity inside the sales
                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);
                            currentQuantity = Integer.parseInt(productQUANTITY);


                            //validator for total stocks remaining
                            totalRemainingStocks = currentQuantity - soldSales;
                            exactRemainingstock = totalRemainingStocks - 1;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //add 1 if exists
                            String count2 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                            mcursor2.moveToFirst();
                            //icount2 == productquantity from cart
                            String icount2 = mcursor2.getString(3);

                            String count3 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                            mcursor3.moveToFirst();
                            //icount3 == sellprice from cart
                            double sellpriceI = mcursor3.getDouble(4);


                            int quantityI = Integer.parseInt(icount2);
                            //Integer sellpriceI = Integer.valueOf(icount3);
                            int newQuantity = quantityI + 1;
                            double newTotalPrice = newQuantity * sellpriceI;

                            String zero = "0";

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);

                            ///////////////////////////////////////////////////////////////


                            if (icount > 0) {

                                //select max may DATA

                                if (quantityinCart <= exactRemainingstock) {

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    String receiptlah = ("R-" + newID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");

                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_LONG).show();


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (quantityinCart <= exactRemainingstock) {

                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }

                            }

                        } else {

                            //this condition == product is not yet inside the cart


                            //STOCK CHECKER
                            // final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));

                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);

                            currentQuantity = Integer.parseInt(productQUANTITY);

                            totalRemainingStocks = currentQuantity - soldSales;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);
                            String asd = String.valueOf(currentQuantity);


                            if (icount > 0) {

                                if (totalRemainingStocks <= 0) {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    int as;
                                    as = 1;

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    String receiptlah = ("R-" + newID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);


                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (totalRemainingStocks <= 0) {
                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);
                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);

                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();


                                }


                            }


                        }

                    }
                });


            } else if (thisCurrency.equals("US Dollars")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.US));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
                final String productNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
                final String productSELL = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
                final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


                long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
                double price = Double.valueOf(productSELL);
                byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                holder.txtProductCode.setText(productCODE);
                holder.txtProductName.setText(productNAME);
                holder.txtProductSellPrice.setText(format.format(price));
                holder.product_thumb.setImageBitmap(bmp);


                final String quantity = "1";
                holder.itemView.setTag(id);

                final String idString = String.valueOf(id);

                //code for adding to cart
                holder.select_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if product already exist; if exists. add 1

                        String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {
                            //condition if the product is already inside the cart
                            //then below code will run
                            //need to minus 1 the == totalremaningstock

                            //STOCK CHECKER
                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart, exactRemainingstock;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //need to check the quantity inside the cart for validation
                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            String SqCart = String.valueOf(quantityinCart);


                            //sum of productquantity inside the sales
                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);
                            currentQuantity = Integer.parseInt(productQUANTITY);


                            //validator for total stocks remaining
                            totalRemainingStocks = currentQuantity - soldSales;
                            exactRemainingstock = totalRemainingStocks - 1;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //add 1 if exists
                            String count2 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                            mcursor2.moveToFirst();
                            //icount2 == productquantity from cart
                            String icount2 = mcursor2.getString(3);

                            String count3 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                            mcursor3.moveToFirst();
                            //icount3 == sellprice from cart
                            double sellpriceI = mcursor3.getDouble(4);


                            int quantityI = Integer.parseInt(icount2);
                            //Integer sellpriceI = Integer.valueOf(icount3);
                            int newQuantity = quantityI + 1;
                            double newTotalPrice = newQuantity * sellpriceI;

                            String zero = "0";

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);

                            ///////////////////////////////////////////////////////////////


                            if (icount > 0) {

                                //select max may DATA

                                if (quantityinCart <= exactRemainingstock) {

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    String receiptlah = ("R-" + newID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");

                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_LONG).show();


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (quantityinCart <= exactRemainingstock) {

                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }

                            }

                        } else {

                            //this condition == product is not yet inside the cart


                            //STOCK CHECKER
                            // final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));

                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);

                            currentQuantity = Integer.parseInt(productQUANTITY);

                            totalRemainingStocks = currentQuantity - soldSales;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);
                            String asd = String.valueOf(currentQuantity);


                            if (icount > 0) {

                                if (totalRemainingStocks <= 0) {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    int as;
                                    as = 1;

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    String receiptlah = ("R-" + newID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);


                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (totalRemainingStocks <= 0) {
                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);
                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);

                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();


                                }


                            }


                        }

                    }
                });

            } else if (thisCurrency.equals("Chinese Yuan")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CHINA));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
                final String productNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
                final String productSELL = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
                final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


                long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
                double price = Double.valueOf(productSELL);
                byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                holder.txtProductCode.setText(productCODE);
                holder.txtProductName.setText(productNAME);
                holder.txtProductSellPrice.setText(format.format(price));
                holder.product_thumb.setImageBitmap(bmp);


                final String quantity = "1";
                holder.itemView.setTag(id);

                final String idString = String.valueOf(id);

                //code for adding to cart
                holder.select_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if product already exist; if exists. add 1

                        String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {
                            //condition if the product is already inside the cart
                            //then below code will run
                            //need to minus 1 the == totalremaningstock

                            //STOCK CHECKER
                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart, exactRemainingstock;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //need to check the quantity inside the cart for validation
                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            String SqCart = String.valueOf(quantityinCart);


                            //sum of productquantity inside the sales
                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);
                            currentQuantity = Integer.parseInt(productQUANTITY);


                            //validator for total stocks remaining
                            totalRemainingStocks = currentQuantity - soldSales;
                            exactRemainingstock = totalRemainingStocks - 1;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //add 1 if exists
                            String count2 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                            mcursor2.moveToFirst();
                            //icount2 == productquantity from cart
                            String icount2 = mcursor2.getString(3);

                            String count3 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                            mcursor3.moveToFirst();
                            //icount3 == sellprice from cart
                            double sellpriceI = mcursor3.getDouble(4);


                            int quantityI = Integer.parseInt(icount2);
                            //Integer sellpriceI = Integer.valueOf(icount3);
                            int newQuantity = quantityI + 1;
                            double newTotalPrice = newQuantity * sellpriceI;

                            String zero = "0";

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);

                            ///////////////////////////////////////////////////////////////


                            if (icount > 0) {

                                //select max may DATA

                                if (quantityinCart <= exactRemainingstock) {

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    String receiptlah = ("R-" + newID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");

                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_LONG).show();


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (quantityinCart <= exactRemainingstock) {

                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }

                            }

                        } else {

                            //this condition == product is not yet inside the cart


                            //STOCK CHECKER
                            // final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));

                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);

                            currentQuantity = Integer.parseInt(productQUANTITY);

                            totalRemainingStocks = currentQuantity - soldSales;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);
                            String asd = String.valueOf(currentQuantity);


                            if (icount > 0) {

                                if (totalRemainingStocks <= 0) {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    int as;
                                    as = 1;

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    String receiptlah = ("R-" + newID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);


                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (totalRemainingStocks <= 0) {
                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);
                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);

                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();


                                }


                            }


                        }

                    }
                });


            } else if (thisCurrency.equals("France Euro")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.FRANCE));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
                final String productNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
                final String productSELL = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
                final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


                long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
                double price = Double.valueOf(productSELL);
                byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                holder.txtProductCode.setText(productCODE);
                holder.txtProductName.setText(productNAME);
                holder.txtProductSellPrice.setText(format.format(price));
                holder.product_thumb.setImageBitmap(bmp);


                final String quantity = "1";
                holder.itemView.setTag(id);

                final String idString = String.valueOf(id);

                //code for adding to cart
                holder.select_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if product already exist; if exists. add 1

                        String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {
                            //condition if the product is already inside the cart
                            //then below code will run
                            //need to minus 1 the == totalremaningstock

                            //STOCK CHECKER
                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart, exactRemainingstock;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //need to check the quantity inside the cart for validation
                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            String SqCart = String.valueOf(quantityinCart);


                            //sum of productquantity inside the sales
                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);
                            currentQuantity = Integer.parseInt(productQUANTITY);


                            //validator for total stocks remaining
                            totalRemainingStocks = currentQuantity - soldSales;
                            exactRemainingstock = totalRemainingStocks - 1;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //add 1 if exists
                            String count2 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                            mcursor2.moveToFirst();
                            //icount2 == productquantity from cart
                            String icount2 = mcursor2.getString(3);

                            String count3 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                            mcursor3.moveToFirst();
                            //icount3 == sellprice from cart
                            double sellpriceI = mcursor3.getDouble(4);


                            int quantityI = Integer.parseInt(icount2);
                            //Integer sellpriceI = Integer.valueOf(icount3);
                            int newQuantity = quantityI + 1;
                            double newTotalPrice = newQuantity * sellpriceI;

                            String zero = "0";

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);

                            ///////////////////////////////////////////////////////////////


                            if (icount > 0) {

                                //select max may DATA

                                if (quantityinCart <= exactRemainingstock) {

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    String receiptlah = ("R-" + newID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");

                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_LONG).show();


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (quantityinCart <= exactRemainingstock) {

                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }

                            }

                        } else {

                            //this condition == product is not yet inside the cart


                            //STOCK CHECKER
                            // final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));

                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);

                            currentQuantity = Integer.parseInt(productQUANTITY);

                            totalRemainingStocks = currentQuantity - soldSales;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);
                            String asd = String.valueOf(currentQuantity);


                            if (icount > 0) {

                                if (totalRemainingStocks <= 0) {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    int as;
                                    as = 1;

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    String receiptlah = ("R-" + newID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);


                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (totalRemainingStocks <= 0) {
                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);
                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);

                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();


                                }


                            }


                        }

                    }
                });

            } else if (thisCurrency.equals("Korea Won")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.KOREA));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
                final String productNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
                final String productSELL = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
                final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


                long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
                double price = Double.valueOf(productSELL);
                byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                holder.txtProductCode.setText(productCODE);
                holder.txtProductName.setText(productNAME);
                holder.txtProductSellPrice.setText(format.format(price));
                holder.product_thumb.setImageBitmap(bmp);


                final String quantity = "1";
                holder.itemView.setTag(id);

                final String idString = String.valueOf(id);

                //code for adding to cart
                holder.select_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if product already exist; if exists. add 1

                        String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {
                            //condition if the product is already inside the cart
                            //then below code will run
                            //need to minus 1 the == totalremaningstock

                            //STOCK CHECKER
                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart, exactRemainingstock;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //need to check the quantity inside the cart for validation
                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            String SqCart = String.valueOf(quantityinCart);


                            //sum of productquantity inside the sales
                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);
                            currentQuantity = Integer.parseInt(productQUANTITY);


                            //validator for total stocks remaining
                            totalRemainingStocks = currentQuantity - soldSales;
                            exactRemainingstock = totalRemainingStocks - 1;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //add 1 if exists
                            String count2 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                            mcursor2.moveToFirst();
                            //icount2 == productquantity from cart
                            String icount2 = mcursor2.getString(3);

                            String count3 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                            mcursor3.moveToFirst();
                            //icount3 == sellprice from cart
                            double sellpriceI = mcursor3.getDouble(4);


                            int quantityI = Integer.parseInt(icount2);
                            //Integer sellpriceI = Integer.valueOf(icount3);
                            int newQuantity = quantityI + 1;
                            double newTotalPrice = newQuantity * sellpriceI;

                            String zero = "0";

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);

                            ///////////////////////////////////////////////////////////////


                            if (icount > 0) {

                                //select max may DATA

                                if (quantityinCart <= exactRemainingstock) {

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    String receiptlah = ("R-" + newID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_LONG).show();


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (quantityinCart <= exactRemainingstock) {

                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    //updated
                                    String defaultval = "0";

                                    mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                            " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                            ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");


                                } else {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                                }

                            }

                        } else {

                            //this condition == product is not yet inside the cart


                            //STOCK CHECKER
                            // final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));

                            int soldSales, currentQuantity, totalRemainingStocks, quantityinCart;
                            String stringTotalRemainingStock, countQ, countQCart;

                            //sum of productquantity inside the cart
                            countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                            mcursorQCart.moveToFirst();
                            quantityinCart = mcursorQCart.getInt(0);

                            countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                            Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                            mcursorQ.moveToFirst();
                            soldSales = mcursorQ.getInt(0);

                            currentQuantity = Integer.parseInt(productQUANTITY);

                            totalRemainingStocks = currentQuantity - soldSales;
                            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                            //Increment Value from database
                            String count = "SELECT count(*) FROM sales";
                            Cursor mcursor = mDatabase.rawQuery(count, null);
                            mcursor.moveToFirst();
                            int icount = mcursor.getInt(0);

                            String a = String.valueOf(icount);
                            String asd = String.valueOf(currentQuantity);


                            if (icount > 0) {

                                if (totalRemainingStocks <= 0) {

                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    int as;
                                    as = 1;

                                    String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                    Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                    mcursor1.moveToFirst();
                                    int icount1 = mcursor1.getInt(0);

                                    int newID = icount1 + 1;
                                    String res = String.valueOf(icount1);

                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);

                                    String receiptlah = ("R-" + newID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);


                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();

                                    // Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();

                                }


                            } else {

                                if (totalRemainingStocks <= 0) {
                                    Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                                } else if (quantityinCart <= totalRemainingStocks) {


                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);
                                    int defaultID = icount + 1;

                                    String receiptlah = ("R-" + defaultID);

                                    ContentValues cv = new ContentValues();
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                    cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                    cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                    cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);

                                    mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                    Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();


                                }


                            }


                        }

                    }
                });

            }

            mcursor.close();
        } else {

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

            if (!mCursor.moveToPosition(position)) {
                return;
            }

            final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
            final String productNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
            final String productSELL = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
            final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


            long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));
            double price = Double.valueOf(productSELL);
            byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
            Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

            holder.txtProductCode.setText(productCODE);
            holder.txtProductName.setText(productNAME);
            holder.txtProductSellPrice.setText(format.format(price));
            holder.product_thumb.setImageBitmap(bmp);


            final String quantity = "1";
            holder.itemView.setTag(id);

            final String idString = String.valueOf(id);

            //code for adding to cart
            holder.select_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check if product already exist; if exists. add 1

                    String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                    if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {
                        //condition if the product is already inside the cart
                        //then below code will run
                        //need to minus 1 the == totalremaningstock

                        //STOCK CHECKER
                        int soldSales, currentQuantity, totalRemainingStocks, quantityinCart, exactRemainingstock;
                        String stringTotalRemainingStock, countQ, countQCart;

                        //need to check the quantity inside the cart for validation
                        //sum of productquantity inside the cart
                        countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                        Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                        mcursorQCart.moveToFirst();
                        quantityinCart = mcursorQCart.getInt(0);

                        String SqCart = String.valueOf(quantityinCart);


                        //sum of productquantity inside the sales
                        countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                        Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                        mcursorQ.moveToFirst();
                        soldSales = mcursorQ.getInt(0);
                        currentQuantity = Integer.parseInt(productQUANTITY);


                        //validator for total stocks remaining
                        totalRemainingStocks = currentQuantity - soldSales;
                        exactRemainingstock = totalRemainingStocks - 1;
                        stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                        //add 1 if exists
                        String count2 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                        mcursor2.moveToFirst();
                        //icount2 == productquantity from cart
                        String icount2 = mcursor2.getString(3);

                        String count3 = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";
                        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                        mcursor3.moveToFirst();
                        //icount3 == sellprice from cart
                        double sellpriceI = mcursor3.getDouble(4);


                        int quantityI = Integer.parseInt(icount2);
                        //Integer sellpriceI = Integer.valueOf(icount3);
                        int newQuantity = quantityI + 1;
                        double newTotalPrice = newQuantity * sellpriceI;

                        String zero = "0";

                        //Increment Value from database
                        String count = "SELECT count(*) FROM sales";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);

                        String a = String.valueOf(icount);

                        ///////////////////////////////////////////////////////////////


                        if (icount > 0) {

                            //select max may DATA

                            if (quantityinCart <= exactRemainingstock) {

                                String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                mcursor1.moveToFirst();
                                int icount1 = mcursor1.getInt(0);

                                int newID = icount1 + 1;
                                String res = String.valueOf(icount1);

                                String receiptlah = ("R-" + newID);

                                //updated
                                String defaultval = "0";

                                mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                        " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                        ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");

                                Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                            }


                        } else {

                            if (quantityinCart <= exactRemainingstock) {

                                int defaultID = icount + 1;

                                String receiptlah = ("R-" + defaultID);

                                //updated
                                String defaultval = "0";

                                mDatabase.execSQL("UPDATE  " + CartContract.CartEntry.TABLE_NAME + " SET PRODUCTQUANTITY ='" + newQuantity + "'," +
                                        " PRODUCTTOTALPRICE ='" + newTotalPrice + "'," + " RECEIPTNO ='" + receiptlah + "' , "+" PRODUCTDISCOUNT = '"+defaultval+"' , " +
                                        ""+" TOTALDISCOUNT = '" + defaultval +"' WHERE PRODUCTCODE='" + productCODE + "'");


                            } else {

                                Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();

                            }

                        }

                    } else {

                        //this condition == product is not yet inside the cart


                        //STOCK CHECKER
                        // final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));

                        int soldSales, currentQuantity, totalRemainingStocks, quantityinCart;
                        String stringTotalRemainingStock, countQ, countQCart;

                        //sum of productquantity inside the cart
                        countQCart = "Select sum(PRODUCTQUANTITY) From cart where PRODUCTCODE = '" + productCODE + "'";
                        Cursor mcursorQCart = mDatabase.rawQuery(countQCart, null);
                        mcursorQCart.moveToFirst();
                        quantityinCart = mcursorQCart.getInt(0);

                        countQ = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
                        Cursor mcursorQ = mDatabase.rawQuery(countQ, null);
                        mcursorQ.moveToFirst();
                        soldSales = mcursorQ.getInt(0);

                        currentQuantity = Integer.parseInt(productQUANTITY);

                        totalRemainingStocks = currentQuantity - soldSales;
                        stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                        //Increment Value from database
                        String count = "SELECT count(*) FROM sales";
                        Cursor mcursor = mDatabase.rawQuery(count, null);
                        mcursor.moveToFirst();
                        int icount = mcursor.getInt(0);

                        String a = String.valueOf(icount);
                        String asd = String.valueOf(currentQuantity);


                        if (icount > 0) {

                            if (totalRemainingStocks <= 0) {

                                Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                            } else if (quantityinCart <= totalRemainingStocks) {


                                int as;
                                as = 1;

                                String count1 = "SELECT _ID FROM sales ORDER BY _ID DESC";
                                Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                                mcursor1.moveToFirst();
                                int icount1 = mcursor1.getInt(0);

                                int newID = icount1 + 1;
                                String res = String.valueOf(icount1);

                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                String formattedDate = df.format(c);

                                String receiptlah = ("R-" + newID);

                                ContentValues cv = new ContentValues();
                                cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);


                                mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();

                                // Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();

                            }


                        } else {

                            if (totalRemainingStocks <= 0) {
                                Toast.makeText(mContext, "Not enough stock for " + productNAME, Toast.LENGTH_LONG).show();


                            } else if (quantityinCart <= totalRemainingStocks) {


                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                String formattedDate = df.format(c);
                                int defaultID = icount + 1;

                                String receiptlah = ("R-" + defaultID);

                                ContentValues cv = new ContentValues();
                                cv.put(CartContract.CartEntry.KEY_PRODUCTCODE, productCODE);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTNAME, productNAME);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTQUANTITY, quantity);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTSELLPRICE, productSELL);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE, productSELL);
                                cv.put(CartContract.CartEntry.KEY_PRODUCTDISCOUNT, "0");
                                cv.put(CartContract.CartEntry.KEY_TOTALDISCOUNT, "0");
                                cv.put(CartContract.CartEntry.KEY_RECEIPTNO, receiptlah);
                                cv.put(CartContract.CartEntry.KEY_DATE, formattedDate);

                                mDatabase.insert(CartContract.CartEntry.TABLE_NAME, null, cv);
                                Toast.makeText(mContext, stringTotalRemainingStock, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(mContext,stringTotalRemainingStock,Toast.LENGTH_LONG).show();


                            }


                        }


                    }

                }
            });


        }

        mcursor.close();

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

    }
}
