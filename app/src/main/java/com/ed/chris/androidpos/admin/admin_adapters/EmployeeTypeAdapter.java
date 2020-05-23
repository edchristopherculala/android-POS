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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.admin.admin_adapters.EmployeeTypeContract.*;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.updateclass.EmployeeTypeUpdate;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class EmployeeTypeAdapter extends RecyclerView.Adapter<EmployeeTypeAdapter.EmployeeTypeViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;
    Animation animation;

    private int currentSelectedPosition = RecyclerView.NO_POSITION;

    public  EmployeeTypeAdapter (Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;

    }

    public class EmployeeTypeViewHolder extends RecyclerView.ViewHolder{

        public TextView employeeTypeText;
        public TextView descriptionEmployeeTypeText;
        public RelativeLayout type_select_layout;
        public TextView EditTextEmployee;
        public TextView EditTextEmployeeDescription;

        public ImageView update;



        public EmployeeTypeViewHolder(@NonNull View itemView) {
            super(itemView);

            employeeTypeText = itemView.findViewById(R.id.textview_employee_type);
            descriptionEmployeeTypeText = itemView.findViewById(R.id.textview_employee_type_description);
            type_select_layout = itemView.findViewById(R.id.select_layout);

            update = itemView.findViewById(R.id.circularEdit1);

        }


    }

    @NonNull
    @Override
    public EmployeeTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.employeetype_item,parent,false);
        return new EmployeeTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeTypeViewHolder holder,final int position) {

        if (!mCursor.moveToPosition(position)) {
            return;
        }

        myDb = new DatabaseHelper(mContext);
        mDatabase = myDb.getWritableDatabase();

            final String employeetype = mCursor.getString(mCursor.getColumnIndex(EmployeeTypeEntry.KEY_EMPLOYEETYPE));
            final String employeetypedescription = mCursor.getString(mCursor.getColumnIndex(EmployeeTypeEntry.KEY_DESCRIPTION));
            long id = mCursor.getLong(mCursor.getColumnIndex(EmployeeTypeEntry._ID));

            holder.employeeTypeText.setText(employeetype);
            holder.descriptionEmployeeTypeText.setText(employeetypedescription);
            holder.itemView.setTag(id);


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
            holder.update.setAnimation(animation);
            holder.update.setVisibility(View.VISIBLE);
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // create account code

                    String queryetype = "Select * From employee where EMPLOYEETYPE = '" + employeetype + "'";

                    if (mDatabase.rawQuery(queryetype, null).getCount() > 0) {

                        Toast.makeText(mContext, "Cannot be update. Employee type is still in use", Toast.LENGTH_LONG).show();

                    }else {

                        // Toast.makeText(mContext, String.valueOf(id), Toast.LENGTH_LONG).show();

                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View v = inflater.inflate(R.layout.fragment_employee_type, null);


                        TextView editextEmployeeType = v.findViewById(R.id.displaytxt);
                        EditText editextEmployeeTypeDescription = v.findViewById(R.id.edittxtdescription1);


                        //Toast.makeText(mContext,employeetype ,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(mContext, EmployeeTypeUpdate.class);

                        intent.putExtra("employeetype", employeetype);
                        intent.putExtra("employeetypedescription", employeetypedescription);

                        mContext.startActivity(intent);



                    }
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
