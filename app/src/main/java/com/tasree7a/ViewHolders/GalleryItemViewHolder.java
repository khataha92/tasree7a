package com.tasree7a.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tasree7a.Enums.Sizes;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.UIUtils;

/**
 * Created by mac on 6/14/17.
 */

public class GalleryItemViewHolder extends RecyclerView.ViewHolder {

    ImageView image;

    public GalleryItemViewHolder(View itemView) {

        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.image);

    }

    public void init(ImageModel imageModel){

        if(image != null){

            UIUtils.loadUrlIntoImageView(imageModel.getImagePath(),image, Sizes.MEDIUM);

//            Picasso.with(ThisApplication.getCurrentActivity()).load(imageModel.getImagePath()).into(image);

        }
    }
}
