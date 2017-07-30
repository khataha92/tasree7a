package com.tasree7a.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.ViewHolders.GalleryItemViewHolder;

import java.util.List;

/**
 * Created by mac on 6/14/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryItemViewHolder> {

    List<ImageModel> imageModels ;

    @Override
    public GalleryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.gallery_image,null);

//        ((TextView)itemView.findViewById(R.id.product_name)).setText();
        return new GalleryItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryItemViewHolder holder, int position) {

        holder.init(imageModels.get(position));

    }

    @Override
    public int getItemCount() {
        return imageModels == null ? 0 : imageModels.size();
    }

    public void setImageModels(List<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }
}
