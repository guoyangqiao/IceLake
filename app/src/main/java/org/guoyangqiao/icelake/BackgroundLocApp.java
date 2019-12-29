package org.guoyangqiao.icelake;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import org.guoyangqiao.icelake.callback.LocationListener;

public class BackgroundLocApp extends AppCompatActivity {
    private static final String TAG = "MAIN";

    public static final String CHANNEL_ICE_LAKE = "org.guoyangqiao.icelake";
    private LocationClient locationClient;
    private boolean started = false;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mediaPlayer = MediaPlayer.create(this, R.raw.sakura_drop);
        this.mediaPlayer.setLooping(true);
        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ImageView locateView = findViewById(R.id.start_img);
        locateView.setImageIcon(Icon.createWithResource(this, R.drawable.routing));
        EditText checkRadius = findViewById(R.id.check_radius);
        KeyListener checkRadiusKeyListener = checkRadius.getKeyListener();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createChannels(mNotificationManager);
        LocationListener locationListener = new LocationListener(findViewById(R.id.latitude), findViewById(R.id.longitude), checkRadius, findViewById(R.id.distance), locateView, mediaPlayer, this);
        locationClient = initLocationClient(locationListener);
        locationClient.start();
        locateView.setOnClickListener(event -> {
                    int imgId;
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ICE_LAKE)
                            .setVisibility(Notification.VISIBILITY_PRIVATE)
                            .setContentText("target distance " + checkRadius.getText())
                            .setContentTitle("We are watching you!!!")
                            .setSmallIcon(R.drawable.routing)
                            .setAutoCancel(true);
                    if (started) {
                        locationListener.stop();
                        locationClient.disableLocInForeground(true);
                        started = false;
                        imgId = R.drawable.routing;
                        checkRadius.setKeyListener(checkRadiusKeyListener);
                    } else {
                        locationListener.start();
                        locationClient.enableLocInForeground(1, notificationBuilder.build());
                        started = true;
                        imgId = R.drawable.safari;
                        checkRadius.setKeyListener(null);
                    }
                    hideKeyBoard();
                    locateView.setImageIcon(Icon.createWithResource(getApplicationContext(), imgId));
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
        locationOption.setOpenAutoNotifyMode(0, 0, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//        locationOption.setOpenAutoNotifyMode();
        locationClient.setLocOption(locationOption);
        return locationClient;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
        vibrator.cancel();
    }

    public void createChannels(NotificationManager notificationManager) {
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ICE_LAKE, "ICE_LAKE", NotificationManager.IMPORTANCE_HIGH);
        mChannel.setShowBadge(true);
        notificationManager.createNotificationChannel(mChannel);
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
