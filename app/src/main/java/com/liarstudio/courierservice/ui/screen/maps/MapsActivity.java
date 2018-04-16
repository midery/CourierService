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
import android.widget.Button;
import android.widget.TextView;

import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liarstudio.courierservice.R;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    /*
    ****** FIELDS AREA ******
    */


    private GoogleMap gMap;

    private static final int PERMISSION_REQUEST_CODE = 1;

    TextView textViewCoordinates;
    Button buttonReady;
    LatLng location;


    /*
    ****** METHODS AREA ******
    */



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
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;


        Intent intent = getIntent();
        double[] coordinates = intent.getDoubleArrayExtra("coordinates");


        //Если запускается из главной формы
        if (getCallingActivity() == null) {

            //Ставим маркер на координатах и закрываем при нажатии на "Готово"
            location = new LatLng(coordinates[0], coordinates[1]);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
            gMap.addMarker(new MarkerOptions().position(location).title("Адрес доставки"));

            buttonReady.setOnClickListener(l -> finish());
        } else {
            //Иначе если координаты != 0, то ставим маркер на координаты...
            if (coordinates[0] != 0 && coordinates[1] != 0) {
                location = new LatLng(coordinates[0], coordinates[1]);

                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
                gMap.addMarker(new MarkerOptions().position(location).title("Адрес доставки"));
            } else
                //Если координаты равны 0, то просим пользователя разрешить определять местоположение и направляем камеру к нему
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);

            //Вешаем обработчики на кнопки, когда форма открыта на редактирование
            gMap.setOnMapClickListener(location -> {
                gMap.clear();
                this.location = location;
                gMap.addMarker(new MarkerOptions().position(location).title("Адрес доставки"));
            });

            buttonReady.setOnClickListener(v -> {
                if (location!=null) {
                    Intent data = new Intent();
                    data.putExtra("coordinates", new double[]{location.latitude, location.longitude});
                    setResult(RESULT_OK, data);
                    finish();
                }
            });

        }

    }


    /*
    * Функция, вызываемая после того, как пользователь ответил на запрос о выдаче прав на
    * определение местоположения.
    * Если на все результаты запроса был положительный ответ, а так же если у пользователя включено
    * определение местоположение по GPS или по сетям, центрируем камеру на нем.
    * Иначе - на офисе компании
    *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean valid = true;


        for (int perms : grantResults) {
            valid = perms == PackageManager.PERMISSION_GRANTED && valid;
        }


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Получаем статус GPS
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Получаем статус сети
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        //
        valid = valid && (isGPSEnabled || isNetworkEnabled);
        if (requestCode == PERMISSION_REQUEST_CODE && valid) {

            try {
                //gMap.setMyLocationEnabled(true);
                Location location;
                if (isNetworkEnabled)
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60, 10, this);

                if (isGPSEnabled)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 10, this);

                location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

                if (location == null)
                    loadDefault();
                else {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15.0f));


                }
            }catch (SecurityException se) {
                se.printStackTrace();
                loadDefault();
            }
        } else {
            loadDefault();
        }
    }

    void loadDefault() {
        LatLng defaultLoc = new LatLng(51.662805, 39.184641);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 15.0f));

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
