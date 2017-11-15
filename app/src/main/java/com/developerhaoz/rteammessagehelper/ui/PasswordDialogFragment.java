package com.developerhaoz.rteammessagehelper.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * @author Haoz
 * @date 2017/11/13.
 */

public class PasswordDialogFragment extends DialogFragment {

    private OnResultListener mListener;

    public static PasswordDialogFragment newInstance(){
        PasswordDialogFragment dialogFragment = new PasswordDialogFragment();
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        WeakReference<Activity> weakReference = new WeakReference<Activity>(getActivity());
        final EditText et = new EditText(weakReference.get());
        et.setBackground(null);
        et.setPadding(60, 40, 0, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入密码");
        builder.setView(et);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onDataResult(et.getText().toString());
                }
            }
        });
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    public void setOnResultListener(OnResultListener listener){
        mListener = listener;
    }

    public interface OnResultListener{
        void onDataResult(String password);
    }
}
