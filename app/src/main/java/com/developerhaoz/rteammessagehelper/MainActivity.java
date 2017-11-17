package com.developerhaoz.rteammessagehelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developerhaoz.rteammessagehelper.bean.ContactBean;
import com.developerhaoz.rteammessagehelper.ui.ContactsAdapter;
import com.developerhaoz.rteammessagehelper.ui.EditMessageActivity;
import com.developerhaoz.rteammessagehelper.ui.PasswordDialogFragment;
import com.developerhaoz.rteammessagehelper.ui.PersonInfoDialogFragment;
import com.developerhaoz.rteammessagehelper.ui.SearchContactsActivity;
import com.developerhaoz.rteammessagehelper.ui.dialog.DialogFragmentHelper;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    TextView mToolbarTip;
    @BindView(R.id.app_toolbar)
    Toolbar mAppToolbar;
    @BindView(R.id.contact_rv_contact)
    RecyclerView mRvContacts;
    RelativeLayout mRlSearch;

    public static ArrayList<ContactBean> mContactBeanList = new ArrayList<>();
    private List<ContactBean> mCheckContactList = new ArrayList<>();
    private Map<Integer, Boolean> mCheckMap = new HashMap<>();
    private ContactsAdapter adatper;

    private static final int SIZE_CONTACT = 20;
    private static final String PASSWORD = "RTeam666";

    private DialogFragment mDialogFragment;

    public static void startActivity(Context context) {
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
        mToolbarTitle.setText("轮俱小助手");
        mToolbarTip.setText("短信");
        mAppToolbar.setNavigationIcon(null);
        mToolbarTip.setOnClickListener(this);
        mRlSearch.setOnClickListener(this);
    }

    private void ensurePassword() {
        PasswordDialogFragment dialogFragment = PasswordDialogFragment.newInstance();
        dialogFragment.setOnResultListener(new PasswordDialogFragment.OnResultListener() {
            @Override
            public void onDataResult(String password) {
                if (PASSWORD.equals(password)) {
                    EditMessageActivity.startActivity(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogFragment.show(MainActivity.this.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
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
                        mContactBeanList.addAll(s);
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
                        mDialogFragment.dismiss();
                    }
                });
    }

    private void initView() {
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTip = (TextView) findViewById(R.id.toolbar_tip);
        mAppToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        mRvContacts = (RecyclerView) findViewById(R.id.contact_rv_contact);
        mRlSearch = (RelativeLayout) findViewById(R.id.main_rl_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_tip:
                ensurePassword();
                break;
            case R.id.main_rl_search:
                SearchContactsActivity.startActivity(this, mContactBeanList);
                break;
            default:
                break;
        }
    }
}
