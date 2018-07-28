package com.tasree7a.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tasree7a.R;
import com.tasree7a.adapters.GalleryPagerAdapter;
import com.tasree7a.models.gallery.ImageModel;

import java.util.List;

public class FullScreenGalleryActivity extends AppCompatActivity {

    public static final String IMAGE_MODELS_LIST = FullScreenGalleryActivity.class.getName() + "IMAGE_MODELS_LIST";
    public static final String IMAGE_POSITION = FullScreenGalleryActivity.class.getName() + "IMAGE_POSITION";

    private List<ImageModel> mImageModelList;
    private int mPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fullscreen_gallery);

        Intent intent = getIntent();
        if (intent != null) {
            //noinspection unchecked
            mImageModelList = (List<ImageModel>) intent.getSerializableExtra(IMAGE_MODELS_LIST);
            mPosition = intent.getIntExtra(IMAGE_POSITION, 0);
        }

        initViews();
    }

    private void initViews() {
        ViewPager mGalleryPager = findViewById(R.id.gallery_pager);
        mGalleryPager.setAdapter(new GalleryPagerAdapter(this, mImageModelList));
        mGalleryPager.setCurrentItem(mPosition);
    }
}
