package com.tasree7a.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasree7a.CustomComponent.CustomGridCalendar;
import com.tasree7a.Enums.CalenderType;
import com.tasree7a.Enums.Language;
import com.tasree7a.Models.Calendar.CalendarViewsModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.ViewHolders.CalendarMonthViewHolder;
import com.tasree7a.interfaces.CalenderCellClickListener;
import com.tasree7a.interfaces.DateCalenderViewListener;
import com.tasree7a.utils.DateFormatsOptions;
import com.tasree7a.utils.DateUtil;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Khalid Taha on 2/2/16.
 * To manage main recyclerView that contains month views
 */
public class CalendarListAdapter extends RecyclerView.Adapter<CalendarMonthViewHolder> implements CalenderCellClickListener {

    private List<LocalDate> listOfMonthCalenders;

    private List<String> monthLabels;

    List<Integer> typesList;

    private List<CalendarMonthAdapter> gridsAdapters;

    private LocalDate checkInDate, markerCheckInDate, today, maxDayInCalender;

    private DateCalenderViewListener mOnDateChangeListener;

    private int heightOfRecycler = 0;

    private Context context;

    private boolean isHandMadeClick = false;

    private List<List<SpannableString>> holidaysLabels;

    private List<List<Bitmap>> holidaysIcons;

    private CalenderType calenderType;


    public CalendarListAdapter(CalendarViewsModel cModel) {

        this.context = ThisApplication.getCurrentActivity();

        this.today = DateUtil.dateToLocalDate(DateUtil.getToday());

        this.markerCheckInDate = cModel.getMarkerCheckInDate();

        this.checkInDate = cModel.getCheckInDate();

        this.calenderType = cModel.getCalenderType();


        Object[] initializedMonthsWithAdapters = initializeMonthsAndAdapters(12);

        this.listOfMonthCalenders = (List<LocalDate>) initializedMonthsWithAdapters[0];

        this.listOfMonthCalenders = (List<LocalDate>) initializedMonthsWithAdapters[0];

        this.gridsAdapters = (List<CalendarMonthAdapter>) initializedMonthsWithAdapters[1];

        this.monthLabels = (List<String>) initializedMonthsWithAdapters[2];

        this.typesList = (List<Integer>) initializedMonthsWithAdapters[3];

        this.holidaysLabels = (List<List<SpannableString>>) initializedMonthsWithAdapters[4];

        this.holidaysIcons = (List<List<Bitmap>>) initializedMonthsWithAdapters[5];

        this.maxDayInCalender = today.plusMonths(12).dayOfMonth().withMaximumValue();

    }

    @Override
    public CalendarMonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.context = parent.getContext();

        View itemView;

