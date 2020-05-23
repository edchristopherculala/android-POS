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
import com.ed.chris.androidpos.admin.updateclass.EmployeeCreateAccount;
import com.ed.chris.androidpos.admin.updateclass.EmployeeUpdate;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation, animation1;

    private int currentSelectedPosition = RecyclerView.NO_POSITION;



    public EmployeeAdapter (Context context, Cursor cursor){


        mContext = context;
        mCursor = cursor;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder{

        public TextView employeeFirstName;
        public TextView employeeLastName;
        public TextView employeeMobile;
        public TextView employeeEmail;
        public TextView employeeEmployeeType;
        public TextView employeeAddress;
        public LinearLayout type_select_layout;

        public ImageView hasAccount;

        public ImageView delete;
        public ImageView update;
        public ImageView create;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            employeeFirstName = itemView.findViewById(R.id.textview_employee_first_name);
            employeeLastName = itemView.findViewById(R.id.textview_employee_last_name);
            employeeMobile = itemView.findViewById(R.id.textview_employee_mobile);
            employeeEmail = itemView.findViewById(R.id.textview_employee_email);
            employeeEmployeeType = itemView.findViewById(R.id.textview_employeetype);
            employeeAddress = itemView.findViewById(R.id.textview_employee_address);
            type_select_layout = itemView.findViewById(R.id.select_layout_1);

            hasAccount = itemView.findViewById(R.id.imageviewhasAccount);

            update = itemView.findViewById(R.id.circularEdit);
            create = itemView.findViewById(R.id.circularAdd);


        }
    }



    @NonNull
    @Override
    public EmployeeAdapter.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.employee_item,parent,false);
        return new EmployeeAdapter.EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeAdapter.EmployeeViewHolder holder, final int position) {

        if (!mCursor.moveToPosition(position)) {
            return;
        }

        myDb = new DatabaseHelper(mContext);
        mDatabase = myDb.getWritableDatabase();

        final String FirstnameEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.KEY_FIRTSNAME));
        final String LastnameEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.KEY_LASTNAME));
        final String MobileEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.KEY_MOBILE));
        final String EmailEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.KEY_EMAIL));
        final String EmployeeTypeEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.KEY_EMPLOYEETYPE));
        final String AddressEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry.KEY_ADDRESS));

        final long id = mCursor.getLong(mCursor.getColumnIndex(EmployeeContract.EmployeeEntry._ID));

        holder.employeeFirstName.setText(FirstnameEmployee);
        holder.employeeLastName.setText(LastnameEmployee);
        holder.employeeMobile.setText(MobileEmployee);
        holder.employeeEmail.setText(EmailEmployee);
        holder.employeeEmployeeType.setText(EmployeeTypeEmployee);
        holder.employeeAddress.setText(AddressEmployee);
        holder.itemView.setTag(id);



        final String idString = String.valueOf(id);


        String queryFN = "Select * From employeeaccounts where FIRSTNAME = '" + FirstnameEmployee + "'";
        String queryLN = "Select * From employeeaccounts where LASTNAME = '" + LastnameEmployee + "'";
        if (mDatabase.rawQuery(queryFN, null).getCount() > 0  &&  mDatabase.rawQuery(queryLN,
                null).getCount() > 0 ) {

           holder.hasAccount.setVisibility(View.VISIBLE);

        }






        holder.type_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //holder.create.setVisibility(View.VISIBLE);
               // holder.update.setVisibility(View.VISIBLE);

                currentSelectedPosition = position;
                notifyDataSetChanged();
            }
        });

        if (currentSelectedPosition == position) {

            animation = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
            holder.create.setAnimation(animation);
            holder.create.setVisibility(View.VISIBLE);
            holder.create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // create account code

                    String queryFN = "Select * From employeeaccounts where FIRSTNAME = '" + FirstnameEmployee + "'";
                    String queryLN = "Select * From employeeaccounts where LASTNAME = '" + LastnameEmployee + "'";
                    if (mDatabase.rawQuery(queryFN, null).getCount() > 0  &&  mDatabase.rawQuery(queryLN,
                            null).getCount() > 0 ) {

                        Toast.makeText(mContext, "This user already has an active account.", Toast.LENGTH_LONG).show();

                    }else {

                        // Toast.makeText(mContext, String.valueOf(id), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(mContext, EmployeeCreateAccount.class);

                        intent.putExtra("employeeFirstName", FirstnameEmployee);
                        intent.putExtra("employeeLastName", LastnameEmployee);
                        intent.putExtra("employeeID", idString);

                        mContext.startActivity(intent);

                    }
                }
            });

            animation = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
            holder.update.setAnimation(animation);
            holder.update.setVisibility(View.VISIBLE);
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // update code


                    Intent intent = new Intent(mContext, EmployeeUpdate.class);

                    intent.putExtra("fn",FirstnameEmployee);
                    intent.putExtra("ln", LastnameEmployee);

                    intent.putExtra("mobile",MobileEmployee);
                    intent.putExtra("email", EmailEmployee);
                    intent.putExtra("employeetype",EmployeeTypeEmployee);
                    intent.putExtra("address", AddressEmployee);
                    intent.putExtra("employeeID", idString);

                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.create.setVisibility(View.GONE);
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
