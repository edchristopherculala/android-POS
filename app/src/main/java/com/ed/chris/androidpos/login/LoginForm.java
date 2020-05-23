package com.ed.chris.androidpos.login;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.AdminMain;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeAdapter;
import com.ed.chris.androidpos.admin.admin_adapters.EmployeeLoginLogoutContract;
import com.ed.chris.androidpos.admin.admin_adapters.TenLoginTrialContract;
import com.ed.chris.androidpos.database.DatabaseHelper;
import com.ed.chris.androidpos.user.User_Main;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.trialy.library.Trialy;
import io.trialy.library.TrialyCallback;

import static io.trialy.library.Constants.STATUS_TRIAL_JUST_ENDED;
import static io.trialy.library.Constants.STATUS_TRIAL_JUST_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_NOT_YET_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_OVER;
import static io.trialy.library.Constants.STATUS_TRIAL_RUNNING;

public class LoginForm extends AppCompatActivity {

    private ImageView ButtonAdmin, ButtonEmployee, buttonClose;
    private EditText editTextUsername, editTextPassword;
    private Button loginUser;

    private TextView txtTrial;
    private SQLiteDatabase mDatabase;
    private EmployeeAdapter mAdapter;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        final Trialy mTrialy = new Trialy(this, "YY2M1C5JSV5QLH8UYJA");
        mTrialy.checkTrial("default", mTrialyCallback);

        editTextUsername = findViewById(R.id.edittxtUSERNAME);
        editTextPassword = findViewById(R.id.edittxtPASSWORD);
        buttonClose = findViewById(R.id.btnCloseApp);
        loginUser = findViewById(R.id.buttonLogin);


       /* txtTrial = findViewById(R.id.txtTrial);


        txtTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrialy.startTrial("default", mTrialyCallback);
                Toast.makeText(LoginForm.this,"Trial started and is available for 7 days!", Toast.LENGTH_LONG).show();


            }
        }); */
        myDb = new DatabaseHelper(this);
        mDatabase = myDb.getWritableDatabase();


