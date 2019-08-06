/*********************************************************************

 Chat server: accept chat messages from clients.

 Sender chatName and GPS coordinates are encoded
 in the messages, and stripped off upon receipt.

 Copyright (c) 2017 Stevens Institute of Technology

 **********************************************************************/
package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.rest.ChatHelper;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.ResultReceiverWrapper;

public class RegisterActivity extends Activity implements OnClickListener, ResultReceiverWrapper.IReceive {

    final static public String TAG = RegisterActivity.class.getCanonicalName();

    /*
     * Widgets for dest address, message text, send button.
     */
    private TextView clientIdText;

    private EditText userNameText;

    private EditText serverUriText;

    private Button registerButton;

    /*
     * Helper for Web service
     */
    private ChatHelper helper;

    /*
     * For receiving ack when registered.
     */
    private ResultReceiverWrapper registerResultReceiver;

    /*
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Initialize settings to default values.
         */
        if (Settings.isRegistered(this)) {
            finish();
            return;
        }

        setContentView(R.layout.register);

        //  initialize registerResultReceiver
        registerResultReceiver = new ResultReceiverWrapper(new Handler());

        //  instantiate helper for service
        helper = new ChatHelper(this,registerResultReceiver);

        //  get references to views

        clientIdText = (TextView) findViewById(R.id.client_id_text);
        clientIdText.setText(Settings.getClientId(this).toString());

        userNameText = (EditText) findViewById(R.id.chat_name_text);

        serverUriText = (EditText) findViewById(R.id.server_uri_text);

        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);

    }

    public void onResume() {
        super.onResume();
        registerResultReceiver.setReceiver(this);
    }

    public void onPause() {
        super.onPause();
        registerResultReceiver.setReceiver(null);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * Callback for the REGISTER button.
     */
    public void onClick(View v) {
        if (helper != null) {

            String userName;

            String serverUri;

            //  get server URI and userName from UI, and use helper to register
            //  set registered in settings upon completion

            userName = userNameText.getText().toString();
            Log.e("mingzi", userName);

            serverUri = serverUriText.getText().toString();

            // helper.register(userName);

            Settings.saveChatName(this, userName);

            Settings.saveServerUri(this, serverUri);

            // End
            helper.register(userName);

            Log.i(TAG, "Registered: " + userName);

            //finish();

        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case RESULT_OK:
                Settings.setRegistered(this, true);
                Log.e(RegisterActivity.class.getCanonicalName(),"RESULT_OK");
                //  show a success toast message
                Toast.makeText(this,"Registered Successfully",Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                //  show a failure toast message
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                break;
        }
    }

}