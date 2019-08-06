package edu.stevens.cs522.chat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import edu.stevens.cs522.chat.R;
import edu.stevens.cs522.chat.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_KEY = "peer";
    public static TextView peerName, peerLatitude, peerLongitude, peerLastTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        Peer peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }

        //  init the UI
        peerName = (TextView) findViewById(R.id.view_user_name);
        peerLastTimestamp = (TextView) findViewById(R.id.view_timestamp);
        peerLatitude = (TextView) findViewById(R.id.latitude);
        peerLongitude = (TextView) findViewById(R.id.longitude);
        peerName.setText(peer.getName());
        peerLastTimestamp.setText(peer.getTimestamp().toString()+"");
        peerLatitude.setText(peer.getLatitude().toString());
        peerLongitude.setText(peer.getLongitude().toString());

    }

}
