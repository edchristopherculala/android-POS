package com.ed.chris.androidpos.admin;

import com.ed.chris.androidpos.admin.admin_adapters.EmployeeCreate;
import com.ed.chris.androidpos.login.LoginForm;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.ed.chris.androidpos.R;
import com.squareup.picasso.Picasso;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.model.HelpLiveo;
import br.liveo.model.Navigation;
import br.liveo.navigationliveo.NavigationLiveo;

public class UserMaintenance extends NavigationLiveo implements OnItemClickListener {
    Spinner spinnerEmployee;

    @Override
    public void onInt(Bundle savedInstanceState) {

        Picasso.with(this).load(R.drawable.test).fit().centerCrop().into(userPhoto);
        this.userBackground.setImageResource(R.drawable.bg);
        this.setTitle("");

        HelpLiveo mHelpLiveo = new HelpLiveo();

        mHelpLiveo.add(("Employee List"), R.drawable.elist);
        mHelpLiveo.add(("Add Employee"), R.drawable.empadd);
        mHelpLiveo.add(("Add Employee Types"), R.drawable.emptypes);
        mHelpLiveo.add(("View Employee Accounts"), R.drawable.viewempaccounts);

        with(this, Navigation.THEME_DARK);
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this) // default theme is dark
                .startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem("Log out", R.drawable.logout)
                .setOnClickFooter(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(UserMaintenance.this);
                        builder.setCancelable(true);
                        builder.setTitle("Logout");
                        builder.setMessage("You will be logged out. Continue?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(UserMaintenance.this, LoginForm.class);
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
        //setContentView(R.layout.activity_user_maintenance);

        // private BottomNavigationView mMainNav;
        FrameLayout mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        spinnerEmployee = findViewById(R.id.spinnerEmployee);


    }

    private void setFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserMaintenance.this, AdminMain.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onItemClick(int position) {

        ProductCategories productCategoriesFragment = new ProductCategories();
        ProductMaintenance productMaintenanceFragment = new ProductMaintenance();
        UserMaintenance userMaintenanceActivity = new UserMaintenance();
        SupplierMaintenance supplierMaintenanceFragment = new SupplierMaintenance();


        EmployeeType employeeTypeFragment = new EmployeeType();
        EmployeeMaintenance employeeMaintenanceFragment = new EmployeeMaintenance();

        if (position == 0) {

            setFragment(employeeMaintenanceFragment);
            this.setTitle("Employee List");

        } else if (position == 1) {

            Intent intent = new Intent(this, EmployeeCreate.class);
            startActivity(intent);
            this.setTitle("Add Employee");
            // return;


        } else if (position == 2) {

            setFragment(employeeTypeFragment);
            this.setTitle("Add Employee Type");


        } else if (position == 3) {

            Intent goIntent = new Intent(this, ViewEmployeeAccounts.class);
            startActivity(goIntent);

            this.setTitle("Employee Accounts");

        }


    }
}
