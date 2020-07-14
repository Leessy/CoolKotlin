package com.aiface.jidacard.card;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Author: 陈博
 * @create time:  2018/12/25  12:17
 */
public class BankCardInfo implements Parcelable, Serializable {
    public int retCode = -1;
    public String cardNo;
    public String uid;
    public String bin;
    public String balance;
    public String time;

    public BankCardInfo() {
    }

    public BankCardInfo(int retCode, String cardNo, String uid, String bin, String balance, String time) {
        this.retCode = retCode;
        this.cardNo = cardNo;
        this.uid = uid;
        this.bin = bin;
        this.balance = balance;
        this.time = time;
    }

    protected BankCardInfo(Parcel in) {
        retCode = in.readInt();
        cardNo = in.readString();
        uid = in.readString();
        bin = in.readString();
        balance = in.readString();
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(retCode);
        dest.writeString(cardNo);
        dest.writeString(uid);
        dest.writeString(bin);
        dest.writeString(balance);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankCardInfo> CREATOR = new Creator<BankCardInfo>() {
        @Override
        public BankCardInfo createFromParcel(Parcel in) {
            return new BankCardInfo(in);
        }

        @Override
        public BankCardInfo[] newArray(int size) {
            return new BankCardInfo[size];
        }
    };

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


    @Override
    public String toString() {
        return "{" +
                "retCode=" + retCode +
                ", cardNo='" + cardNo + '\'' +
                ", uid='" + uid + '\'' +
                ", bin='" + bin + '\'' +
                ", balance='" + balance + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
