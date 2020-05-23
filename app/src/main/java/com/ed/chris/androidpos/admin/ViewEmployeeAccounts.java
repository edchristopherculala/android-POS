package com.ed.chris.androidpos.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeCreateAccountAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeCreateAccountContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

public class ViewEmployeeAccounts extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private EmployeeCreateAccountAdapter mAdapter;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee_accounts);


        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();

        RecyclerView recyclerview = findViewById(R.id.recyclerviewEmployeeAccounts);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EmployeeCreateAccountAdapter(this, getAllItems());
        recyclerview.setAdapter(mAdapter);

        //for swipe deleting
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewEmployeeAccounts.this);
                builder.setCancelable(false);
                builder.setTitle("DELETE");
                builder.setMessage("Are you sure you want to delete this?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                removeItem((long) viewHolder.itemView.getTag());
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mAdapter.swapCursor(getAllItems());
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }
        }).attachToRecyclerView(recyclerview);

    }

    //for swipe deleting
    private void removeItem(long id) {
        mDatabase.delete(EmployeeCreateAccountContract.EmployeeCreateAccountEntry.TABLE_NAME,
                EmployeeCreateAccountContract.EmployeeCreateAccountEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }

    private Cursor getAllItems() {
        return mDatabase.query(
                EmployeeCreateAccountContract.EmployeeCreateAccountEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                EmployeeCreateAccountContract.EmployeeCreateAccountEntry._ID + " DESC"
        );

    }


}
