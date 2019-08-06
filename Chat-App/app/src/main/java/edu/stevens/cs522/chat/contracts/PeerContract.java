package edu.stevens.cs522.chat.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

import static android.drm.DrmStore.DrmObjectType.CONTENT;

/**
 * Created by dduggan.
 */

public class PeerContract extends BaseContract {

    public static final Uri CONTENT_URI = CONTENT_URI(AUTHORITY, "Peer");

    public static final Uri CONTENT_URI(long id) {
        return CONTENT_URI(Long.toString(id));
    }

    public static final Uri CONTENT_URI(String id) {
        return withExtendedPath(CONTENT_URI, id);
    }

    public static final String CONTENT_PATH = CONTENT_PATH(CONTENT_URI);

    public static final String CONTENT_PATH_ITEM = CONTENT_PATH(CONTENT_URI("#"));

    public static final String CONTENT_TYPE = "vnd.android.cursor/vnd."
            + AUTHORITY + "."
            + CONTENT + "s";
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "."
            + CONTENT;

    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String TIMESTAMP = "timestamp";

    public static String getTimestamp(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(TIMESTAMP));
    }

    public static void putTimestamp(ContentValues values, Date timestamp) {
        values.put(TIMESTAMP, timestamp.toString());
    }

    public static Double getLatitude(Cursor c) {
        return c.getDouble(c.getColumnIndexOrThrow(LATITUDE));
    }

    public static void putLatitude(ContentValues values, Double latitude) {
        values.put(LATITUDE, latitude);
    }

    public static Double getLongitude(Cursor c) {
        return c.getDouble(c.getColumnIndexOrThrow(LONGITUDE));
    }

    public static void putLongitude(ContentValues values, Double longitude) {
        values.put(LONGITUDE, longitude);
    }

    public static String getName(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(NAME));
    }

    public static void putName(ContentValues values, String name) {
        values.put(NAME, name);
    }

    public static String getId(Cursor c) {
        return c.getString(c.getColumnIndexOrThrow(ID));
    }

    public static void putId(ContentValues values, String id) {
        values.put(ID, id);
    }

    //  define column names, getters for cursors, setters for contentvalues


}
