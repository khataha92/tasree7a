package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.Adapters.BaseCardAdapter;
import com.tasree7a.Adapters.CardsRecyclerAdapter;
import com.tasree7a.CustomComponent.SalonMapDetails;
import com.tasree7a.Enums.CardFactory;
import com.tasree7a.Enums.CardType;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.Gallery.GalleryModel;
import com.tasree7a.Models.LocationCard.LocationCardModel;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.R;
import com.tasree7a.interfaces.AbstractCallback;

import java.util.ArrayList;

/**
 * Created by mohammad on 5/18/15.
 * This is the fragment that will show languages list and change language
 */
public class SalonDetailsFragment extends BaseFragment implements CardFactory {

    RecyclerView salonDetails;

    BaseCardAdapter adapter;

    SalonModel salonModel;

    boolean didLoadFullHotel = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_salon_details, container, false);

        adapter = new BaseCardAdapter(getCardModels());

        salonDetails = (RecyclerView) rootView.findViewById(R.id.salon_cards);

        salonDetails.setLayoutManager(new LinearLayoutManager(getContext()));

        salonDetails.setAdapter(adapter);

        showLoadingView();

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void fragmentIsVisible() {
        super.fragmentIsVisible();

        RetrofitManager.getInstance().getSalonDetails(salonModel.getId(),new AbstractCallback(){

            @Override
            public void onResult(boolean isSuccess, Object result) {

                if(!isAdded()) return;

                if(isSuccess){

                    salonModel = (SalonModel) result;

                    didLoadFullHotel = true;

                    ((BaseCardAdapter)salonDetails.getAdapter()).setCardModels(getCardModels());

                    salonDetails.getAdapter().notifyDataSetChanged();

                }

                hideLoadingView();

            }

        });
    }

    @Override
    public int getFactoryId() {
        return 0;
    }

    private BaseCardModel getCardModel(CardType type){

        BaseCardModel cardModel = new BaseCardModel();

        cardModel.setCardType(type);

        switch (type){

            case IMAGE_CARD:

                cardModel.setCardValue(salonModel);

                break;

            case GALARY_CARD: {

                GalleryModel galleryModel = new GalleryModel();

                galleryModel.setTitle(getString(R.string.GALLERY));

                galleryModel.setImageModelList(salonModel.getGallery());

                cardModel.setCardValue(galleryModel);
            }
                break;

            case PRODUCTS_CARD: {

                GalleryModel galleryModel = new GalleryModel();

                galleryModel.setTitle(getString(R.string.PRODUCTS));

                galleryModel.setImageModelList(salonModel.getProducts());

                cardModel.setCardValue(galleryModel);

            }
                break;

            case MAP_CARD:

                LocationCardModel locationCardModel = new LocationCardModel();

                locationCardModel.setHasIndicator(true);

                locationCardModel.setAddress(salonModel.getSalonCity());

                locationCardModel.setSalonModel(salonModel);

                locationCardModel.setLatitude(salonModel.getLat());

                locationCardModel.setLongitude(salonModel.getLng());

                cardModel.setCardValue(locationCardModel);

                break;

            case CONTACT_DETAILS:

                cardModel.setCardValue(salonModel);

                break;
        }

        return cardModel;
    }

    @Override
    public ArrayList<BaseCardModel> getCardModels() {

        ArrayList<BaseCardModel> cardModels = new ArrayList<>();

        cardModels.add(getCardModel(CardType.IMAGE_CARD));

        if(!didLoadFullHotel){

            return cardModels;

        }

        if(salonModel.getGallery().size() > 0){

            cardModels.add(getCardModel(CardType.GALARY_CARD));

        }

        if(salonModel.getProducts().size() > 0){

            cardModels.add(getCardModel(CardType.PRODUCTS_CARD));

        }

        cardModels.add(getCardModel(CardType.CONTACT_DETAILS));

        cardModels.add(getCardModel(CardType.MAP_CARD));

//        cardModels.add(getCardModel(CardType.SALON_RATE));

        return cardModels;
    }

    @Override
    public RecyclerView getRecyclerView() {

        return salonDetails;

    }

    @Override
    public CardsRecyclerAdapter getCardsAdapter() {

        return null;

    }

    @Override
    public View getRootView() {

        return rootView;

    }

    public void setSalonModel(SalonModel salonModel) {
        this.salonModel = salonModel;
    }

    private void showLoadingView(){

        rootView.findViewById(R.id.loading).setVisibility(View.VISIBLE);
    }

    private void hideLoadingView(){

        rootView.findViewById(R.id.loading).setVisibility(View.GONE);
    }
}
