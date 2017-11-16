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

    public static final String KEY_MESSAGE = "key_message";

    public static void startActivity(Context context, String message) {
        Intent intent = new Intent(context, DisplayMessageActivity.class);
        intent.putExtra(KEY_MESSAGE, message);
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
            String message = intent.getStringExtra(KEY_MESSAGE);
            mTvMessage.setText(message);
        }

        mBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectContactsActivity.startActivity(DisplayMessageActivity.this, mTvMessage.getText().toString());
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
