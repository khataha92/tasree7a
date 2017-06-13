package com.tasree7a.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mac on 6/4/17.
 */

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.tasree7a.Enums.CardType;
import com.tasree7a.Models.BaseCardModel;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.FontUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;


/**
 * Created by Mojahed Dameri on 3/19/16.
 * BaseCardViewHolder the base and generator for all ViewHolders in CardsRecyclerAdapter
 */
public class BaseCardViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = BaseCardViewHolder.class.getSimpleName();

    public BaseCardModel cardModel;

    public BaseCardViewHolder(View view, BaseCardModel cardModel) {

        super(view);

        this.cardModel = cardModel;

    }

    public static Typeface book() {

        return FontUtil.book();

    }

    public static Typeface medium() {

        return FontUtil.medium();

    }

    public static Typeface black() {

        return FontUtil.black();

    }

    public static Typeface heavy() {

        return FontUtil.heavy();

    }

    /**
     * Make an BaseCardViewHolder by run the internal method on a second thread away from main UI thread
     *
     * @param parent    ViewGroup
     * @param cardModel BaseCardModel
     * @return BaseCardViewHolder
     */
    public static BaseCardViewHolder createViewHolder(final ViewGroup parent, final BaseCardModel cardModel) {

        BaseCardViewHolder cardViewHolder = new BaseCardViewHolder(new View(ThisApplication.getCurrentActivity()), new BaseCardModel().setCardType(CardType.EMPTY));

        Future<BaseCardViewHolder> future = ThisApplication.getNonUIThread()
                .submit(new Callable<BaseCardViewHolder>() {

                    @Override
                    public BaseCardViewHolder call() throws Exception {

                        return BaseCardViewHolder.createViewHolderInternal(parent, cardModel);

                    }
                });

        try {

            cardViewHolder = future.get();

        } catch (Throwable t) {

            try {

                String message = "inflating error at " + cardModel.getCardType().name() + " " + t.toString();

                Crashlytics.log(message);

            } catch (Throwable reportThrowable) {

                Crashlytics.logException(reportThrowable);
            }

        }

        return cardViewHolder;


    }

    private static View inflate(int layout, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(ThisApplication.getCurrentActivity());

        View view = inflater.inflate(layout, parent, false);

        return view;

    }

    private static BaseCardViewHolder createViewHolderInternal(ViewGroup parent, BaseCardModel cardModel) {

        View itemView;

        switch (cardModel.getCardType()) {

            case SALON_RATE:

                itemView = inflate(R.layout.card_salon_rate,parent);

                return new SalonRateViewHolder(itemView,cardModel);

            case IMAGE_CARD:

                itemView = inflate(R.layout.card_salon_header,parent);

                return new ImageCardViewHolder(itemView,cardModel);

            case GALARY_CARD:

                itemView = inflate(R.layout.card_salon_galary,parent);

                return  new GalaryCardViewHolder(itemView,cardModel);

            case PRODUCTS_CARD:

                itemView = inflate(R.layout.card_salon_products,parent);

                return new ProductsCardViewHolder(itemView,cardModel);

            case MAP_CARD:

                itemView = inflate(R.layout.card_salon_map,parent);

                return new MapCardViewHolder(itemView,cardModel);


            default:

                itemView = inflate(R.layout.empty_layout, parent);

                EmptyViewHolder emptyViewHolder = new EmptyViewHolder(itemView);

                emptyViewHolder.cardModel = cardModel;

                return emptyViewHolder;

        }
    }

    public void initializeView() {

    }

    /**
     * Here goes the initialization stuff that cannot be done other than main UI thread, will be called in onBind in CardsRecyclerAdapter
     */
    public void initializeViewOnUI() {


    }

    /*
     * Called on onViewAttachedToWindow in CardsAdapter
     */
    public void onViewAttachedToWindow() {


    }

    /*
    * Called on onViewDetachedFromWindow in CardsAdapter
    */
    public void onViewDetachedFromWindow() {


    }


}

