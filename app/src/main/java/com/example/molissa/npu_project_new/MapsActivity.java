package com.example.molissa.npu_project_new;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    Button findcrab;
    Button find_users_marks;
    Button find_office_marks;
    String url;
    String image_Url;
    String ID;
    ImageView image;
    Boolean not_first_time_showing_info_window = false;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    int RandomGPSswitch;
    LatLng MyPlace;
    Location location;


    private void findview(){
        findcrab = (Button) findViewById(R.id.find_crabs);
        find_office_marks = (Button) findViewById(R.id.find_office_marks);
        find_users_marks = (Button) findViewById(R.id.find_users_marks);
        find_office_marks.setOnClickListener(new BtnOnClick());
        find_users_marks.setOnClickListener(new BtnOnClick());
        findcrab.setOnClickListener(new BtnOnClick());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        findview();
        createLocationRequest();
        searchingGPS();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
        } else
            mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束地圖程式嗎?")
                    .setIcon(R.mipmap.ic_icon)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    }).show();
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setOnInfoWindowClickListener(OnInfoClick);
        mMap.getUiSettings().setCompassEnabled(false); //關閉預設指南針
        mMap.getUiSettings().setZoomGesturesEnabled(true); //關閉縮放手勢
        mMap.getUiSettings().setTiltGesturesEnabled(true); //開啟角度調整手勢
        mMap.getUiSettings().setMapToolbarEnabled(false);   //關閉不必要的工具列
        mMap.getUiSettings().setRotateGesturesEnabled(true);   //啟用鏡頭旋轉
        mMap.getUiSettings().setScrollGesturesEnabled(true); //鎖住鏡頭移動平移手勢

        // Add a marker in Sydney and move the camera


        LatLng ChingluoWetlandGPS = new LatLng(23.598570, 119.646612);
        mMap.addCircle(new CircleOptions().center(ChingluoWetlandGPS).radius(100).strokeColor(Color.GREEN));

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(ChingluoWetlandGPS)
                        .tilt(45)
                        .zoom(18)
                        .build()));

    }


    private void searchingGPS() {
        RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
        url = "http://120.127.16.69/crab/link1.php?ca=select*from%20myorder";
        JsonArrayRequest GPSJson = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject GPS = (JSONObject) response.get(i);
                        Double Lat = GPS.getDouble("Lat");
                        Double Lon = GPS.getDouble("Lon");
                        String title = GPS.getString("Name");
                        LatLng NewGPS = new LatLng(Lat, Lon);
                        if (i < 30) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(NewGPS)
                                    .title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        } else {
                            mMap.addMarker(new MarkerOptions()
                                    .position(NewGPS)
                                    .title(title));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View infoWindow = getLayoutInflater().inflate(R.layout.map_info, null);
                        TextView title = ((TextView) infoWindow.findViewById(R.id.txtTitle));
                        title.setText(marker.getTitle());

                        image = ((ImageView) infoWindow.findViewById(R.id.map_image));
                        ID = marker.getId().substring(1);
                        int Number = Integer.valueOf(ID) + 1;
                        image_Url = "http://120.127.16.69/crab/upload_png/crab-" + Number + ".png";
                        if (not_first_time_showing_info_window) {
                            Picasso.with(MapsActivity.this).load(image_Url).into(image);
                        } else { // if it's the first time, load the image with the callback set
                            not_first_time_showing_info_window = true;
                            Picasso.with(MapsActivity.this).load(image_Url).placeholder(R.mipmap.crab).into(image, new InfoWindowRefresher(marker));
                        }
                        return infoWindow;
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(GPSJson);
    }

    private void searchingofficeGPS() {
        RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
        url = "http://120.127.16.69/crab/link1.php?ca=select*from%20myorder";
        JsonArrayRequest GPSJson = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < 30; i++) {
                        JSONObject GPS = (JSONObject) response.get(i);
                        Double Lat = GPS.getDouble("Lat");
                        Double Lon = GPS.getDouble("Lon");
                        String title = GPS.getString("Name");
                        LatLng NewGPS = new LatLng(Lat, Lon);
                        mMap.addMarker(new MarkerOptions()
                                .position(NewGPS)
                                .title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View infoWindow = getLayoutInflater().inflate(R.layout.map_info, null);
                        TextView title = ((TextView) infoWindow.findViewById(R.id.txtTitle));
                        title.setText(marker.getTitle());

                        image = ((ImageView) infoWindow.findViewById(R.id.map_image));
                        ID = marker.getId().substring(1);
                        int Number = Integer.valueOf(ID) + 1;
                        image_Url = "http://120.127.16.69/crab/upload_png/crab-" + Number + ".png";
                        if (not_first_time_showing_info_window) {
                            Picasso.with(MapsActivity.this).load(image_Url).into(image);
                        } else { // if it's the first time, load the image with the callback set
                            not_first_time_showing_info_window = true;
                            Picasso.with(MapsActivity.this).load(image_Url).placeholder(R.mipmap.crab).into(image, new InfoWindowRefresher(marker));
                        }
                        return infoWindow;
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(GPSJson);
    }

    private void searchinguserGPS() {
        RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
        url = "http://120.127.16.69/crab/link1.php?ca=select*from%20myorder";
        JsonArrayRequest GPSJson = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 30; i < response.length(); i++) {
                        JSONObject GPS = (JSONObject) response.get(i);
                        Double Lat = GPS.getDouble("Lat");
                        Double Lon = GPS.getDouble("Lon");
                        String title = GPS.getString("Name");
                        LatLng NewGPS = new LatLng(Lat, Lon);
                        if (i < 30) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(NewGPS)
                                    .title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        } else {
                            mMap.addMarker(new MarkerOptions()
                                    .position(NewGPS)
                                    .title(title));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View infoWindow = getLayoutInflater().inflate(R.layout.map_info, null);
                        TextView title = ((TextView) infoWindow.findViewById(R.id.txtTitle));
                        title.setText(marker.getTitle());

                        image = ((ImageView) infoWindow.findViewById(R.id.map_image));
                        ID = marker.getId().substring(1);
                        int Number = Integer.valueOf(ID) + 1;
                        image_Url = "http://120.127.16.69/crab/upload_png/crab-" + Number + ".png";
                        if (not_first_time_showing_info_window) {
                            Picasso.with(MapsActivity.this).load(image_Url).into(image);
                        } else { // if it's the first time, load the image with the callback set
                            not_first_time_showing_info_window = true;
                            Picasso.with(MapsActivity.this).load(image_Url).placeholder(R.mipmap.crab).into(image, new InfoWindowRefresher(marker));
                        }
                        return infoWindow;
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(GPSJson);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {

            Toast.makeText(getApplicationContext(), "請確定手機的GPS定位是否開啟", Toast.LENGTH_LONG).show();
            // Blank for a moment...
        } else {
            MyPlace = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(MyPlace)
                            .tilt(45)
                            .zoom(18)
                            .build()));

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        MyPlace = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(MyPlace)
                        .tilt(45)
                        .zoom(18)
                        .build()));
    }

    private void handleNewLocation(Location location) {
        double ramdomGPS = Math.random()/2000;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.question_mark);
        LatLng ramdomCrab1 = new LatLng(location.getLatitude()+ramdomGPS, location.getLongitude()+ramdomGPS);
        LatLng ramdomCrab2 = new LatLng(location.getLatitude()+ramdomGPS, location.getLongitude()-ramdomGPS);
        LatLng ramdomCrab3 = new LatLng(location.getLatitude()-ramdomGPS, location.getLongitude()+ramdomGPS);
        LatLng ramdomCrab4 = new LatLng(location.getLatitude()-ramdomGPS, location.getLongitude()-ramdomGPS);
        LatLng ramdomCrab5 = new LatLng(location.getLatitude()-ramdomGPS, location.getLongitude());
        LatLng ramdomCrab6 = new LatLng(location.getLatitude(), location.getLongitude()-ramdomGPS);
        LatLng ramdomCrab7 = new LatLng(location.getLatitude()+ramdomGPS, location.getLongitude());
        LatLng ramdomCrab8 = new LatLng(location.getLatitude(), location.getLongitude()+ramdomGPS);
        RandomGPSswitch = (int)(Math.random()*8)+1;
        switch (RandomGPSswitch){
            case 1:
                mMap.addMarker(new MarkerOptions()
                        .icon(icon)
                        .title("我是誰?")
                        .position(ramdomCrab1));
                break;
            case 2:
                mMap.addMarker(new MarkerOptions()
                    .icon(icon)
                    .title("我是誰?")
                    .position(ramdomCrab2));
                break;
            case 3:
                mMap.addMarker(new MarkerOptions()
                        .icon(icon)
                        .title("我是誰?")
                        .position(ramdomCrab3));
                break;
            case 4:
                mMap.addMarker(new MarkerOptions()
                        .icon(icon)
                        .title("我是誰?")
                        .position(ramdomCrab4));
                break;
            case 5:
                mMap.addMarker(new MarkerOptions()
                        .icon(icon)
                        .title("我是誰?")
                        .position(ramdomCrab5));
                break;
            case 6:
                mMap.addMarker(new MarkerOptions()
                        .icon(icon)
                        .title("我是誰?")
                        .position(ramdomCrab6));
                break;
            case 7:
                mMap.addMarker(new MarkerOptions()
                        .icon(icon)
                        .title("我是誰?")
                        .position(ramdomCrab7));
                break;
            case 8:
                mMap.addMarker(new MarkerOptions()
                        .icon(icon)
                        .title("我是誰?")
                        .position(ramdomCrab8));
                break;
        }

    }

    private class InfoWindowRefresher implements Callback {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            markerToRefresh.showInfoWindow();
            not_first_time_showing_info_window = false;
        }

        @Override
        public void onError() {
            not_first_time_showing_info_window = false;
        }
    }


    private GoogleMap.OnInfoWindowClickListener OnInfoClick = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String crab_name = marker.getTitle();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if (crab_name != null && !crab_name.equals("")) {
                intent.setClass(MapsActivity.this, EncyclopediaActivity2.class);
                bundle.putString("Name", crab_name);
                //將Bundle物件assign給intent
                intent.putExtras(bundle);
                //切換Activity
                startActivity(intent);
            } else {
                intent.setClass(MapsActivity.this, BiometricsActivity.class);
                ID = marker.getId().substring(1);
                int Number = Integer.valueOf(ID) + 1;
                image_Url = "http://120.127.16.69/crab/upload_png/crab-" + Number + ".png";
                bundle.putString("MapImage", image_Url);
                bundle.putString("ID", String.valueOf(Number));
                intent.putExtras(bundle);
                //切換Activity
                startActivity(intent);
            }
        }
    };


    private class BtnOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.find_crabs:
                    not_first_time_showing_info_window = false;
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(MyPlace)
                                    .tilt(45)
                                    .zoom(18)
                                    .build()));
                    searchingGPS();

                    /*if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    handleNewLocation(location);*/
                    break;
                case R.id.find_office_marks:
                    not_first_time_showing_info_window = false;
                    LatLng ChingluoWetlandGPS = new LatLng(23.598570, 119.646612);
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(ChingluoWetlandGPS)
                                    .tilt(45)
                                    .zoom(18)
                                    .build()));
                    searchingofficeGPS();

                    break;
                case R.id.find_users_marks:
                    not_first_time_showing_info_window = false;
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(MyPlace)
                                    .tilt(45)
                                    .zoom(18)
                                    .build()));
                    searchinguserGPS();

                    break;
            }
        }
    }

}
