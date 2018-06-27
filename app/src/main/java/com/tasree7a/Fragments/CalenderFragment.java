package com.tasree7a.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.FragmentBundle;
import com.tasree7a.R;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.customcomponent.CustomGridCalendar;
import com.tasree7a.enums.CalenderType;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.interfaces.DateCalenderViewListener;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.models.calendar.CalendarViewsModel;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mohammad on 5/3/15.
 * This is the calender fragment
 */
public class CalenderFragment extends BaseFragment {

    private CustomGridCalendar calenderView;

    private CustomButton bottomView;

    private CalenderType calenderType ;

    AbstractCallback callback;

    // LocalDate determine a start of days to be marked always as a special date or something
    private LocalDate markerCheckingDate;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_calender, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {

            calenderType = (CalenderType) bundle.getSerializable(FragmentBundle.CALENDER_TYPE);

            markerCheckingDate = (LocalDate) bundle.getSerializable(FragmentBundle.CHECKIN_MARKER_DATE);

        }

        initializeCustomGridCalender();

        initializeCalenderHeader();

        try {
            //bottom view click listener
            bottomView = (CustomButton)rootView.findViewById(R.id.select_date_bottom_view);

            bottomView.setText(CalenderType.DEFAULT.getFragmentButtonText());

            bottomView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doneButtonIsClicked();
                }
            });

        } catch (Throwable t){

            Log.e("EROOORr", "EROOORr", t);

        }

        return rootView;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void doneButtonIsClicked() {

        LocalDate date = calenderView.getCheckInDate();

        if(callback != null){

            callback.onResult(true,date);

        }

        FragmentManager.popCurrentVisibleFragment();

    }

    private void updateDateSelection(boolean isDone){

        LocalDate selectedCheckInDate = calenderView.getCheckInDate();

        LocalDate maxDayInCalender = calenderView.getMaxDayInCalender();

        if( selectedCheckInDate != null && selectedCheckInDate.isEqual(maxDayInCalender) ) {

            selectedCheckInDate = maxDayInCalender.minusDays(1);

        }

        if (isDone) {

            calenderView.setCheckInDate(selectedCheckInDate);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static LocalDate dateToLocalDate(Date date) {

        Locale locale = new Locale("en");

        return LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd",locale).format(date));

    }

    /**
     * Initialize cells before showing view
     */
    private void initializeCustomGridCalender() {

        Date currentTime = Calendar.getInstance().getTime();

        LocalDate savedChkInDate = dateToLocalDate(currentTime);

        calenderView = (CustomGridCalendar) rootView.findViewById(R.id.custom_grid_calender);

        CalendarViewsModel cModel = new CalendarViewsModel()
                .setCalenderType(calenderType)
                .setMarkerCheckInDate(markerCheckingDate);

        // Set check in date only in normal model
        if (calenderType == CalenderType.DEFAULT) {

            cModel.setCheckInDate(savedChkInDate);

        }

        calenderView.initializeCalender(cModel);

        calenderView.setDateChangeListener(new DateCalenderViewListener() {
            @Override
            public void onDateChanged(LocalDate checkInDate) {

                changeCheckInOutButtonsStyle();

            }

            @Override
            public void onError(String error) {

            }
        });


    }

    private void initializeCalenderHeader() {

        changeCheckInOutButtonsStyle();

    }


    /**
     * Change selectors TextViews and Background according to selection status
     *
     */
    private void changeCheckInOutButtonsStyle() {

        updateDateSelection(false);

    }

    public void setCallback(AbstractCallback callback) {
        this.callback = callback;
    }
}