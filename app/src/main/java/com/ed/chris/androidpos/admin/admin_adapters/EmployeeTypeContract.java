package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class EmployeeTypeContract {

    private EmployeeTypeContract(){}

   public static final class EmployeeTypeEntry implements BaseColumns{


       public static final String TABLE_NAME = "employeetype";
       public static final String KEY_EMPLOYEETYPE = "EMPLOYEETYPE";
       public static final String KEY_DESCRIPTION = "DESCRIPTION";
   }
}
