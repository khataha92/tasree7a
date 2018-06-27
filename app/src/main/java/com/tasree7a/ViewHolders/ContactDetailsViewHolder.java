package com.tasree7a.viewholders;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.models.BaseCardModel;
import com.tasree7a.models.salondetails.SalonModel;

/**
 * Created by mac on 6/5/17.
 */

public class ContactDetailsViewHolder extends BaseCardViewHolder {

    public ContactDetailsViewHolder(View view, final BaseCardModel cardModel) {

        super(view, cardModel);

        final SalonModel salonModel = (SalonModel) cardModel.getCardValue();

        TextView address = itemView.findViewById(R.id.salon_address);

        TextView phone = itemView.findViewById(R.id.contact_phone);

        address.setText(salonModel.getSalonCity());

        phone.setText(salonModel.getOwnerMobileNumber());

        itemView.setOnClickListener(v -> {

            Intent dialIntent = new Intent(Intent.ACTION_DIAL);

            dialIntent.setData(Uri.parse("tel:"+salonModel.getOwnerMobileNumber()));

            ThisApplication.getCurrentActivity().startActivity(dialIntent);

        });

    }
}
