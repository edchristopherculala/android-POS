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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.updateclass.SupplierUpdate;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class SupplierCreateAdapter extends RecyclerView.Adapter<SupplierCreateAdapter.SupplierCreateViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation, animation1;

    private int currentSelectedPosition = RecyclerView.NO_POSITION;

    public  SupplierCreateAdapter (Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;

    }


    public class SupplierCreateViewHolder extends RecyclerView.ViewHolder{

        public TextView txtSupplierNAME;
        public TextView txtSupplierCODE;
        public TextView txtSupplierADDRESS;
        public TextView txtSupplierEMAIL;
        public TextView txtSupplierTELEPHONE;

        public ImageView update;

        public LinearLayout type_select_layout;
        public SupplierCreateViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSupplierNAME = itemView.findViewById(R.id.txtSupplierName);
            txtSupplierCODE = itemView.findViewById(R.id.txtSupplierCode);
            txtSupplierADDRESS = itemView.findViewById(R.id.txtSuplierAddress);
            txtSupplierEMAIL = itemView.findViewById(R.id.txtSupplierEmail);
            txtSupplierTELEPHONE = itemView.findViewById(R.id.txtSupplierTelephone);

            type_select_layout = itemView.findViewById(R.id.select_layout_4);

            update = itemView.findViewById(R.id.circularEditS);




        }
    }

    @NonNull
    @Override
    public SupplierCreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.supplier_item,parent,false);
        return new SupplierCreateAdapter.SupplierCreateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierCreateViewHolder holder, final int position) {

        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String supplierNAME = mCursor.getString(mCursor.getColumnIndex(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_NAME));
        final String supplierCODE = mCursor.getString(mCursor.getColumnIndex(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_CODE));
        final String supplierADDRESS = mCursor.getString(mCursor.getColumnIndex(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_ADDRESS));
        final String supplierEMAIL = mCursor.getString(mCursor.getColumnIndex(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_EMAIL));
        final String supplierTELEPHONE = mCursor.getString(mCursor.getColumnIndex(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_TELEPHONE));

        long id = mCursor.getLong(mCursor.getColumnIndex(SupplierCreateContract.SupplierAccountEntry._ID));
        String suppliername = mCursor.getString(mCursor.getColumnIndex(SupplierCreateContract.SupplierAccountEntry.KEY_SUPPLIER_NAME));


        holder.txtSupplierNAME.setText(supplierNAME);
        holder.txtSupplierCODE.setText(supplierCODE);
        holder.txtSupplierADDRESS.setText(supplierADDRESS);
        holder.txtSupplierEMAIL.setText(supplierEMAIL);
        holder.txtSupplierTELEPHONE.setText(supplierTELEPHONE);



        holder.itemView.setTag(id);
       // holder.itemView.setTag(suppliername);

        final String idString = String.valueOf(id);

        holder.type_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //NEED UPDATE CLASS HERE

                currentSelectedPosition = position;
                notifyDataSetChanged();

            }
        });

        if (currentSelectedPosition == position) {

            animation = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
            holder.update.setAnimation(animation);
            holder.update.setVisibility(View.VISIBLE);
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // update code

                    Intent intent = new Intent(mContext, SupplierUpdate.class);

                    intent.putExtra("suppliername",supplierNAME);
                    intent.putExtra("supplieraddress", supplierADDRESS);

                    intent.putExtra("supplieremail",supplierEMAIL);
                    intent.putExtra("suppliertelephone", supplierTELEPHONE);
                    intent.putExtra("employeeID", idString);

                    mContext.startActivity(intent);
                }
            });
        } else {

            holder.update.setVisibility(View.GONE);
        }
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
}
