package com.example.peter.test;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.View;
import android.os.Debug;
import android.util.Log;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class MainActivity extends ActionBarActivity {
    TextView outputText;
    TextView outputText2;
    ProgressBar myProgressBar;
    int myProgress = 0;
    Handler myHandle;
    static {
        System.loadLibrary("JniDemo");
    }
    public native String getStringFromNative();
    private native int add(int a, int b);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        CharSequence text = getStringFromNative();
        int duration = Toast.LENGTH_SHORT;
        int anser = add(5, 6);
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        outputText = (TextView) findViewById(R.id.textView2);
        outputText2 = (TextView) findViewById(R.id.textView);
        Button buttonStart = (Button)findViewById(R.id.button);
        buttonStart.setOnClickListener(startListener); // Register the onClick listener with the implementation above

        Button buttonStop = (Button)findViewById(R.id.button2);
        buttonStop.setOnClickListener(stopListener); // Register the onClick listener with the implementation above
        Button buttonIntent = (Button)findViewById(R.id.button3);
        buttonIntent.setOnClickListener(intentLinstener);
        outputText.setText("Anser = " + anser);
        outputText2.setText(text);
        if(Debug.isDebuggerConnected()){
            System.out.println(Debug.isDebuggerConnected());
            System.out.println("Debugger is Connected");
        }
        else
        {
            System.out.println(Debug.isDebuggerConnected());
            System.out.println("Debugger is not Connected");
        }

    }

    //Create an anonymous implementation of OnClickListener
    private OnClickListener startListener = new OnClickListener() {
        public void onClick(View v) {
            Context context = getApplicationContext();
            CharSequence text = "onClick() called - Hide button";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            read_history();
        }
    };
    //Create an anonymous implementation of OnClickListener
    private OnClickListener intentLinstener = new OnClickListener() {
        public void onClick(View v) {
            Context context = getApplicationContext();
            CharSequence text = "onClick() called - Intent button";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:021-80961111"));
            startActivity(intent);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private OnClickListener stopListener = new OnClickListener() {
        public void onClick(View v) {
            Context context = getApplicationContext();
            CharSequence text = "onClick() called - Get Contact";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            fetchContacts();
        }
    };
    public void fetchContacts() {

        String phoneNumber = null;
        String email = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {
           while (cursor.moveToNext()) {
              String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
              String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
              int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
              if (hasPhoneNumber > 0) {
                output.append("\n First Name:" + name);
                // Query and loop for every phone number of the contact
                Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                while (phoneCursor.moveToNext()) {
                   phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                   output.append("\n Phone number:" + phoneNumber);
                }
                phoneCursor.close();
                // Query and loop for every email of the contact
                Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
                 while (emailCursor.moveToNext()) {
                   email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                   output.append("\nEmail:" + email);
                 }
               emailCursor.close();
              }
           output.append("\n");
          }
          outputText.setText(output);
        }
    }
    public void read_history(){
        StringBuffer output = new StringBuffer();
        String Title = Browser.BookmarkColumns.TITLE;
        String URL   = Browser.BookmarkColumns.URL;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(android.provider.Browser.BOOKMARKS_URI, null,null, null, null);
        if (cursor.getCount() > 0) {
           int titleIdx = cursor.getColumnIndex(Title);
           int urlIdx = cursor.getColumnIndex(URL);
            while (cursor.moveToNext()) {
                output.append("\nTitle:" +cursor.getString(titleIdx) );
                output.append("\nURL:" + cursor.getString(urlIdx));
            }
            outputText.setText(output);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
