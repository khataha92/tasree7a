package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tasree7a.CustomComponent.CustomSwitch;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.R;
import com.tasree7a.interfaces.AbstractCallback;

import org.joda.time.LocalDate;

/**
 * Created by Mohammad Krm on 8/19/2017.
 */

public class FragmentFeedBack extends BaseFragment {

    CustomSwitch langSwitch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
//view_book_schedule
/*        rootView.findViewById(R.id.select_checkin_date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                FragmentManager.showCalendarFragment(new AbstractCallback(){

                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                        TextView textView = (TextView) v;

                        LocalDate date = (LocalDate) result;

                        textView.setText(date.toString());

                    }
                });

            }
        });

        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.popCurrentVisibleFragment();

            }
        });

*/
        return rootView;
    
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }
}