        /*
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(LoginForm.this);
                builder.setCancelable(true);
                builder.setTitle("Exit");
                builder.setMessage("Closing Application. Continue?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                                System.exit(0);



                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });   */

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
                //adminLogin();
            }
        });

    }

    private void Login(){

        String MASTER_USERNAME = "admin";
        String MASTER_PASSWORD = "admin";
        String TENLOGINTRIAL_USERNAME = "aaasss";
        String TENLOGINTRIAL_PASSWORD = "aaaasss";

       String USERNAME = editTextUsername.getText().toString();
       String PASSWORD = editTextPassword.getText().toString();

       if (USERNAME.isEmpty() && PASSWORD.isEmpty()){
           Toast.makeText(LoginForm.this,"Please Input Username and Password", Toast.LENGTH_LONG).show();
       }else if (USERNAME.isEmpty()){
           Toast.makeText(LoginForm.this,"Please Input Username", Toast.LENGTH_LONG).show();
       }else if (PASSWORD.isEmpty()){
           Toast.makeText(LoginForm.this,"Please Input Password", Toast.LENGTH_LONG).show();
       }else if (USERNAME.equalsIgnoreCase(MASTER_USERNAME) && PASSWORD.equalsIgnoreCase(MASTER_PASSWORD)){

           //add timein timeout


           String empFirstname = "MASTERUSERNAME";
           String empLastname = "MASTERPASSWORD";
           String admin = "ADMIN";
           String out = "na";
           String currentTime;
           Date currentTime1 = Calendar.getInstance().getTime();
           currentTime = String.valueOf(currentTime1);

           ContentValues cv = new ContentValues();
           cv.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEEFIRSTNAME, empFirstname);
           cv.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEELASTNAME, empLastname);
           cv.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEEROLE, admin);
           cv.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEIN, currentTime);
           cv.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEOUT, out);
           mDatabase.insert(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.TABLE_NAME, null, cv);


           Toast.makeText(LoginForm.this,"Welcome Admin!", Toast.LENGTH_LONG).show();
           finish();

           Intent intent = new Intent(LoginForm.this, AdminMain.class);
           startActivity(intent);


       }else if (USERNAME.equalsIgnoreCase(TENLOGINTRIAL_USERNAME) && PASSWORD.equalsIgnoreCase(TENLOGINTRIAL_PASSWORD)){

           String count = "SELECT * FROM trial";
           Cursor mcursor = mDatabase.rawQuery(count, null);
           mcursor.moveToFirst();
           int loginCounter = mcursor.getCount();

           if(loginCounter<5) {

               String loginattempt = "okay";
               String count1 = "SELECT * FROM trial";
               Cursor mcursor1 = mDatabase.rawQuery(count1, null);
               mcursor1.moveToFirst();
               int howmany = mcursor1.getCount();
               ContentValues cv = new ContentValues();
               cv.put(TenLoginTrialContract.TenLoginTrialEntry.KEY_LOGIN, loginattempt);
               mDatabase.insert(TenLoginTrialContract.TenLoginTrialEntry.TABLE_NAME, null, cv);


              //add time in timeout

               String s = "Select * From employeeaccounts where USERNAME = '" + USERNAME + "'";
               Cursor ms = mDatabase.rawQuery(s, null);

               ms.moveToFirst();
               String empFirstname = ms.getString(1);
               String empLastname = ms.getString(2);
               String admin = "ADMIN";
               String out = "na";
               String currentTime;
               Date currentTime1 = Calendar.getInstance().getTime();
               currentTime = String.valueOf(currentTime1);

               ContentValues cv1 = new ContentValues();
               cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEEFIRSTNAME, empFirstname);
               cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEELASTNAME, empLastname);
               cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEEROLE, admin);
               cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEIN, currentTime);
               cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEOUT, out);
               mDatabase.insert(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.TABLE_NAME, null, cv1);

               ms.close();


               Intent intent = new Intent(LoginForm.this, AdminMain.class);
               startActivity(intent);
               finish();


           }else{

               Toast.makeText(LoginForm.this,"Trial Ended!", Toast.LENGTH_LONG).show();

           }

       }  else {

           String queryUSERNAME = "Select * From employeeaccounts where USERNAME = '" + USERNAME + "'";
           String queryPASSWORD = "Select * From employeeaccounts where PASSWORD = '" + PASSWORD + "'";
           if (mDatabase.rawQuery(queryUSERNAME, null).getCount() > 0 && mDatabase.rawQuery(queryPASSWORD,
                   null).getCount() > 0) {

               String result = "";
               String queryStudent = "Select ROLE From employeeaccounts where USERNAME = '" + USERNAME +
                       "'AND PASSWORD = '" + PASSWORD + "' ";
               Cursor cursor = mDatabase.rawQuery(queryStudent, null);
               if (cursor.moveToFirst()) {
                   result = cursor.getString(cursor.getColumnIndex("ROLE"));
               }
               cursor.close();

               if (result.equalsIgnoreCase("admin")){

                   String firstname = "";
                   String queryFN = "Select FIRSTNAME From employeeaccounts where USERNAME = '" + USERNAME +
                           "'AND PASSWORD = '" + PASSWORD + "' ";
                   Cursor cursor1 = mDatabase.rawQuery(queryFN, null);
                   if (cursor1.moveToFirst()) {
                       firstname = cursor1.getString(cursor1.getColumnIndex("FIRSTNAME"));
                   }

                   String FN = firstname;
                   cursor1.close();
                   String lastname = "";
                   String queryLN = "Select LASTNAME From employeeaccounts where USERNAME = '" + USERNAME +
                           "'AND PASSWORD = '" + PASSWORD + "' ";
                   Cursor cursor2 = mDatabase.rawQuery(queryLN, null);
                   if (cursor2.moveToFirst()) {
                       lastname = cursor2.getString(cursor2.getColumnIndex("LASTNAME"));
                   }

                   String LN = lastname;
                   cursor2.close();

                   //for trial -- remove if full version is needed
                   String count = "SELECT * FROM trial";
                   Cursor mcursor = mDatabase.rawQuery(count, null);
                   mcursor.moveToFirst();

                   int loginCounter = mcursor.getCount();

                   if(loginCounter<10) {

                       String loginattempt = "okay";
                       String count1 = "SELECT * FROM trial";
                       Cursor mcursor1 = mDatabase.rawQuery(count1, null);
                       mcursor1.moveToFirst();

                       int howmany = mcursor1.getCount();

                       ContentValues cv = new ContentValues();
                       cv.put(TenLoginTrialContract.TenLoginTrialEntry.KEY_LOGIN, loginattempt);

                       mDatabase.insert(TenLoginTrialContract.TenLoginTrialEntry.TABLE_NAME, null, cv);
                       finish();

                       //add timein timeout -- admin
                       //add time in timeout

                       String s = "Select * From employeeaccounts where USERNAME = '" + USERNAME + "'";
                       Cursor ms = mDatabase.rawQuery(s, null);

                       ms.moveToFirst();
                       String empFirstname = ms.getString(1);
                       String empLastname = ms.getString(2);
                       String admin = "ADMIN";
                       String out = "na";
                       String currentTime;
                       Date currentTime1 = Calendar.getInstance().getTime();
                       currentTime = String.valueOf(currentTime1);

                       ContentValues cv1 = new ContentValues();
                       cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEEFIRSTNAME, empFirstname);
                       cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEELASTNAME, empLastname);
                       cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEEROLE, admin);
                       cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEIN, currentTime);
                       cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEOUT, out);
                       mDatabase.insert(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.TABLE_NAME, null, cv1);

                       ms.close();


                       Intent intent = new Intent(LoginForm.this, AdminMain.class);
                       intent.putExtra("fn",FN);
                       intent.putExtra("ln",LN);
                       startActivity(intent);

                   }else{

                       Toast.makeText(LoginForm.this,"Trial Ended!", Toast.LENGTH_LONG).show();

                   }
               }
               if (result.equalsIgnoreCase("user")){

                   //add timein timeout --user
                   //add time in timeout

                   String s = "Select * From employeeaccounts where USERNAME = '" + USERNAME + "'";
                   Cursor ms = mDatabase.rawQuery(s, null);

                   ms.moveToFirst();
                   String empFirstname = ms.getString(1);
                   String empLastname = ms.getString(2);
                   String user = "USER";
                   String out = "na";
                   String currentTime;
                   Date currentTime1 = Calendar.getInstance().getTime();
                   currentTime = String.valueOf(currentTime1);

                   ContentValues cv1 = new ContentValues();
                   cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEEFIRSTNAME, empFirstname);
                   cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEELASTNAME, empLastname);
                   cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEEROLE, user);
                   cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEIN, currentTime);
                   cv1.put(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.KEY_EMPLOYEETIMEOUT, out);
                   mDatabase.insert(EmployeeLoginLogoutContract.EmployeeLoginLogoutEntry.TABLE_NAME, null, cv1);

                   ms.close();


                   Intent intent = new Intent(LoginForm.this, User_Main.class);
                   startActivity(intent);

                   String count = "Select * From employeeaccounts where USERNAME = '" + USERNAME + "'";
                   Cursor mcursor = mDatabase.rawQuery(count, null);

                   mcursor.moveToFirst();
                   String empFirstname1 = mcursor.getString(1);
                   String empLastname1 = mcursor.getString(2);

                   Toast.makeText(LoginForm.this, "Welcome " +empFirstname1+ " " +empLastname1,Toast.LENGTH_LONG).show();
                   mcursor.close();
                   finish();


               }

           } else {

               Toast.makeText(this, "Account credentials not found!", Toast.LENGTH_LONG).show();

           }
       }
    }

    @Override
    public void onBackPressed() {

    }

    private TrialyCallback mTrialyCallback = new TrialyCallback() {
        @Override
        public void onResult(int status, long timeRemaining, String sku) {
            switch (status){
                case STATUS_TRIAL_JUST_STARTED:
                    //The trial has just started - enable the premium features for the user

                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeRemaining),
                            TimeUnit.MILLISECONDS.toMinutes(timeRemaining) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeRemaining)),
                            TimeUnit.MILLISECONDS.toSeconds(timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining)));
                    String time;
                    time = String.valueOf(timeRemaining);
                    txtTrial = findViewById(R.id.txtTrial);


                    Toast.makeText(LoginForm.this,hms,Toast.LENGTH_LONG).show();
                    txtTrial.setVisibility(View.GONE);
                    loginUser = findViewById(R.id.buttonLogin);
                    loginUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Login();

                            //adminLogin();
                        }
                    });
                    break;
                case STATUS_TRIAL_RUNNING:
                    //The trial is currently running - enable the premium features for the user

                    String hms1 = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeRemaining),
                            TimeUnit.MILLISECONDS.toMinutes(timeRemaining) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeRemaining)),
                            TimeUnit.MILLISECONDS.toSeconds(timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining)));

                    txtTrial = findViewById(R.id.txtTrial);


                    Toast.makeText(LoginForm.this,hms1,Toast.LENGTH_LONG).show();
                    txtTrial.setVisibility(View.GONE);
                    loginUser = findViewById(R.id.buttonLogin);
                    loginUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Login();

                            //adminLogin();
                        }
                    });
                    break;
                case STATUS_TRIAL_JUST_ENDED:
                    //The trial has just ended - block access to the premium features

                    txtTrial = findViewById(R.id.txtTrial);
                    txtTrial.setVisibility(View.GONE);

                    Toast.makeText(LoginForm.this,"Trial Ended!", Toast.LENGTH_LONG).show();


                    break;
                case STATUS_TRIAL_NOT_YET_STARTED:
                    //The user hasn't requested a trial yet - no need to do anything

                    break;
                case STATUS_TRIAL_OVER:
                    //The trial is over

                    txtTrial = findViewById(R.id.txtTrial);
                    txtTrial.setVisibility(View.GONE);
                    Toast.makeText(LoginForm.this,"Trial Ended!", Toast.LENGTH_LONG).show();
                    break;
            }

        }

    };

}