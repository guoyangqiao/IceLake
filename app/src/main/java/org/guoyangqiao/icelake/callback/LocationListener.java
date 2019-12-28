package org.guoyangqiao.icelake.callback;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;

import java.text.DecimalFormat;
import java.util.Arrays;

public class LocationListener extends BDAbstractLocationListener {
    private static final String TAG = "LOCATION";
    private final ImageView locateView;
    private boolean start_calc;
    private TextView latitude_view, longitude_view, distance_view;
    private EditText check_range_view;

    private double dest_latitude, dest_longitude, check_radius;
    private double current_latitude, current_longitude, current_radius;

    private float[] distance = new float[1];

    private static final View.OnClickListener NOOP = (c) -> {

    };

    public LocationListener(TextView latitude_view, TextView longitude_view, EditText check_range_view, TextView distance, ImageView locateView) {
        this.latitude_view = latitude_view;
        this.longitude_view = longitude_view;
        this.check_range_view = check_range_view;
        this.distance_view = distance;
        this.locateView = locateView;
    }

    private void setCurrent(double latitude, double longitude, double radius) {
        this.latitude_view.setText(String.valueOf(latitude));
        this.longitude_view.setText(String.valueOf(longitude));
        this.current_latitude = latitude;
        this.current_longitude = longitude;
        this.current_radius = radius;
    }

    public void start() {
//        setDistanceView("0.00");
        this.dest_latitude = current_latitude;
        this.dest_longitude = current_longitude;
        this.check_radius = Double.parseDouble(check_range_view.getText().toString());
        this.start_calc = true;
    }

    public void stop() {
        this.start_calc = false;
        setDistanceView(null);
    }

    public void onReceiveLocation(BDLocation location) {
        setCurrent(location.getLatitude(), location.getLongitude(), location.getRadius());
        if (start_calc) {
            Location.distanceBetween(this.current_latitude, this.current_longitude, this.dest_latitude, this.dest_longitude, distance);
            setDistanceView(getDistanceStr());
        } else {
            setDistanceView(null);
        }
        if (distance[0] > check_radius) {
            locateView.callOnClick();
        }
        Log.d(TAG, toString());
    }

    private String getDistanceStr() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distance[0]);
    }

    private void setDistanceView(Object val) {
        distance_view.setText(val != null ? String.valueOf(val) : null);
    }

    /**
     * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
     * 自动回调，相同的diagnosticType只会回调一次
     *
     * @param locType           当前定位类型
     * @param diagnosticType    诊断类型（1~9）
     * @param diagnosticMessage 具体的诊断信息释义
     */
    public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
        Log.d(TAG, diagnosticType + "");
        if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {
            //建议打开GPS
        } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {
            //建议打开wifi，不必连接，这样有助于提高网络定位精度！
        } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {
            //定位权限受限，建议提示用户授予APP定位权限！
        } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {
            //网络异常造成定位失败，建议用户确认网络状态是否异常！
        } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {
            //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！
        } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {
            //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！
        } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {
            //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！
        } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {
            //百度定位服务端定位失败
            //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com
        } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {
            //无法获取有效定位依据，但无法确定具体原因
            //建议检查是否有安全软件屏蔽相关定位权限
            //或调用LocationClient.restart()重新启动后重试！
        }
    }

    public String toString() {
        return "LocationListener{" +
                "dest_latitude=" + dest_latitude +
                ", dest_longitude=" + dest_longitude +
                ", check_radius=" + check_radius +
                ", current_latitude=" + current_latitude +
                ", current_longitude=" + current_longitude +
                ", current_radius=" + current_radius +
                ", distance=" + Arrays.toString(distance) +
                '}';
    }
}