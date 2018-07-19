package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.viewholders.SalonGalleryViewHolder;

import java.util.List;

public class SalonGalleryAdapter extends RecyclerView.Adapter<SalonGalleryViewHolder> {

    private List<ImageModel> mImagesList;

    public SalonGalleryAdapter(List<ImageModel> imageModelList) {
        mImagesList = imageModelList;
    }

    @NonNull
    @Override
    public SalonGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalonGalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_salon_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalonGalleryViewHolder holder, int position) {
        holder.bind(mImagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mImagesList != null && !mImagesList.isEmpty() ? mImagesList.size() : 0;
    }
}
