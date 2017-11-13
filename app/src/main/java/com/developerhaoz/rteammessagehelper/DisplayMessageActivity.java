package com.developerhaoz.rteammessagehelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 展示完整短信的 Activity
 *
 * @author Haoz
 * @date 2017/11/13.
 */

public class DisplayMessageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.app_toolbar)
    Toolbar mAppToolbar;
    @BindView(R.id.display_message_et_message)
    TextView mEtMessage;

    public static final String KEY_MESSAGE = "key_message";

    public static void startActivity(Context context, String message){
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
        if(getIntent() != null){
            String message = intent.getStringExtra(KEY_MESSAGE);
            mEtMessage.setText(message);
        }

    }

    private void initView() {
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mAppToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        mEtMessage = (EditText) findViewById(R.id.display_message_et_message);
    }

    private void initToolbar() {
        mToolbarTitle.setText("短信预览");
    }
}
