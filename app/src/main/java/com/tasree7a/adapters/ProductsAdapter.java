package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.interfaces.ProductItemClickListener;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.viewholders.ProductItemViewHolder;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductItemViewHolder> {

    private List<SalonProduct> mProductsList;

    private ProductItemClickListener mProductClickListener;

    public ProductsAdapter(List<SalonProduct> productsList, ProductItemClickListener productItemClickListener) {
        mProductsList = productsList;
        mProductClickListener = productItemClickListener;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product_item, parent, false), mProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, int position) {
        holder.bind(mProductsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mProductsList != null ? mProductsList.size() : 0;
    }
}
