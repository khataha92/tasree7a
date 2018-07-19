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
import com.tasree7a.adapters.ProductsAdapter;
import com.tasree7a.interfaces.ProductItemClickListener;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.UpdateProductRequestModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;

public class SalonProductsGalleryActivity extends AppCompatActivity implements ProductItemClickListener {

    public static final String SALON_PRODUCTS = SalonProductsGalleryActivity.class.getName() + "SALON_PRODUCTS";
    public static final String SALON_ID = SalonProductsGalleryActivity.class.getName() + "SALON_ID";
    public static final String SALON_NAME = SalonProductsGalleryActivity.class.getName() + "SALON_NAME";

    private String mSalonId;
    private String mSalonName;

    private List<SalonProduct> mProductsList = new ArrayList<>();
    private List<String> mSelectedProductsList = new ArrayList<>();

    private ImageView mAddProduct;
    private ImageView mRemoveProducts;
    private ProductsAdapter mSalonProductsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_images_gallery);

        Intent intent = getIntent();
        if (intent != null) {
            //noinspection unchecked
            mProductsList = (List<SalonProduct>) intent.getSerializableExtra(SALON_PRODUCTS);
            mSalonId = intent.getStringExtra(SALON_ID);
            mSalonName = intent.getStringExtra(SALON_NAME);
        }

        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case AddSalonProductActivity.REQUEST_CODE:
                RetrofitManager
                        .getInstance()
                        .getSalonDetails(mSalonId, (isSuccess, result) -> {
                            if (isSuccess) {
                                mProductsList.clear();
                                mProductsList.addAll(((SalonModel) result).getProducts());
                                mSalonProductsAdapter.notifyDataSetChanged();
                            }
                        });
                break;
        }
    }

    @Override
    public void onProductClickedListener(boolean selected, int itemPosition) {
        if (!UserDefaultUtil.isBusinessUser()) {
//            FragmentManager.showGalleryFullScreenFragment(mProductsList.get(itemPosition), position);
        } else {
            if (selected) {
                mSelectedProductsList.add(mProductsList.get(itemPosition).getId());
            } else {
                mSelectedProductsList.remove(mProductsList.get(itemPosition).getId());
            }

            mRemoveProducts.setVisibility(mSelectedProductsList.isEmpty() ? View.GONE : View.VISIBLE);
            mAddProduct.setVisibility(mSelectedProductsList.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void initViews() {
        mAddProduct = findViewById(R.id.add_item);
        mRemoveProducts = findViewById(R.id.remove_item);
        RecyclerView mSalonProductsRecyclerView = findViewById(R.id.gallery);

        mAddProduct.setOnClickListener(v -> openAddSalonProductActivity());
        mRemoveProducts.setOnClickListener(v -> requestDeleteSelectedSalonProducts());

        mSalonProductsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mSalonProductsAdapter = new ProductsAdapter(mProductsList, this);
        mSalonProductsRecyclerView.setAdapter(mSalonProductsAdapter);

        ((TextView) findViewById(R.id.title)).setText(R.string.products_gallery_title);
        ((TextView) findViewById(R.id.salon_name)).setText(mSalonName);
        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void openAddSalonProductActivity() {
        startActivityForResult(new Intent(this, AddSalonProductActivity.class)
                .putExtra(AddSalonProductActivity.SALON_ID, mSalonId), AddSalonProductActivity.REQUEST_CODE);
    }

    private void requestDeleteSelectedSalonProducts() {
        RetrofitManager
                .getInstance()
                .updateSalonProducts(buildRequestDataModel(), null, (isSuccess, result) -> {

                });
    }

    private UpdateProductRequestModel buildRequestDataModel() {
        return new UpdateProductRequestModel();
    }
}
