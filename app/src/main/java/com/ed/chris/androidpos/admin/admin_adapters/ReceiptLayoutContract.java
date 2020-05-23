package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class ReceiptLayoutContract {

    private ReceiptLayoutContract(){}


    public static final class ReceiptLayoutEntry implements BaseColumns {


        public static final String TABLE_NAME = "receiptsettings";
        public static final String KEY_APP_NAME = "APPNAME";
        public static final String KEY_COMPANY_ADDRESS = "COMPANYADDRESS";
        public static final String KEY_COMPANY_TELEPHONE = "COMPANYTELEPHONE";
        public static final String KEY_FOOTER1 = "FOOTERFIRST";
        public static final String KEY_FOOTER2 = "FOOTERSECOND";

    }
}
