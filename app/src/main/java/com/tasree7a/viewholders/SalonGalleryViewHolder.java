package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tasree7a.R;
import com.tasree7a.enums.Sizes;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.utils.UIUtils;

public class SalonGalleryViewHolder extends RecyclerView.ViewHolder {
    public SalonGalleryViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ImageModel imageModel) {
        ImageView imageView = itemView.findViewById(R.id.salon_gallery_item);
        UIUtils.loadUrlIntoImageView(itemView.getContext(), imageModel.getImagePath(), imageView, Sizes.MEDIUM);
    }
}
