package com.ed.chris.androidpos.admin.admin_adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class SalesSummaryAdapter extends RecyclerView.Adapter<SalesSummaryAdapter.SalesSummaryViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation, animation1;

    private int currentSelectedPosition = RecyclerView.NO_POSITION;

    public  SalesSummaryAdapter (Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;

    }

    public class SalesSummaryViewHolder extends RecyclerView.ViewHolder {

        public TextView txtProductnamesalesummary;
        public TextView txtQuantity;
        public TextView txtDiscount;
        public TextView txtPrice;


        public LinearLayout select_sales;

        public SalesSummaryViewHolder(@NonNull View itemView) {
            super(itemView);



            txtProductnamesalesummary = itemView.findViewById(R.id.txtProductNameSalesSummary);
            txtQuantity = itemView.findViewById(R.id.txtProductQuantitySalesSummary);
            txtDiscount = itemView.findViewById(R.id.txtProductDiscountSalesSummary);
            txtPrice = itemView.findViewById(R.id.txtProductTotalPriceSalesSummary);


            select_sales = itemView.findViewById(R.id.select_sale_summary_item);


        }
    }

    @NonNull
    @Override
    public SalesSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.sale_summary_item,parent,false);
        return new SalesSummaryAdapter.SalesSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesSummaryViewHolder holder, int position) {


        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));


        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String productNAME = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTNAME));
        final String soldQUANTITY = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTQUANTITY));
        final String productDISCOUNT = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_TOTALDISCOUNT));
        final String productAMOUNT = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTTOTALPRICE));
        final long id = mCursor.getLong(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry._ID));


        Double discount,totalamount;


        discount = Double.valueOf(productDISCOUNT);
        totalamount = Double.valueOf(productAMOUNT);


        holder.txtProductnamesalesummary.setText(productNAME);
        holder.txtQuantity.setText(soldQUANTITY);
        holder.txtDiscount.setText(format.format(discount));
        holder.txtPrice.setText(format.format(totalamount));


        final String stringID = String.valueOf(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    public void swapCursor(Cursor newCursor){


        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

    }
}
