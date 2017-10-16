package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasree7a.Adapters.GalleryAdapter;
import com.tasree7a.CustomComponent.SpacesItemDecoration;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.ReservationSessionManager;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.Models.SalonDetails.SalonProduct;
import com.tasree7a.R;
import com.tasree7a.utils.FragmentArg;
import com.tasree7a.utils.UIUtils;

import java.util.List;

/**
 * Created by mac on 6/13/17.
 */

public class FragmentGallery extends BaseFragment {

    RecyclerView gallery ;

    TextView title;

    TextView salonName;

    List<ImageModel> imageModelList;

    List<SalonProduct> productsList;

    boolean isProduct = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gallery,null);

        gallery = (RecyclerView) rootView.findViewById(R.id.gallery);

        gallery.setLayoutManager(new GridLayoutManager(getContext(),2));

        title = (TextView) rootView.findViewById(R.id.title);

        salonName = (TextView) rootView.findViewById(R.id.salon_name);

        salonName.setText(ReservationSessionManager.getInstance().getSalonModel().getName());

        Bundle args = getArguments();

        if(args != null){

            if(args.getSerializable(FragmentArg.IMAGE_LIST) != null){

                imageModelList = (List<ImageModel>)getArguments().getSerializable(FragmentArg.IMAGE_LIST);

                title.setText(getString(R.string.GALLERY));

            }
            if(args.getSerializable(FragmentArg.PRODUCTS_LIST) != null){

                productsList = (List<SalonProduct>)getArguments().getSerializable(FragmentArg.PRODUCTS_LIST);

                title.setText(getString(R.string.PRODUCTS));

                isProduct = true;

            }

        }

        GalleryAdapter adapter = new GalleryAdapter();

        adapter.setImageModels(imageModelList);

        if (isProduct){

            adapter.setProductsList(productsList);

        }

        adapter.setIsProduct(isProduct);

        gallery.addItemDecoration(new SpacesItemDecoration(UIUtils.dpToPx(7)));

        gallery.setAdapter(adapter);

        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        return rootView;
    }
}
