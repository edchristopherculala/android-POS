package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class SalesContract {

    private SalesContract(){}


    public static final class SalesEntry implements BaseColumns {


        public static final String TABLE_NAME = "sales";
        public static final String KEY_RECEIPTNO = "RECEIPTNO";
        public static final String KEY_DATE = "DATE";
        public static final String KEY_SUBTOTAL = "SUBTOTAL";
        public static final String KEY_TOTALDISCOUNT = "TOTALDISCOUNT";
        public static final String KEY_TOTALPRICE = "TOTALPRICE";
        public static final String KEY_QRCODE = "QRCODE";


    }
}
