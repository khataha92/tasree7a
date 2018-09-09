package com.tasree7a.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.ReservationSessionManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.salonbooking.SalonService;
import com.tasree7a.models.salondetails.SalonBarber;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.UserDefaultUtil;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class BookScheduleFragment extends BaseFragment {

    public static final String SALON_SERVICES = BookScheduleFragment.class.getName() + "SALON_SERVICES";

    SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
    SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    SalonModel salonModel;

//    List<Integer> reservedTimes = new ArrayList<>();

    TextView availabilityText;

    View confirm;

    CustomRadioGroup radioGroup;

    LocalDate localDate;

    Tasree7aWheel timeWheel;

    List<SalonService> salonServices;
    private LinearLayout mLoading;
    private boolean isValidBookingTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        salonModel = ReservationSessionManager.getInstance().getSalonModel();

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_book_schedule, container, false);

        salonServices = Objects.requireNonNull(getArguments()).getParcelableArrayList(SALON_SERVICES);

        confirm = rootView.findViewById(R.id.confirm);

        availabilityText = rootView.findViewById(R.id.availability_text);

        timeWheel = rootView.findViewById(R.id.time_wheel);

        timeWheel.setAvailable(isTimeValid());

        isValidBookingTime = isTimeValid();

//        if (!isValidBookingTime) {
//            confirm.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
//        } else {
//            confirm.getBackground().setColorFilter(getResources().getColor(R.color.APP_GREEN), PorterDuff.Mode.SRC_ATOP);
//        }

        timeWheel.setAction(() -> {
            isValidBookingTime = isTimeValid();

//            if (!isValidBookingTime) {
//                confirm.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
//            } else {
//                confirm.getBackground().setColorFilter(getResources().getColor(R.color.APP_GREEN), PorterDuff.Mode.SRC_ATOP);
//            }
            confirm.setClickable(isValidBookingTime);
            confirm.setEnabled(isValidBookingTime);
            timeWheel.setAvailable(isValidBookingTime);
        });

        mLoading = rootView.findViewById(R.id.loading);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = "" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
        ((TextView) rootView.findViewById(R.id.select_checkin_date)).setText(date);
        localDate = new LocalDate(date);
        rootView.findViewById(R.id.cancel).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
        rootView.findViewById(R.id.back).setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
        initSalonBarbers();

        rootView.findViewById(R.id.date_container).setOnClickListener(v -> FragmentManager.showCalendarFragment(getActivity(), (isSuccess, result) -> {
            if (isSuccess) {
                localDate = (LocalDate) result;
                ((TextView) rootView.findViewById(R.id.select_checkin_date)).setText(localDate.toString());
            }
        }));

        String total = getString(R.string.TOTAL);

        ((TextView) rootView.findViewById(R.id.total)).setText(total + ": $" + ReservationSessionManager.getInstance().getTotal());

        isValidBookingTime = (salonModel.getReservedTimes() == null || salonServices.isEmpty());
        confirm.setOnClickListener(v -> {

            if (isTimeValid()) {
                String barberId = ((CustomRadioButton) radioGroup.getCheckedItem()).getItemId();
                String salonId = ReservationSessionManager.getInstance().getSalonModel().getId();
                String userId = UserDefaultUtil.getCurrentUser().getId();
                int[] services = new int[salonServices.size()];

                mLoading.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.scroll).setVisibility(View.GONE);
                RetrofitManager.getInstance().addBooking(barberId, salonId, services, userId, localDate.toString(), get24TimeFormat(timeWheel.getTime()), (isSuccess, result) -> {
                    mLoading.setVisibility(View.GONE);
                    rootView.findViewById(R.id.scroll).setVisibility(View.VISIBLE);
                    if (isSuccess) {

                        FragmentManager.popCurrentVisibleFragment();

                        FragmentManager.popCurrentVisibleFragment();

                        Toast.makeText(ThisApplication.getCurrentActivity(), "Added booking successfully", Toast.LENGTH_SHORT).show();

                    } else {

                        try {
                            Toasty.error(getContext(), "Error occurred while trying to add booking", Toast.LENGTH_SHORT).show();
                        } catch (Exception ignore) {
                        }

                    }

                });
            } else {
                Toasty.error(BookScheduleFragment.this.getContext(), "Selected time is reserved please try another time", Toast.LENGTH_LONG).show();
            }

        });

        return rootView;

    }

    private String get24TimeFormat(String toConvert) {
        try {
            Date reserveDate = inFormat.parse(toConvert);
            Calendar reservationTimeCal = Calendar.getInstance();
            reservationTimeCal.setTime(reserveDate);
            return outFormat.format(reservationTimeCal.getTime());
        } catch (ParseException ignored) {
        }
        return null;
    }

    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private boolean isTimeValid() {
        String time24;
        String toTime;
        Calendar reservationTimeCal = Calendar.getInstance();
        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        try {
            time24 = get24TimeFormat(timeWheel.getTime());
            int timeToAdd = 0;
            for (SalonService service : salonServices) {
                timeToAdd = Integer.parseInt(service.getDuration());
            }
            reservationTimeCal.setTime(inFormat.parse(timeWheel.getTime()));
            reservationTimeCal.add(Calendar.MINUTE, timeToAdd);
            toTime = outFormat.format(reservationTimeCal.getTime());

            for (String reservedTime : salonModel.getReservedTimes()) {
                if (reservedTime.equalsIgnoreCase(time24 + "-" + toTime)) {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
        } catch (Exception ignored) {
        }
    }
}
