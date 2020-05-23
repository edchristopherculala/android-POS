package com.ed.chris.androidpos.admin.admin_adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.POS.DiscountActivity;
import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.updateclass.ProductUpdate;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.valueOf;

public class ProductMaintenanceAdapter extends RecyclerView.Adapter<ProductMaintenanceAdapter.ProductMaintenanceViewHolder> {

    private Context mContext;
    private Cursor mCursor, mCursorSold;


    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation, animation1;
    private int currentSelectedPosition = RecyclerView.NO_POSITION;



    public  ProductMaintenanceAdapter (Context context, Cursor cursor, Cursor cursorSold){

        mContext = context;
        mCursor = cursor;
        mCursorSold = cursorSold;




    }



    public class ProductMaintenanceViewHolder extends RecyclerView.ViewHolder {



        public TextView txtProductCode;
        public TextView txtProductCategory;
        public TextView txtProductDescription;
        public TextView txtProductSupplierName;
        public TextView txtProductDateofValidity;
        public TextView txtProductDateofExpiry;
        public TextView txtProductBuyPrice;
        public TextView txtProductSellPrice;
        public TextView txtProductQuantity;

        public ImageView update;
        public ImageView create;

        private Button add_stocks;

        private ImageView productImage;


        public LinearLayout type_select_layout;






        public ProductMaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);




            txtProductCode = itemView.findViewById(R.id.txtProductCode);
            //txtProductCategory = itemView.findViewById(R.id.txtproductcategory);
            txtProductDescription = itemView.findViewById(R.id.txtProductDescription);
          //  txtProductSupplierName = itemView.findViewById(R.id.txtproductsupplier);
           // txtProductDateofValidity = itemView.findViewById(R.id.txtproductvalidity);
           // txtProductDateofExpiry = itemView.findViewById(R.id.txtproductexpiry);
          //  txtProductBuyPrice = itemView.findViewById(R.id.txtproductbuyprice);
          txtProductSellPrice = itemView.findViewById(R.id.txtproductsellprice);
          //  add_stocks = itemView.findViewById(R.id.button_add_stocks);

            txtProductQuantity = itemView.findViewById(R.id.txtProductQUANTITY);

            type_select_layout = itemView.findViewById(R.id.select_layout_5);

           // update = itemView.findViewById(R.id.circularEditP);

