package com.tasree7a.customcomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.tasree7a.R;
import com.tasree7a.enums.Language;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

/**
 * Created by mac on 5/18/17.
 */

public class CustomRatingBar extends LinearLayout {

    float rating = 0;

    int width = 13;

    public CustomRatingBar(Context context) {

        super(context);

        init(null);

    }

    public CustomRatingBar(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        init(attrs);

    }

    public CustomRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init(attrs);

    }

    private void init(AttributeSet attrs) {

        setOrientation(HORIZONTAL);

        if (attrs != null) {

            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomRatingBar);

            for (int i = 0; i < array.getIndexCount(); ++i) {

                int attr = array.getIndex(i);

                switch (attr) {

                    case R.styleable.CustomRatingBar_rating:

                        float rating = array.getFloat(attr, 0);

                        setRating(rating);

                        break;

                }

            }
        }

    }

    public void setRating(float rating) {

        this.rating = rating;

        removeAllViews();

        for (int i = 0; i < (int) rating; i++) {

            addStar(i == 0);

        }

        if ((int) rating < rating) { // rating is with fractions

            addSubStart();
        }

        for (int i = (int) rating + 1; i <= 5; i++) {

            addEmptyStar();
        }

    }

    private void addStar(boolean isFirstItem) {

        ImageView star = new ImageView(getContext());

        int width = UIUtils.dpToPx(this.width);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);

        if (!isFirstItem) {

            int margin = UIUtils.dpToPx(4);

            if (UserDefaultUtil.getUserLanguage(getContext()) == Language.AR) {

                params.setMargins(0, 0, margin, 0);

            } else {

                params.setMargins(margin, 0, 0, 0);

            }
        }

        star.setLayoutParams(params);

        star.setImageResource(R.drawable.ic_star_checked);

        addView(star);

    }

    private void addEmptyStar() {

        ImageView star = new ImageView(getContext());

        int width = UIUtils.dpToPx(this.width);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);

        int margin = UIUtils.dpToPx(4);

        if (UserDefaultUtil.getUserLanguage(getContext()) == Language.AR) {

            params.setMargins(0, 0, margin, 0);

        } else {

            params.setMargins(margin, 0, 0, 0);

        }

        star.setLayoutParams(params);

        star.setImageResource(R.drawable.ic_star_unchecked);

        addView(star);
    }

    private void addSubStart() {


    }
}
