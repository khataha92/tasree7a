package com.tasree7a.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.customcomponent.CustomRadioButton;
import com.tasree7a.customcomponent.CustomRadioGroup;
import com.tasree7a.customcomponent.Tasree7aWheel;
import com.tasree7a.enums.CustomOrientation;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.salonbooking.AvailableTimesResponse;
import com.tasree7a.models.salondetails.SalonBarber;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookScheduleFragment extends BaseFragment {

    SalonModel salonModel;

    List<Integer> availableTimes = new ArrayList<>();

    TextView availabilityText;

    View confirm;

    CustomRadioGroup radioGroup;

    LocalDate localDate;

    Tasree7aWheel timeWheel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        salonModel = ReservationSessionManager.getInstance().getSalonModel();

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_book_schedule, null);

        confirm = rootView.findViewById(R.id.confirm);

        availabilityText = rootView.findViewById(R.id.availability_text);

        timeWheel = rootView.findViewById(R.id.time_wheel);

        timeWheel.setAction(() -> {

            boolean available = salonModel.getAvailableTimesFormatted().indexOf(timeWheel.getTime()+"") != -1;
            confirm.setClickable(available);
            timeWheel.setAvailable(available);

        });
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);

        int month = c.get(Calendar.MONTH) + 1;

        int day = c.get(Calendar.DAY_OF_MONTH);

        String date = "" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);

        getAvailableTimeOnDate(date);

        localDate = new LocalDate(date);

        rootView.findViewById(R.id.cancel).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());

        initSalonBarbers();

        rootView.findViewById(R.id.date_container).setOnClickListener(v -> FragmentManager.showCalendarFragment((isSuccess, result) -> {

            if (isSuccess) {

                localDate = (LocalDate) result;

                ((TextView) rootView.findViewById(R.id.select_checkin_date)).setText(localDate.toString());

                getAvailableTimeOnDate(localDate.toString());

            }

        }));

        String total = getString(R.string.TOTAL);

        ((TextView) rootView.findViewById(R.id.total)).setText(total + ": $" + ReservationSessionManager.getInstance().getTotal());

        confirm.setOnClickListener(v -> {

            //TODO: Causes a crash
            String barberId = ((CustomRadioButton) radioGroup.getCheckedItem()).getItemId();

            String salonId = ReservationSessionManager.getInstance().getSalonModel().getId();

            String userId = UserDefaultUtil.getCurrentUser().getId();

            int[] services = new int[ReservationSessionManager.getInstance().getSelectedServices().size()];

            for (int i = 0; i < ReservationSessionManager.getInstance().getSelectedServices().size(); i++) {

                services[i] = Integer.parseInt(ReservationSessionManager.getInstance().getSelectedServices().get(i).getId());

            }

            RetrofitManager.getInstance().addBooking(barberId, salonId, services, userId, localDate.toString(), timeWheel.getTime() + "", new AbstractCallback() {

                @Override
                public void onResult(boolean isSuccess, Object result) {

                    if (isSuccess) {

                        FragmentManager.popCurrentVisibleFragment();

                        FragmentManager.popCurrentVisibleFragment();

                        Toast.makeText(ThisApplication.getCurrentActivity(), "Added booking successfully", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getContext(), "Error occurred while trying to add booking", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        });

        return rootView;

    }


    private void initSalonBarbers() {

        radioGroup = rootView.findViewById(R.id.salon_barber);

        List<SalonBarber> salonBarberList = salonModel.getSalonBarbers();

        try {

            for (int i = 0; i < salonBarberList.size(); i++) {

                CustomRadioButton salonBarber = new CustomRadioButton(ThisApplication.getCurrentActivity());

                salonBarber.setCustomOrientation(CustomOrientation.HORIZONTAL);

                salonBarber.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                salonBarber.setLayoutParams(params);

                salonBarber.setLabel(salonBarberList.get(i).getBarberFirstName() + " " + salonBarberList.get(i).getBarberLastName());

                salonBarber.setItemId(salonBarberList.get(i).getBarberId());

                if (i == 0) {

                    salonBarber.check();
                }

                radioGroup.addView(salonBarber);

            }

        } catch (Exception e) {

        }

    }


    private void getAvailableTimeOnDate(String date) {

        UIUtils.showLoadingView(rootView, this);

        RetrofitManager.getInstance().getAvailableTimes(ReservationSessionManager.getInstance().getSalonModel().getId(), date, (isSuccess, result) -> {

            UIUtils.hideLoadingView(rootView, BookScheduleFragment.this);

            if (isSuccess) {

                availableTimes = ((AvailableTimesResponse) result).getAvailableTimes();

            }

        });
    }

}
