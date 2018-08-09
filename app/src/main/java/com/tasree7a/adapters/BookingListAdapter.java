package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tasree7a.R;
import com.tasree7a.interfaces.OnBookingStatusChangedListener;
import com.tasree7a.models.bookings.BookingModel;
import com.tasree7a.viewholders.BookingItemViewHolder;

import java.util.List;

public class BookingListAdapter extends RecyclerView.Adapter<BookingItemViewHolder> {

    private List<BookingModel> mBookingList;
    private OnBookingStatusChangedListener mOnBookingStatusChangedListener;

    public BookingListAdapter(List<BookingModel> bookingModelsList, OnBookingStatusChangedListener onBookingStatusChangedListener) {
        mBookingList = bookingModelsList;
        mOnBookingStatusChangedListener = onBookingStatusChangedListener;
    }

    @NonNull
    @Override
    public BookingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_booking_item, parent, false), mOnBookingStatusChangedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingItemViewHolder holder, int position) {
        holder.bind(mBookingList.get(position));
    }

    @Override
    public int getItemCount() {
        return mBookingList != null ? mBookingList.size() : 0;
    }
}
