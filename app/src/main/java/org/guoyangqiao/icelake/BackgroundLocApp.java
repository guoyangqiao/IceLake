package org.guoyangqiao.icelake;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import org.guoyangqiao.icelake.callback.OnStartCallback;

public class BackgroundLocApp extends AppCompatActivity {
    private static final String TAG = "MAIN";

    public static final String CHANNEL_ICE_LAKE = "org.guoyangqiao.icelake";

    private LocationClient locationClient;
    private Notification notification;
    private boolean inFrontState = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationClient = initLocationClient(new OnStartCallback());
        createChannels();
        notification = new Notification.Builder(getApplicationContext(), CHANNEL_ICE_LAKE)
                .setContentTitle("IceLake")
                .setContentText("My destiny")
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true).build();
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音

        Button viewById = (Button) findViewById(R.id.button2);
        viewById.setOnClickListener(event -> {
                    if (inFrontState) {
                        inFrontState = false;
                        locationClient.disableLocInForeground(true);
                        viewById.setText("Start");
                        locationClient.stop();
                    } else {
                        locationClient.enableLocInForeground(1, notification);
                        inFrontState = true;
                        viewById.setText("Stop");
                        locationClient.start();
                    }
                }
        );
    }

    /**
     * 初始化定位参数配置
     *
     * @return
     */

    private LocationClient initLocationClient(BDAbstractLocationListener listener) {
        LocationClient locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(listener);
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("gcj02");
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setNeedDeviceDirect(false);
        locationOption.setLocationNotify(true);
        locationOption.setIgnoreKillProcess(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setIsNeedLocationPoiList(true);
        locationOption.SetIgnoreCacheException(false);
        locationOption.setOpenGps(true);
        locationOption.setIsNeedAltitude(false);
//        locationOption.setOpenAutoNotifyMode(3000, 10, LocationClientOption.LOC_SENSITIVITY_LOW);
        locationOption.setOpenAutoNotifyMode();
        locationClient.setLocOption(locationOption);
        return locationClient;
    }


    public void createChannels() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel androidChannel = new NotificationChannel(CHANNEL_ICE_LAKE, "ANDROID CHANNEL", NotificationManager.IMPORTANCE_DEFAULT);
        androidChannel.enableLights(true);
        androidChannel.enableVibration(true);
        androidChannel.setLightColor(Color.GREEN);
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationManager.createNotificationChannel(androidChannel);
    }

}
