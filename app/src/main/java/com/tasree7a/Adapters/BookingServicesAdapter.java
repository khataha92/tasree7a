package com.tasree7a.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tasree7a.Models.Bookings.BookingServiceModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 8/31/17.
 */

public class BookingServicesAdapter extends BaseAdapter {

    List<BookingServiceModel> bookingServiceList = new ArrayList<>();

    public BookingServicesAdapter(List<BookingServiceModel> serviceModels){

        this.bookingServiceList = serviceModels;
    }

    @Override
    public int getCount() {

        return bookingServiceList.size();

    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = ThisApplication.getCurrentActivity().getLayoutInflater().inflate(R.layout.booking_service_item, null);

        TextView serviceName = (TextView) view.findViewById(R.id.service_name);

        TextView cost = (TextView) view.findViewById(R.id.item_cost);

        serviceName.setText(bookingServiceList.get(position).getServiceName());

        cost.setText("$" + bookingServiceList.get(position).getCost());

        return view;

    }
}
