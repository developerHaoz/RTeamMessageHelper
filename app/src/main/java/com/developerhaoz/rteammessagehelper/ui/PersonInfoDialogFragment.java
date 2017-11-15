package com.developerhaoz.rteammessagehelper.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.developerhaoz.rteammessagehelper.R;
import com.developerhaoz.rteammessagehelper.bean.ContactBean;

/**
 * @author Haoz
 * @date 2017/11/13.
 */

public class PersonInfoDialogFragment extends DialogFragment {

    private static final String KEY_CONTACT = "key_contact";
    private ContactBean mContactBean;

    public static PersonInfoDialogFragment newInstance(ContactBean contactBean){
        PersonInfoDialogFragment dialogFragment = new PersonInfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_CONTACT, contactBean);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactBean = (ContactBean) getArguments().get(KEY_CONTACT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_person_info, null);

        TextView mTvName = (TextView) view.findViewById(R.id.person_info_tv_name);
        TextView mTvDormroom = (TextView) view.findViewById(R.id.person_info_tv_dorm_room);
        TextView mTvCollege = (TextView) view.findViewById(R.id.person_info_tv_college);
        TextView mTvPhoneNumber = (TextView) view.findViewById(R.id.person_info_tv_phone_number);

        StringBuilder stringBuilder = new StringBuilder(mContactBean.getGrade());
        stringBuilder.append("");
        stringBuilder.append(mContactBean.getName());
        mTvName.setText(stringBuilder);
        mTvDormroom.setText(mContactBean.getDormitory());
        mTvCollege.setText(mContactBean.getCollege() + "学院");
        mTvPhoneNumber.setText(mContactBean.getPhone());

        builder.setView(view);
        return builder.create();
    }
}















