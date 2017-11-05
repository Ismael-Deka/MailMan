package com.example.mailman;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ismael on 11/4/2017.
 */

public class MailLoader extends AsyncTaskLoader<ArrayList<Email>> {
    private Gmail mGmailService;
    private GoogleAccountCredential mCredential;
    private Activity mParentActivity;

    public MailLoader(Activity activity,GoogleAccountCredential credential,Context context) {
        super(context);
        mCredential = credential;
        mParentActivity =activity;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mGmailService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, credential)
                .build();
    }

    @Override
    public ArrayList<Email> loadInBackground() {
        String user = mCredential.getSelectedAccountName();
        List<Message> messages;
        ArrayList<Email> messageBodies = new ArrayList<>();
        ListMessagesResponse listResponse;
        try {
            listResponse = mGmailService.users().messages().list(user).execute();
            Log.e("loader","called");
            messages = listResponse.getMessages();
            int i =0;
            for (Message message:listResponse.getMessages()) {
                message = mGmailService.users().messages().get(user,messages.get(i).getId()).setFormat("full").execute();
                messageBodies.add(parseResponse(message));
                i++;

            }
        } catch (UserRecoverableAuthIOException e) {
            e.printStackTrace();
            mParentActivity.startActivityForResult(e.getIntent(), 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messageBodies;
    }

    private String decodeMessege(String str){
        Base64 decoder = new Base64();Log.e("loader",new String(str));
        byte[] rawMessage = decoder.decode(str);

        return StringUtils.newStringUtf8(rawMessage);
    }

    private Email parseResponse(Message message) {
        String from = "";
        String to = "";
        String date = "";
        String subject = "";
        String body = "";
        String snippet = "";

        List<MessagePartHeader> headers = message.getPayload().getHeaders();
        String value;
        for (int i = 0; i < headers.size(); i++) {
            value = headers.get(i).getValue();
            switch (headers.get(i).getName()) {
                case "From":
                    from = value;
                    break;
                case "To":
                    to = value;
                    break;
                case "Date":
                    date = value;
                    break;
                case "Subject":
                    subject = value;
                    break;

            }

            body = StringUtils.newStringUtf8(message.getPayload().getBody().decodeData());
            snippet = message.getSnippet();
            snippet.replace("&#39;","\'");
        } return new Email(from,to,date,subject,body,snippet);

    }
}
