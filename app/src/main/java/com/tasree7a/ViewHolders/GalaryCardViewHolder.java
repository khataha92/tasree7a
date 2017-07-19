package com.tasree7a.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasree7a.Enums.Sizes;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.Gallery.GalleryModel;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 6/5/17.
 */

public class GalaryCardViewHolder extends BaseCardViewHolder {

    ImageView seeAll;

    public GalaryCardViewHolder(View view, final BaseCardModel cardModel) {

        super(view, cardModel);

        seeAll = (ImageView) view.findViewById(R.id.see_all);

        TextView galleryTitle = (TextView) itemView.findViewById(R.id.gallery_title);

        LinearLayout imagesContainer = (LinearLayout) itemView.findViewById(R.id.images_container);

        int width = UIUtils.dpToPx(100);

        GalleryModel galleryModel = (GalleryModel) cardModel.getCardValue();

        galleryTitle.setText(galleryModel.getTitle());

        final List<ImageModel> imageModels =galleryModel.getImageModelList();

        for(int i = 0 ; i < imageModels.size() && i < 10; i++){

            ImageView imageView = new ImageView(ThisApplication.getCurrentActivity());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);

            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            UIUtils.loadUrlIntoImageView(imageModels.get(i).getImagePath(),imageView, Sizes.MEDIUM);

            params.setMargins(i == 0 ? 0 : UIUtils.dpToPx(10),0,0,0);

            imagesContainer.addView(imageView);

        }

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager.showFragmentGallery(new ArrayList<>(imageModels));

            }
        });

    }
}
