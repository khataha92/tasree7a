package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tasree7a.R;
import com.tasree7a.enums.Sizes;
import com.tasree7a.interfaces.GalleryClickListener;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

public class GalleryItemViewHolder extends RecyclerView.ViewHolder {

    private boolean mIsSelected;

    private ImageView mImage;
    private ImageView mSelectionHover;
    private RelativeLayout mImageContainer;

    private GalleryClickListener mGalleryClickListener;

    public GalleryItemViewHolder(View itemView, GalleryClickListener galleryClickListener) {
        super(itemView);
        mGalleryClickListener = galleryClickListener;

        mImage = itemView.findViewById(R.id.image);
        mSelectionHover = itemView.findViewById(R.id.selected);
        mImageContainer = itemView.findViewById(R.id.image_container);

        itemView.setOnClickListener(v -> {
            if (UserDefaultUtil.isBusinessUser()) {
                mIsSelected = !mIsSelected;
                mSelectionHover.setVisibility(mIsSelected ? View.VISIBLE : View.GONE);
                mImageContainer.setAlpha(mIsSelected ? 0.5f : 1.0f);
            }

            mGalleryClickListener.onImageItemClicked(mIsSelected , getAdapterPosition());
        });
    }

    public void bind(ImageModel imageModel) {
        if (mImage != null) {
            UIUtils.loadUrlIntoImageView(itemView.getContext(), imageModel.getImagePath(), mImage, Sizes.MEDIUM);
        }
    }
}
