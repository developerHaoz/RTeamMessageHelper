package com.developerhaoz.rteammessagehelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页面的 Activity
 *
 * @author Haoz
 * @date 2017/11/11.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "11111";

    private List<ContactBean> mContactBeanList = new ArrayList<>();
    private List<ContactBean> mCheckContactList = new ArrayList<>();
    private Map<Integer, Boolean> mCheckMap = new HashMap<>();

    private RecyclerView mRvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvContacts = (RecyclerView) findViewById(R.id.contact_rv_contact);
        initContactBeanList();
        ContactsAdapter adatper = new ContactsAdapter(mContactBeanList);
        adatper.setCheckListener(new ContactsAdapter.CheckListener() {
            @Override
            public void check(int position, boolean isCheck) {
                if(isCheck){
                    mCheckMap.put(position, true);
                }else{
                    mCheckMap.put(position, false);
                }
            }
        });

        mRvContacts.setLayoutManager(new LinearLayoutManager(this));
        mRvContacts.setAdapter(adatper);

        findViewById(R.id.contact_btn_test).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
    }

    private void initContactBeanList() {
        mContactBeanList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mContactBeanList.add(new ContactBean("15626144073", "李泽浩" + i));
            mCheckMap.put(i, false);
        }
    }


}
