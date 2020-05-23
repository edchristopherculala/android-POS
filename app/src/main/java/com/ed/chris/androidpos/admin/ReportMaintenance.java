package com.ed.chris.androidpos.admin;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.SalesAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.SalesContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportMaintenance extends Fragment {


    private SQLiteDatabase mDatabase;

    private SalesAdapter mAdapter;
    private TextView fromDatetxt, toDatetxt, txtTotalSales;
    private final Calendar myCalendar = Calendar.getInstance();


    public ReportMaintenance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_report_maintenance, container, false);


        DatabaseHelper myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();

        fromDatetxt = v.findViewById(R.id.txtviewfromdate);
        toDatetxt = v.findViewById(R.id.txtviewtodate);

        ImageView buttonFromDate = v.findViewById(R.id.buttonfromdate);
        ImageView buttonToDate = v.findViewById(R.id.buttontodate);

        ImageView buttonSearchRecords = v.findViewById(R.id.buttonsearchdateReport);

        txtTotalSales = v.findViewById(R.id.txtTotalSales);


        String myFormat = "dd-MM-yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromDatetxt.setText(sdf.format(myCalendar.getTime()));
        toDatetxt.setText(sdf.format(myCalendar.getTime()));

        final RecyclerView recyclerview = v.findViewById(R.id.recyclerviewReports);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        //for From Date
        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFrom();
            }

        };

        //for To Date
        final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelTo();
            }

        };

        buttonFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), fromDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), toDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSearchRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MY_TABLE = "sales";
                String DATE_COL = "DATE";

                String fromddd = fromDatetxt.getText().toString();
                String toddd = toDatetxt.getText().toString();

                String count3 = "select * from sales where DATE between '" + fromddd + "' AND '" + toddd + "'";

                if (mDatabase.rawQuery(count3, null).getCount() == 0) {
                    Toast.makeText(getActivity(), "No Data!", Toast.LENGTH_SHORT).show();
                    recyclerview.setVisibility(View.GONE);
                    txtTotalSales.setText("0");

                } else {
                    recyclerview.setVisibility(View.VISIBLE);

                    Toast.makeText(getActivity(), "Data Found!", Toast.LENGTH_SHORT).show();

                    Cursor mcursor3 = mDatabase.rawQuery(count3, null);
                    mcursor3.moveToFirst();
                    String icount3 = mcursor3.getString(3);

                    mAdapter = new SalesAdapter(getActivity(), mcursor3);
                    recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerview.setAdapter(mAdapter);

                    mAdapter.swapCursor(mcursor3);

                    //for total
                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));


                    String count2 = "Select SUM(TOTALPRICE) as Total From sales where DATE between '" + fromddd + "' AND '" + toddd + "'";
                    Cursor mcursor2 = mDatabase.rawQuery(count2, null);
                    mcursor2.moveToFirst();
                    Double icount2 = mcursor2.getDouble(0);

                    final String con = String.valueOf(icount2);
                    txtTotalSales.setText(format.format(icount2));

                }

            }
        });

        return v;
    }

    private void findRecords() {

        String MY_TABLE = "sales";
        String DATE_COL = "DATE";

        String minDate = fromDatetxt.getText().toString();
        String maxDate = toDatetxt.getText().toString();
        String count3 = "select * from " + MY_TABLE + " where " + DATE_COL + " BETWEEN " + minDate + " 00:00:00 AND " + maxDate + " 23:59:59";

        Cursor mcursor3 = mDatabase.rawQuery(count3, null);
        mcursor3.moveToFirst();

        mAdapter.swapCursor(mcursor3);

    }

    private void updateLabelFrom() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromDatetxt.setText(sdf.format(myCalendar.getTime()));

        String a = fromDatetxt.getText().toString();

        Toast.makeText(getActivity(), a, Toast.LENGTH_LONG).show();
    }

    private void updateLabelTo() {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        toDatetxt.setText(sdf.format(myCalendar.getTime()));
        String a = toDatetxt.getText().toString();

        Toast.makeText(getActivity(), a, Toast.LENGTH_LONG).show();
    }


    private Cursor getAllItems() {
        return mDatabase.query(
                SalesContract.SalesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SalesContract.SalesEntry._ID + " DESC"
        );

    }

}
