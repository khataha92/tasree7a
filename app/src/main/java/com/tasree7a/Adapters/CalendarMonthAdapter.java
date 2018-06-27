package com.tasree7a.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.enums.CalendarDayViewType;
import com.tasree7a.interfaces.CalenderCellClickListener;
import com.tasree7a.models.calendar.CalendarViewsModel;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.viewholders.CalendarDayViewHolder;

import org.joda.time.LocalDate;

import static com.tasree7a.enums.CalendarDayViewType.EMPTY_DAY;
import static com.tasree7a.enums.CalendarDayViewType.IN_MARKERS_INTERVAL_DAY;
import static com.tasree7a.enums.CalendarDayViewType.IN_SELECTION_DAY;
import static com.tasree7a.enums.CalendarDayViewType.NORMAL_DAY;


/**
 * Created by Khalid Taha on 2/2/16.
 * Display days viewholders in month (days)
 */
public class CalendarMonthAdapter extends RecyclerView.Adapter<CalendarDayViewHolder> {

    private Context context;

    private LocalDate checkInDate, markerCheckInDate;

    private int indexOfMonth;

    private Object[] days;

    private LocalDate today, startDay;

    private CalenderCellClickListener dayClickListener;


    public void setOnItemClickListener(CalenderCellClickListener listener) {
        this.dayClickListener = listener;
    }

    public CalendarMonthAdapter(CalendarViewsModel calendarViewsModel) {

        this.context = ThisApplication.getCurrentActivity();

        this.indexOfMonth = calendarViewsModel.getMonthBeginningCell();

        this.checkInDate = calendarViewsModel.getCheckInDate();

        this.markerCheckInDate = calendarViewsModel.getMarkerCheckInDate();

        this.days = calendarViewsModel.getDays();

        this.startDay = calendarViewsModel.getStartDay();

        this.today = calendarViewsModel.getToday();

    }


    @NonNull
    @Override
    public CalendarDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CalendarDayViewHolder(LayoutInflater.from(context).inflate(R.layout.calendar_normal_day_layout, parent, false));
    }


    @Override
    public int getItemViewType(int position) {

        if (position < indexOfMonth || position > (startDay.dayOfMonth().getMaximumValue() + indexOfMonth)) {

            return EMPTY_DAY.getValue();
        }

        LocalDate dateInPosition = startDay.plusDays(position - indexOfMonth);

        if (checkInDate != null && dateInPosition.isEqual(checkInDate)) {

            return IN_SELECTION_DAY.getValue();

        }

        return NORMAL_DAY.getValue();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    @Override
    public void onBindViewHolder(@NonNull final CalendarDayViewHolder holder, int position) {

        if (position < indexOfMonth || (position > days.length + indexOfMonth)) {
            return;
        }

        // date of cell
        LocalDate date = startDay.plusDays(position - indexOfMonth);

        holder.setDate(date);

        holder.setDayClickListener(dayClickListener);

        String dayText = String.valueOf(date.getDayOfMonth());

        holder.getTextView().setText(dayText);

        CalendarDayViewType thisCellType = CalendarDayViewType.valueOf(getItemViewType(position));

        switch (thisCellType) {

            case NORMAL_DAY:

                if (date.isBefore(today)) {

                    holder.getTextView().setTextColor(ThisApplication.getCurrentActivity().getResources().getColor(R.color.GRAY));

                } else if (date.isAfter(today)) {

                    holder.getTextView().setTextColor(ThisApplication.getCurrentActivity().getResources().getColor(R.color.APP_TEXT_COLOR));

                } else if (date.isEqual(today)) {

                    holder.getFullMarkerBG().setBackground(
                            UIUtils.generateShapeBackground(
                                    ThisApplication.getCurrentActivity().getResources().getColor(R.color.TRANSPARENT_BLACK_COLOR),
                                    ThisApplication.getCurrentActivity().getResources().getColor(R.color.APP_TEXT_COLOR), GradientDrawable.OVAL, 4));

                    holder.getFullSelectionBG().setBackground(
                            UIUtils.generateShapeBackground(
                                    ThisApplication.getCurrentActivity().getResources().getColor(R.color.TRANSPARENT_BLACK_COLOR),
                                    ThisApplication.getCurrentActivity().getResources().getColor(R.color.APP_TEXT_COLOR), GradientDrawable.OVAL, 4));

                    holder.getTextView().setTextColor(ThisApplication.getCurrentActivity().getResources().getColor(R.color.APP_TEXT_COLOR));
                }

                return;

            case IN_MARKERS_INTERVAL_DAY:

                bindViewHolderOnSpecialDay(holder, date, thisCellType);

                break;

            case IN_SELECTION_DAY:

                bindViewHolderOnSpecialDay(holder, date, IN_SELECTION_DAY);

                break;

        }

    }

    private void bindViewHolderOnSpecialDay(CalendarDayViewHolder holder, LocalDate date, CalendarDayViewType cellType) {

        if (cellType == IN_SELECTION_DAY) {

            holder.getTextView().setTextColor(Color.WHITE);

        } else {

            holder.getTextView().setTextColor(ThisApplication.getCurrentActivity().getResources().getColor(R.color.APP_TEXT_COLOR));

        }

        LocalDate checinDate = cellType == IN_MARKERS_INTERVAL_DAY? markerCheckInDate : checkInDate;

        int cellBg = cellType == IN_MARKERS_INTERVAL_DAY? R.color.BLACK : R.color.APP_GREEN;

        View fullBgToShow = cellType == IN_MARKERS_INTERVAL_DAY? holder.getFullMarkerBG() : holder.getFullSelectionBG();

        View fullBgToHide = cellType != IN_MARKERS_INTERVAL_DAY? holder.getFullMarkerBG() : holder.getFullSelectionBG();

        if (date.isEqual(checinDate)) {//if it's in start interval

            fullBgToHide.setVisibility(View.GONE);

            fullBgToShow.setVisibility(View.VISIBLE);

            fullBgToShow.setBackground(
                    UIUtils.generateShapeBackground(ThisApplication.getCurrentActivity().getResources().getColor(cellBg), ThisApplication.getCurrentActivity().getResources().getColor(cellBg), GradientDrawable.OVAL, 4));

        }

    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public int getIndexOfMonth() {
        return indexOfMonth;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

}
