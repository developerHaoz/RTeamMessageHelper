package com.developerhaoz.rteammessagehelper.util;

import com.developerhaoz.rteammessagehelper.bean.ContactBean;
import com.hans.alpha.network.RequestCall;
import com.hans.alpha.network.builder.GetBuilder;
import com.hans.alpha.network.builder.PostFormBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * API 的封装类
 *
 * @author Haoz
 * @date 2017/11/13.
 */

public class MessageApi {

    private static final String URL_SEND_MESSAGE = "http:///120.77.157.25:8888/send";
    private static final String URL_GET_CONTACTS = "http://120.77.157.25:8888/member/list";
    private static final String KEY_ACTIVITY = "activity";
    private static final String KEY_DATE = "date";
    private static final String KEY_MEMBERS = "members";

    public static String sendMessage(String activity, String time, String membersJson) throws IOException{

        RequestCall requestCall = new PostFormBuilder()
                .url(URL_SEND_MESSAGE)
                .addParams(KEY_ACTIVITY, activity)
                .addParams(KEY_DATE, time)
                .addParams(KEY_MEMBERS, membersJson)
                .build();

        Response response = requestCall.doSceneSync();
        String result = response.body().string();
        return result;
    }

    public static List<ContactBean> getContactList() throws IOException{

        RequestCall requestCall = new GetBuilder()
                .url(URL_GET_CONTACTS)
                .build();

        Response response = requestCall.doSceneSync();
        String result = response.body().string();
        return GsonUtil.getContactList(result);
    }

}


















