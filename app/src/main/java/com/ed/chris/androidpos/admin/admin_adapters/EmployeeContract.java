package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class EmployeeContract {

    private EmployeeContract(){}


    public static final class EmployeeEntry implements BaseColumns {


        public static final String TABLE_NAME = "employee";
        public static final String KEY_FIRTSNAME = "FIRSTNAME";
        public static final String KEY_LASTNAME = "LASTNAME";
        public static final String KEY_MOBILE = "MOBILE";
        public static final String KEY_EMAIL = "EMAIL";
        public static final String KEY_EMPLOYEETYPE = "EMPLOYEETYPE";
        public static final String KEY_ADDRESS = "ADDRESS";
        public static final String KEY_EMPLOYEETYPE_ID = "EMPLOYEETYPEID";
    }
}
