package com.tasree7a.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.ViewHolders.PopularSallonsItemViewHolder;

/**
 * Created by mac on 5/11/17.
 */

public class PopularSallonsAdapter extends RecyclerView.Adapter<PopularSallonsItemViewHolder> {
    @Override
    public PopularSallonsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ThisApplication.getCurrentActivity()).inflate(R.layout.popular_list_item,null);

        return new PopularSallonsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PopularSallonsItemViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.showSalonDetailsFragment();

            }

        });

    }

    @Override
    public int getItemCount() {
        return 9;
    }
}
