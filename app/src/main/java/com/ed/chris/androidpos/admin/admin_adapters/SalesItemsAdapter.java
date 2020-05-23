package com.ed.chris.androidpos.admin.admin_adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class SalesItemsAdapter extends RecyclerView.Adapter<SalesItemsAdapter.SalesItemsViewHolder> {


    private Context mContext;
    private Cursor mCursor;

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation, animation1;

    private int currentSelectedPosition = RecyclerView.NO_POSITION;

    public  SalesItemsAdapter (Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;

    }

    public class SalesItemsViewHolder extends RecyclerView.ViewHolder {

        public TextView txtProductName;
        public TextView txtProductCode;
        public TextView txtSellPrice1;
        public TextView txtSellPrice;
        public TextView txtDiscount;
        public TextView txtQuantity;
        public TextView txtTotalPrice;




        public RelativeLayout select_sales_item;

        public SalesItemsViewHolder(@NonNull View itemView) {
            super(itemView);


            txtProductName = itemView.findViewById(R.id.productNameRPI);
            txtProductCode = itemView.findViewById(R.id.productCodeRPI);
            txtSellPrice1 = itemView.findViewById(R.id.txtsellpriceRPI1);
            txtSellPrice = itemView.findViewById(R.id.txtsellpriceRPI);
            txtDiscount = itemView.findViewById(R.id.txtDiscountRPI);
            txtQuantity = itemView.findViewById(R.id.txtquantityRPI);
            txtTotalPrice = itemView.findViewById(R.id.txttotalpriceRPI);


            select_sales_item = itemView.findViewById(R.id.selectitemRPI);


        }
    }

    @NonNull
    @Override
    public SalesItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.receipt_products_item,parent,false);
        return new SalesItemsAdapter.SalesItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesItemsViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position)) {
            return;
        }


        final String PRODUCTNAME = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTNAME));
        final String PRODUCTCODE = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTCODE));
        final String SELLPRICE = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTSELLPRICE));

        final String DISCOUNT = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTDISCOUNT));
        final String QUANTITY = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTQUANTITY));
        final String TOTALPRICE = mCursor.getString(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry.KEY_PRODUCTTOTALPRICE));

        final long id = mCursor.getLong(mCursor.getColumnIndex(SalesItemsContract.SalesItemsEntry._ID));

        holder.txtProductName.setText(PRODUCTNAME);
        holder.txtProductCode.setText(PRODUCTCODE);
        holder.txtSellPrice1.setText(SELLPRICE);
        holder.txtSellPrice.setText(SELLPRICE);
        holder.txtDiscount.setText(DISCOUNT);
        holder.txtQuantity.setText(QUANTITY);
        holder.txtTotalPrice.setText(TOTALPRICE);


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
