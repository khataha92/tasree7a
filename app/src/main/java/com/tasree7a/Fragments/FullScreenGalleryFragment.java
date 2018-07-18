package com.tasree7a.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.adapters.GalleryPagerAdapter;
import com.tasree7a.models.gallery.ImageModel;

import java.util.List;

/**
 * Created by mac on 7/31/17.
 */

public class FullScreenGalleryFragment extends BaseFragment {

    ViewPager galleryPager;

    List<ImageModel> imageModelList;

    int position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fullscreen_gallery, container, false);

        galleryPager = rootView.findViewById(R.id.gallery_pager);

        GalleryPagerAdapter galleryPagerAdapter = new GalleryPagerAdapter(getContext(), imageModelList);

        galleryPager.setAdapter(galleryPagerAdapter);

        galleryPager.setCurrentItem(position);

        return rootView;
    }

    public void setImageModelList(List<ImageModel> imageModelList) {
        this.imageModelList = imageModelList;
    }

    public void setPosition(int position) {

        this.position = position;

    }
}
