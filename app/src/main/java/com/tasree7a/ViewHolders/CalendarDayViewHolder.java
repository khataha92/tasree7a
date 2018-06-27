package com.tasree7a.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.enums.FontsType;
import com.tasree7a.interfaces.CalenderCellClickListener;
import com.tasree7a.utils.FontUtil;

import org.joda.time.LocalDate;

/**
 * Created by Khalid Taha on 2/9/16.
 * Day View Holder for CustomGridCalendar
 */
public class CalendarDayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textView;

    private View leftCheckLineSelection
            , rightCheckLineSelection
            , leftCheckLineMarker
            , rightCheckLineMarker
            , fullSelectionBG
            , fullMarkerBG;

    private LocalDate date;

    private LinearLayout holidaysContainer;

    private CalenderCellClickListener dayClickListener;


    public CalendarDayViewHolder(View itemView) {
        super(itemView);

        leftCheckLineSelection = itemView.findViewById(R.id.left_checkline_selection);

        rightCheckLineSelection = itemView.findViewById(R.id.right_checkline_selection);

        leftCheckLineMarker = itemView.findViewById(R.id.left_checkline_marker);

        rightCheckLineMarker = itemView.findViewById(R.id.right_checkline_marker);

        fullMarkerBG = itemView.findViewById(R.id.marker_full_bg);

        fullSelectionBG = itemView.findViewById(R.id.selection_full_bg);

        textView = itemView.findViewById(R.id.textView);

        textView.setTypeface(FontUtil.getFont(FontsType.BOOK));

        holidaysContainer = itemView.findViewById(R.id.holidays_container);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(dayClickListener != null) {

            dayClickListener.onItemClick(date, v);

        }
    }


    public LocalDate getDate() {

        return date;
    }


    public void setDate(LocalDate date) {

        this.date = date;
    }


    public TextView getTextView() {

        return textView;
    }


    public View getLeftCheckLineSelection() {

        return leftCheckLineSelection;
    }


    public View getRightCheckLineSelection() {

        return rightCheckLineSelection;
    }


    public View getLeftCheckLineMarker() {

        return leftCheckLineMarker;
    }


    public View getRightCheckLineMarker() {

        return rightCheckLineMarker;
    }


    public View getFullSelectionBG() {

        return fullSelectionBG;
    }


    public View getFullMarkerBG() {

        return fullMarkerBG;
    }


    public void setTextView(TextView textView) {

        this.textView = textView;
    }


    public void setDayClickListener(CalenderCellClickListener dayClickListener) {

        this.dayClickListener = dayClickListener;
    }


    public LinearLayout getHolidaysContainer() {

        return holidaysContainer;
    }


    public void setHolidaysContainer(LinearLayout holidaysContainer) {

        this.holidaysContainer = holidaysContainer;
    }


    public void resetColors() {

        this.fullSelectionBG.setBackgroundColor(0);

        this.leftCheckLineMarker.setBackgroundColor(0);

        this.rightCheckLineMarker.setBackgroundColor(0);

        this.leftCheckLineSelection.setBackgroundColor(0);

        this.rightCheckLineSelection.setBackgroundColor(0);

        this.fullMarkerBG.setBackgroundColor(0);


    }
}
