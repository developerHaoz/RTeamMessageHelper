package com.developerhaoz.rteammessagehelper.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Haoz
 * @date 2017/11/15.
 */

public class CommonDialogFragment extends DialogFragment {

    /**
     * 监听弹出窗是否被取消
     */
    private OnDialogCancelListener mCancelListener;

    /**
     * 回调获得需要显示的dialog
     */
    private OnCallDialog mOnCallDialog;

    public interface OnDialogCancelListener {
        void onCancel();
    }

    public interface OnCallDialog {
        Dialog getDialog(Context context);
    }

    public static CommonDialogFragment newInstance(OnCallDialog call, boolean cancelable) {
        return newInstance(call, cancelable, null);
    }

    public static CommonDialogFragment newInstance(OnCallDialog call, boolean cancelable, OnDialogCancelListener cancelListener) {
        CommonDialogFragment instance = new CommonDialogFragment();
        instance.setCancelable(cancelable);
        instance.mCancelListener = cancelListener;
        instance.mOnCallDialog = call;
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (null == mOnCallDialog) {
            super.onCreateDialog(savedInstanceState);
        }
        return mOnCallDialog.getDialog(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(false);
        if (dialog != null) {
            //避免dialog的宽度过小
//            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);

            //在5.0以下的版本会出现白色背景边框，若在5.0以上设置则会造成文字部分的背景也变成透明
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                //目前只有这两个dialog会出现边框
                if (dialog instanceof ProgressDialog || dialog instanceof DatePickerDialog) {
                    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }
            // changed by haoz
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;
            window.setAttributes(windowParams);
        }

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mCancelListener != null) {
            mCancelListener.onCancel();
        }
    }

}
