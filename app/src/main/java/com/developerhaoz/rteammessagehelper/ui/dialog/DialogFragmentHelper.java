package com.developerhaoz.rteammessagehelper.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.developerhaoz.rteammessagehelper.R;

import java.util.Calendar;

/**
 * @author Haoz
 * @date 2017/11/15.
 */

public class DialogFragmentHelper {

    private static final String dialog_positive = "确定";
    private static final String dialog_negative = "取消";

    private static final String TAG_HEAD = DialogFragmentHelper.class.getName();

    /**
     * 加载中的弹出窗
     */
    private static final int PROGRESS_THEME = R.style.Base_AlertDialog;
    private static final String PROGRESS_TAG = TAG_HEAD + ":progress";

    public static CommonDialogFragment showProgress(FragmentManager manager, String mes) {
        return showProgress(manager, mes, true, null);
    }

    public static CommonDialogFragment showProgress(FragmentManager manager, String mes, boolean cancelable) {
        return showProgress(manager, mes, cancelable, null);
    }

    public static CommonDialogFragment showProgress(FragmentManager manager, final String mes, boolean cancelable,
                                                    CommonDialogFragment.OnDialogCancelListener cListener) {
        CommonDialogFragment dialog = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(final Context context) {
                ProgressDialog pd = new ProgressDialog(context, PROGRESS_THEME);
                pd.setMessage(mes);
                return pd;
            }
        }, cancelable, cListener);
        dialog.show(manager, PROGRESS_TAG);
        return dialog;
    }


    /**
     * 简单提示弹出窗
     */
    private static final int TIPS_THEME = R.style.Base_AlertDialog;
    private static final String TIPS_TAG = TAG_HEAD + ":tips";

    public static void showTips(FragmentManager manager, String mes) {
        showTips(manager, mes, true, null);
    }

    public static void showTips(FragmentManager manager, String mes, boolean cancelable) {
        showTips(manager, mes, cancelable, null);
    }

    public static void showTips(FragmentManager manager, final String mes, boolean cancelable,
                                CommonDialogFragment.OnDialogCancelListener cListener) {

        CommonDialogFragment dialog = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(final Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, TIPS_THEME);
                builder.setMessage(mes);
                builder.setNegativeButton(dialog_positive, null);
                return builder.create();
            }
        }, cancelable, cListener);
        dialog.show(manager, TIPS_TAG);
    }

    /**
     * 确定取消框
     */
    private static final int CONFIRM_THEME = R.style.Base_AlertDialog;
    private static final String CONFIRM_TAG = TAG_HEAD + ":confirm";

    public static DialogFragment showConfirmDailog(FragmentManager manager, String msg,
                                                   IDialogResultListener<Integer> dListener) {
        showConfirmDialog(manager, msg, dListener, true, null);
        return null;
    }

    public static void showConfirmDialog(FragmentManager manager, final String msg,
                                         final IDialogResultListener<Integer> dListener,
                                         boolean cancelable, CommonDialogFragment.OnDialogCancelListener cListener) {
        CommonDialogFragment dialog = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(final Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, CONFIRM_THEME);
                builder.setMessage(msg);
                builder.setPositiveButton(dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dListener != null) {
                            dListener.onDataResult(which);
                        }
                    }
                });
                builder.setNegativeButton(dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dListener != null) {
                            dListener.onDataResult(which);
                        }
                    }
                });
                return builder.create();
            }
        }, cancelable, cListener);
        dialog.show(manager, CONFIRM_TAG);
    }

    /**
     * 带列表的弹出窗
     */
    private static final int LIST_THEME = R.style.Base_AlertDialog;
    private static final String LIST_TAG = TAG_HEAD + ":members";

    public static DialogFragment showListDialog(FragmentManager manager, final String title, final String[] items,
                                                final IDialogResultListener<Integer> dListener, boolean cancelable) {
        CommonDialogFragment dialog = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(final Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, LIST_THEME);
                builder.setTitle(title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dListener != null) {
                            dListener.onDataResult(which);
                        }
                    }
                });
                return builder.create();
            }
        }, cancelable, null);
        dialog.show(manager, LIST_TAG);
        return null;
    }

    /**
     * 选择日期
     */
    private static final int DATE_THEME = R.style.Base_AlertDialog;
    private static final String DATE_TAG = TAG_HEAD + ":members";

    public static DialogFragment showDataDialog(FragmentManager manager, final String title, final Calendar calendar,
                                                final IDialogResultListener<Calendar> dListener,
                                                final boolean cancelable) {
        CommonDialogFragment dialog = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(final Context context) {
                final DatePickerDialog dateDialog = new DatePickerDialog(context, DATE_THEME,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                calendar.set(year, monthOfYear, dayOfMonth);
                                dListener.onDataResult(calendar);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dateDialog.setTitle(title);
                dateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        dateDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(dialog_positive);
                        dateDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(dialog_negative);
                    }
                });
                return dateDialog;
            }
        }, cancelable, null);
        dialog.show(manager, DATE_TAG);
        return null;
    }

    /**
     * 选择时间
     */
    private static final int TIME_THEME = R.style.Base_AlertDialog;
    private static final String TIME_TAG = TAG_HEAD + ":time";

    public static void showTimeDialog(FragmentManager manager, final String title, final Calendar calendar,
                                      final IDialogResultListener<Calendar> dListener, final boolean cancelable) {
        CommonDialogFragment dialog = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(final Context context) {
                final TimePickerDialog dateDialog = new TimePickerDialog(context, TIME_THEME,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (dListener != null) {
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);
                                    dListener.onDataResult(calendar);
                                }
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                dateDialog.setTitle(title);
                dateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        dateDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(dialog_positive);
                        dateDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(dialog_negative);
                    }
                });
                return dateDialog;
            }
        }, cancelable, null);
        dialog.show(manager, DATE_TAG);
    }

    /**
     * 带输入框的弹出窗
     */
    private static final int INSERT_THEME = R.style.Base_AlertDialog;
    private static final String INSERT_TAG = TAG_HEAD + ":insert";

    public static void showInsertDialog(FragmentManager manager, final String title,
                                        final IDialogResultListener<String> dListener, boolean cancelable) {
        CommonDialogFragment dialog = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(final Context context) {
                final EditText et = new EditText(context);
                et.setBackground(null);
                et.setPadding(60, 40, 0, 0);
                et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(context, INSERT_THEME);
                builder.setTitle(title);
                builder.setView(et);
                builder.setPositiveButton(dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dListener != null) {
                            dListener.onDataResult(et.getText().toString());
                        }
                    }
                });
                builder.setNegativeButton(dialog_negative, null);
                return builder.create();
            }
        }, cancelable, null);
        dialog.show(manager, INSERT_TAG);
    }

    /**
     * 带输入密码框的弹出窗
     */
    private static final int PASSWORD_INSERT_THEME = R.style.Base_AlertDialog;
    private static final String PASSWORD_INSERT_TAG = TAG_HEAD + ":insert";

    public static void showPasswordInsertDialog(FragmentManager manager, final String title,
                                                final IDialogResultListener<String> dListener, boolean cancelable) {
        CommonDialogFragment dialog = CommonDialogFragment.newInstance(new CommonDialogFragment.OnCallDialog() {
            @Override
            public Dialog getDialog(final Context context) {
                final EditText et = new EditText(context);
                et.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et.setEnabled(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(context, PASSWORD_INSERT_THEME);
                builder.setTitle(title);
                builder.setView(et);
                builder.setPositiveButton(dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dListener != null) {
                            dListener.onDataResult(et.getText().toString());
                        }
                    }
                });
                builder.setNegativeButton(dialog_negative, null);
                return builder.create();
            }
        }, cancelable, null);
        dialog.show(manager, INSERT_TAG);
    }



}
