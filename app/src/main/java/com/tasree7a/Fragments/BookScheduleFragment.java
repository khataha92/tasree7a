package com.tasree7a.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tasree7a.Adapters.SalonServicesAdapter;
import com.tasree7a.CustomComponent.CustomRadioButton;
import com.tasree7a.CustomComponent.CustomRadioGroup;
import com.tasree7a.CustomComponent.CustomTimePicker;
import com.tasree7a.Enums.CustomOrientation;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.ReservationSessionManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Managers.SessionManager;
import com.tasree7a.Models.SalonBooking.AvailableTimesResponse;
import com.tasree7a.Models.SalonBooking.SalonService;
import com.tasree7a.Models.SalonDetails.SalonBarber;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mac on 9/17/17.
 */

public class BookScheduleFragment extends BaseFragment {

    SalonModel salonModel;

    List<Integer> availableTimes = new ArrayList<>();

    CustomTimePicker timePicker;

    TextView availabilityText;

    View confirm;

    CustomRadioGroup radioGroup;

    LocalDate localDate;

    int finalTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        salonModel = ReservationSessionManager.getInstance().getSalonModel();

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_book_schedule, null);

        confirm = rootView.findViewById(R.id.confirm);

        timePicker = (CustomTimePicker) rootView.findViewById(R.id.timePicker);

        availabilityText = (TextView) rootView.findViewById(R.id.availability_text);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                finalTime = convertTimeToInt(hourOfDay, minute);

                if(availableTimes.indexOf(finalTime) == -1) {

                    availabilityText.setVisibility(View.VISIBLE);

                    confirm.setClickable(false);

                    ((TextView)confirm).setTextColor(Color.GRAY);

                } else {

                    availabilityText.setVisibility(View.INVISIBLE);

                    confirm.setClickable(true);

                    ((TextView)confirm).setTextColor(Color.WHITE);
                }

            }
        });



        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);

        int month = c.get(Calendar.MONTH) + 1;

        int day = c.get(Calendar.DAY_OF_MONTH);

        String date = "" + year + "-" + (month < 10 ? "0" + month : month) +"-"+ (day < 10 ? "0" + day : day);

        getAvailableTimeOnDate(date);

        localDate = new LocalDate(date);

        rootView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

        initSalonBarbers();

        rootView.findViewById(R.id.select_checkin_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                FragmentManager.showCalendarFragment(new AbstractCallback() {
                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                        if(isSuccess){

                            localDate = (LocalDate) result;

                            ((TextView)v).setText(localDate.toString());

                            getAvailableTimeOnDate(localDate.toString());

                        }

                    }
                });

            }
        });

        String total = getString(R.string.TOTAL);

        ((TextView) rootView.findViewById(R.id.total)).setText(total + ": $" +ReservationSessionManager.getInstance().getTotal());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Causes a crash
                String barberId = ((CustomRadioButton) radioGroup.getCheckedItem()).getItemId();

                String salonId = ReservationSessionManager.getInstance().getSalonModel().getId();

                String userId = UserDefaultUtil.getCurrentUser().getId();

                if(finalTime == 0) {

                    Calendar c = Calendar.getInstance();

                    int hours = c.get(Calendar.HOUR_OF_DAY);

                    int minutes = c.get(Calendar.MINUTE);

                    finalTime = convertTimeToInt(hours, minutes);

                }

                int[] services = new int[ReservationSessionManager.getInstance().getSelectedServices().size()];

                for(int i = 0 ; i < ReservationSessionManager.getInstance().getSelectedServices().size() ; i++) {

                    services[i] = Integer.parseInt(ReservationSessionManager.getInstance().getSelectedServices().get(i).getId());

                }

                RetrofitManager.getInstance().addBooking(barberId, salonId, services, userId, localDate.toString(), "" + finalTime, new AbstractCallback() {
                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                        if(isSuccess) {

                            FragmentManager.popCurrentVisibleFragment();

                            FragmentManager.popCurrentVisibleFragment();

                            Toast.makeText(ThisApplication.getCurrentActivity(), "Added booking successfully", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getContext(), "Error occurred while trying to add booking" , Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });

        return rootView;

    }

    private void initSalonBarbers(){

        radioGroup = (CustomRadioGroup) rootView.findViewById(R.id.salon_barber);

        List<SalonBarber> salonBarberList = salonModel.getSalonBarbers();

        for(int i = 0 ; i < salonBarberList.size() ; i++){

            CustomRadioButton salonBarber = new CustomRadioButton(ThisApplication.getCurrentActivity());

            salonBarber.setCustomOrientation(CustomOrientation.HORIZONTAL);

            salonBarber.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            salonBarber.setLayoutParams(params);

            salonBarber.setLabel(salonBarberList.get(i).getBarberFirstName() + " " + salonBarberList.get(i).getBarberLastName());

            salonBarber.setItemId(salonBarberList.get(i).getBarberId());

            if(i == 0) {

                salonBarber.check();
            }

            radioGroup.addView(salonBarber);

        }

    }

    private void getAvailableTimeOnDate(String date) {

        UIUtils.showLoadingView(rootView, this);

        RetrofitManager.getInstance().getAvailableTimes(ReservationSessionManager.getInstance().getSalonModel().getId(), date, new AbstractCallback() {
            @Override
            public void onResult(boolean isSuccess, Object result) {

                UIUtils.hideLoadingView(rootView, BookScheduleFragment.this);

                if(isSuccess) {

                    availableTimes = ((AvailableTimesResponse) result) .getAvailableTimes();

                }

            }
        });
    }

    private int convertTimeToInt(int hourOfDay, int minute) {

        int temp = 0;

        if(minute >= 30) {

            temp = 1;
        }

        finalTime = hourOfDay * 2 + temp;

        return finalTime;

    }
}
