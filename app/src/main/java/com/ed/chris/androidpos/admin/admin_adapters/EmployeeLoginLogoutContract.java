package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class EmployeeLoginLogoutContract {

    private EmployeeLoginLogoutContract(){}


    public static final class EmployeeLoginLogoutEntry implements BaseColumns{

        public static final String TABLE_NAME = "employeeloginlogout";
        public static final String KEY_EMPLOYEEFIRSTNAME = "FIRSTNAME";
        public static final String KEY_EMPLOYEELASTNAME = "LASTNAME";
        public static final String KEY_EMPLOYEEROLE = "ROLE";
        public static final String KEY_EMPLOYEETIMEIN= "TIMEIN";
        public static final String KEY_EMPLOYEETIMEOUT = "TIMEOUT";

    }
}
