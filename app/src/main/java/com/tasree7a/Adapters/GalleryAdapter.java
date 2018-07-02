package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.ImageGalleryClickListener;
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

    private List<ImageModel> mImageModels;
    private ImageGalleryClickListener mImageGalleryClickListener;

    public GalleryAdapter(List<ImageModel> imageModels, ImageGalleryClickListener imageGalleryClickListener) {
        mImageModels = imageModels;
        mImageGalleryClickListener = imageGalleryClickListener;
    }

    @NonNull
    @Override
    public GalleryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryItemViewHolder(LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.gallery_image, parent, false), mImageGalleryClickListener);
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
