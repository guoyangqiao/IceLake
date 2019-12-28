package org.guoyangqiao.icelake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.baidu.geofence.GeoFenceClient;
import com.baidu.geofence.model.DPoint;

class GeoFenceApp extends AppCompatActivity {
    private static final String TAG = "GEO_FENCE";
    public static final String GEO_FENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";

    private GeoFenceClient geoFenceClient;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geoFenceClient = new GeoFenceClient(getApplicationContext());
        geoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN_OUT);
        geoFenceClient.addGeoFence(new DPoint(66.49, 34.432022), GeoFenceClient.BD09LL, 100, "Xuhui Garden");
        geoFenceClient.createPendingIntent(GEO_FENCE_BROADCAST_ACTION);
        geoFenceClient.setGeoFenceListener((list, errorCode, s) -> {
            if (errorCode == com.baidu.geofence.GeoFence.ADDGEOFENCE_SUCCESS) {//判断围栏是否创建成功
                Log.d(TAG, "fence created");
            } else {
                Log.d(TAG, "fence create failed");
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEO_FENCE_BROADCAST_ACTION);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GEO_FENCE_BROADCAST_ACTION)) {
                    Bundle bundle = intent.getExtras();
                    int status = bundle.getInt(com.baidu.geofence.GeoFence.BUNDLE_KEY_FENCESTATUS);
                    String customId = bundle.getString(com.baidu.geofence.GeoFence.BUNDLE_KEY_CUSTOMID);
                    String fenceId = bundle.getString(com.baidu.geofence.GeoFence.BUNDLE_KEY_FENCEID);
                    com.baidu.geofence.GeoFence fence = bundle.getParcelable(com.baidu.geofence.GeoFence.BUNDLE_KEY_FENCE);
                }
            }
        }, filter);
    }
}
