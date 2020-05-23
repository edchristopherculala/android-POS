package com.ed.chris.androidpos.admin.admin_adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.POS.DiscountActivity;
import com.ed.chris.androidpos.POS.UpdateQuantityActivity;
import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;

    public CartAdapter(Context context, Cursor cursor) {

        mContext = context;
        mCursor = cursor;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView productCODE;
        public TextView productNAME;
        public TextView productQUANTITY;
        public TextView productPRICE;
        public TextView productTOTALPRICE;
        public TextView productDISCOUNT;
        public LinearLayout select_item;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            productCODE = itemView.findViewById(R.id.txtProductCodeCart);
            productNAME = itemView.findViewById(R.id.txtProductNameCart);
            productQUANTITY = itemView.findViewById(R.id.txtProductQuantityCart);
            productPRICE = itemView.findViewById(R.id.txtProductPriceCart);
            productTOTALPRICE = itemView.findViewById(R.id.txtProductTotalPriceCart);
            productDISCOUNT = itemView.findViewById(R.id.txtProductDiscount);
            select_item = itemView.findViewById(R.id.select_cart_item);

        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        myDb = new DatabaseHelper(mContext);
        mDatabase = myDb.getWritableDatabase();

        String count, a;
        Cursor mcursorlah;
        final Double price, discount, totalprice;
        int icount;

        count = "SELECT count(*) FROM currencysettings";
        mcursorlah = mDatabase.rawQuery(count, null);
        mcursorlah.moveToFirst();
        icount = mcursorlah.getInt(0);

        a = String.valueOf(icount);
        if (icount > 0) {
            String currency1, thisCurrency;
            Cursor mcursor1;

            currency1 = "SELECT CURRENCY FROM currencysettings";
            mcursor1 = mDatabase.rawQuery(currency1, null);
            mcursor1.moveToFirst();
            thisCurrency = mcursor1.getString(0);

            if (thisCurrency.equals("Philippine Peso")) {

                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                myDb = new DatabaseHelper(mContext);
                mDatabase = myDb.getWritableDatabase();

                final String productcode, productname, productquantity, pproductprice, producttotalprice, productdiscount;
                final long id;

                productcode = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTCODE));
                productname = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTNAME));
                productquantity = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTQUANTITY));
                pproductprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTSELLPRICE));
                producttotalprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE));
                productdiscount = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTDISCOUNT));
                id = mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID));

                price = Double.valueOf(pproductprice);
                totalprice = Double.valueOf(producttotalprice);


                //STOCK CHECKER
                final int soldSales, currentQuantity, totalRemainingStocks, inititalQuantity;
                final String stringTotalRemainingStock, count3, countQproducts;

                //total sales of the item
                count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                soldSales = mcursor3.getInt(0);

                //total quantity of the item upon adding
                countQproducts = "Select sum(PRODUCTQUANTITY) From products where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursorQproducts = mDatabase.rawQuery(countQproducts, null);
                mcursorQproducts.moveToFirst();
                currentQuantity = mcursorQproducts.getInt(0);

                totalRemainingStocks = currentQuantity - soldSales;
                stringTotalRemainingStock = String.valueOf(totalRemainingStocks);
                //END OF STOCK CHECKER

                holder.productCODE.setText(productcode);
                holder.productNAME.setText(productname);
                holder.productQUANTITY.setText(productquantity);
                holder.productPRICE.setText(format.format(price));
                holder.productTOTALPRICE.setText(format.format(totalprice));
                holder.productDISCOUNT.setText(productdiscount);
                holder.itemView.setTag(id);

                holder.productQUANTITY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, UpdateQuantityActivity.class);
                        intent.putExtra("pname", productname);
                        intent.putExtra("pquantity", productquantity);
                        intent.putExtra("pid", productcode);
                        intent.putExtra("pprice", price);
                        intent.putExtra("ptotal", producttotalprice);
                        intent.putExtra("currentStocks", stringTotalRemainingStock);
                        mContext.startActivity(intent);

                    }
                });

                holder.productDISCOUNT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(mContext, DiscountActivity.class);
                        intent1.putExtra("pname", productname);
                        intent1.putExtra("pquantity", productquantity);
                        intent1.putExtra("pid", productcode);
                        intent1.putExtra("pprice", price);
                        intent1.putExtra("ptotal", totalprice);
                        intent1.putExtra("pdiscount", productdiscount);
                        mContext.startActivity(intent1);

                    }
                });

            } else if (thisCurrency.equals("Canadian Dollars")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CANADA));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                myDb = new DatabaseHelper(mContext);
                mDatabase = myDb.getWritableDatabase();


                final String productcode = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTCODE));
                final String productname = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTNAME));
                final String productquantity = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTQUANTITY));
                final String pproductprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTSELLPRICE));
                final String producttotalprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE));
                final String productdiscount = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTDISCOUNT));
                final long id = mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID));

                price = Double.valueOf(pproductprice);
                totalprice = Double.valueOf(producttotalprice);


                //STOCK CHECKER
                final int soldSales, currentQuantity, totalRemainingStocks, inititalQuantity;
                final String stringTotalRemainingStock, count3, countQproducts;

                //total sales of the item
                count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                soldSales = mcursor3.getInt(0);


                //total quantity of the item upon adding
                countQproducts = "Select sum(PRODUCTQUANTITY) From products where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursorQproducts = mDatabase.rawQuery(countQproducts, null);
                mcursorQproducts.moveToFirst();
                currentQuantity = mcursorQproducts.getInt(0);


                totalRemainingStocks = currentQuantity - soldSales;
                stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                //END OF STOCK CHECKER

                holder.productCODE.setText(productcode);
                holder.productNAME.setText(productname);
                holder.productQUANTITY.setText(productquantity);
                holder.productPRICE.setText(format.format(price));
                holder.productTOTALPRICE.setText(format.format(totalprice));
                holder.productDISCOUNT.setText(productdiscount);
                holder.itemView.setTag(id);

                holder.productQUANTITY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, UpdateQuantityActivity.class);
                        intent.putExtra("pname", productname);
                        intent.putExtra("pquantity", productquantity);
                        intent.putExtra("pid", productcode);
                        intent.putExtra("pprice", price);
                        intent.putExtra("ptotal", producttotalprice);
                        intent.putExtra("currentStocks", stringTotalRemainingStock);
                        mContext.startActivity(intent);

                    }
                });

                holder.productDISCOUNT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(mContext, DiscountActivity.class);
                        intent1.putExtra("pname", productname);
                        intent1.putExtra("pquantity", productquantity);
                        intent1.putExtra("pid", productcode);
                        intent1.putExtra("pprice", price);
                        intent1.putExtra("ptotal", totalprice);
                        intent1.putExtra("pdiscount", productdiscount);
                        mContext.startActivity(intent1);

                    }
                });
            } else if (thisCurrency.equals("Japanese Yen")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.JAPAN));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                myDb = new DatabaseHelper(mContext);
                mDatabase = myDb.getWritableDatabase();


                final String productcode = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTCODE));
                final String productname = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTNAME));
                final String productquantity = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTQUANTITY));
                final String pproductprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTSELLPRICE));
                final String producttotalprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE));
                final String productdiscount = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTDISCOUNT));
                final long id = mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID));

                price = Double.valueOf(pproductprice);
                totalprice = Double.valueOf(producttotalprice);


                //STOCK CHECKER
                final int soldSales, currentQuantity, totalRemainingStocks, inititalQuantity;
                final String stringTotalRemainingStock, count3, countQproducts;

                //total sales of the item
                count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                soldSales = mcursor3.getInt(0);


                //total quantity of the item upon adding
                countQproducts = "Select sum(PRODUCTQUANTITY) From products where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursorQproducts = mDatabase.rawQuery(countQproducts, null);
                mcursorQproducts.moveToFirst();
                currentQuantity = mcursorQproducts.getInt(0);


                totalRemainingStocks = currentQuantity - soldSales;
                stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                //END OF STOCK CHECKER

                holder.productCODE.setText(productcode);
                holder.productNAME.setText(productname);
                holder.productQUANTITY.setText(productquantity);
                holder.productPRICE.setText(format.format(price));
                holder.productTOTALPRICE.setText(format.format(totalprice));
                holder.productDISCOUNT.setText(productdiscount);
                holder.itemView.setTag(id);

                holder.productQUANTITY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, UpdateQuantityActivity.class);
                        intent.putExtra("pname", productname);
                        intent.putExtra("pquantity", productquantity);
                        intent.putExtra("pid", productcode);
                        intent.putExtra("pprice", price);
                        intent.putExtra("ptotal", producttotalprice);
                        intent.putExtra("currentStocks", stringTotalRemainingStock);
                        mContext.startActivity(intent);

                    }
                });

                holder.productDISCOUNT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(mContext, DiscountActivity.class);
                        intent1.putExtra("pname", productname);
                        intent1.putExtra("pquantity", productquantity);
                        intent1.putExtra("pid", productcode);
                        intent1.putExtra("pprice", price);
                        intent1.putExtra("ptotal", totalprice);
                        intent1.putExtra("pdiscount", productdiscount);
                        mContext.startActivity(intent1);

                    }
                });


            } else if (thisCurrency.equals("US Dollars")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.US));


                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                myDb = new DatabaseHelper(mContext);
                mDatabase = myDb.getWritableDatabase();


                final String productcode = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTCODE));
                final String productname = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTNAME));
                final String productquantity = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTQUANTITY));
                final String pproductprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTSELLPRICE));
                final String producttotalprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE));
                final String productdiscount = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTDISCOUNT));
                final long id = mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID));

                price = Double.valueOf(pproductprice);
                totalprice = Double.valueOf(producttotalprice);


                //STOCK CHECKER
                final int soldSales, currentQuantity, totalRemainingStocks, inititalQuantity;
                final String stringTotalRemainingStock, count3, countQproducts;

                //total sales of the item
                count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                soldSales = mcursor3.getInt(0);


                //total quantity of the item upon adding
                countQproducts = "Select sum(PRODUCTQUANTITY) From products where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursorQproducts = mDatabase.rawQuery(countQproducts, null);
                mcursorQproducts.moveToFirst();
                currentQuantity = mcursorQproducts.getInt(0);


                totalRemainingStocks = currentQuantity - soldSales;
                stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                //END OF STOCK CHECKER

                holder.productCODE.setText(productcode);
                holder.productNAME.setText(productname);
                holder.productQUANTITY.setText(productquantity);
                holder.productPRICE.setText(format.format(price));
                holder.productTOTALPRICE.setText(format.format(totalprice));
                holder.productDISCOUNT.setText(productdiscount);
                holder.itemView.setTag(id);

                holder.productQUANTITY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, UpdateQuantityActivity.class);
                        intent.putExtra("pname", productname);
                        intent.putExtra("pquantity", productquantity);
                        intent.putExtra("pid", productcode);
                        intent.putExtra("pprice", price);
                        intent.putExtra("ptotal", producttotalprice);
                        intent.putExtra("currentStocks", stringTotalRemainingStock);
                        mContext.startActivity(intent);

                    }
                });

                holder.productDISCOUNT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(mContext, DiscountActivity.class);
                        intent1.putExtra("pname", productname);
                        intent1.putExtra("pquantity", productquantity);
                        intent1.putExtra("pid", productcode);
                        intent1.putExtra("pprice", price);
                        intent1.putExtra("ptotal", totalprice);
                        intent1.putExtra("pdiscount", productdiscount);
                        mContext.startActivity(intent1);

                    }
                });
            } else if (thisCurrency.equals("Chinese Yuan")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CHINA));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                myDb = new DatabaseHelper(mContext);
                mDatabase = myDb.getWritableDatabase();


                final String productcode = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTCODE));
                final String productname = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTNAME));
                final String productquantity = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTQUANTITY));
                final String pproductprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTSELLPRICE));
                final String producttotalprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE));
                final String productdiscount = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTDISCOUNT));
                final long id = mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID));

                price = Double.valueOf(pproductprice);
                totalprice = Double.valueOf(producttotalprice);


                //STOCK CHECKER
                final int soldSales, currentQuantity, totalRemainingStocks, inititalQuantity;
                final String stringTotalRemainingStock, count3, countQproducts;

                //total sales of the item
                count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                soldSales = mcursor3.getInt(0);


                //total quantity of the item upon adding
                countQproducts = "Select sum(PRODUCTQUANTITY) From products where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursorQproducts = mDatabase.rawQuery(countQproducts, null);
                mcursorQproducts.moveToFirst();
                currentQuantity = mcursorQproducts.getInt(0);


                totalRemainingStocks = currentQuantity - soldSales;
                stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                //END OF STOCK CHECKER

                holder.productCODE.setText(productcode);
                holder.productNAME.setText(productname);
                holder.productQUANTITY.setText(productquantity);
                holder.productPRICE.setText(format.format(price));
                holder.productTOTALPRICE.setText(format.format(totalprice));
                holder.productDISCOUNT.setText(productdiscount);
                holder.itemView.setTag(id);

                holder.productQUANTITY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, UpdateQuantityActivity.class);
                        intent.putExtra("pname", productname);
                        intent.putExtra("pquantity", productquantity);
                        intent.putExtra("pid", productcode);
                        intent.putExtra("pprice", price);
                        intent.putExtra("ptotal", producttotalprice);
                        intent.putExtra("currentStocks", stringTotalRemainingStock);
                        mContext.startActivity(intent);

                    }
                });

                holder.productDISCOUNT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(mContext, DiscountActivity.class);
                        intent1.putExtra("pname", productname);
                        intent1.putExtra("pquantity", productquantity);
                        intent1.putExtra("pid", productcode);
                        intent1.putExtra("pprice", price);
                        intent1.putExtra("ptotal", totalprice);
                        intent1.putExtra("pdiscount", productdiscount);
                        mContext.startActivity(intent1);

                    }
                });
            } else if (thisCurrency.equals("France Euro")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.FRANCE));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                myDb = new DatabaseHelper(mContext);
                mDatabase = myDb.getWritableDatabase();


                final String productcode = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTCODE));
                final String productname = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTNAME));
                final String productquantity = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTQUANTITY));
                final String pproductprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTSELLPRICE));
                final String producttotalprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE));
                final String productdiscount = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTDISCOUNT));
                final long id = mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID));

                price = Double.valueOf(pproductprice);
                totalprice = Double.valueOf(producttotalprice);


                //STOCK CHECKER
                final int soldSales, currentQuantity, totalRemainingStocks, inititalQuantity;
                final String stringTotalRemainingStock, count3, countQproducts;

                //total sales of the item
                count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                soldSales = mcursor3.getInt(0);


                //total quantity of the item upon adding
                countQproducts = "Select sum(PRODUCTQUANTITY) From products where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursorQproducts = mDatabase.rawQuery(countQproducts, null);
                mcursorQproducts.moveToFirst();
                currentQuantity = mcursorQproducts.getInt(0);


                totalRemainingStocks = currentQuantity - soldSales;
                stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                //END OF STOCK CHECKER

                holder.productCODE.setText(productcode);
                holder.productNAME.setText(productname);
                holder.productQUANTITY.setText(productquantity);
                holder.productPRICE.setText(format.format(price));
                holder.productTOTALPRICE.setText(format.format(totalprice));
                holder.productDISCOUNT.setText(productdiscount);
                holder.itemView.setTag(id);

                holder.productQUANTITY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, UpdateQuantityActivity.class);
                        intent.putExtra("pname", productname);
                        intent.putExtra("pquantity", productquantity);
                        intent.putExtra("pid", productcode);
                        intent.putExtra("pprice", price);
                        intent.putExtra("ptotal", producttotalprice);
                        intent.putExtra("currentStocks", stringTotalRemainingStock);
                        mContext.startActivity(intent);

                    }
                });

                holder.productDISCOUNT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(mContext, DiscountActivity.class);
                        intent1.putExtra("pname", productname);
                        intent1.putExtra("pquantity", productquantity);
                        intent1.putExtra("pid", productcode);
                        intent1.putExtra("pprice", price);
                        intent1.putExtra("ptotal", totalprice);
                        intent1.putExtra("pdiscount", productdiscount);
                        mContext.startActivity(intent1);

                    }
                });

            } else if (thisCurrency.equals("Korea Won")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.KOREA));

                if (!mCursor.moveToPosition(position)) {
                    return;
                }

                myDb = new DatabaseHelper(mContext);
                mDatabase = myDb.getWritableDatabase();


                final String productcode = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTCODE));
                final String productname = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTNAME));
                final String productquantity = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTQUANTITY));
                final String pproductprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTSELLPRICE));
                final String producttotalprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE));
                final String productdiscount = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTDISCOUNT));
                final long id = mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID));

                price = Double.valueOf(pproductprice);
                totalprice = Double.valueOf(producttotalprice);


                //STOCK CHECKER
                final int soldSales, currentQuantity, totalRemainingStocks, inititalQuantity;
                final String stringTotalRemainingStock, count3, countQproducts;

                //total sales of the item
                count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                mcursor3.moveToFirst();
                soldSales = mcursor3.getInt(0);


                //total quantity of the item upon adding
                countQproducts = "Select sum(PRODUCTQUANTITY) From products where PRODUCTCODE = '" + productcode + "'";
                Cursor mcursorQproducts = mDatabase.rawQuery(countQproducts, null);
                mcursorQproducts.moveToFirst();
                currentQuantity = mcursorQproducts.getInt(0);


                totalRemainingStocks = currentQuantity - soldSales;
                stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

                //END OF STOCK CHECKER

                holder.productCODE.setText(productcode);
                holder.productNAME.setText(productname);
                holder.productQUANTITY.setText(productquantity);
                holder.productPRICE.setText(format.format(price));
                holder.productTOTALPRICE.setText(format.format(totalprice));
                holder.productDISCOUNT.setText(productdiscount);
                holder.itemView.setTag(id);

                holder.productQUANTITY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, UpdateQuantityActivity.class);
                        intent.putExtra("pname", productname);
                        intent.putExtra("pquantity", productquantity);
                        intent.putExtra("pid", productcode);
                        intent.putExtra("pprice", price);
                        intent.putExtra("ptotal", producttotalprice);
                        intent.putExtra("currentStocks", stringTotalRemainingStock);
                        mContext.startActivity(intent);

                    }
                });

                holder.productDISCOUNT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent1 = new Intent(mContext, DiscountActivity.class);
                        intent1.putExtra("pname", productname);
                        intent1.putExtra("pquantity", productquantity);
                        intent1.putExtra("pid", productcode);
                        intent1.putExtra("pprice", price);
                        intent1.putExtra("ptotal", totalprice);
                        intent1.putExtra("pdiscount", productdiscount);
                        mContext.startActivity(intent1);

                    }
                });

            }

        } else {

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));

            if (!mCursor.moveToPosition(position)) {
                return;
            }

            myDb = new DatabaseHelper(mContext);
            mDatabase = myDb.getWritableDatabase();


            final String productcode = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTCODE));
            final String productname = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTNAME));
            final String productquantity = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTQUANTITY));
            final String pproductprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTSELLPRICE));
            final String producttotalprice = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTTOTALPRICE));
            final String productdiscount = mCursor.getString(mCursor.getColumnIndex(CartContract.CartEntry.KEY_PRODUCTDISCOUNT));
            final long id = mCursor.getLong(mCursor.getColumnIndex(CartContract.CartEntry._ID));

            price = Double.valueOf(pproductprice);
            totalprice = Double.valueOf(producttotalprice);


            //STOCK CHECKER
            final int soldSales, currentQuantity, totalRemainingStocks, inititalQuantity;
            final String stringTotalRemainingStock, count3, countQproducts;

            //total sales of the item
            count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productcode + "'";
            Cursor mcursor3 = mDatabase.rawQuery(count3, null);
            mcursor3.moveToFirst();
            soldSales = mcursor3.getInt(0);


            //total quantity of the item upon adding
            countQproducts = "Select sum(PRODUCTQUANTITY) From products where PRODUCTCODE = '" + productcode + "'";
            Cursor mcursorQproducts = mDatabase.rawQuery(countQproducts, null);
            mcursorQproducts.moveToFirst();
            currentQuantity = mcursorQproducts.getInt(0);


            totalRemainingStocks = currentQuantity - soldSales;
            stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

            //END OF STOCK CHECKER

            holder.productCODE.setText(productcode);
            holder.productNAME.setText(productname);
            holder.productQUANTITY.setText(productquantity);
            holder.productPRICE.setText(format.format(price));
            holder.productTOTALPRICE.setText(format.format(totalprice));
            holder.productDISCOUNT.setText(productdiscount);
            holder.itemView.setTag(id);

            holder.productQUANTITY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, UpdateQuantityActivity.class);
                    intent.putExtra("pname", productname);
                    intent.putExtra("pquantity", productquantity);
                    intent.putExtra("pid", productcode);
                    intent.putExtra("pprice", price);
                    intent.putExtra("ptotal", producttotalprice);
                    intent.putExtra("currentStocks", stringTotalRemainingStock);


                    mContext.startActivity(intent);

                }
            });

            holder.productDISCOUNT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent1 = new Intent(mContext, DiscountActivity.class);
                    intent1.putExtra("pname", productname);
                    intent1.putExtra("pquantity", productquantity);
                    intent1.putExtra("pid", productcode);
                    intent1.putExtra("pprice", price);
                    intent1.putExtra("ptotal", totalprice);
                    intent1.putExtra("pdiscount", productdiscount);
                    mContext.startActivity(intent1);

                }
            });
        }

        mcursorlah.close();

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

    }
}
