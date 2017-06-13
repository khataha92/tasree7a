package com.tasree7a.Models;

import com.tasree7a.Enums.CardFactory;
import com.tasree7a.Enums.CardType;

import java.util.List;

/**
 * Created by mac on 5/9/16.
 */
public class BaseCardModel {

    private CardType cardType;

    private Object cardValue;

    private Object data;

    private CardFactory cardFactory;

    private boolean isStickyCard;

    public BaseCardModel setData(Object data) {

        this.data = data;

        return this;

    }

    public Object getData() {

        return data;

    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof  BaseCardModel){

            BaseCardModel model = (BaseCardModel)o;

            if(model.getCardType() == cardType){

                if(cardValue != null && model.cardValue != null)

                if(cardValue.equals(model.getCardValue())){

                    return true;

                }
                else{

                    return false;

                }

            }
            else {

                return false;

            }

        }

        return super.equals(o);
    }

    public BaseCardModel setCardFactory(CardFactory cardFactory) {

        this.cardFactory = cardFactory;

        return this;
    }

    public CardFactory getCardFactory() {

        return cardFactory;
    }

    public BaseCardModel setCardType(CardType cardType) {

        this.cardType = cardType;

        return this;

    }

    public BaseCardModel setCardValue(Object cardValue) {

        this.cardValue = cardValue;

        return this;

    }

    public CardType getCardType() {

        return cardType;

    }

    public Object getCardValue() {

        return cardValue;

    }

    public boolean isStickyCard() {
        return isStickyCard;
    }

    public void setStickyCard(boolean stickyCard) {
        isStickyCard = stickyCard;
    }

    public static BaseCardModel getCardModelFromList(List<BaseCardModel> models, CardType cardType) {

        for (BaseCardModel model : models) {

            if (model.getCardType().equals(cardType)) {

                return model;

            }

        }

        return null;
    }
}
