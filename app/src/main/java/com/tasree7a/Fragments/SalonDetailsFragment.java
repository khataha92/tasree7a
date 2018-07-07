package com.tasree7a.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.MainActivity;
import com.tasree7a.adapters.BaseCardAdapter;
import com.tasree7a.adapters.CardsRecyclerAdapter;
import com.tasree7a.customcomponent.CustomSwitch;
import com.tasree7a.enums.CardFactory;
import com.tasree7a.enums.CardType;
import com.tasree7a.enums.Language;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.BaseCardModel;
import com.tasree7a.models.gallery.GalleryModel;
import com.tasree7a.models.locationcard.LocationCardModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.observables.GallaryItemsChangedObservable;
import com.tasree7a.observables.MenuIconClickedObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by mohammad on 5/18/15.
 * This is the fragment that will show languages list and change language
 */
public class SalonDetailsFragment extends BaseFragment implements CardFactory, Observer {

    private boolean didLoadFullSalon = false;
//    private boolean isBusiness = false;

    private SalonModel salonModel;

    private RecyclerView salonDetails;
    private DrawerLayout nvDrawer;
    private NavigationView nvView;
    private View navHeader;
    private ImageView closeDrawer;
    private CustomSwitch langSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_salon_details, container, false);

        nvDrawer = rootView.findViewById(R.id.drawer_layout);
        nvView = rootView.findViewById(R.id.nvView);
        navHeader = nvView.getHeaderView(0);
        closeDrawer = nvView.getHeaderView(0).findViewById(R.id.close_menu);
        salonDetails = rootView.findViewById(R.id.salon_cards);
        salonDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        salonDetails.setAdapter(new BaseCardAdapter(getCardModels()));

        //TODO: Don't do that, just show the loader before requesting data then when the result is recieved hide the loader
        if (!didLoadFullSalon) {
            showLoadingView();
        }

        //TODO: do this before opining the fragment
        if (isSalonDataValid()) {
            FragmentManager.showSalonInfoFragment(true);
        }
        addObservables();
        initSideMenuViews();
        return rootView;
    }


    private void addObservables() {
        MenuIconClickedObservable.sharedInstance().addObserver(this);
        GallaryItemsChangedObservable.sharedInstance().addObserver(this);
    }

    private void initSideMenuViews() {
        if (!salonModel.isBusiness()) {
            nvDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        navHeader.findViewById(R.id.profile_image).setOnClickListener(v -> FragmentManager.showProfileFragment());
        closeDrawer.setOnClickListener(v -> nvDrawer.closeDrawers());

        initLangButton();
        initNavigationView();
    }

    private void initNavigationView() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels / 1.5);
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) nvView.getLayoutParams();
        params.width = width;
        nvView.setLayoutParams(params);
        nvView.setNavigationItemSelectedListener(
                menuItem -> {
                    int itemId = menuItem.getItemId();
                    switch (itemId) {
                        case R.id.my_salon:
                            nvDrawer.closeDrawers();
                            break;
                        case R.id.bookings:
                            FragmentManager.showFragmentBookingList();
                            break;
                        case R.id.logout:
                            UserDefaultUtil.logout();
                            AccessToken.setCurrentAccessToken(null);
                            startActivity(new Intent(getContext(), MainActivity.class));
                            Objects.requireNonNull(getActivity()).finish();
                            break;
                        case R.id.settings:
                            FragmentManager.showSettingsFragment();
                            break;
                    }

                    nvDrawer.closeDrawers();
                    return true;
                });
    }

    private void initLangButton() {
        langSwitch = navHeader.findViewById(R.id.switch_item);
        langSwitch.setChecked(UserDefaultUtil.isAppLanguageArabic());
        langSwitch.setAction(() -> UIUtils.showConfirmLanguageChangeDialog(langSwitch));
        if (UserDefaultUtil.getAppLanguage() == Language.AR)
            ThisApplication.getCurrentActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            ThisApplication.getCurrentActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSalonDetails();
    }

    @Override
    public void fragmentIsVisible() {
        super.fragmentIsVisible();
    }

    @Override
    public int getFactoryId() {
        return 0;
    }

    private BaseCardModel getCardModel(CardType type) {
        BaseCardModel cardModel = new BaseCardModel();
        cardModel.setCardType(type);
        switch (type) {
            case IMAGE_CARD:
                cardModel.setCardValue(salonModel);
                break;
            case GALARY_CARD: {
                GalleryModel galleryModel = new GalleryModel();
                galleryModel.setTitle(getString(R.string.GALLERY));
                galleryModel.setImageModelList(salonModel.getGallery());
                galleryModel.setSalonModel(salonModel);
                galleryModel.setType(0);
                cardModel.setCardValue(galleryModel);
            }
            break;
            case PRODUCTS_CARD: {
                GalleryModel galleryModel = new GalleryModel();
                galleryModel.setTitle(getString(R.string.PRODUCTS));
                galleryModel.setImageModelList(salonModel.getProductsImages());
                galleryModel.setProducts(salonModel.getProducts());
                galleryModel.setSalonModel(salonModel);
                galleryModel.setType(1);
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
        if (!didLoadFullSalon) {
            return cardModels;
        }
        if (UserDefaultUtil.isBusinessUser()
                || salonModel.getGallery() != null && !salonModel.getGallery().isEmpty())
            cardModels.add(getCardModel(CardType.GALARY_CARD));
        if (UserDefaultUtil.isBusinessUser()
                || salonModel.getProducts() != null && !salonModel.getProducts().isEmpty())
            cardModels.add(getCardModel(CardType.PRODUCTS_CARD));
        cardModels.add(getCardModel(CardType.CONTACT_DETAILS));
        cardModels.add(getCardModel(CardType.MAP_CARD));
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

    private void showLoadingView() {
        rootView.findViewById(R.id.loading).setVisibility(View.VISIBLE);
    }

    private void hideLoadingView() {
        rootView.findViewById(R.id.loading).setVisibility(View.GONE);
    }

    public void setDidLoadFullSalon(boolean didLoadFullSalon) {
        this.didLoadFullSalon = didLoadFullSalon;
    }

//    public void setBusiness(boolean business) {
//        isBusiness = business;
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeObservers();
    }

    private void removeObservers() {
        MenuIconClickedObservable.sharedInstance().deleteObserver(this);
        GallaryItemsChangedObservable.sharedInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MenuIconClickedObservable) {
            nvDrawer.openDrawer(nvView);
        } else if (o instanceof GallaryItemsChangedObservable) {
            //TODO: No No No don't
            //TODO: return result from Activity -> SalonGalary/Products to activity
            getSalonDetails();
        }
    }

    public void getSalonDetails() {
        RetrofitManager.getInstance().getSalonDetails(salonModel.getId(), (isSuccess, result) -> {
            if (!isAdded()) return;
            if (isSuccess) {
                salonModel = (SalonModel) result;
//                salonModel.setSalonBarbers(temp.getSalonBarbers());
//                salonModel.setLocationModel(temp.getLocationModel());
//                salonModel.setDistance(temp.getDistance());
//                salonModel.setLat(temp.getLat());
//                salonModel.setLng(temp.getLng());
//                salonModel.setOwnerMobileNumber(temp.getOwnerMobileNumber());
//                salonModel.setOwnerName(temp.getOwnerName());
//                salonModel.setSalonType(temp.getSalonType());
//                salonModel.setGallery(temp.getGallery());
//                salonModel.setProducts(temp.getProducts());
//                salonModel.setSalonCity(temp.getCity());
//                salonModel.setAvailableTimes(temp.getAvailableTimes());
                didLoadFullSalon = true;
                ((BaseCardAdapter) salonDetails.getAdapter()).setCardModels(getCardModels());
                salonDetails.getAdapter().notifyDataSetChanged();
                UserDefaultUtil.setCurrentSalonUser(salonModel);
                ReservationSessionManager.getInstance().setSalonModel(salonModel);
            }
            hideLoadingView();
        });
    }

    public boolean isSalonDataValid() {
        return UserDefaultUtil.getCurrentUser().isBusiness()
                && (Integer.parseInt(UserDefaultUtil.getCurrentUser().getId()) == -1);
    }
}
