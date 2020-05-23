package com.ed.chris.androidpos.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.ed.chris.androidpos.POS.PosMain;
import com.ed.chris.androidpos.admin.AdminMain;
import com.ed.chris.androidpos.database.DatabaseHelper;
import com.ed.chris.androidpos.login.LoginForm;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.ed.chris.androidpos.R;

import com.squareup.picasso.Picasso;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.model.HelpLiveo;
import br.liveo.model.Navigation;
import br.liveo.navigationliveo.NavigationLiveo;

public class User_Main extends NavigationLiveo implements OnItemClickListener {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;


    private PosMain posmain;


    private HelpLiveo mHelpLiveo;

    private SQLiteDatabase mDatabase;
    DatabaseHelper myDb;




    Class fragmentClass;
    public static Fragment fragment;

    @Override
    public void onInt(Bundle savedInstanceState) {


        myDb = new DatabaseHelper(User_Main.this);
        mDatabase = myDb.getWritableDatabase();



        //count employee

        String countEmployee = "SELECT count(*) FROM employee";
        Cursor cursorEmployee = mDatabase.rawQuery(countEmployee, null);
        cursorEmployee.moveToFirst();
        int icount = cursorEmployee.getInt(0);

        String a = String.valueOf(icount);

        //count products
        String countProducts = "SELECT count(*) FROM products";
        Cursor cursorProducts = mDatabase.rawQuery(countProducts, null);
        cursorProducts.moveToFirst();
        int countProd = cursorProducts.getInt(0);

        //count supplier
        String countSupplier = "SELECT count(*) FROM supplier";
        Cursor cursorSupplier = mDatabase.rawQuery(countSupplier, null);
        cursorSupplier.moveToFirst();
        int countSup = cursorSupplier.getInt(0);


        //get username

        /*this.userName.setText(firstNAME+ " " +lastNAME);
        this.userEmail.setText("rudsonlive@gmail.com");
        this.userPhoto.setImageResource(R.drawable.ic_rudsonlive); */

        Picasso.with(User_Main.this).load(R.drawable.test).fit().centerCrop().into(userPhoto);

        //this.userPhoto.setImageResource(R.drawable.applogo);
        User_Main.this.userBackground.setImageResource(R.drawable.bg);


        // Creating items navigation
        mHelpLiveo = new HelpLiveo();

        mHelpLiveo.add(("POS"), R.drawable.pos);
        mHelpLiveo.addSeparator(); // Item separator

        with(User_Main.this, Navigation.THEME_DARK);
        with(User_Main.this)
                .startingPosition(0)
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem("Log out", R.drawable.logout)
                .setOnClickFooter(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Main.this);
                        builder.setCancelable(true);
                        builder.setTitle("Logout");
                        builder.setMessage("You will be logged out. Continue?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(User_Main.this, LoginForm.class);
                                        startActivity(intent);
                                        finish();

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
                })
                .build();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(User_Main.this);
        builder.setCancelable(true);
        builder.setTitle("Logout");
        builder.setMessage("You will be logged out. Continue?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(User_Main.this, LoginForm.class);
                        startActivity(intent);
                        finish();

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

    @Override
    public void onItemClick(int position) {

        posmain = new PosMain();

        if (position == 0){


            setFragment(posmain);
            this.setTitle("Android POS");

        }


    }

}
