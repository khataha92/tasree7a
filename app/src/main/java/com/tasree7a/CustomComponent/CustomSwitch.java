package com.tasree7a.CustomComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.tasree7a.Enums.Language;
import com.tasree7a.R;
import com.tasree7a.utils.UIUtils;
import com.tasree7a.utils.UserDefaultUtil;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by mac on 5/17/17.
 */

public class CustomSwitch extends LinearLayout {

    private boolean isChecked;

    private Runnable action;


    public CustomSwitch(Context context) {

        super(context);

        init(null);

    }


    public CustomSwitch(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        init(attrs);

    }


    public CustomSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init(attrs);

    }


    private void init(AttributeSet attrs) {

        setOrientation(HORIZONTAL);

        View view = new View(getContext());

        int diameter = UIUtils.dpToPx(10);

        LinearLayout.LayoutParams params = new LayoutParams(diameter, diameter);

        view.setLayoutParams(params);

        view.setBackgroundResource(R.drawable.switch_style_thumb);

        addView(view);

        int padding = UIUtils.dpToPx(2);

        setPadding(padding, padding, padding, padding);

        setBackgroundResource(R.drawable.switch_light_style);

        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

//                setChecked(!isChecked);

                if (action != null) {

                    action.run();

                }

            }
        });

        if (attrs != null) {

            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSwitch);

            for (int i = 0; i < array.getIndexCount(); ++i) {

                int attr = array.getIndex(i);

                switch (attr) {

                    case R.styleable.CustomSwitch_checked:

                        boolean isChecked = array.getBoolean(attr, false);

                        setChecked(isChecked);

                        break;

                    case R.styleable.CustomSwitch_light:

                        boolean light = array.getBoolean(attr, true);

                        if (!light) {

                            int reference = array.getResourceId(attr, R.drawable.switch_dark_style);

                            setBackgroundResource(reference);
                        }

                        break;

                    case R.styleable.CustomSwitch_stroke:

                        int reference = array.getResourceId(attr, R.drawable.switch_light_style);

                        //TODO: Fix it ... needs to be borders not BG
                        setBackgroundResource(reference);

                        break;
                }
            }

            array.recycle();
        }

    }


    public boolean isChecked() {

        return isChecked;

    }


    public Runnable getAction() {

        return action;
    }


    public void setAction(Runnable action) {

        this.action = action;
    }


    public void setChecked(boolean isChecked) {

        this.isChecked = isChecked;

        if (UserDefaultUtil.isAppLanguageArabic()) {

            if (isChecked) {

                setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            } else {

                setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }

        } else {

            if (isChecked) {

                setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            } else {

                setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            }

        }

    }
}
