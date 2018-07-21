package com.tasree7a.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.adapters.SalonServicesAdapter;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.interfaces.SalonServiceSelectionListener;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.salonbooking.SalonService;
import com.tasree7a.models.salonbooking.SalonServicesResponse;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;

public class SalonServicesActivity extends AppCompatActivity implements SalonServiceSelectionListener {

    public static final String IS_SELECTION_MODE = SalonServicesActivity.class.getName() + "IS_SELECTION_MODE";
    public static final String SALON_ID = SalonServicesActivity.class.getName() + "SALON_ID";

//    private boolean isSelectionMode;isSelectionMode
//    private float mTotalPrice = 0.0f;
    private String mSalonId;

    private List<SalonService> mSalonServicesList = new ArrayList<>();
    private List<SalonService> mSelectedSalonServicesList = new ArrayList<>();

    private SalonServicesAdapter mServicesAdapter;

    private LinearLayout mTotalPriceContainer;
    private TextView mTotalPriceTextView;

    private ImageView mAddService;
    private ImageView mRemoveService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_services);

        Intent intent = getIntent();
        if (intent != null) {
//            isSelectionMode = intent.getBooleanExtra(IS_SELECTION_MODE, false);
            mSalonId = intent.getStringExtra(SALON_ID);
        }

        initViews();
        requestSalonServices();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case AddSalonServiceActivity.REQUEST_CODE:
                requestSalonServices();
                break;
        }
    }

    @Override
    public void onSalonServiceClicked(boolean selected, int position) {
        if (UserDefaultUtil.isBusinessUser()) {
            mSalonServicesList.get(position).setSelected(selected);

            if (selected) {
                mSelectedSalonServicesList.add(mSalonServicesList.get(position));
            } else {
                mSelectedSalonServicesList.remove(mSalonServicesList.get(position));
            }

            mServicesAdapter.notifyItemChanged(position);

            mAddService.setVisibility(mSelectedSalonServicesList.isEmpty() ? View.VISIBLE : View.GONE);
            mRemoveService.setVisibility(mSelectedSalonServicesList.isEmpty() ? View.GONE : View.VISIBLE);
//            mTotalPriceContainer.setVisibility(View.GONE);

        } else {
            Log.d("TODO", "TODO");
//            if (selected) {
//                mTotalPrice += mSalonServicesList.get(position).getPrice();
//            } else {
//                mTotalPrice -= mSalonServicesList.get(position).getPrice();
//            }
//            mTotalPriceTextView.setText(String.valueOf(mTotalPrice));
//            mTotalPriceContainer.setVisibility(View.VISIBLE);
        }

    }

    private void initViews() {

        mAddService = findViewById(R.id.add_item);
        mRemoveService = findViewById(R.id.delete_item);

        mAddService.setVisibility(mSelectedSalonServicesList.isEmpty() ? View.VISIBLE : View.GONE);
        mRemoveService.setVisibility(mSelectedSalonServicesList.isEmpty() ? View.GONE : View.VISIBLE);

        mTotalPriceContainer = findViewById(R.id.price_container);
        mTotalPriceContainer.setVisibility(View.GONE);

        mAddService.setOnClickListener(v -> startActivityForResult(new Intent(this, AddSalonServiceActivity.class), AddSalonServiceActivity.REQUEST_CODE));
        mRemoveService.setOnClickListener(v -> requestSalonServicesRemoval());

        RecyclerView mServicesList = findViewById(R.id.services_list);
        mServicesList.setLayoutManager(new GridLayoutManager(this, 2));

        mServicesAdapter = new SalonServicesAdapter(mSalonServicesList, this);
        mServicesList.setAdapter(mServicesAdapter);

        findViewById(R.id.back).setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

    private void requestSalonServicesRemoval() {
        for (SalonService salonService : mSelectedSalonServicesList) {
            RetrofitManager
                    .getInstance()
                    .deleteSalonService(mSalonId, salonService.getId(), (isSuccess, result) -> {
                        int index = mSalonServicesList.indexOf(salonService);
                        mSalonServicesList.remove(salonService);
                        mServicesAdapter.notifyItemRemoved(index);
                    });
        }
    }

    private void requestSalonServices() {

        RetrofitManager.getInstance().getSalonServices(ReservationSessionManager.getInstance().getSalonModel().getId(), (isSuccess, result) -> {

            if (isSuccess) {
                mSalonServicesList.clear();
                mSalonServicesList.addAll(((SalonServicesResponse) result).getServices());
                if (mSalonServicesList == null || mSalonServicesList.size() == 0) {
                    //TODO: Show Empty textview message
                    Log.d("EMPTY", "EMPTY_VIEW_MESSAGE");
                } else {
                    mServicesAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
