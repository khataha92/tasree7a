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
import android.widget.TextView;

import com.facebook.AccessToken;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.activities.MainActivity;
import com.tasree7a.activities.SalonServicesActivity;
import com.tasree7a.adapters.BaseCardAdapter;
import com.tasree7a.adapters.CardsRecyclerAdapter;
import com.tasree7a.customcomponent.CustomRatingBar;
import com.tasree7a.customcomponent.CustomSwitch;
import com.tasree7a.enums.CardFactory;
import com.tasree7a.enums.CardType;
import com.tasree7a.enums.Language;
import com.tasree7a.enums.Sizes;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.BaseCardModel;
import com.tasree7a.models.gallery.GalleryModel;
import com.tasree7a.models.locationcard.LocationCardModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.observables.FavoriteChangeObservable;
import com.tasree7a.observables.GallaryItemsChangedObservable;
import com.tasree7a.observables.MenuIconClickedObservable;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class SalonDetailsFragment extends BaseFragment implements CardFactory, Observer {
    public static final String SALON_MODEL = SalonDetailsFragment.class.getName() + "SALON_MODEL";

    private SalonModel salonModel;

    private View navHeader;
    private View bookNow;
    private View addToFavorite;
    private ImageView closeDrawer;
    private ImageView back;
    private ImageView salonCover;
    private TextView bookNowLbl;
    private TextView salonName;
    private RecyclerView salonDetails;
    private DrawerLayout nvDrawer;
    private NavigationView nvView;
    private CustomRatingBar ratingBar;
    private CustomSwitch langSwitch;

    private View.OnClickListener listener = v -> {
        ReservationSessionManager.getInstance().setSalonModel(salonModel);
        FragmentManager.showBookNowFragment();
    };
    private View.OnClickListener servicesListener = v -> rootView.getContext().startActivity(new Intent(getContext(), SalonServicesActivity.class));


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_salon_details, container, false);

        nvDrawer = rootView.findViewById(R.id.drawer_layout);
        nvView = rootView.findViewById(R.id.nvView);
        navHeader = nvView.getHeaderView(0);
        closeDrawer = nvView.getHeaderView(0).findViewById(R.id.close_menu);
        //TODO: After getting data
        salonDetails = rootView.findViewById(R.id.salon_cards);

        Bundle args = getArguments();
        if (args != null) {
            salonModel = (SalonModel) args.getSerializable(SALON_MODEL);
        }

        initViews(rootView);
        addObservables();
        initSideMenuViews();
        return rootView;
    }

    private void initViews(View rootView) {
        back = rootView.findViewById(R.id.back);
        back.setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
        bookNowLbl = rootView.findViewById(R.id.book_now_lbl);

        addToFavorite = rootView.findViewById(R.id.add_to_favorite);
        addToFavorite.setOnClickListener(v -> {
            if (UserDefaultUtil.isSalonFavorite(salonModel)) {
                UserDefaultUtil.removeSalonFromFavorite(salonModel);
                ((ImageView) v).setImageResource(R.drawable.ic_favorite_unchecked);
            } else {
                UserDefaultUtil.addSalonToFavorite(salonModel);
                ((ImageView) v).setImageResource(R.drawable.ic_favorite_checked);
            }
            FavoriteChangeObservable.sharedInstance().setFavoriteChanged(salonModel);
        });

        ratingBar = rootView.findViewById(R.id.salon_rating);
        salonName = rootView.findViewById(R.id.sallon_name);
        salonCover = rootView.findViewById(R.id.salon_image);
        bookNow = rootView.findViewById(R.id.bookNow);
    }

    private void loadSalonData() {
        ratingBar.setRating(salonModel.getRating());
        salonName.setText(salonModel.getName());
        UIUtils.loadUrlIntoImageView(getContext(), salonModel.getImage(), salonCover, Sizes.MEDIUM);

        if (UserDefaultUtil.isSalonFavorite(salonModel)) {
            ((ImageView) addToFavorite).setImageResource(R.drawable.ic_favorite_checked);
        } else {
            ((ImageView) addToFavorite).setImageResource(R.drawable.ic_favorite_unchecked);
        }

        if (UserDefaultUtil.isBusinessUser()) {
            addToFavorite.setVisibility(View.GONE);
            bookNowLbl.setText(R.string.SERVICES);

            back.setImageResource(R.drawable.ic_side_menu);
            back.setColorFilter(ThisApplication.getCurrentActivity().getBaseContext().getResources().getColor(R.color.WHITE));
            back.setPadding(0, 0, 0, 0);
            back.setOnClickListener(v -> MenuIconClickedObservable.sharedInstance().menuIconClicked());

            bookNow.setOnClickListener(servicesListener);
            bookNowLbl.setOnClickListener(servicesListener);
        } else {
            bookNow.setOnClickListener(listener);
            bookNowLbl.setOnClickListener(listener);
        }
    }

    private void addObservables() {
        MenuIconClickedObservable.sharedInstance().addObserver(this);
        GallaryItemsChangedObservable.sharedInstance().addObserver(this);
    }

    private void initSideMenuViews() {
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

    private void hideLoadingView() {
        rootView.findViewById(R.id.loading).setVisibility(View.GONE);
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
            //TODO: No No No don't
            //TODO: return result from Activity -> SalonGalary/Products to activity
            getSalonDetails();
        }
    }

    public void getSalonDetails() {
        RetrofitManager.getInstance().getSalonDetails(salonModel == null ? UserDefaultUtil.getCurrentUser().getSalonId() : salonModel.getId(), (isSuccess, result) -> {
            if (!isAdded()) return;
            if (isSuccess) {
                //TODO: Refactor this
                salonModel = (SalonModel) result;
                loadSalonData();
                UserDefaultUtil.setCurrentSalonUser(salonModel);
                ReservationSessionManager.getInstance().setSalonModel(salonModel);
                salonDetails.setLayoutManager(new LinearLayoutManager(getContext()));
                salonDetails.setAdapter(new BaseCardAdapter(getCardModels()));
                salonDetails.getAdapter().notifyDataSetChanged();
                if (!salonModel.isBusiness()) {
                    nvDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }
            hideLoadingView();
        });
    }

    public boolean isSalonDataValid() {
        return UserDefaultUtil.getCurrentUser().isBusiness()
                && (Integer.parseInt(UserDefaultUtil.getCurrentUser().getId()) == -1);
    }
}
