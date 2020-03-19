package com.aiface.jidacard.card;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 读卡数据实体
 */
public class IDCardInfo implements Parcelable, Serializable {
    public int retCode = -1;//0=身份证；1=外国人居住证；
    public String uuid;
    public String name;
    public String sex;
    public String nation;
    public String birthday;
    public String address;
    public String cardNo;
    public String issueOffice;
    public String validDateStart;
    public String validDateEnd;
    public Bitmap picBitmap;
    public String cardSn;
    public byte[] finger;
    public String cardType;

    public String eng_name;//英文名（外国人居住证）
    public String ver;//证件版本号（外国人居住证）
    public String district_code;//证件版本号（外国人居住证）
    public String issuer_code;//国籍地区码（外国人居住证）

    public String passNo;//港澳台同行号码


    public IDCardInfo() {
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIssueOffice() {
        return issueOffice;
    }

    public void setIssueOffice(String issueOffice) {
        this.issueOffice = issueOffice;
    }

    public String getValidDateStart() {
        return validDateStart;
    }

    public void setValidDateStart(String validDateStart) {
        this.validDateStart = validDateStart;
    }

    public String getValidDateEnd() {
        return validDateEnd;
    }

    public void setValidDateEnd(String validDateEnd) {
        this.validDateEnd = validDateEnd;
    }

    public Bitmap getPicBitmap() {
        return picBitmap;
    }

    public void setPicBitmap(Bitmap picBitmap) {
        this.picBitmap = picBitmap;
    }

    public String getPassNo() {
        return passNo;
    }

    public void setPassNo(String passNo) {
        this.passNo = passNo;
    }

    public String getCardSn() {
        return cardSn;
    }

    public void setCardSn(String cardSn) {
        this.cardSn = cardSn;
    }

    public byte[] getFinger() {
        return finger;
    }

    public void setFinger(byte[] finger) {
        this.finger = finger;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getEng_name() {
        return eng_name;
    }

    public void setEng_name(String eng_name) {
        this.eng_name = eng_name;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getIssuer_code() {
        return issuer_code;
    }

    public void setIssuer_code(String issuer_code) {
        this.issuer_code = issuer_code;
    }

    public static Creator<IDCardInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "IDCardInfo{" +
                "retCode=" + retCode +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", issueOffice='" + issueOffice + '\'' +
                ", validDateStart='" + validDateStart + '\'' +
                ", validDateEnd='" + validDateEnd + '\'' +
//                ", picBitmap=" + picBitmap +
                ", passNo='" + passNo + '\'' +
                ", cardSn='" + cardSn + '\'' +
//                ", finger=" + Arrays.toString(finger) +
                ", cardType='" + cardType + '\'' +
                ", district_code='" + district_code + '\'' +
                ", eng_name='" + eng_name + '\'' +
                ", ver='" + ver + '\'' +
                ", issuer_code='" + issuer_code + '\'' +
                '}';
    }

    protected IDCardInfo(Parcel in) {
        retCode = in.readInt();
        uuid = in.readString();
        name = in.readString();
        sex = in.readString();
        nation = in.readString();
        birthday = in.readString();
        address = in.readString();
        cardNo = in.readString();
        issueOffice = in.readString();
        validDateStart = in.readString();
        validDateEnd = in.readString();
        picBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        passNo = in.readString();
        cardSn = in.readString();
        finger = in.createByteArray();
        cardType = in.readString();
        district_code = in.readString();
        eng_name = in.readString();
        ver = in.readString();
        issuer_code = in.readString();
    }

    public static final Creator<IDCardInfo> CREATOR = new Creator<IDCardInfo>() {
        @Override
        public IDCardInfo createFromParcel(Parcel in) {
            return new IDCardInfo(in);
        }

        @Override
        public IDCardInfo[] newArray(int size) {
            return new IDCardInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(retCode);
        parcel.writeString(uuid);
        parcel.writeString(name);
        parcel.writeString(sex);
        parcel.writeString(nation);
        parcel.writeString(birthday);
        parcel.writeString(address);
        parcel.writeString(cardNo);
        parcel.writeString(issueOffice);
        parcel.writeString(validDateStart);
        parcel.writeString(validDateEnd);
        parcel.writeParcelable(picBitmap, i);
        parcel.writeString(passNo);
        parcel.writeString(cardSn);
        parcel.writeByteArray(finger);
        parcel.writeString(cardType);
        parcel.writeString(district_code);
        parcel.writeString(eng_name);
        parcel.writeString(ver);
        parcel.writeString(issuer_code);
    }
}
