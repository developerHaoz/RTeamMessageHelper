package com.developerhaoz.rteammessagehelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.developerhaoz.rteammessagehelper.bean.ContactBean;
import com.developerhaoz.rteammessagehelper.R;

/**
 * 个人资料详情的 Activity
 *
 * @author Haoz
 * @date 2017/11/13.
 */

public class PersonInfoActivity extends AppCompatActivity {

    private static final String KEY_CONTACT = "key_contact";

    public static void startActivity(Context context, ContactBean contactBean){
        Intent intent = new Intent(context, PersonInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_CONTACT, contactBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            ContactBean contactBean = (ContactBean) bundle.get(KEY_CONTACT);
        }
    }
}
