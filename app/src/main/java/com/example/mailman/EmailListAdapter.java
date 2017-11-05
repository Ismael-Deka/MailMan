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

import java.util.ArrayList;

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
}
