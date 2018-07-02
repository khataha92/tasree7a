package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.enums.Sizes;
import com.tasree7a.interfaces.ImageGalleryClickListener;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.observables.ItemSelectedObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

/**
 * Created by mac on 6/14/17.
 * <p>
 * gallery
 */

public class GalleryItemViewHolder extends RecyclerView.ViewHolder {

    private boolean mIsSelected;

    public ImageView mImage;
    public ImageView mSelectionHover;
    public RelativeLayout mImageContainer;

    private ImageGalleryClickListener mImageGalleryClickListener;

    public GalleryItemViewHolder(View itemView, ImageGalleryClickListener imageGalleryClickListener) {
        super(itemView);
        mImageGalleryClickListener = imageGalleryClickListener;

        mImage = itemView.findViewById(R.id.image);
        mSelectionHover = itemView.findViewById(R.id.selected);
        mImageContainer = itemView.findViewById(R.id.image_container);

        itemView.setOnClickListener(v -> {
            if (!UserDefaultUtil.isBusinessUser()) {
                mImageGalleryClickListener.onImageItemClicked(false, getAdapterPosition());
            } else {
                mIsSelected = !mIsSelected;
                mSelectionHover.setVisibility(mIsSelected ? View.VISIBLE : View.GONE);
                mImageContainer.setAlpha(mIsSelected ? 0.5f : 1.0f);
//                ItemSelectedObservable.sharedInstance().setItemSelected(ReservationSessionManager.getInstance().getSelectedItems().size() != 0);
            }
        });
    }

    public void bind(ImageModel imageModel) {
        if (mImage != null) {
            UIUtils.loadUrlIntoImageView(imageModel.getImagePath(), mImage, Sizes.MEDIUM);
        }
    }
}
