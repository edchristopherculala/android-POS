package com.ed.chris.androidpos.admin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.ed.chris.androidpos.R;
import com.squareup.picasso.Picasso;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.model.HelpLiveo;
import br.liveo.model.Navigation;
import br.liveo.navigationliveo.NavigationLiveo;

public class ReportMaintenanceMain extends NavigationLiveo implements OnItemClickListener {

    private FrameLayout mMainFrame;


    @Override
    public void onInt(Bundle savedInstanceState) {

        Picasso.with(this).load(R.drawable.test).fit().centerCrop().into(userPhoto);
        this.userBackground.setImageResource(R.drawable.bg);
        this.setTitle("");
        // Creating items navigation

        HelpLiveo mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(("Per Transaction Report"), R.drawable.pertransaction);
        mHelpLiveo.add(("Sales Summary Report"), R.drawable.persales);
        with(this, Navigation.THEME_DARK);
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this) // default theme is dark
                .startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .footerItem("Log out", R.drawable.logout)
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
    public void onItemClick(int position) {


        ReportMaintenance perTransactionReportFragment = new ReportMaintenance();
        SaleSummaryReportMaintenance perSalesSummaryReportFragment = new SaleSummaryReportMaintenance();

        if (position == 0) {

            setFragment(perTransactionReportFragment);
            this.setTitle("Per Transaction Report");

        } else if (position == 1) {

            setFragment(perSalesSummaryReportFragment);
            this.setTitle("Sales Summary Report");

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ReportMaintenanceMain.this, AdminMain.class);
        startActivity(intent);

        finish();
    }
}
