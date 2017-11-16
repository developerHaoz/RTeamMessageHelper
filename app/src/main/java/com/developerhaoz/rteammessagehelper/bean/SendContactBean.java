package com.developerhaoz.rteammessagehelper.bean;

/**
 * @author Haoz
 * @date 2017/11/14.
 */

public class SendContactBean {

    /**
     * grade : R13
     * name : 李泽浩
     * phone : 15626144073
     */

    private String grade;
    private String name;
    private String phone;

    public SendContactBean(String grade, String name, String phone) {
        this.grade = grade;
        this.name = name;
        this.phone = phone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
