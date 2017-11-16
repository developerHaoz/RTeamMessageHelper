package com.developerhaoz.rteammessagehelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.developerhaoz.rteammessagehelper.R;
import com.developerhaoz.rteammessagehelper.bean.ContactBean;
import com.developerhaoz.rteammessagehelper.bean.SendContactBean;
import com.developerhaoz.rteammessagehelper.ui.dialog.DialogFragmentHelper;
import com.developerhaoz.rteammessagehelper.util.GsonUtil;
import com.developerhaoz.rteammessagehelper.util.MessageApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Haoz
 * @date 2017/11/14.
 */

public class SelectContactsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_tip)
    TextView mToolbarTip;
    @BindView(R.id.app_toolbar)
    Toolbar mAppToolbar;
    @BindView(R.id.contact_rv_contact)
    RecyclerView mRvContact;
    CheckBox mCbAllCheck;
    TextView mTvPin;
    TextView mTvSelectAll;
    ImageView mIvPin;
    ImageView mIvSelectAll;

    private ContactsAdapter mAdapter;
    private List<ContactBean> mContactList = new ArrayList<>();
    private List<ContactBean> mCheckContactList = new ArrayList<>();
    public static boolean isAll = false;
    private static final String KEY_MESSAGE = "key_message";
    private String message;
    private DialogFragment mDialogFragment;

    public static void startActivity(Context context, String message) {
        Intent intent = new Intent(context, SelectContactsActivity.class);
        intent.putExtra(KEY_MESSAGE, message);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        ButterKnife.bind(this);
        initView();
        initToolbar();
        initRecyclerView();
        Intent intent = getIntent();
        if (intent != null) {
            message = intent.getStringExtra(KEY_MESSAGE);
        }
    }

    private void initToolbar() {
        mToolbarTitle.setText("选择人员");
        mToolbarTip.setText("确定");
        mAppToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbarTip.setOnClickListener(SelectContactsActivity.this);
        mTvPin.setOnClickListener(SelectContactsActivity.this);
        mIvPin.setOnClickListener(SelectContactsActivity.this);
        mTvSelectAll.setOnClickListener(SelectContactsActivity.this);
        mIvSelectAll.setOnClickListener(SelectContactsActivity.this);
    }

    private void initRecyclerView() {
        mDialogFragment = DialogFragmentHelper.showProgress(getSupportFragmentManager(), "正在获取会员信息...");
        Observable.create(new Observable.OnSubscribe<List<ContactBean>>() {
            @Override
            public void call(Subscriber<? super List<ContactBean>> subscriber) {
                try {
                    List<ContactBean> response = MessageApi.getContactList();
                    subscriber.onNext(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ContactBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<ContactBean> s) {
                        mContactList = s;
                        mAdapter = new ContactsAdapter(mContactList, ContactsAdapter.TYPE_SENDMESSAGE);
                        mAdapter.setOnClickListener(new ContactsAdapter.OnContactClickListener() {
                            @Override
                            public void onClick(ContactBean contactBean) {
                                PersonInfoDialogFragment personInfoDialogFragment = PersonInfoDialogFragment.newInstance(contactBean);
                                personInfoDialogFragment.show(SelectContactsActivity.this.getSupportFragmentManager(), personInfoDialogFragment.getClass().getSimpleName());
                            }
                        });
                        mRvContact.setLayoutManager(new LinearLayoutManager(SelectContactsActivity.this));
                        mRvContact.setAdapter(mAdapter);
                        mDialogFragment.dismiss();
                    }
                });

    }

    private void initView() {
        mAppToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTip = (TextView) findViewById(R.id.toolbar_tip);
        mRvContact = (RecyclerView) findViewById(R.id.contact_rv_contact);
        mTvPin = (TextView) findViewById(R.id.main_tv_pin);
        mTvSelectAll = (TextView) findViewById(R.id.main_tv_select_all);
        mIvPin = (ImageView) findViewById(R.id.main_iv_pin);
        mIvSelectAll = (ImageView) findViewById(R.id.main_iv_select_all);
    }

    private void allSelect() {
        isAll = !isAll;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_tip:
                Map map = ContactsAdapter.mCheckMap;
                for (int i = 0; i < mContactList.size(); i++) {
                    if (map.containsKey(i)) {
                        mCheckContactList.add(mContactList.get(i));
                    }
                }
                List<SendContactBean> sendContactList = GsonUtil.getSendContactList(mCheckContactList);
                String contactsJson = GsonUtil.getContactsJson(sendContactList);
                break;
            case R.id.main_tv_pin:
                mRvContact.scrollToPosition(0);
                break;
            case R.id.main_iv_pin:
                mRvContact.scrollToPosition(0);
                break;
            case R.id.main_tv_select_all:
                allSelect();
                break;
            case R.id.main_iv_select_all:
                allSelect();
                break;
            default:
                break;
        }
    }
}
