package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.observables.ItemSelectedObservable;
import com.tasree7a.utils.UserDefaultUtil;
import com.tasree7a.viewholders.GalleryItemViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mac on 6/14/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryItemViewHolder> {

    private List<ImageModel> imageModels = new ArrayList<>();

    private List<SalonProduct> productsList = new ArrayList<>();

    private boolean isProduct;


    @NonNull
    @Override
    public GalleryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.gallery_image, null);

//        ((TextView)itemView.findViewById(R.id.product_name)).setText();
        return new GalleryItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final GalleryItemViewHolder holder, final int position) {

        try {

            holder.init(imageModels.get(position), isProduct, isProduct ? productsList.get(position) : null);

        } catch (IndexOutOfBoundsException e) {

            Log.d("crash", "crash: ", e);

        }
        if (!UserDefaultUtil.isBusinessUser()) {

            holder.itemView.setOnClickListener(v -> FragmentManager.showGalleryFullScreenFragment(imageModels, position));

        } else {

            holder.itemView.setOnClickListener(v -> {

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

            });
        }

    }


    @Override
    public int getItemCount() {

        return !isProduct ? (imageModels == null ? 0 : imageModels.size()) : (productsList == null ? 0 : productsList.size());
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
