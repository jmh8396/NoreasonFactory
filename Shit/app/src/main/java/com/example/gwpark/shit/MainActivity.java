package com.example.gwpark.shit;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gwpark.shit.Data.Place;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener {

    private static final String LOG_TAG = "MapView";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.mapView) MapView mMapView;

    @BindView(R.id.vpfragment) View vpFragment;

    public static ArrayList<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMapView.setDaumMapApiKey("ffff34bc54ffab743160f13bfa261013");
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setPOIItemEventListener(this);
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
//        createDefaultMarker(mMapView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.title);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                drawerView.bringToFront();

            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        vpFragment.setVisibility(View.GONE);
        vpFragment.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                return false;
            }
        });

        downloadDB();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (vpFragment.getVisibility() == View.VISIBLE) {
                vpFragment.setVisibility(View.GONE);
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorite) {
            // Handle the camera action
        } else if (id == R.id.nav_criterion) {

        } else if (id == R.id.nav_proposal) {

        } else if (id == R.id.nav_group) {

        } else if (id == R.id.nav_info) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void controlMapMove(int which) {
        switch (which) {
            case 0: // Move to
            {
                mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
            }
            break;
            case 1: // Zoom to
            {
                mMapView.setZoomLevel(7, true);
            }
            break;
            case 2: // Move and Zoom to
            {
                mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(33.41, 126.52), 9, true);
            }
            break;
            case 3: // Zoom In
            {
                mMapView.zoomIn(true);
            }
            break;
            case 4: // Zoom Out
            {
                mMapView.zoomOut(true);
            }
            break;
            case 5: // Rotate Map 60, Unrotate Map
            {
                if (mMapView.getMapRotationAngle() == 0.0f) {
                    mMapView.setMapRotationAngle(60.0f, true);
                } else {
                    mMapView.setMapRotationAngle(0.0f, true);
                }
            }
            break;
        }
    }

    /**
     * 지도 타일 컨트롤.
     */
    private void controlMapTile(int which) {
        switch (which) {
            case 0: // Standard
            {
                mMapView.setMapType(MapView.MapType.Standard);
            }
            break;
            case 1: // Satellite
            {
                mMapView.setMapType(MapView.MapType.Satellite);
            }
            break;
            case 2: // Hybrid
            {
                mMapView.setMapType(MapView.MapType.Hybrid);
            }
            break;
            case 3: // HD Map Tile On/Off
            {
                if (mMapView.getMapTileMode() == MapView.MapTileMode.HD2X) {
                    //Set to Standard Mode
                    mMapView.setMapTileMode(MapView.MapTileMode.Standard);
                } else if (mMapView.getMapTileMode() == MapView.MapTileMode.HD) {
                    //Set to HD 2X Mode
                    mMapView.setMapTileMode(MapView.MapTileMode.HD2X);
                } else {
                    //Set to HD Mode
                    mMapView.setMapTileMode(MapView.MapTileMode.HD);
                }
            }
            break;
            case 4: // Clear Map Tile Cache
            {
                MapView.clearMapTilePersistentCache();
            }
            break;
        }
    }

    //	/////////////////////////////////////////////////////////////////////////////////////////////////
    // net.daum.mf.map.api.MapView.OpenAPIKeyAuthenticationResultListener

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int resultCode, String resultMessage) {
        Log.i(LOG_TAG,	String.format("Open API Key Authentication Result : code=%d, message=%s", resultCode, resultMessage));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // net.daum.mf.map.api.MapView.MapViewEventListener

    public void onMapViewInitialized(MapView mapView) {
        Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
        //mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.5666103, 126.9783882), 2, true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapCenterPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DaumMapLibrarySample");
        alertDialog.setMessage(String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DaumMapLibrarySample");
        alertDialog.setMessage(String.format("Long-Press on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewSingleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewDragStarted (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewDragEnded (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewMoveFinished (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
        Log.i(LOG_TAG, String.format("MapView onMapViewZoomLevelChanged (%d)", zoomLevel));
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        ViewPagerFragment.sectionsPagerAdapter.notifyViewPagerDataSetChanged(mapPOIItem);
        vpFragment.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    public static class ViewPagerFragment extends Fragment {
        @BindView(R.id.container) ViewPager mViewPager;
        @BindView(R.id.tabs) TabLayout tabs;
        static PagerAdapter sectionsPagerAdapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet, container, false);
            ButterKnife.bind(this, view);
            mViewPager.setOffscreenPageLimit(3);
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            sectionsPagerAdapter = new PagerAdapter(getChildFragmentManager(), getContext(), PagerAdapter.TabItem.INFO, PagerAdapter.TabItem.PHOTO, PagerAdapter.TabItem.REVIEW);
            mViewPager.setAdapter(sectionsPagerAdapter);
            tabs.setupWithViewPager(mViewPager);
        }
    }

    public class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            ((ImageView) mCalloutBalloon.findViewById(R.id.badge)).setImageResource(R.drawable.ic_menu_slideshow);
            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }

    private void downloadDB() {
        // DB를 download하기 위한 Thread를 만든다
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // URL에 연결한다
                    URL url = new URL("http://ec2-52-78-183-64.ap-northeast-2.compute.amazonaws.com:80/atofree.db");
                    URLConnection conn = url.openConnection();

                    InputStream is = conn.getInputStream();

                    // Internal data 폴더가 없으면 생성한다
                    File dir = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/" + getPackageName());
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    // Internal data 폴더에 파일 이름까지 쓰고 파일이 있으면 지우고 생성한다
                    File target = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/" + getPackageName() + "/atofree.db");
                    if (target.exists()) {
                        target.delete();
                    }
                    target.createNewFile();

                    // File을 쓰기 위한 Stream
                    FileOutputStream fos = new FileOutputStream(target);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int bufferLength = 0;
                    int totalLength = 0;

                    // buffer를 설정하고 InputStream에서 불러와 파일에 저장한다
                    byte[] buffer = new byte[1024];
                    while((bufferLength = is.read(buffer)) > 0) {
                        bos.write(buffer, 0, bufferLength);
                        bos.flush();
                        totalLength += bufferLength;
                    }

                    bos.close();
                    fos.close();
                    is.close();

                    // 파일을 저장한 후 Internal Data의 DB를 불러온다
                    loadDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start(); // Thread 작동 시작
    }

    private void loadDB() {
        // Internal data에 있는 DB를 불러오고 쿼리를 날린다
        SQLiteDatabase db = SQLiteDatabase.openDatabase(Environment.getDataDirectory().getAbsolutePath() + "/data/" + getPackageName() + "/atofree.db", null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        Cursor cursor = db.rawQuery("select * from place", null);

        // 받아온 Data를 DataSet에 추가한다
        places = new ArrayList<>();
        while (cursor.moveToNext()) {
            places.add(new Place(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
        }

        for (int i=0;i<places.size();i++) {
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(places.get(i).name);
            marker.setTag(Integer.parseInt(places.get(i).pid));

            String[] sp = places.get(i).location.split(",");
            Log.d("test", sp[0]+", "+sp[1]);
            MapPoint mp = MapPoint.mapPointWithGeoCoord(Float.parseFloat(sp[0]), Float.parseFloat(sp[1]));

            marker.setMapPoint(mp);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mMapView.addPOIItem(marker);
        }

        cursor.close();
        db.close();
    }
}
