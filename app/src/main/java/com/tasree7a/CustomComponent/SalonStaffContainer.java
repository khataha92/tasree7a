package com.tasree7a.CustomComponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tasree7a.Fragments.AddStaffMemberFragment;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Models.AddNewStaffMemberDataModel;
import com.tasree7a.Models.Barber;
import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AbstractCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khalid Taha on 10/16/16.
 */

public class SalonStaffContainer extends LinearLayout {

    List<AddNewStaffMemberDataModel> barbers = new ArrayList<>();

    LinearLayout itemsContainer;

    private Context context;


    public SalonStaffContainer(Context context) {

        super(context);

        init(context);
    }


    public SalonStaffContainer(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }


    public SalonStaffContainer(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

        this.context = context;

        Context appContext = isInEditMode() ? context : ThisApplication.getCurrentActivity();

        LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.add_staff_container, this, true);

        if (isInEditMode()) return;

        itemsContainer = (LinearLayout) rootView.findViewById(R.id.items_container);

        LinearLayout addStaffItem = (LinearLayout) rootView.findViewById(R.id.add_staff);

        addStaffItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager.showAddNewStaffFragment(new AbstractCallback() {

                    @Override
                    public void onResult(boolean isSuccess, Object result) {

                        if (isSuccess && result != null) {

                            AddNewStaffMemberDataModel staffMemberDataModel = (AddNewStaffMemberDataModel) result;

                            addNewItem(staffMemberDataModel);

                        }

                    }
                });

            }
        });


    }


    private void addNewItem(AddNewStaffMemberDataModel staffMemberDataModel) {

        Context appContext = isInEditMode() ? context : ThisApplication.getCurrentActivity();

        LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RelativeLayout barberItem = (RelativeLayout) inflater.inflate(R.layout.staff_item_view, null);

        ((TextView) barberItem.findViewById(R.id.new_item)).setText(staffMemberDataModel.getStaffEmail());

        barberItem.findViewById(R.id.new_item).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO: handle removing items

                Toast.makeText(ThisApplication.getCurrentActivity().getApplicationContext(),
                        "delete it!!",
                        Toast.LENGTH_LONG).show();

            }
        });

        if (barberItem.getParent() != null)
            ((ViewGroup) barberItem.getParent()).removeView(barberItem);

        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.setMargins(25, 15, 25, 0);

        barberItem.setLayoutParams(lp);

        itemsContainer.addView(barberItem, 0);

        barbers.add(staffMemberDataModel);
    }


    public List<AddNewStaffMemberDataModel> getBarbers() {

        return barbers;
    }
}
