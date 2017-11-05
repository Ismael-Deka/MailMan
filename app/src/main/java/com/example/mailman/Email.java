package com.example.mailman;

/**
 * Created by Ismael on 11/4/2017.
 */

public class Email {
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
}
