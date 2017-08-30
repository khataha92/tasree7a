package com.tasree7a.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tasree7a.R;

/**
 * Created by mac on 8/27/17.
 */

public class SalonServiceViewHolder extends RecyclerView.ViewHolder {

    public SalonServiceViewHolder(final View itemView) {

        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(itemView.findViewById(R.id.green_circle).getVisibility() == View.GONE) {

                    itemView.findViewById(R.id.green_circle).setVisibility(View.VISIBLE);

                } else{

                    itemView.findViewById(R.id.green_circle).setVisibility(View.GONE);
                }

            }
        });

    }


}
