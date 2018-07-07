package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.GalleryClickListener;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.viewholders.GalleryItemViewHolder;

import java.util.List;


/**
 * Created by mac on 6/14/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryItemViewHolder> {

    private List<ImageModel> mImageModels;
    private GalleryClickListener mGalleryClickListener;

    public GalleryAdapter(List<ImageModel> imageModels, GalleryClickListener galleryClickListener) {
        mImageModels = imageModels;
        mGalleryClickListener = galleryClickListener;
    }

    @NonNull
    @Override
    public GalleryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryItemViewHolder(LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.gallery_image, parent, false), mGalleryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryItemViewHolder holder, final int position) {
        holder.bind(mImageModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mImageModels == null ? 0 : mImageModels.size();
    }
}
