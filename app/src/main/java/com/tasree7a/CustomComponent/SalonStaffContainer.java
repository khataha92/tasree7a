package com.tasree7a.customcomponent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.interfaces.AddBarberClickListener;
import com.tasree7a.models.AddNewBarberRequestModel;
import com.tasree7a.models.AddNewStaffMemberDataModel;
import com.tasree7a.models.salondetails.SalonBarber;
import com.tasree7a.utils.UserDefaultUtil;

import java.util.ArrayList;
import java.util.List;

public class SalonStaffContainer extends LinearLayout {

    private List<AddNewBarberRequestModel> barbers = new ArrayList<>();
    private List<SalonBarber> salonBarbers = new ArrayList<>();
    private LinearLayout itemsContainer;
    private AddBarberClickListener mAddNewBarberClicked;

    public SalonStaffContainer(Context context) {
        super(context);
        init();
    }

    public SalonStaffContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.add_staff_container, this, true);
        if (isInEditMode()) return;
        itemsContainer = rootView.findViewById(R.id.items_container);
        salonBarbers = UserDefaultUtil.getCurrentSalonUser() != null ? UserDefaultUtil.getCurrentSalonUser().getSalonBarbers() : salonBarbers;
        if (salonBarbers.size() != 0) {
            preFillBarbers(salonBarbers);
        }

        LinearLayout addStaffItem = rootView.findViewById(R.id.add_staff);
        addStaffItem.setOnClickListener(v -> mAddNewBarberClicked.onAddNewBarberClicked());
    }

    public void preFillBarbers(List<SalonBarber> barbersList) {
        AddNewBarberRequestModel temp = new AddNewBarberRequestModel();
        for (SalonBarber barber : barbersList) {
            temp.setFirstName(barber.getBarberFirstName());
            temp.setLastName(barber.getBarberLastName());
            temp.setEmail(barber.getEmail());
            addNewBarber(temp);
        }
    }

    public void addNewBarber(AddNewBarberRequestModel staffMemberDataModel) {
        RelativeLayout barberItem = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.staff_item_view, this, false);
        ((TextView) barberItem.findViewById(R.id.new_item)).setText(staffMemberDataModel.getEmail());
        barberItem.findViewById(R.id.new_item).setOnClickListener(v -> {
            //TODO: handle removing items
            Toast.makeText(ThisApplication.getCurrentActivity().getApplicationContext(),
                    "delete it!!",
                    Toast.LENGTH_LONG).show();
        });

        if (barberItem.getParent() != null)
            ((ViewGroup) barberItem.getParent()).removeView(barberItem);
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(25, 15, 25, 0);
        barberItem.setLayoutParams(lp);
        itemsContainer.addView(barberItem, 0);
        barbers.add(staffMemberDataModel);
    }

    public List<AddNewBarberRequestModel> getBarbers() {
        return barbers;
    }

    public void setOnAddNewBarberClicked(AddBarberClickListener addNewBarberClicked) {
        this.mAddNewBarberClicked = addNewBarberClicked;
    }
}
