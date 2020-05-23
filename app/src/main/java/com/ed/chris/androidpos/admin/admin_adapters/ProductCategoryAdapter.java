package com.ed.chris.androidpos.admin.admin_adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.chris.androidpos.R;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryViewHolder> {


    private Context mContext;
    private Cursor mCursor;

    public  ProductCategoryAdapter (Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;

    }


    public class ProductCategoryViewHolder extends RecyclerView.ViewHolder {


        public TextView txtProductCategory;
        public CardView type_select_layout;

        public ProductCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProductCategory = itemView.findViewById(R.id.textview_product_category);
            type_select_layout = itemView.findViewById(R.id.select_layout_3);

        }
    }

    @NonNull
    @Override
    public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.product_category_item,parent,false);
        return new ProductCategoryAdapter.ProductCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryViewHolder holder, int position) {


        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String productCategory = mCursor.getString(mCursor.getColumnIndex(ProductCategoryContract.ProductCategoryEntry.KEY_PRODUCT_CATEGORY));
        long id = mCursor.getLong(mCursor.getColumnIndex(ProductCategoryContract.ProductCategoryEntry._ID));

        holder.txtProductCategory.setText(productCategory);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){

        if (mCursor != null){
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

    }
}
