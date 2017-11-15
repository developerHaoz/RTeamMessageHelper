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

import com.developerhaoz.rteammessagehelper.bean.ContactBean;
import com.developerhaoz.rteammessagehelper.ui.ContactsAdapter;
import com.developerhaoz.rteammessagehelper.ui.EditMessageActivity;
import com.developerhaoz.rteammessagehelper.ui.PasswordDialogFragment;
import com.developerhaoz.rteammessagehelper.ui.PersonInfoDialogFragment;
import com.developerhaoz.rteammessagehelper.util.MessageApi;

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
    TextView mToolbarTip;
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
    private static final String PASSWORD = "RTeam666";

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
        initRecyclerView();
    }

    private void initToolbar() {
        mContactBtnTest.setVisibility(View.GONE);
        mToolbarTitle.setText("轮俱小助手");
        mToolbarTip.setText("短信");
        mToolbarTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordDialogFragment dialogFragment = PasswordDialogFragment.newInstance();
                dialogFragment.setOnResultListener(new PasswordDialogFragment.OnResultListener() {
                    @Override
                    public void onDataResult(String password) {
//                        if(PASSWORD.equals(password)){
                            EditMessageActivity.startActivity(MainActivity.this);
//                        }else {
//                            Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
                dialogFragment.show(MainActivity.this.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
            }
        });
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
                            public void onClick(ContactBean contactBean) {
                                PersonInfoDialogFragment personInfoDialogFragment = PersonInfoDialogFragment.newInstance(contactBean);
                                personInfoDialogFragment.show(MainActivity.this.getSupportFragmentManager(), personInfoDialogFragment.getClass().getSimpleName());
                            }
                        });
                        mRvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        mRvContacts.setAdapter(adatper);
                    }
                });

    }

    private void initView() {
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTip = (TextView) findViewById(R.id.toolbar_tip);
        mAppToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        mContactBtnTest = (Button) findViewById(R.id.contact_btn_test);
        mRvContacts = (RecyclerView) findViewById(R.id.contact_rv_contact);
    }

}
