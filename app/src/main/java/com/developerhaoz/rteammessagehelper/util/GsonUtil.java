package com.developerhaoz.rteammessagehelper.util;

import com.developerhaoz.rteammessagehelper.bean.ContactBean;
import com.developerhaoz.rteammessagehelper.bean.SendContactBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hans.alpha.utils.JSONUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Haoz
 * @date 2017/11/13.
 */

public class GsonUtil {

    private static final String STAUTS_OK = "200";
    private static final String DATA = "data";

    public static List<ContactBean> getContactList(String contactsJson) {
        String status = JSONUtil.getString(contactsJson, "status", "");

        if (STAUTS_OK.equals(status)) {
            Gson gson = new Gson();
            String data = JSONUtil.getString(contactsJson, "data", "");
            Type type = new TypeToken<List<ContactBean>>() {
            }.getType();
            List<ContactBean> contactBeanList = gson.fromJson(data, type);
            return contactBeanList;
        }
        return null;
    }

    public static List<SendContactBean> getSendContactList(List<ContactBean> contactBeanList){
        List<SendContactBean> result = new ArrayList<>();
        for (ContactBean contactBean : contactBeanList) {
            result.add(new SendContactBean(contactBean.getGrade(), contactBean.getName(), contactBean.getPhone()));
        }
        return result;
    }

    public static String getContactsJson(List<SendContactBean> contactBeanList) {
        Gson gson = new Gson();
        String result = gson.toJson(contactBeanList);
        return result;
    }

    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }
}
