package com.tasree7a.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.fragments.HomeFragment;
import com.tasree7a.interfaces.ScrollListener;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.models.salondetails.SalonModel;
import com.tasree7a.utils.UserDefaultUtil;
import com.tasree7a.viewholders.PopularSalonsItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PopularSalonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LOADER_VIEW_TYPE = 0;
    private static final int SALON_VIEW_TYPE = 1;

    private boolean mHasMore;
    private List<SalonModel> salonModels;
    private Context mContext;
    private ScrollListener mScrollListener;

    public PopularSalonsAdapter(Context context, List<SalonModel> salons, ScrollListener scrollListener) {
        mContext = context;
        mScrollListener = scrollListener;
        salonModels = salons;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOADER_VIEW_TYPE) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loader, parent, false)) {
            };
        } else {
            return new PopularSalonsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_list_item, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        Log.d("View_Type", "viewType_" + viewType);

        if (viewType == LOADER_VIEW_TYPE) {
            mScrollListener.onReachedEnd();

        } else {
            ((PopularSalonsItemViewHolder) holder).imageView.setOnClickListener(v -> FragmentManager.showSalonDetailsFragment(mContext, salonModels.get(position)));
            ((PopularSalonsItemViewHolder) holder).favorite.setImageResource(UserDefaultUtil.isSalonFavorite(salonModels.get(position))
                    ? R.drawable.ic_favorite_checked
                    : R.drawable.ic_favorite_unchecked);
            ((PopularSalonsItemViewHolder) holder).favorite.setOnClickListener(v -> {
                if (UserDefaultUtil.isSalonFavorite(salonModels.get(position))) {
                    ((PopularSalonsItemViewHolder) holder).favorite.setImageResource(R.drawable.ic_favorite_unchecked);
                    UserDefaultUtil.removeSalonFromFavorite(salonModels.get(position));
                } else {
                    ((PopularSalonsItemViewHolder) holder).favorite.setImageResource(R.drawable.ic_favorite_checked);
                    UserDefaultUtil.addSalonToFavorite(salonModels.get(position));
                }
            });

            if (UserDefaultUtil.isSalonFavorite(salonModels.get(position))) {
                ((PopularSalonsItemViewHolder) holder).favorite.setImageResource(R.drawable.ic_favorite_checked);
            } else {
                ((PopularSalonsItemViewHolder) holder).favorite.setImageResource(R.drawable.ic_favorite_unchecked);
            }

            try {
                ((PopularSalonsItemViewHolder) holder).ratingBar.setRating(salonModels.get(position).getRating());
                ((PopularSalonsItemViewHolder) holder).city.setText(salonModels.get(position).getSalonCity());
                ((PopularSalonsItemViewHolder) holder).sallonName.setText(salonModels.get(position).getName() + ",");

                Picasso.with(mContext)
                        .load(salonModels.get(position).getImage()).into(((PopularSalonsItemViewHolder) holder).imageView);
            } catch (Exception e) {
                Log.e("CRASH", "CRASH: ", e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return salonModels == null || salonModels.size() == 0 ? 0 : salonModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("POS_I_TION", "pos_" + position + "     size_" + salonModels.size());
        if (mHasMore && (position + 1) == salonModels.size()) {
            return LOADER_VIEW_TYPE;
        } else {
            return SALON_VIEW_TYPE;
        }
    }

    public void setSalonModels(List<SalonModel> salonModels) {
        this.salonModels.clear();
        this.salonModels.addAll(salonModels);
        this.notifyDataSetChanged();
    }

    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
    }
}
