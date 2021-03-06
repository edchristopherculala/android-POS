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
import com.ed.chris.androidpos.admin.admin_adapters.SalesSummaryAdapter;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaleSummaryReportMaintenance extends Fragment {


    private SQLiteDatabase mDatabase;

    //change adapter to reportsadapter
    private SalesSummaryAdapter mAdapter;
    private TextView fromDatetxt, toDatetxt, txtTotalSales;
    private final Calendar myCalendar = Calendar.getInstance();

    public SaleSummaryReportMaintenance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sale_summary_report_maintenance, container, false);

        DatabaseHelper myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();

        fromDatetxt = v.findViewById(R.id.txtviewfromdate1);
        toDatetxt = v.findViewById(R.id.txtviewtodate1);

        ImageView buttonFromDate = v.findViewById(R.id.buttonfromdate1);
        ImageView buttonToDate = v.findViewById(R.id.buttontodate1);

        ImageView buttonSearchRecords = v.findViewById(R.id.buttonsearchdateReport1);

        txtTotalSales = v.findViewById(R.id.txtTotalSales1);

        String myFormat = "dd-MM-yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromDatetxt.setText(sdf.format(myCalendar.getTime()));
        toDatetxt.setText(sdf.format(myCalendar.getTime()));

        final RecyclerView recyclerview = v.findViewById(R.id.recyclerviewSaleSummaryReports);
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

                String count3 = "select _ID, PRODUCTNAME, (SELECT SUM(PRODUCTQUANTITY) where DATE between '" + fromddd + "' AND '" + toddd + "') AS PRODUCTQUANTITY, " +
                        "(SELECT SUM(TOTALDISCOUNT) where DATE between '" + fromddd + "' AND '" + toddd + "') AS TOTALDISCOUNT, " +
                        "(SELECT SUM(PRODUCTTOTALPRICE) where DATE between '" + fromddd + "' AND '" + toddd + "') AS PRODUCTTOTALPRICE " +
                        "from salessummary where DATE between '" + fromddd + "' AND '" + toddd + "' GROUP BY PRODUCTNAME";

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

                    mAdapter = new SalesSummaryAdapter(getActivity(), mcursor3);
                    recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerview.setAdapter(mAdapter);

                    mAdapter.swapCursor(mcursor3);

                    //for total
                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("tl", "PH"));


                    String count2 = "Select SUM(PRODUCTTOTALPRICE) as Total From salessummary where DATE between '" + fromddd + "' AND '" + toddd + "'";
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


}
