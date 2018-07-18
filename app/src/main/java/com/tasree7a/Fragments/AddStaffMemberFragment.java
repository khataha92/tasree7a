package com.tasree7a.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tasree7a.R;
import com.tasree7a.customcomponent.CustomButton;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.managers.FragmentManager;
import com.tasree7a.managers.RetrofitManager;
import com.tasree7a.models.AddNewBarberRequestModel;
import com.tasree7a.models.AddNewStaffMemberDataModel;
import com.tasree7a.models.salondetails.SalonModel;

import static android.view.View.GONE;

public class AddStaffMemberFragment extends BaseFragment {
    private EditText staffName;
    private EditText staffEmail;
    private EditText staffPass;
    private EditText staffPassConfirm;
    private SalonModel salonModel;
    private CustomButton save;
    private CustomButton cancel;
    private AbstractCallback addStaffCallback;
    private AddNewStaffMemberDataModel staffMemberDataModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_add_salon_barber, container, false);

        staffName = rootView.findViewById(R.id.staff_name);
        staffEmail = rootView.findViewById(R.id.email);
        staffPass = rootView.findViewById(R.id.password);
        staffPassConfirm = rootView.findViewById(R.id.confirm);
        save = rootView.findViewById(R.id.save);
        cancel = rootView.findViewById(R.id.cancel);

        save.setOnClickListener(v -> {
            if ((staffPass.getText().toString().length() != 0
                    && staffPassConfirm.getText().toString().length() != 0)
                    && staffPass.getText().toString().equals(staffPassConfirm.getText().toString())) {
                staffMemberDataModel = new AddNewStaffMemberDataModel();
                staffMemberDataModel.setStaffName(staffName.getText().toString());
                staffMemberDataModel.setStaffEmail(staffEmail.getText().toString());
                staffMemberDataModel.setStaffPass(staffPass.getText().toString());
                if (salonModel != null) {
                    AddNewBarberRequestModel barberModel;
                    barberModel = new AddNewBarberRequestModel();
                    barberModel.setSalonId(salonModel.getId());
                    try {
                        barberModel.setLastName(staffName.getText().toString().split(" ")[0]);
                        barberModel.setFirstName(staffName.getText().toString().split(" ")[1]);
                    } catch (Exception ignore) {
                    }

                    barberModel.setEmail(staffMemberDataModel.getStaffEmail());
                    barberModel.setPass(staffMemberDataModel.getStaffPass());
                    barberModel.setCreatedAt("1");
                    barberModel.setStartTime("12");
                    barberModel.setEndTime("15");
                    barberModel.setUpdatedAt("16");
                    barberModel.setUserName(staffName.getText().toString().split(" ")[0]);
                    RetrofitManager.getInstance()
                            .addNewBarber(barberModel, (isSuccess, result) -> {
                            });
                    barberModel = null;
                }
                if (addStaffCallback != null) {
                    addStaffCallback.onResult(true, staffMemberDataModel);
                }
                FragmentManager.popCurrentVisibleFragment();
            }
        });

        if (salonModel != null) {
            cancel.setVisibility(GONE);
        } else {
            cancel.setOnClickListener(v -> FragmentManager.popCurrentVisibleFragment());
        }
        return rootView;
    }

    public void setAddStaffCallback(AbstractCallback addStaffCallback) {
        this.addStaffCallback = addStaffCallback;
    }

    @Override
    public boolean onBackPressed() {
        if (salonModel != null) {
            return false;
        }
        return super.onBackPressed();
    }

    public void setSalonModel(SalonModel salonModel) {
        this.salonModel = salonModel;
    }

}
