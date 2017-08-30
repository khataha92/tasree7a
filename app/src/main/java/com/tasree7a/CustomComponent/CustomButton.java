package com.tasree7a.CustomComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.tasree7a.Enums.FontsType;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.utils.FontUtil;
import com.tasree7a.utils.UIUtils;

/**
 * Created by Khalid Taha on 7/31/16.
 * Custom button that can be customized with ease of use and customizable
 */

public class CustomButton extends LinearLayout {

    private ImageView buttonIcon;

    private TextView buttonText;

    private int icon;

    private int radius = 0;


    public CustomButton(Context context) {

        super(context);

        init(context, null);
    }


    public CustomButton(Context context, AttributeSet attrs) {

        super(context, attrs);

        init(context, attrs);
    }


    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        setGravity(Gravity.CENTER);

        Context appContext = isInEditMode() ? context : ThisApplication.getCurrentActivity();

        LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.view_custom_button, this, true);

        if (isInEditMode()) return;

        buttonText = (TextView) rootView.findViewById(R.id.button_tv);

        buttonIcon = (ImageView) rootView.findViewById(R.id.button_iv);


        // Set attributes from xml if exists
        if (attrs != null) {

            TypedArray a = ThisApplication.getCurrentActivity().obtainStyledAttributes(attrs, R.styleable.CustomButton);

            int backgroundColor = 0, strokeColor = 0;

            for (int i = 0; i < a.getIndexCount(); ++i) {

                int attr = a.getIndex(i);

                switch (attr) {

                    case R.styleable.CustomButton_customButtonText:

                        setText(a.getString(attr));

                        break;

                    case R.styleable.CustomButton_customButtonTextSize:

                        setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(attr, UIUtils.dpToPx(15)));

                        break;

                    case R.styleable.CustomButton_customButtonTextColor:

                        setTextColor(a.getColor(attr, ThisApplication.getCurrentActivity().getResources().getColor(R.color.BLACK)));

                        break;

                    case R.styleable.CustomButton_customButtonBackgroundColor:

                        backgroundColor = a.getColor(attr, 0);

//                        setBackgroundColor(backgroundColor);

                        break;

                    case R.styleable.CustomButton_customButtonBackgroundDrawable:

                        setBackground(a.getDrawable(attr));

                        break;

                    case R.styleable.CustomButton_customButtonStrokeColor:

                        strokeColor = a.getColor(attr, 0);

                        break;

                    case R.styleable.CustomButton_customButtonImageSrc:

                        setIcon(a.getResourceId(attr, 0));

                        break;

                    case R.styleable.CustomButton_customFonts:

                        setTextTypeFace(FontsType.valueOf(a.getInteger(attr, 0)));

                        break;

                    case R.styleable.CustomButton_customButtonRadius:

                        radius = a.getInt(attr, 0);

                        break;


                }

            }

//            setButtonBackgroundAndStrock(backgroundColor, strokeColor, GradientDrawable.RECTANGLE, radius);

            a.recycle();

        }

    }


    private void setButtonBackgroundAndStrock(int bg, int strock, int shape, int radius) {

        UIUtils.generateShapeBackground(bg, strock, shape, radius);

    }


    public void setText(String string) {

        buttonText.setText(string);

    }


    /**
     * Text size in sp
     *
     * @param textSize in text size in sp
     */
    public void setTextSize(float textSize) {

        buttonText.setTextSize(textSize);

    }


    public void setText(SpannableString spannableString) {

        buttonText.setText(spannableString);
    }


    /**
     * Text size in specific unit
     *
     * @param textSize value in unit that you specified
     * @param unit     int represents unit from ex: TypedValue.COMPLEX_UNIT_PX, TypedValue.COMPLEX_UNIT_SP
     */
    public void setTextSize(int unit, float textSize) {

        buttonText.setTextSize(unit, textSize);

    }


    /**
     * Button icon will be by default gone, if the drawable resulted from the resource
     * is also null, it will be gone, else visible
     *
     * @param drawableId int drawable resource
     */
    public void setIcon(int drawableId) {

        icon = drawableId;

        Drawable imageDrawable = null;

        try {

            imageDrawable = ContextCompat.getDrawable(ThisApplication.getCurrentActivity(), drawableId);

        } catch (Throwable t) {

        }

        buttonIcon.setVisibility(imageDrawable == null ? GONE : VISIBLE);

        buttonIcon.setImageDrawable(imageDrawable);

    }


    public void setTextTypeFace(FontsType fontType) {

        buttonText.setTypeface(FontUtil.getFont(fontType));

    }


    public void setTextColor(int colorResource) {

        buttonText.setTextColor(colorResource);

    }


    public void setButtonEnabled(boolean buttonEnabled) {

        setEnabled(buttonEnabled);

        if (buttonEnabled) {

            buttonText.getPaint().setColorFilter(null);

            getBackground().clearColorFilter();

        } else {

            int geryOutColor = UIUtils.changeAlpha(UIUtils.getColor(R.color.WHITE), 0.5f);

            for (Drawable drawable : buttonText.getCompoundDrawables()) {

                if (drawable != null) {

                    drawable.setColorFilter(new PorterDuffColorFilter(geryOutColor, PorterDuff.Mode.MULTIPLY));

                }
            }

            getBackground().setColorFilter(geryOutColor, PorterDuff.Mode.MULTIPLY);

        }

    }


    // To prevent multilple clicks in a hort time
    private long previousClickTime = System.currentTimeMillis();


    @Override
    public void setOnClickListener(final OnClickListener onClickListener) {

        OnClickListener incubatingOnClick = new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (onClickListener == null) return;

                long nowClickTime = System.currentTimeMillis();

                if (nowClickTime - previousClickTime > 500) { // time between first click and second click more than half sec

                    onClickListener.onClick(v);

                    previousClickTime = nowClickTime;

                }

            }


            ;
        };
        super.setOnClickListener(incubatingOnClick);

    }


    public int getIcon() {

        return icon;
    }

    public void setButtonAppearance(@StyleRes int resId) {

        TypedArray typedArray = ThisApplication.getCurrentActivity().obtainStyledAttributes(resId, R.styleable.CustomButton);

        extractAttrs(typedArray);

    }

    private void extractAttrs(TypedArray a) {

        if (a != null) {

            int backgroundColor = 0, strokeColor = 0;

            int radius = 4;

            for (int i = 0; i < a.getIndexCount(); ++i) {

                int attr = a.getIndex(i);

                switch (attr) {

                    case R.styleable.CustomButton_customButtonText:

                        setText(a.getString(attr));

                        break;

                    case R.styleable.CustomButton_customButtonTextSize:

                        setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimensionPixelSize(attr, UIUtils.dpToPx(15)));

                        break;

                    case R.styleable.CustomButton_customButtonBackgroundColor:

                        backgroundColor = a.getColor(attr, 0);

                        break;

                    case R.styleable.CustomButton_customButtonStrokeColor:

                        strokeColor = a.getColor(attr, 0);

                        break;

                    case R.styleable.CustomButton_customButtonImageSrc:

                        setIcon(a.getResourceId(attr, 0));

                        break;

                    case R.styleable.CustomButton_radius:

                        radius = a.getInt(attr, 0);

                        break;
                }

            }

            a.recycle();

        }


    }


    public void setButtonHeight(int val) {

        ((RelativeLayout) findViewById(R.id.button_container)).getLayoutParams().height = val;

    }


    public void setButtonLineSpacing() {

        buttonText.setLineSpacing(0.3f, 1.0f);

    }


}
