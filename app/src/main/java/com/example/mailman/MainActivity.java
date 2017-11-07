package com.example.mailman;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,LoaderManager.LoaderCallbacks<ArrayList<Email>>{

    private static final int RC_SIGN_IN = 9001;
    private final String LOG_TAG = "Main Activity";
    private GoogleApiClient mGoogleApiClient;
    private GoogleAccountCredential mCredential;
    private ListView mList;
    ArrayList<Email> mEmails;

    private ProgressBar mProgressBar;
    private SignInButton mSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(GmailScopes.GMAIL_READONLY))
                .setBackOff(new ExponentialBackOff());

        mList = (ListView) findViewById(R.id.email_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        if(savedInstanceState == null) {
            mSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
        }else{
            byte[] data = savedInstanceState.getByteArray("emails");
            populateList(inflateEmails(data));

        }





    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mEmails != null) {
            outState.putByteArray("emails", compressEmails(mEmails));
        }
        super.onSaveInstanceState(outState);
    }

    public byte[] compressEmails(ArrayList<Email> emails){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = null;
        try {
            gzipOut = new GZIPOutputStream(baos);
            ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
            objectOut.writeObject(emails);
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    public ArrayList<Email> inflateEmails(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ArrayList<Email> emails = new ArrayList<>();
        GZIPInputStream gzipIn = null;
        try {
            gzipIn = new GZIPInputStream(bais);
            ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
            emails = (ArrayList<Email>) objectIn.readObject();
            objectIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return emails;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mCredential.setSelectedAccountName(acct.getEmail());
            getLoaderManager().initLoader(1,null,this).forceLoad();

        }
    }
    public void populateList(ArrayList<Email> emails){
        mEmails = emails;
        EmailListAdapter adapter = new EmailListAdapter(this,emails);
        final ArrayList<Email> e = emails;
        mList.setAdapter(adapter);
        mList.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,EmailActivity.class);
                intent.putExtra("subject",e.get(i).getSubject());
                intent.putExtra("from",e.get(i).getSender());
                intent.putExtra("body",e.get(i).getBody());
                startActivity(intent);

            }
        });

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public Loader<ArrayList<Email>> onCreateLoader(int id, Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);
        mSignInButton.setVisibility(View.GONE);
        return new MailLoader(this,mCredential,this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Email>> loader, ArrayList<Email> data) {
        populateList(data);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Email>> loader) {

    }
}
