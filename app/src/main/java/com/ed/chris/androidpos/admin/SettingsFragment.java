package com.ed.chris.androidpos.admin;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.admin_adapters.CurrencySettingsContract;
import com.ed.chris.androidpos.admin.admin_adapters.ReceiptLayoutContract;
import com.ed.chris.androidpos.database.DatabaseHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;


    private EditText edtAPPNAME, edtCOMPANYADDRESS, edtCOMPANYTELEPHONE, edtFOOTER1, edtFOOTER2;
    private TextView txtCurrency;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        myDb = new DatabaseHelper(getActivity());
        mDatabase = myDb.getWritableDatabase();
        Spinner spinnerCurrency = v.findViewById(R.id.spinnerCurrency);
        txtCurrency = v.findViewById(R.id.txtviewCurrency);
        TextView whatCurrency = v.findViewById(R.id.txtviewwhatCurrency);
        ImageView buttonSaveCURRENCY = v.findViewById(R.id.buttonsaveCurrency);

        String count = "SELECT count(*) FROM currencysettings";
        Cursor mcursor = mDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        String a = String.valueOf(icount);
        if (icount > 0) {

            String currency1 = "SELECT CURRENCY FROM currencysettings";
            Cursor mcursor1 = mDatabase.rawQuery(currency1, null);
            mcursor1.moveToFirst();
            String thisCurrency = mcursor1.getString(0);

            whatCurrency.setText(thisCurrency);
        } else {

            whatCurrency.setText("Currency has not been set");

        }

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Philippine Peso");
        arrayList.add("Canadian Dollars");
        arrayList.add("Japanese Yen");
        arrayList.add("US Dollars");
        arrayList.add("Chinese Yuan");
        arrayList.add("France Euro");
        arrayList.add("Korea Won");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(arrayAdapter);

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String settingsCurrency = parent.getItemAtPosition(position).toString();
                txtCurrency.setText(settingsCurrency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonSaveCURRENCY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrency();
            }
        });
        edtAPPNAME = v.findViewById(R.id.edittxtAPPNAME);
        edtCOMPANYADDRESS = v.findViewById(R.id.edittxtCOMPANYADDRESS);
        edtCOMPANYTELEPHONE = v.findViewById(R.id.edittxtCOMPANYTELEPHONE);
        edtFOOTER1 = v.findViewById(R.id.edittxtFOOTER1);
        edtFOOTER2 = v.findViewById(R.id.edittxtFOOTER2);
        ImageView buttonSAVERECEIPTDETAILS = v.findViewById(R.id.buttonsavereceiptsettings);
        buttonSAVERECEIPTDETAILS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        return v;
    }


    private void addItem() {

        String APPNAME, COMPANYADDRESS, COMPANYTELEPHONE, FIRSTFOOTER, SECONDFOOTER;
        String paddedAPPNAME, paddedCOMPANYADDRESS, paddedCOMPANYTELEPHONE, paddedFIRSTFOOTER, paddedSECONDFOOTER;

        APPNAME = edtAPPNAME.getText().toString();
        COMPANYADDRESS = edtCOMPANYADDRESS.getText().toString();
        COMPANYTELEPHONE = edtCOMPANYTELEPHONE.getText().toString();
        FIRSTFOOTER = edtFOOTER1.getText().toString();
        SECONDFOOTER = edtFOOTER2.getText().toString();

        paddedAPPNAME = "                         " + APPNAME;
        paddedCOMPANYADDRESS = "\n                            " + COMPANYADDRESS;
        paddedCOMPANYTELEPHONE = "\n                            " + COMPANYTELEPHONE;


        paddedFIRSTFOOTER = "\n" + FIRSTFOOTER;
        paddedSECONDFOOTER = "" + SECONDFOOTER;


        if (APPNAME.isEmpty() && COMPANYADDRESS.isEmpty() && COMPANYTELEPHONE.isEmpty() && FIRSTFOOTER.isEmpty() && SECONDFOOTER.isEmpty()) {

            Toast.makeText(getActivity(), "Please fill in the form.", Toast.LENGTH_LONG).show();

        } else if (APPNAME.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter app name", Toast.LENGTH_LONG).show();

        } else if (COMPANYADDRESS.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter company address", Toast.LENGTH_LONG).show();
        } else if (COMPANYTELEPHONE.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter company telephone", Toast.LENGTH_LONG).show();
        } else if (FIRSTFOOTER.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter first footer", Toast.LENGTH_LONG).show();
        } else if (SECONDFOOTER.isEmpty()) {

            Toast.makeText(getActivity(), "Please enter second footer", Toast.LENGTH_LONG).show();
        } else {


            String receiptsettings = "SELECT * FROM receiptsettings";
            Cursor receiptcursor = mDatabase.rawQuery(receiptsettings, null);
            receiptcursor.moveToFirst();

            int counter = receiptcursor.getCount();

            if (counter > 0) {
                String UNIQUEID = ("1");

                mDatabase.execSQL("UPDATE  " + ReceiptLayoutContract.ReceiptLayoutEntry.TABLE_NAME + " SET APPNAME ='" + paddedAPPNAME + "'," +
                        " COMPANYADDRESS ='" + paddedCOMPANYADDRESS + "'," + " COMPANYTELEPHONE = '" + paddedCOMPANYTELEPHONE + "'," +
                        " FOOTERFIRST = '" + paddedFIRSTFOOTER + "'," + " FOOTERSECOND = '" + paddedSECONDFOOTER + "' WHERE _ID='" + UNIQUEID + "'");
                Toast.makeText(getActivity(), "Settings Updated!", Toast.LENGTH_LONG).show();

            } else {

                ContentValues cv = new ContentValues();
                cv.put(ReceiptLayoutContract.ReceiptLayoutEntry.KEY_APP_NAME, paddedAPPNAME);
                cv.put(ReceiptLayoutContract.ReceiptLayoutEntry.KEY_COMPANY_ADDRESS, paddedCOMPANYADDRESS);
                cv.put(ReceiptLayoutContract.ReceiptLayoutEntry.KEY_COMPANY_TELEPHONE, paddedCOMPANYTELEPHONE);
                cv.put(ReceiptLayoutContract.ReceiptLayoutEntry.KEY_FOOTER1, paddedFIRSTFOOTER);
                cv.put(ReceiptLayoutContract.ReceiptLayoutEntry.KEY_FOOTER2, paddedSECONDFOOTER);

                mDatabase.insert(ReceiptLayoutContract.ReceiptLayoutEntry.TABLE_NAME, null, cv);

                Toast.makeText(getActivity(), "Settings for receipt added!", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void saveCurrency() {

        String count = "SELECT count(*) FROM currencysettings";
        Cursor mcursor = mDatabase.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        String a = String.valueOf(icount);
        if (icount > 0) {
            String newCurrency = txtCurrency.getText().toString();

            mDatabase.execSQL("UPDATE  " + CurrencySettingsContract.CurrencySettingsEntry.TABLE_NAME +
                    " SET CURRENCY ='" + newCurrency + "'");

            Toast.makeText(getActivity(), "Currency updated to " + newCurrency, Toast.LENGTH_SHORT).show();

        } else {

            String currency1 = "SELECT CURRENCY FROM currencysettings";
            Cursor mcursor1 = mDatabase.rawQuery(currency1, null);
            mcursor1.moveToFirst();

            String currency = txtCurrency.getText().toString().trim();
            ContentValues cv = new ContentValues();

            cv.put(CurrencySettingsContract.CurrencySettingsEntry.KEY_CURRENCY, currency);
            mDatabase.insert(CurrencySettingsContract.CurrencySettingsEntry.TABLE_NAME, null, cv);

            Toast.makeText(getActivity(), currency + "Successfull!", Toast.LENGTH_SHORT).show();
        }

    }

}
