package com.developerhaoz.rteammessagehelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.developerhaoz.rteammessagehelper.R;
import com.developerhaoz.rteammessagehelper.bean.ContactBean;
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
    @BindView(R.id.contact_btn_test)
    Button mBtnTest;
    @BindView(R.id.contact_rv_contact)
    RecyclerView mRvContact;

    private ContactsAdapter mAdapter;
    private List<ContactBean> mContactList = new ArrayList<>();
    private List<ContactBean> mCheckContactList = new ArrayList<>();
    public static boolean isAll = false;
    private static final String KEY_MESSAGE = "key_message";
    private String message;

    public static void startActivity(Context context, String message) {
        Intent intent = new Intent(context, SelectContactsActivity.class);
        intent.putExtra(KEY_MESSAGE, message);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mBtnTest.setOnClickListener(SelectContactsActivity.this);
    }

    private void initRecyclerView() {

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
                    }
                });

    }

    private void initView() {
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTip = (TextView) findViewById(R.id.toolbar_tip);
        mRvContact = (RecyclerView) findViewById(R.id.contact_rv_contact);
        mBtnTest = (Button) findViewById(R.id.contact_btn_test);
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
                String contactsJson = GsonUtil.getContactsJson(mCheckContactList);

                break;
            case R.id.contact_btn_test:
                isAll = !isAll;
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
