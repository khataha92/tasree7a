package com.tasree7a.viewholders;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.enums.Sizes;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.models.BaseCardModel;
import com.tasree7a.models.gallery.GalleryModel;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 6/5/17.
 */

public class GalaryCardViewHolder extends BaseCardViewHolder {

    private ImageView seeAll;

    List<SalonProduct> salonProducts = null;

    private GalleryModel galleryModel;


    public GalaryCardViewHolder(View view, final BaseCardModel cardModel) {

        super(view, cardModel);

        seeAll = view.findViewById(R.id.see_all);

        TextView galleryTitle = itemView.findViewById(R.id.gallery_title);

        LinearLayout imagesContainer = itemView.findViewById(R.id.images_container);

        HorizontalScrollView scroll = itemView.findViewById(R.id.horizental_scroll);

        int width = UIUtils.dpToPx(100);

        galleryModel = (GalleryModel) cardModel.getCardValue();

        galleryTitle.setText(galleryModel.getTitle());

        final SalonModel salon = galleryModel.getSalonModel();

        final List<ImageModel> imageModels = galleryModel.getImageModelList();

        for (int i = 0; i < imageModels.size() && i < 10; i++) {

            ImageView imageView = new ImageView(ThisApplication.getCurrentActivity());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);

            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            UIUtils.loadUrlIntoImageView(itemView.getContext(), imageModels.get(i).getImagePath(), imageView, Sizes.MEDIUM);

            params.setMargins(i == 0 ? 0 : UIUtils.dpToPx(10), 0, 0, 0);

            imagesContainer.addView(imageView);

        }

        final List<SalonProduct> salonProducts = galleryModel.getProducts();

        if (salon.isBusiness()) {

            seeAll.setImageResource(R.drawable.ic_edit);

        }

        if ((galleryModel.getImageModelList() == null || galleryModel.getImageModelList().size() == 0) && (galleryModel.getProducts() == null || galleryModel.getProducts().size() == 0)) {

            scroll.setVisibility(View.GONE);

        }

        seeAll.setOnClickListener(v -> {

            if (!salon.isBusiness()) {

                FragmentManager.showFragmentGallery(salon, new ArrayList<>(imageModels), salonProducts == null ? null : new ArrayList<>(salonProducts));

            } else {

                //show add/delete fragment

                if (galleryModel.getType() == 1)

                    if (salonProducts == null || salonProducts.size() == 0)
                        FragmentManager.showAddProductFragment((isSuccess, result) -> {

                        });
                    else
                        FragmentManager.showFragmentGallery(salon, new ArrayList<>(imageModels), salonProducts == null ? null : new ArrayList<>(salonProducts));

                else if (galleryModel.getType() == 0)

                    if (galleryModel.getImageModelList() == null || galleryModel.getImageModelList().size() == 0)
                        FragmentManager.showAddGalleryItemFragment(salon, (isSuccess, result) -> {

                        });
                    else
                        FragmentManager.showFragmentGallery(salon, new ArrayList<>(imageModels), salonProducts == null ? null : new ArrayList<>(salonProducts));

            }
        });

    }
}
