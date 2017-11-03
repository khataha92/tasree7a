package com.tasree7a.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.tasree7a.Adapters.BaseCardAdapter;
import com.tasree7a.Adapters.CardsRecyclerAdapter;
import com.tasree7a.CustomComponent.CustomSwitch;
import com.tasree7a.Enums.CardFactory;
import com.tasree7a.Enums.CardType;
import com.tasree7a.Enums.Language;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.ReservationSessionManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.Gallery.GalleryModel;
import com.tasree7a.Models.Gallery.ImageModel;
import com.tasree7a.Models.LocationCard.LocationCardModel;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.Observables.GallaryItemsChangedObservable;
import com.tasree7a.Observables.MenuIconClickedObservable;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.MainActivity;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by mohammad on 5/18/15.
 * This is the fragment that will show languages list and change language
 */
public class SalonDetailsFragment extends BaseFragment implements CardFactory, Observer {

    RecyclerView salonDetails;

    BaseCardAdapter adapter;

    SalonModel salonModel;

    DrawerLayout nvDrawer;

    NavigationView nvView;

    boolean didLoadFullSalon = false;

    boolean isBusiness = false;

    View navHeader;

    ImageView closeDrawer;

    CustomSwitch langSwitch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_salon_details, container, false);

        nvDrawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);

        nvView = (NavigationView) rootView.findViewById(R.id.nvView);

        navHeader = nvView.getHeaderView(0);

        closeDrawer = (ImageView) nvView.getHeaderView(0).findViewById(R.id.close_menu);

        adapter = new BaseCardAdapter(getCardModels());

        salonDetails = (RecyclerView) rootView.findViewById(R.id.salon_cards);

        salonDetails.setLayoutManager(new LinearLayoutManager(getContext()));

        salonDetails.setAdapter(adapter);

        if (!didLoadFullSalon) {

            showLoadingView();

        }

        ReservationSessionManager.getInstance().setSalonModel(salonModel);

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

        initProfileImage();

        initCloseButton();

        initLangButton();

        initNavigationView();
    }


    private void initProfileImage() {

        navHeader.findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.showProfileFragment();

            }
        });

    }


    private void initCloseButton() {

        closeDrawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                nvDrawer.closeDrawers();
            }
        });

    }


    private void initNavigationView() {

        int width = (int) (getResources().getDisplayMetrics().widthPixels / 1.5);

        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) nvView.getLayoutParams();

        params.width = width;

        nvView.setLayoutParams(params);

        nvView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

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

                                getActivity().finish();

                                break;

                            case R.id.settings:

                                FragmentManager.showSettingsFragment();

                                break;
                        }

                        nvDrawer.closeDrawers();

                        return true;
                    }
                });

    }


    private void initLangButton() {

        langSwitch = (CustomSwitch) navHeader.findViewById(R.id.switch_item);

        langSwitch.setChecked(UserDefaultUtil.isAppLanguageArabic());

        langSwitch.setAction(new Runnable() {

            @Override
            public void run() {

                UIUtils.showConfirmLanguageChangeDialog(langSwitch);

            }
        });

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

        fragmentIsVisible();
    }


    @Override
    public void onStart() {

        super.onStart();

        fragmentIsVisible();

    }


    @Override
    public void fragmentIsVisible() {

        super.fragmentIsVisible();

        getSalonDetails();

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

        cardModels.add(getCardModel(CardType.GALARY_CARD));

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


    public void setBusiness(boolean business) {

        isBusiness = business;
    }


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

            fragmentIsVisible();

        }
    }


    public boolean isSalonDataValid() {

        return UserDefaultUtil.getCurrentUser().isBusiness()
                && (Integer.parseInt(UserDefaultUtil.getCurrentUser().getId()) == -1);

    }


    public void getSalonDetails() {

        RetrofitManager.getInstance().getSalonDetails(salonModel.getId(), new AbstractCallback() {

            @Override
            public void onResult(boolean isSuccess, Object result) {

                if (!isAdded()) return;

                if (isSuccess) {

                    SalonModel temp = (SalonModel) result;

                    salonModel.setSalonBarbers(temp.getSalonBarbers());

                    salonModel.setLocationModel(temp.getLocationModel());

                    salonModel.setDistance(temp.getDistance());

                    salonModel.setLat(temp.getLat());

                    salonModel.setLng(temp.getLng());

                    salonModel.setOwnerMobileNumber(temp.getOwnerMobileNumber());

                    salonModel.setOwnerName(temp.getOwnerName());

                    salonModel.setSalonType(temp.getSalonType());

                    salonModel.setGallery(temp.getGallery());

                    salonModel.setProducts(temp.getProducts());

                    salonModel.setSalonCity(temp.getCity());

                    didLoadFullSalon = true;

                    ((BaseCardAdapter) salonDetails.getAdapter()).setCardModels(getCardModels());

                    salonDetails.getAdapter().notifyDataSetChanged();

                    UserDefaultUtil.setCurrentSalonUser(salonModel);

                }

                hideLoadingView();

            }

        });
    }
}
