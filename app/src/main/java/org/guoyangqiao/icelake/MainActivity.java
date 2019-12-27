package org.guoyangqiao.icelake;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import org.guoyangqiao.icelake.callback.OnStartCallback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        for (String access : ACCESS_COARSE_LOCATION) {
            if (ContextCompat.checkSelfPermission(this, access) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        LocationClient locationClient = initLocationOption();
        View viewById = findViewById(R.id.button2);
        viewById.setOnClickListener((v -> {
            Log.d(TAG, viewById.toString() + " start.....");
            locationClient.start();
            Log.d(TAG, viewById.toString() + " end.....");
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化定位参数配置
     *
     * @return
     */

    private LocationClient initLocationOption() {
        LocationClient locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new OnStartCallback());
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
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
        locationOption.setOpenAutoNotifyMode(3000, 10, LocationClientOption.LOC_SENSITIVITY_LOW);
        locationClient.setLocOption(locationOption);
        return locationClient;
    }
}
