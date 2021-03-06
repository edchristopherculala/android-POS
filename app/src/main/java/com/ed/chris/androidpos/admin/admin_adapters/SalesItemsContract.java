package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class SalesItemsContract {

    private SalesItemsContract(){}


    public static final class SalesItemsEntry implements BaseColumns {


        public static final String TABLE_NAME = "salesitems";
        public static final String KEY_PRODUCTCODE = "PRODUCTCODE";
        public static final String KEY_PRODUCTNAME = "PRODUCTNAME";
        public static final String KEY_PRODUCTQUANTITY = "PRODUCTQUANTITY";
        public static final String KEY_PRODUCTSELLPRICE = "PRODUCTSELLPRICE";
        public static final String KEY_PRODUCTTOTALPRICE = "PRODUCTTOTALPRICE";
        public static final String KEY_PRODUCTDISCOUNT = "PRODUCTDISCOUNT";
        public static final String KEY_TOTALDISCOUNT = "TOTALDISCOUNT";
        public static final String KEY_RECEIPTNO = "RECEIPTNO";
        public static final String KEY_DATE = "DATE";

    }
}
