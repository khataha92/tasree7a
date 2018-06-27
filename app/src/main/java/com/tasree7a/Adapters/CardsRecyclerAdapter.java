package com.tasree7a.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tasree7a.enums.CardFactory;
import com.tasree7a.enums.CardType;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.models.BaseCardModel;
import com.tasree7a.viewholders.BaseCardViewHolder;
import com.tasree7a.viewholders.EmptyViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CardsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public WeakReference<CardFactory> cardFactory = new WeakReference<>(null);

    public RecyclerView.ViewHolder stickyHeaderViewHolder;

    protected List<BaseCardViewHolder> viewHolders = new ArrayList<>();

    private AbstractCallback onBindAction;

    public void setCardFactory(CardFactory cardFactory) {
        this.cardFactory =new WeakReference<>(cardFactory);
    }

    public CardsRecyclerAdapter() {


    }

    public RecyclerView.ViewHolder getStickyHeaderViewHolder() {

        return stickyHeaderViewHolder;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return viewHolders.get(viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ((BaseCardViewHolder) holder).cardModel = viewHolders.get(position).cardModel;

        if (!(holder instanceof EmptyViewHolder)) {

            ((BaseCardViewHolder) holder).initializeViewOnUI();

            if (onBindAction != null) onBindAction.onResult(true, holder);

        }

    }

    @Override
    public int getItemCount() {

        return viewHolders.size();//cardFactory.getCardModels().size();

    }

    @Override
    public int getItemViewType(int position) {

        return position;

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ((BaseCardViewHolder) holder).onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        ((BaseCardViewHolder) holder).onViewDetachedFromWindow();

    }

    private BaseCardModel getCardModel(int position) {

        if (cardFactory.get() == null) return null;

        return cardFactory.get().getCardModels().get(position);

    }

    /**
     * If viewHolders contains a view holder with specific card type
     * @param cardType CardType
     * @return boolean
     */
    public boolean containsCardType(CardType cardType) {

        for (BaseCardViewHolder baseCardViewHolder : viewHolders) {

            if (baseCardViewHolder.cardModel.getCardType() == cardType) {

                return true;

            }

        }

        return false;

    }


    public void setViewHolders(List<BaseCardViewHolder> viewHolders) {
        this.viewHolders = viewHolders;
    }

    public List<BaseCardViewHolder> getViewHolders() {
        return viewHolders;
    }

    public void cleanViewHolders(RecyclerView recyclerView) {

        for (int i=0; i < getItemCount() && recyclerView != null; i++) {

            BaseCardViewHolder holder = (BaseCardViewHolder) recyclerView.findViewHolderForLayoutPosition(i);

            if (holder != null && holder.cardModel != null) {

                holder.cardModel.setCardFactory(null);

                holder.cardModel.setCardType(null);

                holder.cardModel.setCardValue(null);

                holder.cardModel.setData(null);

                holder.cardModel = null;

            }

            if (i < viewHolders.size() && viewHolders.get(i) != null && viewHolders.get(i).cardModel != null) {

                viewHolders.get(i).cardModel.setCardFactory(null);

                viewHolders.get(i).cardModel.setCardType(null);

                viewHolders.get(i).cardModel.setCardValue(null);

                viewHolders.get(i).cardModel.setData(null);

            }

        }
    }

    public AbstractCallback getOnBindAction() {
        return onBindAction;
    }

    public void setOnBindAction(AbstractCallback onBindAction) {
        this.onBindAction = onBindAction;
    }
}
