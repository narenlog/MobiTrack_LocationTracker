package com.parse.anywall;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.parse.Parse;
import com.parse.ParseObject;
import com.testflightapp.lib.TestFlight;

public class Application extends android.app.Application {
  // Debugging switch
  public static final boolean APPDEBUG = false;
  
  // Debugging tag for the application
  public static final String APPTAG = "AnyWall";

  // Key for saving the search distance preference
  private static final String KEY_SEARCH_DISTANCE = "searchDistance";

  private static SharedPreferences preferences;

  public Application() {
  }

  @Override
  public void onCreate() {
    super.onCreate();

    
  //Initialize TestFlight with your app token.
    TestFlight.takeOff(this, "18afa9a9-6d6a-4769-95c5-2d00541ae758");
    
    ParseObject.registerSubclass(AnywallPost.class);
   /* Parse.initialize(this, "YOUR_PARSE_APPLICATION_ID",
        "YOUR_PARSE_CLIENT_KEY");*/
        Parse.initialize(this, "71adOetxcx2qGt2CTBuaIM6kwqq9cl8w2f0npLGQ",
        "PwQW3XOSTh9bFBEnZgDs2Gj865lipKwgnwLD73U5");
    preferences = getSharedPreferences("com.parse.anywall", Context.MODE_PRIVATE);
    
    
 // output debug to LogCat, with tag LittleFluffyLocationLibrary
    LocationLibrary.showDebugOutput(true);

    try {
        // in most cases the following initialising code using defaults is probably sufficient:
        //
        // LocationLibrary.initialiseLibrary(getBaseContext(), "com.your.package.name");
        //
        // however for the purposes of the test app, we will request unrealistically frequent location broadcasts
        // every 1 minute, and force a location update if there hasn't been one for 2 minutes.
        LocationLibrary.initialiseLibrary(getBaseContext(), 60 * 1000, 2 * 60 * 1000, "mobi.littlefluffytoys.littlefluffytestclient");
        //LocationLibrary.useFineAccuracyForRequests(true);
    }
    catch (UnsupportedOperationException ex) {
        Log.d("TestApplication", "UnsupportedOperationException thrown - the device doesn't have any location providers");
    }
    
  }

  public static float getSearchDistance() {
    return preferences.getFloat(KEY_SEARCH_DISTANCE, 250);
  }

  public static void setSearchDistance(float value) {
    preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
  }

}
