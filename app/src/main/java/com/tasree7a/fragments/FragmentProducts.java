package com.tasree7a.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.adapters.ProductsAdapter;
import com.tasree7a.customcomponent.SpacesItemDecoration;
import com.tasree7a.interfaces.ProductItemClickListener;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.models.gallery.ImageModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.models.salondetails.SalonProduct;
import com.tasree7a.observables.GallaryItemsChangedObservable;
import com.tasree7a.observables.ItemSelectedObservable;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class FragmentProducts extends BaseFragment implements Observer, ProductItemClickListener {

    private boolean mIsSelectionMode = false;

    private List<ImageModel> mImageModels;
    private SalonModel mSalonModel;
    private List<String> mSelectedProductsList = new ArrayList<>();
    private List<SalonProduct> mProductsList;

    private RecyclerView mRecyclerView;
    private ProductsAdapter mProductsAdapter;
    private TextView mTitle;
    private TextView mSalonName;
    private ImageView mChangeItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_salon_images_gallery, container, false);
        mRecyclerView = rootView.findViewById(R.id.gallery);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mTitle = rootView.findViewById(R.id.title);
        mSalonName = rootView.findViewById(R.id.salon_name);
        mSalonName.setText(ReservationSessionManager.getInstance().getSalonModel().getName());
        mChangeItems = rootView.findViewById(R.id.change_items);
        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        extractArgumentsData();
        initChangeItemsView();
        addObservers();
        initProductsList();
        return rootView;
    }

    @Override
    public void onProductClickedListener(boolean isSelection, int itemPosition) {
        if (!isSelection) {
            FragmentManager.showGalleryFullScreenFragment(mImageModels, itemPosition);
        } else {
            mSelectedProductsList.add(mProductsList.get(itemPosition).getId());
        }
    }

    private void extractArgumentsData() {
        Bundle args = getArguments();
        if (args != null) {
            if (args.getSerializable(FragmentArg.IMAGE_LIST) != null) {
                //noinspection unchecked
                mImageModels = (List<ImageModel>) getArguments().getSerializable(FragmentArg.IMAGE_LIST);
                //noinspection unchecked
                mProductsList = (List<SalonProduct>) getArguments().getSerializable(FragmentArg.PRODUCTS_LIST);
                mTitle.setText(getString(R.string.PRODUCTS));
            }
        }
    }

    private void initProductsList() {
        mProductsAdapter = new ProductsAdapter(mProductsList, this);
        mRecyclerView.setAdapter(mProductsAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(UIUtils.dpToPx(7)));
    }

    private void initChangeItemsView() {
        //TODO: Minor Refactor
        if (!UserDefaultUtil.isBusinessUser()) {
            mChangeItems.setVisibility(View.GONE);
        } else {
            mChangeItems.setOnClickListener(v -> {
                if (mIsSelectionMode) {
                    deleteProduct(mSelectedProductsList);
                } else {
                    addProduct();
                }
            });
        }
    }

    private void deleteProduct(List<String> items) {
//        UpdateProductRequestModel model;
//        for (final String item : items) {
//            model = new UpdateProductRequestModel();
//            model.setOperation("DELETE");
//            model.setProductId(item);
//            model.setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());
//            RetrofitManager.getInstance().updateSalonProducts(model, (isSuccess, result) -> {
//                if (isSuccess) {
//                    for (SalonProduct prod : mProductsList) {
//                        if (prod.getId().equalsIgnoreCase(item)) {
//                            mProductsList.remove(prod);
//                            mProductsAdapter.setProductsList(mProductsList);
//                            break;
//                        }
//                    }
//                }
//            });
//        }
    }

    private void addProduct() {
//        FragmentManager.showAddProductFragment((isSuccess, result) -> RetrofitManager.getInstance().getSalonDetails(UserDefaultUtil.getCurrentUser().getSalonId(), (isSuccess1, result1) -> {
//            List<SalonProduct> products = ((SalonModel) result1).getProducts();
//            mProductsAdapter.setmImageModels(((SalonModel) result1).getProductsImages());
//            mProductsAdapter.setProductsList(products);
//            GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(new ArrayList<>());
//        }));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeObservables();
    }

    @Override
    public boolean onBackPressed() {
        GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(new ArrayList<>());
        return super.onBackPressed();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ItemSelectedObservable) {
            if ((boolean) arg) {
                // TODO: Show delete icon in top bar
                mChangeItems.setImageResource(R.drawable.ic_remove);
                mIsSelectionMode = true;
            } else {
                mIsSelectionMode = false;
                //TODO: show add icon in header
                mChangeItems.setImageResource(R.drawable.ic_add_white_24dp);
            }
        } else if (o instanceof GallaryItemsChangedObservable) {
            mProductsAdapter.notifyDataSetChanged();
            UIUtils.hideSweetLoadingDialog();
        }
    }


    private void addObservers() {
        ItemSelectedObservable.sharedInstance().addObserver(this);
        GallaryItemsChangedObservable.sharedInstance().addObserver(this);
    }

    private void removeObservables() {
        ItemSelectedObservable.sharedInstance().deleteObserver(this);
        GallaryItemsChangedObservable.sharedInstance().deleteObserver(this);
    }

    public SalonModel getSalon() {
        return mSalonModel;
    }

    public void setSalon(SalonModel salon) {
        this.mSalonModel = salon;
    }

}
