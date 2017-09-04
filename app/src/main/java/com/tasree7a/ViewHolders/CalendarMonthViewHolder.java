package com.tasree7a.ViewHolders;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tasree7a.CustomComponent.RtlGridLayoutManager;
import com.tasree7a.Enums.FontsType;
import com.tasree7a.Enums.Language;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.FontUtil;
import com.tasree7a.utils.UserDefaultUtil;


/**
 * Created by Khalid Taha on 28/06/2015.
 * Month View Holder for CustomGridCalendar
 */
public class CalendarMonthViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;
    private TextView monthLabel;
    private View upperSpace;
    private View itemView;
    private LinearLayout holidaysList;

    public CalendarMonthViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;

        recyclerView = (RecyclerView) itemView.findViewById(R.id.calendar_grid);
        monthLabel = (TextView) itemView.findViewById(R.id.month_label);
        upperSpace = itemView.findViewById(R.id.upperSpace);
        holidaysList = (LinearLayout) itemView.findViewById(R.id.holidaysList);


        if (recyclerView != null) {

            if (UserDefaultUtil.getUserLanguage()== Language.AR) {
                recyclerView.setLayoutManager(new RtlGridLayoutManager(itemView.getContext(), 7));
            }else{
                recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 7));
            }


        } else {

            monthLabel.setTextColor(ThisApplication.getCurrentActivity().getResources().getColor(R.color.APP_TEXT_COLOR));

            monthLabel.setTypeface(FontUtil.getFont(FontsType.HEAVY));

            monthLabel.setTextSize(15);

        }
    }


    public void setRecyclerView(RecyclerView gridView) {

        this.recyclerView = gridView;
    }

    public void setMonthLabel(TextView monthLabel) {
        this.monthLabel = monthLabel;
    }


    public void setUpperSpace(View upperSpace) {
        this.upperSpace = upperSpace;
    }

    public RecyclerView getRecyclerView() {

        return this.recyclerView;
    }

    public TextView getMonthLabel() {
        return monthLabel;
    }

    public View getUpperSpace() {
        return upperSpace;
    }

    public View getItemView() {
        return itemView;
    }

    public LinearLayout getHolidaysList() {
        return holidaysList;
    }

    public void setHolidaysList(LinearLayout holidaysList) {
        this.holidaysList = holidaysList;
    }
}
