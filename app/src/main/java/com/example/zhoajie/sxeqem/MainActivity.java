package com.example.zhoajie.sxeqem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapController;
import com.tianditu.android.maps.MapView;

import com.tianditu.android.maps.MapView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private MapView mMapView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_locate:
                    mTextMessage.setText(R.string.title_locate);
                    return true;
                case R.id.navigation_data:
                    mTextMessage.setText(R.string.title_data);
                    return true;
                case R.id.navigation_estimate:
                    mTextMessage.setText(R.string.title_estimate);
                    return true;
                case R.id.navigation_dynamic:
                    mTextMessage.setText(R.string.title_dynamic);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //地图视图
        mMapView = (MapView) findViewById(R.id.main_mapview);
        MapController mMapController = mMapView.getController();
        mMapView.setBuiltInZoomControls(true);
        GeoPoint point = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
        mMapController.setCenter(point);
        mMapController.setZoom(12);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mMapView.onDestroy();
        super.onDestroy();
    }

}
