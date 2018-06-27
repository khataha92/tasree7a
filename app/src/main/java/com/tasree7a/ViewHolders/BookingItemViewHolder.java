package com.tasree7a.viewholders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.enums.BookingStatus;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.BaseCardModel;
import com.tasree7a.models.bookings.BookingModel;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.observables.BookingStatusChangedObservable;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

/**
 * Created by Khalid Taha on 2/9/16.
 * Day View Holder for CustomGridCalendar
 */
public class BookingItemViewHolder extends BaseCardViewHolder {

    private static final int REQUEST_CALL_PERMISSION = 1111;

    public BookingItemViewHolder(View view, BaseCardModel cardModel) {

        super(view, cardModel);

        final BookingModel model = (BookingModel) cardModel.getCardValue();

        ImageView location = itemView.findViewById(R.id.salon_location);

        ImageView callSalon = itemView.findViewById(R.id.salon_phone);

        final TextView bookingId = itemView.findViewById(R.id.booking_id);

        bookingId.setText(model.getBookingId());

        TextView salonName = itemView.findViewById(R.id.salon_name);

        salonName.setText(model.getSalonName());

        TextView bookingTime = itemView.findViewById(R.id.booking_time);

        bookingTime.setText(model.getBookingTime());

        TextView salonAddress = itemView.findViewById(R.id.salon_address);

        salonAddress.setText(model.getSalonAddress());

        LinearLayout services = itemView.findViewById(R.id.booking_services);

        for (int i = 0; i < model.getBookingServiceList().size(); i++) {

            View v = ThisApplication.getCurrentActivity().getLayoutInflater().inflate(R.layout.booking_service_item, services);

            TextView serviceName = v.findViewById(R.id.service_name);

            TextView cost = v.findViewById(R.id.item_cost);

            serviceName.setText(model.getBookingServiceList().get(i).getServiceName());

            cost.setText("$" + model.getBookingServiceList().get(i).getCost());

        }

        itemView.findViewById(R.id.cancel_booking).setOnClickListener(v -> RetrofitManager.getInstance().updateBookingStatus(model.getBookingId(), UserDefaultUtil.isBusinessUser() ? BookingStatus.CANCELED_BY_BARBER : BookingStatus.CANCELED_BY_USER, new AbstractCallback() {
            @Override
            public void onResult(boolean isSuccess, Object result) {
                Log.d("RESPONCE_SUCCESS", result.toString());
                BookingStatusChangedObservable.sharedInstance().setStatusChanged(model);
            }
        }));

        if (model.getBookStatus() == BookingStatus.CANCELED_BY_BARBER.value
                || model.getBookStatus() == BookingStatus.CANCELED_BY_USER.value) {
            itemView.findViewById(R.id.badge).setBackgroundColor(itemView.getContext().getResources().getColor(R.color.red_btn_bg_color));
        }

        location.setOnClickListener(v -> {

            List<SalonModel> models = new ArrayList<>();

            models.add(model.getSalon());

            FragmentManager.showMapViewFragment(models);

        });

        callSalon.setOnClickListener(v -> {

            if (ActivityCompat.checkSelfPermission(ThisApplication.getCurrentActivity().getApplicationContext(),
                    CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                Intent callIntent = new Intent(Intent.ACTION_CALL);

                callIntent.setData(Uri.parse("tel:" + model.getSalon().getOwnerMobileNumber()));

                ThisApplication.getCurrentActivity().startActivity(callIntent);

            } else {

                ActivityCompat.requestPermissions(ThisApplication.getCurrentActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CALL_PERMISSION);

                if (ActivityCompat.checkSelfPermission(ThisApplication.getCurrentActivity().getApplicationContext(),
                        CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);

                    callIntent.setData(Uri.parse("tel:" + model.getSalon().getOwnerMobileNumber()));

                    ThisApplication.getCurrentActivity().startActivity(callIntent);


                }
            }

        });

    }

}
