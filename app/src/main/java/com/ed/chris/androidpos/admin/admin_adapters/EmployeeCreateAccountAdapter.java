package com.ed.chris.androidpos.admin.admin_adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.R;

public class EmployeeCreateAccountAdapter extends RecyclerView.Adapter<EmployeeCreateAccountAdapter.EmployeeCreateAccountViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public EmployeeCreateAccountAdapter (Context context, Cursor cursor){


        mContext = context;
        mCursor = cursor;
    }

    public class EmployeeCreateAccountViewHolder extends RecyclerView.ViewHolder {

        public TextView employeeFirstName;
        public TextView employeeLastName;
        public TextView employeeUsername;
        public TextView employeePassword;
        public TextView employeeRole;

        public LinearLayout type_select_layout;


        public EmployeeCreateAccountViewHolder(@NonNull View itemView) {
            super(itemView);

            //get id's of items

            employeeFirstName = itemView.findViewById(R.id.txtFN);
            employeeLastName = itemView.findViewById(R.id.txtLN);
            employeeUsername = itemView.findViewById(R.id.txtusername);
            employeePassword = itemView.findViewById(R.id.txtpassword);
            employeeRole = itemView.findViewById(R.id.txtrole);

            type_select_layout = itemView.findViewById(R.id.select_layout_2);




        }
    }

    @NonNull
    @Override
    public EmployeeCreateAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.employee_accounts_item,parent,false);
        return new EmployeeCreateAccountAdapter.EmployeeCreateAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeCreateAccountViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String FirstnameEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_FIRTSNAME));
        final String LastnameEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_LASTNAME));
        final String UsernameEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_USERNAME));
        final String PasswordEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_PASSWORD));
        final String RoleEmployee = mCursor.getString(mCursor.getColumnIndex(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.KEY_ROLE));


        long id = mCursor.getLong(mCursor.getColumnIndex(EmployeeCreateAccountContract.EmployeeCreateAccountEntry._ID));

        holder.employeeFirstName.setText(FirstnameEmployee);
        holder.employeeLastName.setText(LastnameEmployee);
        holder.employeeUsername.setText(UsernameEmployee);
        holder.employeePassword.setText(PasswordEmployee);
        holder.employeeRole.setText(RoleEmployee);

        holder.itemView.setTag(id);




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
