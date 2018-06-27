package com.tasree7a.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tasree7a.models.BaseCardModel;
import com.tasree7a.viewholders.BaseCardViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by KhalidTaha on 2/2/17.
 */

public class BaseCardAdapter extends RecyclerView.Adapter<BaseCardViewHolder> {

    private List<BaseCardModel> cardModels;

    private List<BaseCardViewHolder> viewHolders = new ArrayList<>();


    public BaseCardAdapter(List<BaseCardModel> cardModelList) {

        cardModels = cardModelList;

    }


    @NonNull
    @Override
    public BaseCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BaseCardViewHolder viewHolder = BaseCardViewHolder.createViewHolder(parent, cardModels.get(viewType));

        viewHolders.add(viewHolder);

        return viewHolder;

    }


    @Override
    public int getItemViewType(int position) {

        return position;

    }


    @Override
    public void onBindViewHolder(@NonNull BaseCardViewHolder holder, int position) {

        holder.initializeViewOnUI();

    }


    public void setCardModels(List<BaseCardModel> cardModels) {

        this.cardModels.clear();

        this.cardModels.addAll(cardModels);

        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {

        return cardModels.size();
    }


    public List<BaseCardViewHolder> getViewHolders() {

        return viewHolders;

    }


    @Override
    public void onViewAttachedToWindow(@NonNull BaseCardViewHolder holder) {

        super.onViewAttachedToWindow(holder);

        holder.onViewAttachedToWindow();
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull BaseCardViewHolder holder) {

        super.onViewDetachedFromWindow(holder);

        holder.onViewDetachedFromWindow();

    }

}
