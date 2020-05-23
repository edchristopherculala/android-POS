package com.ed.chris.androidpos.admin.admin_adapters;

import android.provider.BaseColumns;

public class ProductCategoryContract {

    private ProductCategoryContract(){}


    public static final class ProductCategoryEntry implements BaseColumns {


        public static final String TABLE_NAME = "productcategory";
        public static final String KEY_PRODUCT_CATEGORY = "CATEGORY";

    }
}
