package com.tasree7a.customcomponent;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.adapters.CalendarListAdapter;
import com.tasree7a.adapters.CalendarMonthAdapter;
import com.tasree7a.enums.FontsType;
import com.tasree7a.interfaces.DateCalenderViewListener;
import com.tasree7a.interfaces.RecyclerViewScrollTopListener;
import com.tasree7a.models.calendar.CalendarViewsModel;
import com.tasree7a.utils.FontUtil;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import org.joda.time.LocalDate;

/**
 * Created by Khalid Taha on 2/3/16.
 * CustomGridCalendar is the new calendar to be used starting from v1.5
 */
public class CustomGridCalendar extends RelativeLayout {

    private static final String TAG = CustomGridCalendar.class.getSimpleName();

    private CalendarListAdapter calendarListAdapter;

    private DateCalenderViewListener mOnDateChangeListener;

    private RecyclerViewScrollTopListener recyclerViewScrollTopListener;

    private RecyclerView calenderList;

    private TextView[] txtDays = new TextView[7];

    public CustomGridCalendar(Context context) {
        super(context);

    }

    public CustomGridCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomGridCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomGridCalendar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


    }


    // markerCheckInDate: LocalDate determine a start of days to be marked always as a special date or something
    public void initializeCalender(CalendarViewsModel cModel) {

        inflate(getContext(), R.layout.custom_view_grid_calendar, this);

        //initialize days strip
        for (int i = 1; i <= txtDays.length; i++) {

            txtDays[i - 1] = (TextView) findViewById(UIUtils.getResourceId("d" + i, "id"));

            txtDays[i - 1].setTypeface(FontUtil.getFont(FontsType.BOOK));

            txtDays[i - 1].setText(getResources().getStringArray(R.array.DAYS_APPREVIATIONS)[i - 1]);

            if (UserDefaultUtil.getUserLanguage().name().equalsIgnoreCase("ar")) {

                txtDays[i - 1].setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

            } else {

                txtDays[i - 1].setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

            }

        }

        calenderList = (RecyclerView) findViewById(R.id.calendar_list);

        calenderList.setHasFixedSize(true);

        calenderList.getRecycledViewPool().setMaxRecycledViews(1, 1000);

        calenderList.setItemViewCacheSize(12 + (12 * 7));

        LinearLayoutManagerWithSmoothScroller linearLayoutManagerWithSmoothScroller = new LinearLayoutManagerWithSmoothScroller(ThisApplication.getCurrentActivity());

        linearLayoutManagerWithSmoothScroller.setMillisecondsPerInch(1);

        calenderList.setLayoutManager(linearLayoutManagerWithSmoothScroller);

        calenderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (recyclerViewScrollTopListener == null)
                    return;

                //Tocheck if  recycler is on top
                if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0) {

                    recyclerViewScrollTopListener.recyclerIsTop(true);

                } else {

                    recyclerViewScrollTopListener.recyclerIsTop(false);

                }

            }
        });


        calendarListAdapter = new CalendarListAdapter(cModel);

        calendarListAdapter.setDateChangeListener(new DateCalenderViewListener() {
            @Override
            public void onDateChanged(LocalDate chkIn) {

                mOnDateChangeListener.onDateChanged(chkIn);
            }

            @Override
            public void onError(String error) {

                mOnDateChangeListener.onError(error);
            }
        });

        calenderList.setAdapter(calendarListAdapter);

        calenderList.postDelayed(new Runnable() {
            @Override
            public void run() {
                makeTheCheckInVisible();
            }
        }, 100);

    }


    public void scrollToPosition(final int position) {

        calenderList.post(new Runnable() {
            @Override
            public void run() {
                calenderList.getLayoutManager().smoothScrollToPosition(calenderList, null, position);
            }
        });
    }

    public LocalDate getCheckInDate() {

        if (calendarListAdapter == null) return null;

        return calendarListAdapter.getCheckInDate();
    }

    public void setCheckInDate(LocalDate date) {

        if (calendarListAdapter != null)
            calendarListAdapter.setCheckInDate(date);
    }

    public LocalDate getMaxDayInCalender() {

        if (calendarListAdapter != null) {
            return calendarListAdapter.getMaxDayInCalender();
        }

        return null;
    }


    /**
     * Set the callback that will handel date changing
     *
     * @param listener The date change listener
     */
    public void setDateChangeListener(final DateCalenderViewListener listener) {

        mOnDateChangeListener = listener;

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//        if(!generateHeightsOnce && parentWidth > 0 && calendarListAdapter != null){
//
//            int height = (int)( (float)parentWidth / 6 );
//
//            calendarListAdapter.setHeights(height);
//
//            generateHeightsOnce = true;
//        }
//
//        this.setMeasuredDimension(parentWidth, parentHeight);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }


    /**
     * If the check in date is not visible, scroll to it instead of seeing greyed-out cells
     */
    private void makeTheCheckInVisible() {

        int sum = 0;

        LocalDate intervalStart = calendarListAdapter.getCheckInDate() != null ?
                calendarListAdapter.getCheckInDate() : calendarListAdapter.getMarkerCheckInDate();

        if (intervalStart == null) {

            return;

        }

        for (CalendarMonthAdapter adapter : calendarListAdapter.getGridsAdapters()) {

            if (adapter == null) {

                continue;

            } else if (adapter.getStartDay().getDayOfMonth() != 1) {

                continue;

            } else if (adapter.getStartDay().isAfter(intervalStart)) {

                break;

            }

            int daysWithOffset;

            if (adapter.getStartDay().getMonthOfYear() == intervalStart.getMonthOfYear() &&
                    adapter.getStartDay().getYear() == intervalStart.getYear()) {

                daysWithOffset = adapter.getIndexOfMonth() + intervalStart.getDayOfMonth();

            } else {

                daysWithOffset = adapter.getIndexOfMonth() + adapter.getStartDay().dayOfMonth().getMaximumValue();

            }


            int numberOfRows = (int) Math.ceil((double) daysWithOffset / 7.0);

            sum = sum + numberOfRows + 1; //extra one for the empty label

        }

        int lastRowVisible = ((LinearLayoutManager) calenderList.getLayoutManager()).findLastCompletelyVisibleItemPosition();

        if (sum >= calendarListAdapter.getItemCount()) {

            calenderList.getLayoutManager().scrollToPosition(calendarListAdapter.getItemCount() - 1);

        } else if (sum > lastRowVisible) {

            calenderList.getLayoutManager().scrollToPosition(sum);

        }
    }


    public void setRecyclerViewScrollTopListener(RecyclerViewScrollTopListener recyclerViewScrollTopListener) {
        this.recyclerViewScrollTopListener = recyclerViewScrollTopListener;
    }
}