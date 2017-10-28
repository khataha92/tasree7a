package com.tasree7a.ViewHolders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tasree7a.Adapters.BookingServicesAdapter;
import com.tasree7a.Enums.FontsType;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.Bookings.BookingModel;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.CalenderCellClickListener;
import com.tasree7a.utils.FontUtil;

import org.joda.time.LocalDate;

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

        ImageView location = (ImageView) itemView.findViewById(R.id.salon_location);

        ImageView callSalon = (ImageView) itemView.findViewById(R.id.salon_phone);

        TextView bookingId = (TextView) itemView.findViewById(R.id.booking_id);

        bookingId.setText(model.getBookingId());

        TextView salonName = (TextView) itemView.findViewById(R.id.salon_name);

        salonName.setText(model.getSalonName());

        TextView bookingTime = (TextView) itemView.findViewById(R.id.booking_time);

        bookingTime.setText(model.getBookingTime());

        TextView salonAddress = (TextView) itemView.findViewById(R.id.salon_address);

        salonAddress.setText(model.getSalonAddress());

        LinearLayout services = (LinearLayout) itemView.findViewById(R.id.booking_services);

        for (int i = 0; i < model.getBookingServiceList().size(); i++) {

            View v = ThisApplication.getCurrentActivity().getLayoutInflater().inflate(R.layout.booking_service_item, services);

            TextView serviceName = (TextView) v.findViewById(R.id.service_name);

            TextView cost = (TextView) v.findViewById(R.id.item_cost);

            serviceName.setText(model.getBookingServiceList().get(i).getServiceName());

            cost.setText("$" + model.getBookingServiceList().get(i).getCost());

        }


        location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                List<SalonModel> models = new ArrayList<>();

                models.add(model.getSalon());

                FragmentManager.showMapViewFragment(models);

            }
        });

        callSalon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

            }
        });

    }

}
