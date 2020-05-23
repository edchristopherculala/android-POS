package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class EmployeeCreateAccountContract {

    private EmployeeCreateAccountContract(){}


    public static final class EmployeeCreateAccountEntry implements BaseColumns {


        public static final String TABLE_NAME = "employeeaccounts";
        public static final String KEY_FIRTSNAME = "FIRSTNAME";
        public static final String KEY_LASTNAME = "LASTNAME";
        public static final String KEY_USERNAME = "USERNAME";
        public static final String KEY_PASSWORD = "PASSWORD";
        public static final String KEY_ROLE = "ROLE";
        public static final String KEY_ID = "USERID";

    }
}
