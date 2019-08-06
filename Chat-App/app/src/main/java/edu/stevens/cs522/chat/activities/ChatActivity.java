/*********************************************************************

    Chat server: accept chat messages from clients.
    
    Sender chatName and GPS coordinates are encoded
    in the messages, and stripped off upon receipt.

    Copyright (c) 2017 Stevens Institute of Technology

**********************************************************************/
package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.async.QueryBuilder;
import edu.stevens.cs522.chat.contracts.MessageContract;
import edu.stevens.cs522.chat.entities.ChatMessage;
import edu.stevens.cs522.chat.entities.Peer;
import edu.stevens.cs522.chat.managers.MessageManager;
import edu.stevens.cs522.chat.managers.PeerManager;
import edu.stevens.cs522.chat.managers.TypedCursor;
import edu.stevens.cs522.chat.rest.ChatHelper;
import edu.stevens.cs522.chat.settings.Settings;
import edu.stevens.cs522.chat.util.DateUtils;
import edu.stevens.cs522.chat.util.ResultReceiverWrapper;

public class ChatActivity extends Activity implements OnClickListener, QueryBuilder.IQueryListener<ChatMessage>, ResultReceiverWrapper.IReceive {

	final static public String TAG = ChatActivity.class.getCanonicalName();
		
    /*
     * UI for displaying received messages
     */
	private SimpleCursorAdapter messages;
	
	private ListView messageList;

    private SimpleCursorAdapter messagesAdapter;

    private MessageManager messageManager;

    private PeerManager peerManager;

    /*
     * Widgets for dest address, message text, send button.
     */
    private EditText chatRoomName;

    private EditText messageText;

    private Button sendButton;


    /*
     * Helper for Web service
     */
    private ChatHelper helper;

    /*
     * For receiving ack when message is sent.
     */
    private ResultReceiverWrapper sendResultReceiver;
	
	/*
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //  initialize sendResultReceiver
        sendResultReceiver = new ResultReceiverWrapper(new Handler());

        setContentView(R.layout.messages);
        messageList = (ListView) findViewById(R.id.message_list);
        chatRoomName=(EditText) findViewById(R.id.chat_room);
        messageText = (EditText) findViewById(R.id.message_text);
        sendButton = (Button) findViewById(R.id.send_button);
        //  use SimpleCursorAdapter to display the messages received.
        String[] from = {MessageContract.SENDER, MessageContract.MESSAGE_TEXT};
        int [] to ={R.id.sender,R.id.message};
        messagesAdapter=new SimpleCursorAdapter(this, R.layout.message, null, from, to,0);
        messageList.setAdapter(messagesAdapter);
        //  create the message and peer managers, and initiate a query for all messages
        peerManager = new PeerManager(this);
        messageManager = new MessageManager(this);
        messageManager.getAllMessagesAsync(this);
        //  instantiate helper for service
        helper=new ChatHelper(this,sendResultReceiver);

        sendButton.setOnClickListener(this);
        /**
         * Initialize settings to default values.
         */
		if (!Settings.isRegistered(this)) {
			//  launch registration activity
            Intent register=new Intent(this, RegisterActivity.class);
            startActivity(register);
            return;
		}




    }

	public void onResume() {
        super.onResume();
        sendResultReceiver.setReceiver(this);
    }

    public void onPause() {
        super.onPause();
        sendResultReceiver.setReceiver(null);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //  inflate a menu with PEERS and SETTINGS options
        getMenuInflater().inflate(R.menu.chatserver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            //  PEERS provide the UI for viewing list of peers
            case R.id.peers:
                Intent intent1 = new Intent(this, ViewPeersActivity.class);
                startActivity(intent1);
                break;

            //  SETTINGS provide the UI for settings
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            default:
        }
        return false;
    }



    /*
     * Callback for the SEND button.
     */
    public void onClick(View v) {
        if (helper != null) {

            String chatRoom;
            String message = null;

            //  get chatRoom and message from UI, and use helper to post a message
            chatRoom=chatRoomName.getText().toString();
            message=messageText.getText().toString();
            helper.postMessage(chatRoom,message);
            Log.i(ChatActivity.class.getCanonicalName(),"177");
//            helper.postMessage(chatRoom,message);
            Log.i(ChatActivity.class.getCanonicalName(),"179");
            //  add the message to the database
            final ChatMessage newMessage=new ChatMessage();
            newMessage.sender=Settings.getChatName(this);
            newMessage.setTimestamp(DateUtils.now());
            newMessage.setMessageText(message);
            Peer peer = new Peer();
            peer.setName(newMessage.sender);
            peer.setTimestamp(newMessage.getTimestamp());
            Log.i(ChatActivity.class.getCanonicalName(),"190");
            messageManager.persistAsync(newMessage);
            //rp.perform(new PostMessageRequest(Settings.getChatName(this),Settings.getClientId(this),chatRoom,message));

            /*peerManager.persistAsync(peer, new IContinue<Long>() {
                @Override
                public void kontinue(Long id) {
                    Log.i(ChatActivity.class.getCanonicalName(),"persisting peer and message");
                    newMessage.senderId = id;
                    messageManager.persistAsync(newMessage);
                }
            });*/


            // End

            Log.i(TAG, "Sent message: " + message);

            messageText.setText("");
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case RESULT_OK:
                if (resultCode == RESULT_OK)

                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
                //  show a success toast message
                break;
            default:
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                //  show a failure toast message
                break;
        }
    }

    @Override
    public void handleResults(TypedCursor<ChatMessage> results) {
        //
        messagesAdapter.swapCursor(results.getCursor());
    }

    @Override
    public void closeResults() {
        //
        messagesAdapter.swapCursor(null);
    }

}