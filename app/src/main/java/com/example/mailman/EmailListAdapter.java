package com.example.mailman;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ismael on 11/5/2017.
 */

public class EmailListAdapter extends ArrayAdapter<Email> {



    public EmailListAdapter(@NonNull Context context, ArrayList<Email> emails) {
        super(context, 0,emails);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.email_list_item, parent, false);
        }

        Email email = getItem(position);

        TextView subjectView = (TextView) listItemView.findViewById(R.id.subject);
        TextView snippetView = (TextView) listItemView.findViewById(R.id.snippet);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        subjectView.setText(email.getSubject());
        snippetView.setText(Html.fromHtml(email.getSnippet()));
        dateView.setText(email.getDate());

        return listItemView;

    }

    private String formatDate(String strCurrentDate){
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        String date = format.format(newDate);

        return date;
    }
}
