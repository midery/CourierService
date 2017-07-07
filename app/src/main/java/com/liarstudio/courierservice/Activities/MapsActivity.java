package com.liarstudio.courierservice.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liarstudio.courierservice.R;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int PERMISSION_REQUEST_CODE = 1;

    TextView textViewCoordinates;
    Button buttonReady;
    Button iconCenterMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textViewCoordinates = (TextView)findViewById(R.id.textViewCoordinates);
        buttonReady = (Button)findViewById(R.id.buttonReady);
        iconCenterMap = (Button)findViewById(R.id.iconCenterMap);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

        if (getCallingActivity() == null) {
            iconCenterMap.setVisibility(View.INVISIBLE);
            Intent intent = getIntent();
            if (intent.hasExtra("coordinates")) {
                double[] coordinates = intent.getDoubleArrayExtra("coordinates");

                LatLng gmapCoordinates = new LatLng(coordinates[0], coordinates[1]);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gmapCoordinates, 15.0f));
                mMap.addMarker(new MarkerOptions().position(gmapCoordinates).title("Адрес доставки"));
            }
            buttonReady.setOnClickListener(l -> finish());
        } else  {
            mMap.setOnCameraMoveListener(() -> textViewCoordinates.setText(mMap.getCameraPosition().target.toString()));

            buttonReady.setOnClickListener( l -> {
                Intent data = new Intent();
                double[] coordinates = {mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude} ;
                data.putExtra("coordinates", coordinates);
                setResult(RESULT_OK, data);
                finish();
            });

        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

           mMap.setMyLocationEnabled(true);

           LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
           Location location = service.getLastKnownLocation(service.getBestProvider(new Criteria(), false));
           LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

        } else {
            LatLng DSR = new LatLng(51.662805, 39.184641 );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DSR, 15.0f));
            // Permission was denied. Display an error message.
        }
        textViewCoordinates.setText(mMap.getCameraPosition().target.toString());
    }
}
