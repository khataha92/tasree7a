package com.tasree7a.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.adapters.GalleryAdapter;
import com.tasree7a.customcomponent.SpacesItemDecoration;
import com.tasree7a.fragments.FullScreenGalleryActivity;
import com.tasree7a.interfaces.GalleryClickListener;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.UpdateSalonImagesRequestModel;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SalonImagesGalleryActivity extends AppCompatActivity implements GalleryClickListener {

    public static final String IMAGES_LIST = SalonImagesGalleryActivity.class.getName() + "IMAGES_LIST";
    public static final String SALON_ID = SalonImagesGalleryActivity.class.getName() + "SALON_ID";
    public static final String SALON_NAME = SalonImagesGalleryActivity.class.getName() + "SALON_NAME";

    private String mSalonId;
    private String mSalonName;

    private List<ImageModel> mImageModelsList;
    private List<String> mSelectedImagesList = new ArrayList<>();

    private GalleryAdapter mImagesListAdapter;

    private TextView mSalonNameView;
    private ImageView mAddImage;
    private ImageView mRemoveImages;
    private RecyclerView mImagesListRecyclerView;
    private View mLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_images_gallery);

        Intent intent = getIntent();
        if (intent != null) {
            //noinspection unchecked
            mImageModelsList = (List<ImageModel>) intent.getSerializableExtra(IMAGES_LIST);
            mSalonId = intent.getStringExtra(SALON_ID);
            mSalonName = intent.getStringExtra(SALON_NAME);
        }

        initViews();
        initChangeItemsView();
        initImagesList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data != null) return;

        switch (requestCode) {
            case AddSalonImageActivity.REQUEST_CODE:
                //TODO Should have an API only for getting images;
                RetrofitManager.getInstance().getSalonDetails(mSalonId, UserDefaultUtil.getCurrentUser().getId(), (isSuccess, result) -> {
                    if (isSuccess) {
                        mImageModelsList.clear();
                        mImageModelsList.addAll(((SalonModel) result).getGallery());
                        mImagesListAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }

    @Override
    public void onImageItemClicked(boolean selected, int position) {
        if (!UserDefaultUtil.isBusinessUser()) {
            startActivity(new Intent(this, FullScreenGalleryActivity.class)
                    .putExtra(FullScreenGalleryActivity.IMAGE_MODELS_LIST, (Serializable) mImageModelsList)
                    .putExtra(FullScreenGalleryActivity.IMAGE_POSITION, position));
        } else {
            if (selected) {
                mSelectedImagesList.add(mImageModelsList.get(position).getImageId());
            } else {
                mSelectedImagesList.remove(mImageModelsList.get(position).getImageId());
            }

            mRemoveImages.setVisibility(mSelectedImagesList.isEmpty() ? View.GONE : View.VISIBLE);
            mAddImage.setVisibility(mSelectedImagesList.isEmpty() ? View.VISIBLE : View.GONE
            );
        }
    }

    private void initViews() {
        mLoadingView = findViewById(R.id.loading);

        mImagesListRecyclerView = findViewById(R.id.gallery);
        mImagesListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mSalonNameView = findViewById(R.id.salon_name);
        mAddImage = findViewById(R.id.add_item);
        mRemoveImages = findViewById(R.id.remove_item);

        mSalonNameView.setText(ReservationSessionManager.getInstance().getSalonModel().getName());
        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void initChangeItemsView() {
        if (!UserDefaultUtil.isBusinessUser()) {
            mAddImage.setVisibility(View.GONE);
            mRemoveImages.setVisibility(View.GONE);
        } else {
            mAddImage.setOnClickListener(v -> addGalleryItem());
            mRemoveImages.setOnClickListener(v -> deleteGalleryItem(mSelectedImagesList));
        }
    }

    private void addGalleryItem() {
        startActivityForResult(new Intent(this, AddSalonImageActivity.class)
                .putExtra(AddSalonImageActivity.SALON_ID, mSalonId), AddSalonImageActivity.REQUEST_CODE);
    }

    private void deleteGalleryItem(List<String> items) {
        UpdateSalonImagesRequestModel model;
        for (final String item : items) {
            model = new UpdateSalonImagesRequestModel();
            model.setSalonId(mSalonId);
            model.setOperation("DELETE");
            model.setImageId(items);
            RetrofitManager.getInstance().updateSalonImages(UserDefaultUtil.getCurrentUser().getId(),
                    model, null, (isSuccess, result) -> {
                        if (isSuccess) {
                            for (ImageModel imageModel : mImageModelsList) {
                                if (imageModel.getImageId().equalsIgnoreCase(item)) {
                                    mImageModelsList.remove(imageModel);
                                    mImagesListAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    });
        }
    }

    private void initImagesList() {
        mImagesListAdapter = new GalleryAdapter(mImageModelsList, this);
        mImagesListRecyclerView.setAdapter(mImagesListAdapter);
        mImagesListRecyclerView.addItemDecoration(new SpacesItemDecoration(UIUtils.dpToPx(7)));
    }
}
