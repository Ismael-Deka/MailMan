package com.example.mailman;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ismael on 11/4/2017.
 */

public class Email implements Parcelable,Serializable {
    private String mFrom;
    private String mTo;
    private String mDate;
    private String mSubject;
    private String mBody;
    private String mSnippet;

    public Email(String newFrom, String newTo, String newDate, String newSubject, String newBody, String newSnippet){
        mFrom = newFrom;
        mTo = newTo;
        mDate = newDate;
        mSubject = newSubject;
        mBody = newBody;
        mSnippet = newSnippet;
    }
    public Email(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.mFrom = data[0];
        this.mTo = data[1];
        this.mDate = data[2];
        this.mSubject = data[3];
        this.mBody = data[4];
        this.mSnippet = data[5];
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.mFrom,
                this.mTo,
                this.mDate,
                this.mSubject,
                this.mBody,
                this.mSnippet});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Email createFromParcel(Parcel in) {
            return new Email(in);
        }

        public Email[] newArray(int size) {
            return new Email[size];
        }
    };

    public String getSender() {
        return mFrom;
    }

    public String getReceivingAddress() {
        return mTo;
    }

    public String getDate() {
        return mDate;
    }

    public String getSubject() {
        return mSubject;
    }

    public String getBody() {
        return mBody;
    }

    public String getSnippet() {
        return mSnippet;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
