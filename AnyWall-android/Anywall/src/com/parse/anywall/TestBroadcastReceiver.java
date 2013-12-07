package com.parse.anywall;

import java.util.Date;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocationBroadcastReceiver", "onReceive: received location update");
        
        final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
        
        // The broadcast has woken up your app, and so you could do anything now - 
        // perhaps send the location to a server, or refresh an on-screen widget.
        // We're gonna create a notification.
        
        // Construct the notification.
        Notification notification = new Notification(R.drawable.notification, "Locaton updated " + locationInfo.getTimestampAgeInSeconds() + " seconds ago", System.currentTimeMillis());

        Intent contentIntent = new Intent(context, TestActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        notification.setLatestEventInfo(context, "Location update broadcast received", "Timestamped " + LocationInfo.formatTimeAndDay(locationInfo.lastLocationUpdateTimestamp, true), contentPendingIntent);
        
        // Trigger the notification.
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1234, notification);
        
        
        //---- START : TRACK A SCHOOL VAN'S LOCATION EVERY TIME IT MOVES. Logic copied from "postButton" onClick() method.
        final ParseGeoPoint myPoint = new ParseGeoPoint(locationInfo.lastLat, locationInfo.lastLong);

        if(ParseUser.getCurrentUser() == null){
        	return;
        }
        
        // Create a post.
        AnywallPost post = new AnywallPost();
        // Set the location to the current user's location
        post.setLocation(myPoint);
        post.setText("Background Location : " + new Date() + " Lat/Long : "+ myPoint.getLatitude()+"/"+ myPoint.getLongitude());
        post.setUser(ParseUser.getCurrentUser());
        ParseACL acl = new ParseACL();
        // Give public read access
        acl.setPublicReadAccess(true);
        post.setACL(acl);
        // Save the post
        post.saveInBackground(new SaveCallback() {
        	@Override
        	public void done(ParseException e) {
        		 // Update the list view and the map
        		
                //doListQuery();
                //doMapQuery();
        	}
        });
        //---- END : TRACK A SCHOOL VAN'S LOCATION EVERY TIME IT MOVES.
        
    }
}