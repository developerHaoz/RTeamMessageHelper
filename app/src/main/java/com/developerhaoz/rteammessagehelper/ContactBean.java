package com.developerhaoz.rteammessagehelper;

/**
 * 联系人的实体类
 *
 * @author Haoz
 * @date 2017/11/11.
 */

public class ContactBean {

    private String mPhone;
    private String mName;

    public ContactBean(String phone, String name) {
        mPhone = phone;
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
