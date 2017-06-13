package com.tasree7a.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.ViewHolders.BaseCardViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by KhalidTaha on 2/2/17.
 */

public class BaseCardAdapter extends RecyclerView.Adapter<BaseCardViewHolder>{

    List<BaseCardModel> cardModels;

    List<BaseCardViewHolder> viewHolders = new ArrayList<>();

    public BaseCardAdapter(List<BaseCardModel> cardModelList){

        cardModels = cardModelList;

    }

    @Override
    public BaseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        BaseCardViewHolder viewHolder = BaseCardViewHolder.createViewHolder(parent,cardModels.get(viewType));

        viewHolders.add(viewHolder);

        return viewHolder;

    }

    @Override
    public int getItemViewType(int position) {

        return position;

    }

    @Override
    public void onBindViewHolder(BaseCardViewHolder holder, int position) {

        holder.initializeViewOnUI();

    }

    @Override
    public int getItemCount() {
        return cardModels.size();
    }

    public List<BaseCardViewHolder> getViewHolders() {

        return viewHolders;

    }

    @Override
    public void onViewAttachedToWindow(BaseCardViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(BaseCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.onViewDetachedFromWindow();

    }

}