            productImage = itemView.findViewById(R.id.productImage);




        }
    }

    @NonNull
    @Override
    public ProductMaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.product_maintenance_item,parent,false);
        return new ProductMaintenanceAdapter.ProductMaintenanceViewHolder(view);



    }

    @Override
    public void onBindViewHolder(@NonNull ProductMaintenanceViewHolder holder, final int position) {


        myDb = new DatabaseHelper(mContext);
        mDatabase = myDb.getWritableDatabase();



        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String productCODE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CODE));
        final String productCATEGORY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_CATEGORY));
        final String productDESCRIPTION = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DESCRIPTION));
        final String productSUPPLIERNAME = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SUPPLIER_NAME));
        final String productDATEOFVALIDITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFVALIDITY));
        final String productDATEOFEXPIRY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_DATEOFEXPIRY));
        final String productBUYPRICE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_BUY_PRICE));
        final String productSELLPRICE = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_SELL_PRICE));
        final String productQUANTITY = mCursor.getString(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry.KEY_PRODUCT_QUANTITY));


        currencyChecker(holder, productSELLPRICE);

        //check sales -- deduct to main quantity
        //STOCK CHECKER
        int soldSales, currentQuantity, totalRemainingStocks;
        String stringTotalRemainingStock,count3;

        count3 = "Select sum(PRODUCTQUANTITY) From salesitems where PRODUCTCODE = '" + productCODE + "'";
        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
        mcursor3.moveToFirst();
        soldSales = mcursor3.getInt(0);

        currentQuantity = Integer.parseInt(productQUANTITY);

        totalRemainingStocks = currentQuantity - soldSales;
        stringTotalRemainingStock = String.valueOf(totalRemainingStocks);

        //END OF STOCK CHECKER


        long id = mCursor.getLong(mCursor.getColumnIndex(ProductMaintenanceContract.ProductMaintenanceEntry._ID));


        byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("PRODUCTIMAGE"));
        final Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

        holder.txtProductCode.setText(productCODE);
       // holder.txtProductCategory.setText(productCATEGORY);
        holder.txtProductDescription.setText(productDESCRIPTION);
       // holder.txtProductSupplierName.setText(productSUPPLIERNAME);
       // holder.txtProductDateofValidity.setText(productDATEOFVALIDITY);
       // holder.txtProductDateofExpiry.setText(productDATEOFEXPIRY);
       // holder.txtProductBuyPrice.setText(productBUYPRICE);


        holder.txtProductQuantity.setText(stringTotalRemainingStock);

        holder.productImage.setImageBitmap(bmp);



        holder.itemView.setTag(id);

        final String idString = String.valueOf(id);




        holder.type_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //holder.create.setVisibility(View.VISIBLE);
                // holder.update.setVisibility(View.VISIBLE);

                currentSelectedPosition = position;
                notifyDataSetChanged();
            }
        });

        if (currentSelectedPosition == position){

            String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";

            if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {


                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(false);
                builder.setTitle("UNABLE TO UPDATE ");
                builder.setMessage("Product cannot be updated. \nProduct is in the cart.");
                builder.setPositiveButton("Okay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();






            }else {

                Intent intent = new Intent(mContext, ProductUpdate.class);

                intent.putExtra("productname", productDESCRIPTION);
                intent.putExtra("productcategory", productCATEGORY);

                intent.putExtra("productsuppliername", productSUPPLIERNAME);
                intent.putExtra("dateofvalidity", productDATEOFVALIDITY);
                intent.putExtra("dateofexpiry", productDATEOFEXPIRY);
                intent.putExtra("buyprice", productBUYPRICE);
                intent.putExtra("sellprice", productSELLPRICE);
                intent.putExtra("ID", idString);




                mContext.startActivity(intent);

            }
        } else {

        }

    /*    if (currentSelectedPosition == position) {


            //for adding of stocks



            animation = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
            holder.update.setAnimation(animation);
            holder.update.setVisibility(View.VISIBLE);
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // update code


                    //check if product is in the cart.
                    //if product existed this cannot be updated

                    String queryProductCODE = "Select * From cart where PRODUCTCODE = '" + productCODE + "'";

                    if (mDatabase.rawQuery(queryProductCODE, null).getCount() > 0) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setCancelable(false);
                        builder.setTitle("UNABLE TO UPDATE ");
                        builder.setMessage("Product cannot be updated. \nProduct is in the cart.");
                        builder.setPositiveButton("Okay",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();






                    }else {

                        Intent intent = new Intent(mContext, ProductUpdate.class);

                        intent.putExtra("productname", productDESCRIPTION);
                        intent.putExtra("productcategory", productCATEGORY);

                        intent.putExtra("productsuppliername", productSUPPLIERNAME);
                        intent.putExtra("dateofvalidity", productDATEOFVALIDITY);
                        intent.putExtra("dateofexpiry", productDATEOFEXPIRY);
                        intent.putExtra("buyprice", productBUYPRICE);
                        intent.putExtra("sellprice", productSELLPRICE);
                        intent.putExtra("ID", idString);




                        mContext.startActivity(intent);

                    }
                }
            });


        } else {

            holder.update.setVisibility(View.GONE);

        }  */


      /*  holder.add_stocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(mContext, Add_Stocks_Dialog.class);
                intent1.putExtra("pname",productDESCRIPTION);
                intent1.putExtra("pquantity",productQUANTITY);
                intent1.putExtra("pid",productCODE);

                mContext.startActivity(intent1);

            }
        });  */

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){

        if (mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

    }

    private void currencyChecker(ProductMaintenanceViewHolder holder, String productSELLPRICE) {


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

                double sellprice = Double.parseDouble(productSELLPRICE);
                BigDecimal bd = new BigDecimal(sellprice);
                String p = String.valueOf(bd);
                holder.txtProductSellPrice.setText(format.format(bd));

            } else if (thisCurrency.equals("Canadian Dollars")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CANADA));

                double sellprice = Double.parseDouble(productSELLPRICE);
                BigDecimal bd = new BigDecimal(sellprice);
                String p = String.valueOf(bd);
                holder.txtProductSellPrice.setText(format.format(bd));

            } else if (thisCurrency.equals("Japanese Yen")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.JAPAN));

                double sellprice = Double.parseDouble(productSELLPRICE);
                BigDecimal bd = new BigDecimal(sellprice);
                String p = String.valueOf(bd);
                holder.txtProductSellPrice.setText(format.format(bd));

            } else if (thisCurrency.equals("US Dollars")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.US));

                double sellprice = Double.parseDouble(productSELLPRICE);
                BigDecimal bd = new BigDecimal(sellprice);
                String p = String.valueOf(bd);
                holder.txtProductSellPrice.setText(format.format(bd));

            } else if (thisCurrency.equals("Chinese Yuan")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.CHINA));

                double sellprice = Double.parseDouble(productSELLPRICE);
                BigDecimal bd = new BigDecimal(sellprice);
                String p = String.valueOf(bd);
                holder.txtProductSellPrice.setText(format.format(bd));


            } else if (thisCurrency.equals("France Euro")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.FRANCE));

                double sellprice = Double.parseDouble(productSELLPRICE);
                BigDecimal bd = new BigDecimal(sellprice);
                String p = String.valueOf(bd);
                holder.txtProductSellPrice.setText(format.format(bd));

            } else if (thisCurrency.equals("Korea Won")) {
                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance(Locale.KOREA));

                double sellprice = Double.parseDouble(productSELLPRICE);
                BigDecimal bd = new BigDecimal(sellprice);
                String p = String.valueOf(bd);
                holder.txtProductSellPrice.setText(format.format(bd));


            } else if (icountP == 0) {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));
                double sellprice = Double.parseDouble(productSELLPRICE);
                BigDecimal bd = new BigDecimal(sellprice);
                String p = String.valueOf(bd);
                holder.txtProductSellPrice.setText(format.format(bd));




            }


        }
    }

}
