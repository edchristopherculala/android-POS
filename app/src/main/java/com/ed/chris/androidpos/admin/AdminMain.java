package com.ed.chris.androidpos.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.ed.chris.androidpos.POS.PosMain;
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
import android.widget.Toast;

import com.ed.chris.androidpos.R;

import com.squareup.picasso.Picasso;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.model.HelpLiveo;
import br.liveo.model.Navigation;
import br.liveo.navigationliveo.NavigationLiveo;

public class AdminMain extends NavigationLiveo implements OnItemClickListener {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private ProductCategories productCategoriesFragment;
    private ProductMaintenance productMaintenanceFragment;
    private UserMaintenance userMaintenanceActivity;
    private ReportMaintenance perTransactionReportFragment;
    private SaleSummaryReportMaintenance perSalesSummaryReportFragment;
    private PosMain posmain;
    private SupplierMaintenance supplierMaintenanceFragment;
    private ReportMaintenance reportMaintenance;
    private SettingsFragment settingsFragment;
    private BackupAndRestoreFragment backupAndRestoreFragment;
    private HelpLiveo mHelpLiveo;
    private SQLiteDatabase mDatabase;

    DatabaseHelper myDb;

    Class fragmentClass;
    public static Fragment fragment;

    @Override
    public void onInt(Bundle savedInstanceState) {


        myDb = new DatabaseHelper(AdminMain.this);
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

        /*this.userName.setText(firstNAME+ " " +lastNAME);
        this.userEmail.setText("rudsonlive@gmail.com");
        this.userPhoto.setImageResource(R.drawable.ic_rudsonlive); */
        Picasso.with(AdminMain.this).load(R.drawable.test).into(userPhoto);
        //this.userPhoto.setImageResource(R.drawable.applogo);
        AdminMain.this.userBackground.setImageResource(R.drawable.bg);
        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(("Employee"), R.drawable.usermaintenance, icount);
        mHelpLiveo.add(("Products"), R.drawable.productmaintenance1, countProd);
        mHelpLiveo.add(("Supplier"), R.drawable.supplier, countSup);
        mHelpLiveo.add(("Per Transaction Report"), R.drawable.pertransaction);
        mHelpLiveo.add(("Sales Report"), R.drawable.persales);
        mHelpLiveo.add(("POS"), R.drawable.pos);
        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(("Settings"), R.drawable.settings);
        mHelpLiveo.add(("Backup and Restore"), R.drawable.backuprestore);
        with(AdminMain.this, Navigation.THEME_DARK);
        with(AdminMain.this)
                .startingPosition(5)
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem("Log out", R.drawable.logout)
                .setOnClickFooter(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMain.this);
                        builder.setCancelable(true);
                        builder.setTitle("Logout");
                        builder.setMessage("You will be logged out. Continue?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(AdminMain.this, LoginForm.class);
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


        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMain.this);
        builder.setCancelable(true);
        builder.setTitle("Logout");
        builder.setMessage("You will be logged out. Continue?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(AdminMain.this, LoginForm.class);
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

        productCategoriesFragment = new ProductCategories();
        productMaintenanceFragment = new ProductMaintenance();
        userMaintenanceActivity = new UserMaintenance();
        supplierMaintenanceFragment = new SupplierMaintenance();
        reportMaintenance = new ReportMaintenance();
        settingsFragment = new SettingsFragment();
        backupAndRestoreFragment = new BackupAndRestoreFragment();


        perTransactionReportFragment = new ReportMaintenance();
        perSalesSummaryReportFragment = new SaleSummaryReportMaintenance();

        posmain = new PosMain();

        if (position == 0) {

            Intent intent = new Intent(AdminMain.this, UserMaintenance.class);
            startActivity(intent);

        } else if (position == 1) {

            Intent intent = new Intent(AdminMain.this, ProductMain.class);
            startActivity(intent);

        } else if (position == 2) {

            Intent intent = new Intent(AdminMain.this, SupplierMain.class);
            startActivity(intent);
            this.setTitle("List of Suppliers");


        } else if (position == 3) {

            setFragment(perTransactionReportFragment);
            this.setTitle("Per Transaction Report");


        } else if (position == 4) {
            setFragment(perSalesSummaryReportFragment);
            this.setTitle("Sales Report");


        } else if (position == 5) {

            setFragment(posmain);
            this.setTitle("Android POS");


        } else if (position == 7) {

            setFragment(settingsFragment);
            this.setTitle("Settings");
            Toast.makeText(AdminMain.this, "Settings", Toast.LENGTH_LONG).show();

        } else if (position == 8) {

            setFragment(backupAndRestoreFragment);
            this.setTitle("Backup and Restore");

            Toast.makeText(AdminMain.this, "Backup and Restore", Toast.LENGTH_LONG).show();

        }


    }

}
