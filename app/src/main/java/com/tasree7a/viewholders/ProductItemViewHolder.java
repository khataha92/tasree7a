package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.enums.Sizes;
import com.tasree7a.interfaces.ProductItemClickListener;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.observables.ItemSelectedObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.Locale;

public class ProductItemViewHolder extends RecyclerView.ViewHolder {

    private boolean mIsSelected = false;

    private ImageModel mImageModel;
    private SalonProduct mSalonProduct;

    private TextView mName;
    private TextView mPrice;
    private ImageView mSelectionHover;
    private ImageView mImage;
    private RelativeLayout mImageContainer;

    public ProductItemViewHolder(View itemView, ProductItemClickListener productItemClickListener) {
        super(itemView);

        itemView.setOnClickListener(v -> {
            if (!UserDefaultUtil.isBusinessUser()) {
                productItemClickListener.onProductClickedListener(false, getAdapterPosition());
            } else {
                mIsSelected = !mIsSelected;
                mSelectionHover.setVisibility(mIsSelected ? View.VISIBLE : View.GONE);
                mImageContainer.setAlpha(mIsSelected ? 0.5f : 1.0f);
//                ItemSelectedObservable.sharedInstance().setItemSelected(ReservationSessionManager.getInstance().getSelectedItems().size() != 0);
            }
        });

        mImage = itemView.findViewById(R.id.image);
        mName = itemView.findViewById(R.id.product_name);
        mPrice = itemView.findViewById(R.id.product_price);
        mSelectionHover = itemView.findViewById(R.id.selected);
        mImageContainer = itemView.findViewById(R.id.image_container);
    }

    public void bind(ImageModel imageModel, SalonProduct product) {
        mImageModel = imageModel;
        mSalonProduct = product;

        if (mImage != null) {
            UIUtils.loadUrlIntoImageView(itemView.getContext(), mImageModel.getImagePath(), mImage, Sizes.MEDIUM);
        }

        itemView.findViewById(R.id.product_details).setVisibility(View.VISIBLE);
        mName.setText(mSalonProduct.getName());
        mPrice.setText(String.format(Locale.ENGLISH, "$%s", mSalonProduct.getPrice()));
    }
}
