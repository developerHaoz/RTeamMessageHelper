package com.developerhaoz.rteammessagehelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.developerhaoz.rteammessagehelper.R;

import butterknife.ButterKnife;

/**
 * 展示完整短信的 Activity
 *
 * @author Haoz
 * @date 2017/11/13.
 */

public class DisplayMessageActivity extends AppCompatActivity {

    TextView mToolbarTitle;
    Toolbar mAppToolbar;
    TextView mTvMessage;
    Button mBtnSelect;

    private static final String KEY_ACTIVITY = "key_activity";
    private static final String KEY_TIME = "key_time";

    private String activty;
    private String time;

    public static void startActivity(Context context, String activity, String time) {
        Intent intent = new Intent(context, DisplayMessageActivity.class);
        intent.putExtra(KEY_ACTIVITY, activity);
        intent.putExtra(KEY_TIME, time);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        ButterKnife.bind(this);
        initView();
        initToolbar();
        Intent intent = getIntent();
        if (getIntent() != null) {
            activty = intent.getStringExtra(KEY_ACTIVITY);
            time = intent.getStringExtra(KEY_TIME);
            StringBuilder stringBuilder = new StringBuilder();
            String prefix = "「广工轮俱」亲爱的广工轮俱 R13 成员苟雅莉你好，广工轮俱将于";
            stringBuilder.append(prefix);
            stringBuilder.append(time);
            stringBuilder.append("进行" + activty);
            stringBuilder.append(",请准时参加。");
            mTvMessage.setText(stringBuilder);
        }

        mBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectContactsActivity.startActivity(DisplayMessageActivity.this, activty, time);
            }
        });

    }

    private void initView() {
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mAppToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        mTvMessage = (TextView) findViewById(R.id.display_message_tv_message);
        mBtnSelect = (Button) findViewById(R.id.display_message_btn_select);
    }

    private void initToolbar() {
        mAppToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbarTitle.setText("短信预览");
    }
}
