package edu.stevens.cs522.chat.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.chat.contracts.PeerContract;
import edu.stevens.cs522.chat.util.DateUtils;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable {

    public long id;

    public String name;

    // Last time we heard from this peer.
    public Date timestamp;

    public Double longitude;

    public Double latitude;

    public Peer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    //  add operations for parcels (Parcelable), cursors and contentvalues

    public Peer(Cursor cursor) {
        //
        this.id = Long.parseLong(PeerContract.getId(cursor));
        this.name = PeerContract.getName(cursor);
        this.timestamp = DateUtils.getDate(cursor,2);
        this.latitude=PeerContract.getLatitude(cursor);
        this.longitude=PeerContract.getLongitude(cursor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Peer(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.timestamp = DateUtils.readDate(in);
        this.latitude=in.readDouble();
        this.longitude=in.readDouble();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        DateUtils.writeDate(dest,this.timestamp);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public static final Parcelable.Creator<Peer> CREATOR = new Parcelable.Creator<Peer>() {
        public Peer createFromParcel(Parcel source) {
            return new Peer(source);
        }

        public Peer[] newArray(int size) {
            return new Peer[size];
        }
    };
    public void writeToProvider(ContentValues values) {
        PeerContract.putName(values, name);
        PeerContract.putTimestamp(values, timestamp);
        PeerContract.putLatitude(values,latitude);
        PeerContract.putLongitude(values,longitude);
    }
}
