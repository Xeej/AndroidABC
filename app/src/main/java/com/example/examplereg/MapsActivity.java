package com.example.examplereg;

import androidx.annotation.ColorRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LatLng Lastlatlng;
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="New Cordinates:"+latitude + ": "+longitude;
            Toast.makeText(MapsActivity.this,msg, Toast.LENGTH_LONG).show();
           /* MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));*/
            mMap.addPolyline(new PolylineOptions()
                    .add(Lastlatlng)
                    .add(new LatLng(latitude,longitude)).width(8F)
                    .color(Color.RED));
            Lastlatlng = new LatLng(latitude,longitude);
           // Polyline polyline = mMap.addPolyline(polyOptions);
            //polylines.add(polyline);

          //  mMap.addPolyline(new PolylineOptions().add(new LatLng(latitude,longitude)));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

            Toast.makeText(MapsActivity.this,"onStatusChanged", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MapsActivity.this,"onProviderEnabled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MapsActivity.this,"onProviderDisabled", Toast.LENGTH_LONG).show();
        }
    };



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 777;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {
                //показываем диалог
                new AlertDialog.Builder(this)
                        .setTitle("?")
                        .setMessage("??")
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Юзер одобрил
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                //запрашиваем пермишен, уже не показывая диалогов с пояснениями
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // пермишен получен можем работать с locationManager
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 40,1,locationListener);
                        Toast.makeText(MapsActivity.this,"Permission accept", Toast.LENGTH_LONG).show();
                    }

                } else {

                    //пермишен не был получее =(
                    Toast.makeText(MapsActivity.this,"Permission disabled", Toast.LENGTH_LONG).show();

                }
                return;
            }

        }
    }

public double youlg,yoult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (checkLocationPermission())
        {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,5,locationListener);

           // Toast.makeText(MapsActivity.this,var.toString(), Toast.LENGTH_LONG).show();

               Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                 if (location!=null)
                 {
                     youlg=location.getLongitude();
                     yoult=location.getLatitude();
                 }
            else Toast.makeText(MapsActivity.this,"No Permissions", Toast.LENGTH_LONG).show();
        }






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
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng you = new LatLng(yoult,youlg);
       // mMap.addMarker(new MarkerOptions().position(you).title("Its you"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(you));
        Lastlatlng = you;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you,17));


        /*mMap.addPolyline(new PolylineOptions()
                .add(Lastlatlng)
                .add(new LatLng(33,34))
                .add(new LatLng(23,34)).width(8F)
                .color(Color.RED));*/

    }
}
