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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.adapters.SalonServicesAdapter;
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

    private boolean isSelectionMode;
    private float mTotalPrice = 0.0f;

    private List<SalonService> mSalonServicesList = new ArrayList<>();

    private SalonServicesAdapter mServicesAdapter;

    private LinearLayout mTotalPriceContainer;
    private TextView mTotalPriceTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_services);

        Intent intent = getIntent();
        if (intent != null) {
            isSelectionMode = intent.getBooleanExtra(IS_SELECTION_MODE, false);
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
            mServicesAdapter.notifyItemChanged(position);
            mTotalPriceContainer.setVisibility(View.GONE);
        } else {
            if (selected) {
                mTotalPrice += mSalonServicesList.get(position).getPrice();
            } else {
                mTotalPrice -= mSalonServicesList.get(position).getPrice();
            }
            mTotalPriceTextView.setText(String.valueOf(mTotalPrice));
            mTotalPriceContainer.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {

        findViewById(R.id.add_delete).setOnClickListener(v -> startActivityForResult(new Intent(this, AddSalonServiceActivity.class), AddSalonServiceActivity.REQUEST_CODE));

        RecyclerView mServicesList = findViewById(R.id.services_list);
        mServicesList.setLayoutManager(new GridLayoutManager(this, 2));

        mServicesAdapter = new SalonServicesAdapter(mSalonServicesList, this);
        mServicesList.setAdapter(mServicesAdapter);

        findViewById(R.id.back).setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });
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
