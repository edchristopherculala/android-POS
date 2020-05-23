package com.ed.chris.androidpos.admin.admin_adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.ReceiptInformation;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder> {



    private Context mContext;
    private Cursor mCursor;

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation, animation1;

    private int currentSelectedPosition = RecyclerView.NO_POSITION;

    public  SalesAdapter (Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;

    }
    public class SalesViewHolder extends RecyclerView.ViewHolder {

        public TextView txtReceiptNumber;
        public TextView txtDate;
        public TextView txtAmount;

        public ImageView buttonView;


        public LinearLayout select_sales;

        public SalesViewHolder(@NonNull View itemView) {
            super(itemView);


            txtReceiptNumber = itemView.findViewById(R.id.txtreceiptnumbersales);
            txtDate = itemView.findViewById(R.id.txtDateSales);
            txtAmount = itemView.findViewById(R.id.txtAmountSales);

            buttonView = itemView.findViewById(R.id.buttonViewSales);
            select_sales = itemView.findViewById(R.id.select_sales);


        }
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.sales_item,parent,false);
        return new SalesAdapter.SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, final int position) {

        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String receiptNUMBER = mCursor.getString(mCursor.getColumnIndex(SalesContract.SalesEntry.KEY_RECEIPTNO));
        final String saleDATE = mCursor.getString(mCursor.getColumnIndex(SalesContract.SalesEntry.KEY_DATE));
        final String saleAMOUNT = mCursor.getString(mCursor.getColumnIndex(SalesContract.SalesEntry.KEY_TOTALPRICE));

        final long id = mCursor.getLong(mCursor.getColumnIndex(SalesContract.SalesEntry._ID));

        holder.txtReceiptNumber.setText(receiptNUMBER);
        holder.txtDate.setText(saleDATE);
        holder.txtAmount.setText(saleAMOUNT);


        final String stringID = String.valueOf(id);
        holder.select_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //NEED UPDATE CLASS HERE

                currentSelectedPosition = position;
                notifyDataSetChanged();

            }
        });

        if (currentSelectedPosition == position) {

            animation = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
            holder.buttonView.setAnimation(animation);
            holder.buttonView.setVisibility(View.VISIBLE);
            holder.buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // update code

                    Toast.makeText(mContext,stringID,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(mContext, ReceiptInformation.class);
                    intent.putExtra("receiptnumber",receiptNUMBER);
                    mContext.startActivity(intent);

                }
            });
        } else {

            holder.buttonView.setVisibility(View.GONE);
        }

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
