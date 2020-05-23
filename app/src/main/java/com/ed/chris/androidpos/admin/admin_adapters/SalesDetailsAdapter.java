package com.ed.chris.androidpos.admin.admin_adapters;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class SalesDetailsAdapter extends RecyclerView.Adapter<SalesDetailsAdapter.SalesDetailsViewHolder> {



    private Context mContext;
    private Cursor mCursor;

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation, animation1;

    private int currentSelectedPosition = RecyclerView.NO_POSITION;

    public  SalesDetailsAdapter (Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;

    }

    public class SalesDetailsViewHolder extends RecyclerView.ViewHolder {

        public TextView txtReceiptNumber;
        public TextView txtDate;
        public TextView txtSubtotal;
        public TextView txtTotalDiscount;
        public TextView txtTotalPrice;
        public ImageView QrImageSales;


        public LinearLayout select_sales;

        public SalesDetailsViewHolder(@NonNull View itemView) {
            super(itemView);


            txtReceiptNumber = itemView.findViewById(R.id.txtReceiptNodetails);
            txtDate = itemView.findViewById(R.id.txtDateDetails);
            txtSubtotal = itemView.findViewById(R.id.txtSubtotalDetails);
            txtTotalDiscount = itemView.findViewById(R.id.txtTotalDiscountDetails);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalTotalPriceDetails);

            QrImageSales = itemView.findViewById(R.id.qrcodesales);




            select_sales = itemView.findViewById(R.id.select_sales_details);


        }
    }

    @NonNull
    @Override
    public SalesDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.sales_details_item,parent,false);
        return new SalesDetailsAdapter.SalesDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesDetailsViewHolder holder, int position) {


        if (!mCursor.moveToPosition(position)) {
            return;
        }


        final String receiptNUMBER = mCursor.getString(mCursor.getColumnIndex(SalesContract.SalesEntry.KEY_RECEIPTNO));
        final String saleDATE = mCursor.getString(mCursor.getColumnIndex(SalesContract.SalesEntry.KEY_DATE));
        final String saleSUBTOTAL = mCursor.getString(mCursor.getColumnIndex(SalesContract.SalesEntry.KEY_SUBTOTAL));
        final String saleDISCOUNT = mCursor.getString(mCursor.getColumnIndex(SalesContract.SalesEntry.KEY_TOTALDISCOUNT));
        final String saleAMOUNT = mCursor.getString(mCursor.getColumnIndex(SalesContract.SalesEntry.KEY_TOTALPRICE));



        final long id = mCursor.getLong(mCursor.getColumnIndex(SalesContract.SalesEntry._ID));


        byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("QRCODE"));


        Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);

        holder.txtReceiptNumber.setText(receiptNUMBER);
        holder.txtDate.setText(saleDATE);
        holder.txtSubtotal.setText(saleSUBTOTAL);
        holder.txtTotalDiscount.setText(saleDISCOUNT);
        holder.txtTotalPrice.setText(saleAMOUNT);

        holder.QrImageSales.setImageBitmap(bmp);


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
