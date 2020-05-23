package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class StocksContract {

    private StocksContract(){}


    public static final class StocksEntry implements BaseColumns {


        public static final String TABLE_NAME = "stocks";
        public static final String KEY_PRODUCTNAME = "PRODUCTNAME";
        public static final String KEY_PRODUCTCATEGORY  = "PRODUCTCATEGORY";
        public static final String KEY_PRODUCTSUPPLIERNAME  = "PRODUCTSUPPLIERNAME";
        public static final String KEY_PRODUCTQUANTITY  = "PRODUCTQUANTITY";
        public static final String KEY_PRODUCTID  = "PRODUCTID";

    }
}
