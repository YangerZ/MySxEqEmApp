package com.example.zhoajie.sxeqem;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.Overlay;
import com.tianditu.android.maps.OverlayItem;
import com.tianditu.android.maps.overlay.MarkerOverlay;
import com.tianditu.android.maps.renderoption.DrawableOption;

/**
 * Created by Administrator on 2017/9/4.
 */

public class MapUtil extends Object {
    private MapView _MapView;
    public MapUtil(MapView _mapview,Object _mO)
    {
        _MapView=_mapview;
    }
    //做地理事情

    /**
     * 经纬度定位到当前地图
     * @param mLat  纬度
     * @param mLon  经度
     */


    private MarkerOverlay mMarker= null;
    public void locateByPosition(double mLat, double mLon, Drawable drw){
        if(mMarker!=null){
            _MapView.removeOverlay(mMarker);

        }
        float anchor[][] = { { 0.5f, 1.0f }, { 0.5f, (54.0f - 9.0f) / 54.0f },
                { 0.5f, 0.5f }, { 0.5f, 0.5f }, { 0.5f, 1.0f } };

        GeoPoint point = new GeoPoint((int) (mLat * 1E6), (int) (mLon * 1E6));
        DrawableOption option = new DrawableOption();
        option.setAnchor(anchor[0][0], anchor[0][1]);
        mMarker=new MarkerOverlay();
        mMarker.setOption(option);
        mMarker.setPosition(point);
        mMarker.setIcon(drw);
        mMarker.setTitle("震源位置：X:"+mLat+" Y:"+mLon);
        _MapView.addOverlay(mMarker);
    }
}
