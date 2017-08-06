package com.tasree7a.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.Models.SalonDetails.SalonProduct;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.ViewHolders.GalleryItemViewHolder;

import java.util.List;

/**
 * Created by mac on 6/14/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryItemViewHolder> {

    List<ImageModel> imageModels ;

    List<SalonProduct> productsList;

    boolean isProduct;

    @Override
    public GalleryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.gallery_image,null);

//        ((TextView)itemView.findViewById(R.id.product_name)).setText();
        return new GalleryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryItemViewHolder holder, final int position) {

        holder.init(imageModels.get(position), isProduct, isProduct ? productsList.get(position) : null);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager.showGalleryFullScreenFragment(imageModels,position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return imageModels == null ? 0 : imageModels.size();
    }

    public void setImageModels(List<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }


    public void setProductsList(List<SalonProduct> productsList) {

        this.productsList = productsList;
    }


    public boolean isProduct() {

        return isProduct;
    }


    public void setIsProduct(boolean product) {

        isProduct = product;
    }
}
