package com.example.mailman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class EmailActivity extends AppCompatActivity {

    String mSubject;
    String mFrom;
    String mBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Intent intent = getIntent();

        mSubject = intent.getStringExtra("subject");
        mFrom = intent.getStringExtra("from");
        mBody = intent.getStringExtra("body");

        TextView subjectView = (TextView) findViewById(R.id.subject_full);
        TextView fromView = (TextView) findViewById(R.id.from);

        TextView bodyView = (TextView) findViewById(R.id.body);

        subjectView.setText(mSubject);
        fromView.setText(mFrom);
        if(mBody!=null)
        bodyView.setText(Html.fromHtml(mBody));

    }
}
