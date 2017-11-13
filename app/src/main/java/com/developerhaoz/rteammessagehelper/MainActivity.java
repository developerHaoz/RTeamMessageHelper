package com.developerhaoz.rteammessagehelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 主页面的 Activity
 *
 * @author Haoz
 * @date 2017/11/11.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.app_toolbar)
    Toolbar mAppToolbar;
    @BindView(R.id.contact_btn_test)
    Button mContactBtnTest;
    @BindView(R.id.contact_rv_contact)
    RecyclerView mRvContacts;

    private List<ContactBean> mContactBeanList = new ArrayList<>();
    private List<ContactBean> mCheckContactList = new ArrayList<>();
    private Map<Integer, Boolean> mCheckMap = new HashMap<>();
    private ContactsAdapter adatper;

    private static final int SIZE_CONTACT = 20;
    public static boolean isAll = false;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initToolbar();
        initContactBeanList();
        initRecyclerView();

        findViewById(R.id.contact_btn_test).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isAll = !isAll;
                adatper.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mAppToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        mContactBtnTest = (Button) findViewById(R.id.contact_btn_test);
        mRvContacts = (RecyclerView) findViewById(R.id.contact_rv_contact);
    }

    private void initToolbar() {
        mToolbarTitle.setText("轮俱小助手");
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
                        adatper = new ContactsAdapter(s, ContactsAdapter.TYPE_CONTACTS);
                        adatper.setOnClickListener(new ContactsAdapter.OnContactClickListener() {
                            @Override
                            public void onClick() {
                                PersonInfoActivity.startActivity(MainActivity.this);
                            }
                        });
                        mRvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        mRvContacts.setAdapter(adatper);
                    }
                });

    }

    private void initContactBeanList() {
        mContactBeanList = new ArrayList<>();
        for (int i = 0; i < SIZE_CONTACT; i++) {
//            mContactBeanList.add(new ContactBean("15626144073", "李泽浩" + i));
            mCheckMap.put(i, false);
        }
    }


}
