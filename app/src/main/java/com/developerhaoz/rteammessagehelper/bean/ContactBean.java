package com.developerhaoz.rteammessagehelper.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 联系人的实体类
 *
 * @author Haoz
 * @date 2017/11/11.
 */

public class ContactBean implements Parcelable {

    /**
     * id : 77
     * name : 李莹莹
     * sex : 女
     * college : 环境
     * studentNumber : 3217006213
     * phone : 13760362629
     * dormitory : 东十二529
     * grade : R13
     */

    private int id;
    private String name;
    private String sex;
    private String college;
    private String studentNumber;
    private String phone;
    private String dormitory;
    private String grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDormitory() {
        return dormitory;
    }

    public void setDormitory(String dormitory) {
        this.dormitory = dormitory;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.college);
        dest.writeString(this.studentNumber);
        dest.writeString(this.phone);
        dest.writeString(this.dormitory);
        dest.writeString(this.grade);
    }

    public ContactBean() {
    }

    public ContactBean(String grade, String name, String phone){
        this.grade = grade;
        this.name = name;
        this.phone = phone;
    }

    protected ContactBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.sex = in.readString();
        this.college = in.readString();
        this.studentNumber = in.readString();
        this.phone = in.readString();
        this.dormitory = in.readString();
        this.grade = in.readString();
    }

    public static final Parcelable.Creator<ContactBean> CREATOR = new Parcelable.Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel source) {
            return new ContactBean(source);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };
}
