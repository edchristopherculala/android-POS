package com.ed.chris.androidpos.admin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ed.chris.androidpos.POS.PosMain;
import com.ed.chris.androidpos.R;
import com.ed.chris.androidpos.admin.updateclass.ProductMaintenanceAdd;
import com.ed.chris.androidpos.login.LoginForm;
import com.squareup.picasso.Picasso;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.model.HelpLiveo;
import br.liveo.model.Navigation;
import br.liveo.navigationliveo.NavigationLiveo;

public class ProductMain extends NavigationLiveo implements OnItemClickListener {

    @Override
    public void onInt(Bundle savedInstanceState) {

        //get username

        /*this.userName.setText("Rudson Lima");
        this.userEmail.setText("rudsonlive@gmail.com");
        this.userPhoto.setImageResource(R.drawable.ic_rudsonlive); */
        Picasso.with(this).load(R.drawable.test).fit().centerCrop().into(userPhoto);
        this.userBackground.setImageResource(R.drawable.bg);
        // Creating items navigation
        HelpLiveo mHelpLiveo = new HelpLiveo();

        mHelpLiveo.add(("Product Lists"), R.drawable.products);
        mHelpLiveo.add(("Create Product"), R.drawable.productadd);
        mHelpLiveo.add(("Create Product Category"), R.drawable.productcategories);
        mHelpLiveo.add(("POS"), R.drawable.pos);

        with(this, Navigation.THEME_DARK);
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this) // default theme is dark
                .startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem("Log out", R.drawable.logout)
                .setOnClickFooter(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductMain.this);
                        builder.setCancelable(true);
                        builder.setTitle("Logout");
                        builder.setMessage("You will be logged out. Continue?");
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(ProductMain.this, LoginForm.class);
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
        //setContentView(R.layout.activity_product_main);
    }

    @Override
    public void onItemClick(int position) {

        ProductMaintenance productMaintenanceFragment = new ProductMaintenance();
        ProductCategories productCategoriesFragment = new ProductCategories();

        PosMain posmain = new PosMain();

        if (position == 0) {


            setFragment(productMaintenanceFragment);
            this.setTitle("Add Products");

            //Toast.makeText(this, "EMPLOYEE", Toast.LENGTH_LONG).show();

            // setFragment(productCategoriesFragment);
            // return ;
        } else if (position == 1) {

            Intent intent = new Intent(this, ProductMaintenanceAdd.class);
            startActivity(intent);
            this.setTitle("Create Products");
            // return;


        } else if (position == 2) {

            setFragment(productCategoriesFragment);
            this.setTitle("Create Product Category");
            // return;


        } else if (position == 3) {

            Intent newIntent = new Intent(this, AdminMain.class);
            startActivity(newIntent);
        }


    }

    private void setFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
