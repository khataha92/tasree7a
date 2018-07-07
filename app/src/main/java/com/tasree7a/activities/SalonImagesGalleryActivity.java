package com.tasree7a.activities;

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
import com.tasree7a.interfaces.GalleryClickListener;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;

public class SalonImagesGalleryActivity extends AppCompatActivity implements GalleryClickListener {

    private boolean isSelecting = false;

    private SalonModel salon;
    private List<ImageModel> imageModelList;
    private List<String> mSelectedImagesList = new ArrayList<>();

    private RecyclerView gallery;
    private GalleryAdapter adapter;
    private TextView salonName;
    private ImageView changeItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_images_gallery);

        Intent intent = getIntent();
        if (intent != null) {
            imageModelList = (List<ImageModel>) intent.getSerializableExtra(FragmentArg.IMAGE_LIST);
            salon = (SalonModel) intent.getSerializableExtra(FragmentArg.SALON);
        }

        initViews();
        initChangeItemsView();
        addObservers();
        initImagesList();
    }

    @Override
    public void onImageItemClicked(boolean isSelection, int position) {
        if (!isSelection) {
            FragmentManager.showGalleryFullScreenFragment(imageModelList, position);
        } else {
            mSelectedImagesList.add(imageModelList.get(position).getImageId());
        }
    }

    private void initViews() {
        gallery = findViewById(R.id.gallery);
        gallery.setLayoutManager(new GridLayoutManager(this, 2));
        salonName = findViewById(R.id.salon_name);
        changeItems = findViewById(R.id.change_items);

        salonName.setText(ReservationSessionManager.getInstance().getSalonModel().getName());
        findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
    }

    private void initChangeItemsView() {
        //TODO: Minor Refactor
        if (!UserDefaultUtil.isBusinessUser()) {
            changeItems.setVisibility(View.GONE);
        } else {
            changeItems.setOnClickListener(v -> {
                if (isSelecting) {
                    deleteGalleryItem(mSelectedImagesList);
                } else {
                    addGalleryItem();
                }
            });
        }
    }

    private void addGalleryItem() {
        //        FragmentManager.showAddGalleryItemFragment(salon, (isSuccess, result) -> RetrofitManager.getInstance().getSalonDetails(UserDefaultUtil.getCurrentUser().getSalonId(), (isSuccess1, result1) -> {
//            List<ImageModel> imageModels = ((SalonModel) result1).getGallery();
//            adapter.setmImageModels(imageModels);
//            GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(imageModels);
//        }));
    }

    private void deleteGalleryItem(List<String> items) {
//        UpdateSalonImagesRequestModel model;
//        for (final String item : items) {
//            model = new UpdateSalonImagesRequestModel();
//            model.setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());
//            model.setOperation("DELETE");
//            model.setImageId(item);
//            TODO: check el mImage id and put it here
//            RetrofitManager.getInstance().updateSalonImages(model, (isSuccess, result) -> {
//                if (isSuccess) {
//                    for (ImageModel imageModel : imageModelList) {
//                        if (imageModel.getImageId().equalsIgnoreCase(item)) {
//                            imageModelList.remove(imageModel);
//                            adapter.setmImageModels(imageModelList);
//                            break;
//                        }
//                    }
//                }
//
//                GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(imageModelList);
//            });
//            model = null;
//        }
    }

    private void addObservers() {
//        ItemSelectedObservable.sharedInstance().addObserver(this);
//        GallaryItemsChangedObservable.sharedInstance().addObserver(this);
    }

    private void initImagesList() {
        adapter = new GalleryAdapter(imageModelList, this);
        gallery.setAdapter(adapter);
        gallery.addItemDecoration(new SpacesItemDecoration(UIUtils.dpToPx(7)));
    }
}
