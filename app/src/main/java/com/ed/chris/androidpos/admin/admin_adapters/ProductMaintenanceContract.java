package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class ProductMaintenanceContract {

    private ProductMaintenanceContract(){}


    public static final class ProductMaintenanceEntry implements BaseColumns {


        public static final String TABLE_NAME = "products";
        public static final String KEY_PRODUCT_CODE = "PRODUCTCODE";
        public static final String KEY_PRODUCT_CATEGORY  = "PRODUCTCATEGORY";
        public static final String KEY_PRODUCT_DESCRIPTION  = "PRODUCTDESCRIPTION";
        public static final String KEY_PRODUCT_SUPPLIER_NAME  = "PRODUCTSUPPLIERNAME";
        public static final String KEY_PRODUCT_DATEOFVALIDITY  = "PRODUCTDATEOFVALIDITY";
        public static final String KEY_PRODUCT_DATEOFEXPIRY  = "PRODUCTDATEOFEXPIRY";
        public static final String KEY_PRODUCT_BUY_PRICE  = "PRODUCTBUYPRICE";
        public static final String KEY_PRODUCT_SELL_PRICE  = "PRODUCTSELLPRICE";
        public static final String KEY_PRODUCT_SUPPLIER_ID  = "PRODUCTSUPPLIERID";
        public static final String KEY_PRODUCT_CATEGORY_ID  = "PRODUCTCATEGORYID";
        public static final String KEY_PRODUCT_QUANTITY  = "PRODUCTQUANTITY";
        public static final String KEY_PRODUCT_IMAGE  = "PRODUCTIMAGE";





    }
}
