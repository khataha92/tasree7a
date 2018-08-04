package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.enums.Sizes;
import com.tasree7a.interfaces.ProductItemClickListener;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.Locale;

public class ProductItemViewHolder extends RecyclerView.ViewHolder {

    private boolean mIsSelected = false;

    private SalonProduct mSalonProduct;

    private TextView mName;
    private TextView mPrice;
    private ImageView mSelectionHover;
    private ImageView mImage;
    private RelativeLayout mImageContainer;

    public ProductItemViewHolder(View itemView, ProductItemClickListener productItemClickListener) {
        super(itemView);

        itemView.setOnClickListener(v -> {
            if (UserDefaultUtil.isBusinessUser()) {
                mIsSelected = !mIsSelected;
                mSelectionHover.setVisibility(mIsSelected ? View.VISIBLE : View.GONE);
                mImageContainer.setAlpha(mIsSelected ? 0.5f : 1.0f);
            }
            productItemClickListener.onProductClickedListener(mIsSelected, getAdapterPosition());
        });

        mImage = itemView.findViewById(R.id.image);
        mName = itemView.findViewById(R.id.product_name);
        mPrice = itemView.findViewById(R.id.product_price);
        mSelectionHover = itemView.findViewById(R.id.selected);
        mImageContainer = itemView.findViewById(R.id.image_container);
    }

    public void bind(SalonProduct product) {
        mSalonProduct = product;

        if (mSalonProduct != null) {
            UIUtils.loadUrlIntoImageView(itemView.getContext(), mSalonProduct.getUrl(), mImage, Sizes.MEDIUM);
        }

        itemView.findViewById(R.id.product_details).setVisibility(View.VISIBLE);
        mName.setText(mSalonProduct.getName());
        mPrice.setText(String.format(Locale.ENGLISH, "%s", mSalonProduct.getPrice()));
    }
}
