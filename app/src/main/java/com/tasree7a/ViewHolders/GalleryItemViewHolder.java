package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.enums.Sizes;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.utils.UIUtils;

/**
 * Created by mac on 6/14/17.
 * <p>
 * gallery
 */

public class GalleryItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;

    public TextView name;

    public TextView price;

    public ImageView selectenImage;

    public RelativeLayout imageContainer;


    public GalleryItemViewHolder(View itemView) {

        super(itemView);

        image = itemView.findViewById(R.id.image);

        name = itemView.findViewById(R.id.product_name);

        price = itemView.findViewById(R.id.product_price);

        selectenImage = itemView.findViewById(R.id.selected);

        imageContainer = itemView.findViewById(R.id.image_container);

    }


    public void init(ImageModel imageModel, boolean isProduct, SalonProduct product) {

        if (image != null) {

            UIUtils.loadUrlIntoImageView(imageModel.getImagePath(), image, Sizes.MEDIUM);

        }

        if (isProduct) {

            itemView.findViewById(R.id.product_details).setVisibility(View.VISIBLE);

            name.setText(product.getName());

            price.setText("$" + product.getPrice());

        }
    }
}
