package biz.thomaskeller.android.computescrollbug;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class MainActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";
    MapView mapView;
    MapListener mMapListener;

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapview);
        mMapListener = new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                count++;
                if(count == 100){
                    StackTraceElement[] trace = Thread.currentThread().getStackTrace();
                    for(int i=0; i<trace.length; i++)
                        Log.d(LOG_TAG, "onScroll: trace[i] = " + trace[i]);
                }
                Log.d(LOG_TAG, "onScroll");
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Log.d(LOG_TAG, "onZoom");
                return false;
            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.setBuiltInZoomControls(true);
        mapView.setMapListener(mMapListener);

        final MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(mapView.getContext()), mapView, new DefaultResourceProxyImpl(mapView.getContext()));
        mapView.getOverlays().add(mLocationOverlay);
        mLocationOverlay.enableMyLocation();

        findViewById(R.id.button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPoint myLocation = mLocationOverlay.getMyLocation();
                if (myLocation != null) {
                    mapView.getController().animateTo(myLocation);
                }
            }
        });
    }
}
