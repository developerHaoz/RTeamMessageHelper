package com.developerhaoz.rteammessagehelper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.developerhaoz.rteammessagehelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 编辑短信的 Activity
 *
 * @author Haoz
 * @date 2017/11/13.
 */

public class EditMessageActivity extends AppCompatActivity {

    @BindView(R.id.edit_message_et_activity_name)
    EditText mEtActivityName;
    @BindView(R.id.edit_message_et_activity_time)
    EditText mEtActivityTime;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.app_toolbar)
    Toolbar mAppToolbar;
    @BindView(R.id.toolbar_tip)
    TextView mToolbarTip;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, EditMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar() {
        initView();
        mToolbarTitle.setText("编辑短信");
        mAppToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbarTip.setText("预览");
        mToolbarTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                String prefix = "「广工轮俱」亲爱的广工轮俱 R13 成员苟雅莉你好，广工轮俱将于";
                stringBuilder.append(prefix);
                stringBuilder.append(mEtActivityTime.getText().toString());
                stringBuilder.append("进行" + mEtActivityName.getText().toString());
                stringBuilder.append(",请准时参加。");
                DisplayMessageActivity.startActivity(EditMessageActivity.this, String.valueOf(stringBuilder));
            }
        });
    }

    private void initView() {
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTip = (TextView) findViewById(R.id.toolbar_tip);
        mEtActivityName = (EditText) findViewById(R.id.edit_message_et_activity_name);
        mEtActivityTime = (EditText) findViewById(R.id.edit_message_et_activity_time);
    }
}
