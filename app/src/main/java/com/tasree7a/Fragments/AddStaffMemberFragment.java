package com.tasree7a.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tasree7a.CustomComponent.CustomButton;
import com.tasree7a.Managers.FragmentManager;
import com.tasree7a.Managers.RetrofitManager;
import com.tasree7a.Models.AddNewBarberRequestModel;
import com.tasree7a.Models.AddNewStaffMemberDataModel;
import com.tasree7a.Models.Login.User;
import com.tasree7a.Models.SalonDetails.SalonModel;
import com.tasree7a.R;
import com.tasree7a.interfaces.AbstractCallback;
import com.tasree7a.utils.UserDefaultUtil;

import static android.view.View.GONE;

/**
 * Created by SamiKhleaf on 10/20/17.
 */

public class AddStaffMemberFragment extends BaseFragment {

    //Data Views:
    private EditText staffName;

    private EditText staffEmail;

    private EditText staffPass;

    private EditText staffPassConfirm;

    private SalonModel salonModel;

    //Buttons
    private CustomButton save;

    private CustomButton cancel;

    //Callback to communicate with the container
    private AbstractCallback addStaffCallback;

    private AddNewStaffMemberDataModel staffMemberDataModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.add_staff, container, false);

        staffName = (EditText) rootView.findViewById(R.id.staff_name);
        staffEmail = (EditText) rootView.findViewById(R.id.email);
        staffPass = (EditText) rootView.findViewById(R.id.password);
        staffPassConfirm = (EditText) rootView.findViewById(R.id.confirm);

        save = (CustomButton) rootView.findViewById(R.id.save);
        cancel = (CustomButton) rootView.findViewById(R.id.cancel);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

                        barberModel.setLastName(staffName.getText().toString().split(" ")[0]);

                        barberModel.setFirstName(staffName.getText().toString().split(" ")[1]);

                        barberModel.setEmail(staffMemberDataModel.getStaffEmail());

                        barberModel.setPass(staffMemberDataModel.getStaffPass());

                        barberModel.setCreatedAt("1");

                        barberModel.setStartTime("12");

                        barberModel.setEndTime("15");

                        barberModel.setUpdatedAt("16");

                        barberModel.setUserName(staffName.getText().toString().split(" ")[0]);

                        RetrofitManager.getInstance().addNewBarber(barberModel, new AbstractCallback() {

                            @Override
                            public void onResult(boolean isSuccess, Object result) {

                            }
                        });

                        barberModel = null;
                    }


                    if (addStaffCallback != null) {

                        addStaffCallback.onResult(true, staffMemberDataModel);

                    }

                    FragmentManager.popCurrentVisibleFragment();
                }

            }
        });

        if (salonModel != null) {

            cancel.setVisibility(GONE);

        } else {

            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    FragmentManager.popCurrentVisibleFragment();
                }
            });
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
