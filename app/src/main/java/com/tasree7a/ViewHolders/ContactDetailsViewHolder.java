package com.tasree7a.ViewHolders;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.Models.PopularSalons.SalonModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;

/**
 * Created by mac on 6/5/17.
 */

public class ContactDetailsViewHolder extends BaseCardViewHolder {

    public ContactDetailsViewHolder(View view, final BaseCardModel cardModel) {

        super(view, cardModel);

        final SalonModel salonModel = (SalonModel) cardModel.getCardValue();

        TextView address = (TextView) itemView.findViewById(R.id.salon_address);

        TextView phone = (TextView) itemView.findViewById(R.id.contact_phone);

        address.setText(salonModel.getSalonCity());

        phone.setText(salonModel.getOwnerMobileNumber());

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent dialIntent = new Intent(Intent.ACTION_DIAL);

                dialIntent.setData(Uri.parse("tel:"+salonModel.getOwnerMobileNumber()));

                ThisApplication.getCurrentActivity().startActivity(dialIntent);

            }
        });

    }
}
