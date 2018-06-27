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
import com.tasree7a.adapters.GalleryAdapter;
import com.tasree7a.customcomponent.SpacesItemDecoration;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.UpdateProductRequestModel;
import com.tasree7a.models.UpdateSalonImagesRequestModel;
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


/**
 * Created by mac on 6/13/17.
 */

public class FragmentGallery extends BaseFragment implements Observer {

    RecyclerView gallery;

    GalleryAdapter adapter;

    TextView title;

    TextView salonName;

    ImageView changeItems;

    List<ImageModel> imageModelList;

    List<SalonProduct> productsList;

    boolean isProduct = false;

    boolean isSelecting = false;

    SalonModel salon;


    public SalonModel getSalon() {

        return salon;
    }


    public void setSalon(SalonModel salon) {

        this.salon = salon;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gallery, null);

        gallery = rootView.findViewById(R.id.gallery);

        gallery.setLayoutManager(new GridLayoutManager(getContext(), 2));

        title = rootView.findViewById(R.id.title);

        salonName = rootView.findViewById(R.id.salon_name);

        salonName.setText(ReservationSessionManager.getInstance().getSalonModel().getName());

        changeItems = rootView.findViewById(R.id.change_items);

        extractArgumentsData();

        initChangeItemsVeiw();

        addObservers();

        initAdapter();

        initGalleryRecycler();

        initBackButton();

        return rootView;
    }


    private void initBackButton() {

        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
    }


    private void initGalleryRecycler() {

        gallery.addItemDecoration(new SpacesItemDecoration(UIUtils.dpToPx(7)));

        gallery.setAdapter(adapter);
    }


    private void initAdapter() {

        adapter = new GalleryAdapter();

        adapter.setImageModels(imageModelList);

        if (isProduct) {

            adapter.setProductsList(productsList);

        }

        adapter.setIsProduct(isProduct);

    }


    private void extractArgumentsData() {

        Bundle args = getArguments();

        if (args != null) {

            if (args.getSerializable(FragmentArg.IMAGE_LIST) != null) {

                imageModelList = (List<ImageModel>) getArguments().getSerializable(FragmentArg.IMAGE_LIST);

                title.setText(getString(R.string.GALLERY));

            }
            if (args.getSerializable(FragmentArg.PRODUCTS_LIST) != null) {

                productsList = (List<SalonProduct>) getArguments().getSerializable(FragmentArg.PRODUCTS_LIST);

                title.setText(getString(R.string.PRODUCTS));

                isProduct = true;

            }

        }
    }


    private void initChangeItemsVeiw() {

        if (!UserDefaultUtil.isBusinessUser()) {

            changeItems.setVisibility(View.GONE);

        } else {

            changeItems.setOnClickListener(v -> {

                if (isSelecting) {

                    List<String> items = ReservationSessionManager.getInstance().getSelectedItems();

                    if (!isProduct) {
                        //TODO: Delete Gallary Item
                        deleteGalleryItem(items);

                    } else {

                        //TODO: Delete Product
                        deleteProduct(items);
                    }

                } else {

                    if (!isProduct) {
                        //TODO: ADD
                        addGallaryItem();

                    } else {

                        addProduct();
                    }

                }

            });
        }
    }


    private void deleteGalleryItem(List<String> items) {

        UpdateSalonImagesRequestModel model;

        for (final String item : items) {

            model = new UpdateSalonImagesRequestModel();
            model.setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());
            model.setOperation("DELETE");
            model.setImageId(item);

            //TODO: check el image id and put it here
            RetrofitManager.getInstance().updateSalonImages(model, (isSuccess, result) -> {

                if (isSuccess) {

                    for (ImageModel imageModel : imageModelList) {

                        if (imageModel.getImageId().equalsIgnoreCase(item)) {

                            imageModelList.remove(imageModel);

                            adapter.setImageModels(imageModelList);

                            break;

                        }

                    }
                }

                GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(imageModelList);

            });

            model = null;
        }
    }


    private void deleteProduct(List<String> items) {

        UpdateProductRequestModel model;

        for (final String item : items) {

            model = new UpdateProductRequestModel();

            model.setOperation("DELETE");
            model.setProductId(item);
            model.setSalonId(UserDefaultUtil.getCurrentUser().getSalonId());

            RetrofitManager.getInstance().updateSalonProducts(model, (isSuccess, result) -> {

                if (isSuccess) {

                    for (SalonProduct prod : productsList) {

                        if (prod.getId().equalsIgnoreCase(item)) {

                            productsList.remove(prod);

                            adapter.setProductsList(productsList);

                            break;

                        }
                    }
                }

            });

        }

    }


    private void addGallaryItem() {

        FragmentManager.showAddGalleryItemFragment(salon, (isSuccess, result) -> RetrofitManager.getInstance().getSalonDetails(UserDefaultUtil.getCurrentUser().getSalonId(), (isSuccess1, result1) -> {

            List<ImageModel> imageModels = ((SalonModel) result1).getGallery();

            adapter.setImageModels(imageModels);

            GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(imageModels);

        }));
    }


    private void addProduct() {

        FragmentManager.showAddProductFragment((isSuccess, result) -> RetrofitManager.getInstance().getSalonDetails(UserDefaultUtil.getCurrentUser().getSalonId(), (isSuccess1, result1) -> {

            List<SalonProduct> products = ((SalonModel) result1).getProducts();

            adapter.setImageModels(((SalonModel) result1).getProductsImages());

            adapter.setProductsList(products);

            GallaryItemsChangedObservable.sharedInstance().setGallaryChanged(new ArrayList<>());

        }));

    }


    private void addObservers() {

        ItemSelectedObservable.sharedInstance().addObserver(this);

        GallaryItemsChangedObservable.sharedInstance().addObserver(this);

    }


    private void removeObservables() {

        ItemSelectedObservable.sharedInstance().deleteObserver(this);

        GallaryItemsChangedObservable.sharedInstance().deleteObserver(this);

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

                changeItems.setImageResource(R.drawable.ic_remove);

                isSelecting = true;

            } else {

                isSelecting = false;
                //TODO: show add icon in header
                changeItems.setImageResource(R.drawable.ic_add_white_24dp);

            }
        } else if (o instanceof GallaryItemsChangedObservable) {

            adapter.notifyDataSetChanged();

            UIUtils.hideSweetLoadingDialog();

        }

    }
}
