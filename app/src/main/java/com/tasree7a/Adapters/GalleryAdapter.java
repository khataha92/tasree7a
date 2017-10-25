package com.tasree7a.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.ReservationSessionManager;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.Models.SalonDetails.SalonProduct;
import com.tasree7a.Observables.ItemSelectedObservable;
import com.tasree7a.Observables.ServicesTotalChangeObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.ViewHolders.GalleryItemViewHolder;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 6/14/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryItemViewHolder> {

    List<ImageModel> imageModels = new ArrayList<>();

    List<SalonProduct> productsList = new ArrayList<>();

    boolean isProduct;


    @Override
    public GalleryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.gallery_image, null);

//        ((TextView)itemView.findViewById(R.id.product_name)).setText();
        return new GalleryItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final GalleryItemViewHolder holder, final int position) {

        holder.init(imageModels.get(position), isProduct, isProduct ? productsList.get(position) : null);

        if (!UserDefaultUtil.isBusinessUser()) {
            
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    FragmentManager.showGalleryFullScreenFragment(imageModels, position);

                }
            });

        } else {

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (holder.itemView.findViewById(R.id.selected).getVisibility() == View.GONE) {

                        holder.itemView.findViewById(R.id.selected).setVisibility(View.VISIBLE);

                        holder.itemView.findViewById(R.id.image_container).setAlpha(0.5f);

                        ReservationSessionManager.getInstance().addSelectedItem(isProduct ? productsList.get(position).getId() : imageModels.get(position).getImageId());

                    } else {

                        holder.itemView.findViewById(R.id.selected).setVisibility(View.GONE);

                        holder.itemView.findViewById(R.id.image_container).setAlpha(1.0f);

                        ReservationSessionManager.getInstance().removeSelectedItem(isProduct ? productsList.get(position).getId() : imageModels.get(position).getImageId());

                    }

                    ItemSelectedObservable.sharedInstance().setItemSelected(ReservationSessionManager.getInstance().getSelectedItems().size() != 0);

                }
            });
        }

    }


    @Override
    public int getItemCount() {

        return imageModels == null ? 0 : imageModels.size();
    }


    public void setImageModels(List<ImageModel> imageModels) {

        this.imageModels.clear();

        this.imageModels.addAll(imageModels);

        this.notifyDataSetChanged();

    }


    public void setProductsList(List<SalonProduct> productsList) {

        this.productsList.clear();

        this.productsList.addAll(productsList);

        this.notifyDataSetChanged();
    }


    public boolean isProduct() {

        return isProduct;
    }


    public void setIsProduct(boolean product) {

        isProduct = product;
    }
}
