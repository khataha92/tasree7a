package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.enums.Sizes;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.utils.UIUtils;

import java.util.List;

/**
 * Created by mac on 7/31/17.
 */

public class GalleryPagerAdapter extends PagerAdapter {

    List<ImageModel> images;

    public GalleryPagerAdapter(List<ImageModel> images){

        this.images = images;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View v = ThisApplication.getCurrentActivity().getLayoutInflater().inflate(R.layout.pager_image_card, null);

        ImageView imageView = (ImageView) v.findViewById(R.id.image);

        UIUtils.loadUrlIntoImageView(images.get(position).getImagePath(),imageView, Sizes.LARGE);

        container.addView(v);

        return v;

    }

    @Override
    public int getCount() {

        return images == null ? 0 : images.size();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);

        container.removeView((View)object);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