        switch (viewType){

            case 0:
                itemView = LayoutInflater.
                        from(context).
                        inflate(R.layout.calendar_month_layout_label, parent, false);
                return new CalendarMonthViewHolder(itemView);

            case 1:
                itemView = LayoutInflater.
                        from(context).
                        inflate(R.layout.calendar_month_layout_recyclerview, parent, false);
                return new CalendarMonthViewHolder(itemView);

            default:

                return null;

        }
    }

    @Override
    public void onBindViewHolder(CalendarMonthViewHolder holder, int positionRecycler) {

        switch (getItemViewType(positionRecycler)) {

            case 0://label

                bindLabel(holder, positionRecycler);

             return;

            case 1://week

                bindWeek(holder, positionRecycler);

                return;

            default:

                return;
        }

    }

    private void bindWeek(CalendarMonthViewHolder holder, int positionRecycler) {

        gridsAdapters.get(positionRecycler).setOnItemClickListener(this);

        holder.getRecyclerView().setAdapter(gridsAdapters.get(positionRecycler));

    }

    private void bindLabel(CalendarMonthViewHolder holder, int positionRecycler) {

        if (positionRecycler == 0) {

            holder.getUpperSpace().getLayoutParams().height = UIUtils.dpToPx(40);

        }

        holder.getMonthLabel().setText(monthLabels.get(positionRecycler));

        holder.getMonthLabel().setTextColor(ThisApplication.getCurrentActivity().getResources().getColor(R.color.APP_TEXT_COLOR));

        holder.getHolidaysList().removeAllViews();

    }

    @Override
    public int getItemViewType(int position) {
        return typesList.get(position);
    }

    @Override
    public int getItemCount() {
        return listOfMonthCalenders.size();
    }

    public List<CalendarMonthAdapter> getGridsAdapters() {
        return gridsAdapters;
    }

    public void setCheckInDate(LocalDate date) {

        this.checkInDate = date;

        reassignCheckInOutsInAdapters();

        notifyDataSetChanged();
    }


    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getMarkerCheckInDate() {

        return markerCheckInDate;
    }


    public void setMarkerCheckInDate(LocalDate markerCheckInDate) {

        this.markerCheckInDate = markerCheckInDate;
    }



    public LocalDate getMaxDayInCalender() {

        return maxDayInCalender;

    }

    public void setHeights(int height) {
        this.heightOfRecycler = height;
    }


    /**
     * handles a click event on any date cell in calender
     *
     * @param newDate the date of the clicked cell
     */
    private void clickHappenedOnMonthGrid(LocalDate newDate) {

        int period = 1;

        int maxAllowedNights = 1;

        if(checkInDate != null){

            period = Days.daysBetween(checkInDate,newDate).getDays();

        }


        checkInDate = newDate;

        isHandMadeClick = false;

        reassignCheckInOutsInAdapters();

        notifyDataSetChanged();

        mOnDateChangeListener.onDateChanged(checkInDate);

    }


    /**
     * set the new check in out dates to the adapters
     */
    private void reassignCheckInOutsInAdapters() {

        for (CalendarMonthAdapter gridsAdapter : gridsAdapters) {

            if(gridsAdapter != null) {

                gridsAdapter.setCheckInDate(checkInDate);

            }

        }

    }

    /**
     * Set the callback that will handel date changing
     *
     * @param listener The date change listener
     */
    public void setDateChangeListener(final DateCalenderViewListener listener) {

        mOnDateChangeListener = listener;

    }


    /**
     * prepare arrays contains dates, adapters and month labels for the next n months
     *
     * @param numberOfMonths number of months to be ready in calneder when first opened
     * @return object array, index 0:dates array, index 1:adapters array, index 2:month labels, index 3:heights
     */
    private Object[] initializeMonthsAndAdapters(int numberOfMonths) {

        boolean isRTL = UserDefaultUtil.getAppLanguage() == Language.AR;

        LocalDate date = new LocalDate().withDayOfMonth(1);

        LocalDate maxDate = date.plusMonths(numberOfMonths).dayOfMonth().withMaximumValue();

        List<Integer> types = new ArrayList<>();

        List<String> labels = new ArrayList<>();

        List<Object[]> cells = new ArrayList<>();

        List<CalendarMonthAdapter> adapters = new ArrayList<>();

        List<List<SpannableString>> holidaysLabelsGenerated = new ArrayList<>();

        List<List<Bitmap>> holidaysIconsGenerated = new ArrayList<>();

        while ( !date.isAfter(maxDate)) {


            if (date.getDayOfMonth() == 1) {

                //time for label
                if (labels.isEmpty() || labels.get(labels.size() - 1) == null) {

                    types.add(0);

                    labels.add(DateUtil.format(DateUtil.localDateToDate(date), isRTL? DateFormatsOptions.CALENDER_ALERT_RTL: DateFormatsOptions.CALENDER_ALERT_LTR));

                    cells.add(null);

                    adapters.add(null);

                } else {

                    types.add(1);

                    labels.add(null);

                    holidaysLabelsGenerated.add(null);

                    holidaysIconsGenerated.add(null);

                    Object[] cell = new Object[7];

                    cells.add(cell);

                    int monthStartIndex = date.getDayOfWeek() >= 7 ? 0 : date.getDayOfWeek();

                    CalendarViewsModel monthModel = new CalendarViewsModel()
                            .setMonthBeginningCell(monthStartIndex)
                            .setCalenderType(calenderType)
                            .setCheckInDate(checkInDate)
                            .setDays(cell)
                            .setMarkerCheckInDate(markerCheckInDate)
                            .setStartDay(date)
                            .setToday(today);

                    CalendarMonthAdapter calendarMonthAdapter = new CalendarMonthAdapter(monthModel);

//                    calendarMonthAdapter.setHolidays(map);

                    adapters.add(calendarMonthAdapter);

                    date = date.plusDays(7 - monthStartIndex);
                }
            } else {

                types.add(1);

                labels.add(null);

                holidaysLabelsGenerated.add(null);

                holidaysIconsGenerated.add(null);

                if ((date.getDayOfMonth() + 7) > date.dayOfMonth().getMaximumValue()) {//end of month

                    int difference = date.dayOfMonth().getMaximumValue() - date.getDayOfMonth();

                    Object[] cell = new Object[difference + 1];

                    cells.add(cell);


                    CalendarViewsModel monthModel = new CalendarViewsModel()
                            .setMonthBeginningCell(0)
                            .setCalenderType(calenderType)
                            .setCheckInDate(checkInDate)
                            .setDays(cell)
                            .setMarkerCheckInDate(markerCheckInDate)
                            .setStartDay(date)
                            .setToday(today);

                    CalendarMonthAdapter calendarMonthAdapter = new CalendarMonthAdapter(monthModel);

//                    calendarMonthAdapter.setHolidays(map);

                    adapters.add(calendarMonthAdapter);

                    date = date.plusMonths(1).withDayOfMonth(1);

                } else {

                    Object[] cell = new Object[7];

                    cells.add(cell);

                    CalendarViewsModel monthModel = new CalendarViewsModel()
                            .setMonthBeginningCell(0)
                            .setCalenderType(calenderType)
                            .setCheckInDate(checkInDate)
                            .setDays(cell)
                            .setMarkerCheckInDate(markerCheckInDate)
                            .setStartDay(date)
                            .setToday(today);

                    CalendarMonthAdapter calendarMonthAdapter = new CalendarMonthAdapter(monthModel);

//                    calendarMonthAdapter.setHolidays(map);

                    adapters.add(calendarMonthAdapter);

                    date = date.plusDays(7);
                }
            }

        }


        Object[] result = new Object[6];
        result[0] = cells;
        result[1] = adapters;
        result[2] = labels;
        result[3] = types;
        result[4] = holidaysLabelsGenerated;
        result[5] = holidaysIconsGenerated;

        return result;
    }


    @Override
    public void onItemClick(LocalDate date, View v) {

        if (date != null) {

            LocalDate maxDayInMonth = date.dayOfMonth().withMaximumValue();

            if (!date.isBefore(today) && !date.isAfter(maxDayInMonth)) {

                clickHappenedOnMonthGrid(date);

            }
        }

    }
}