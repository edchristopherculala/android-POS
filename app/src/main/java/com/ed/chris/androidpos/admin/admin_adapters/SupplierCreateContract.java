package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class SupplierCreateContract {

    private SupplierCreateContract(){}


    public static final class SupplierAccountEntry implements BaseColumns {


        public static final String TABLE_NAME = "supplier";
        public static final String KEY_SUPPLIER_CODE = "SUPPLIERCODE";
        public static final String KEY_SUPPLIER_NAME  = "SUPPLIERNAME";
        public static final String KEY_SUPPLIER_ADDRESS  = "SUPPLIERADDRESS";
        public static final String KEY_SUPPLIER_EMAIL  = "SUPPLIEREMAIL";
        public static final String KEY_SUPPLIER_TELEPHONE  = "SUPPLIERTELEPHONE";

    }
}
