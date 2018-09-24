package com.example.gonzalo.proyectofinalandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CoordenadasDeUsuario extends AppCompatActivity {

    TextView tvlatitud,tvlongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenadas_de_usuario);

        tvlatitud=findViewById(R.id.tvlatitud);
        tvlongitud=findViewById(R.id.tvlongitud);

        //Verifico si tengo el permiso "ACCESS_FINE_LOCATION",para el uso del GPS
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        //Si el permiso del uso de GPS esta denegado
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);


            }
        }
    }

    //Metodo para obtener las coordenadas por medio del GPS
    public void obtenerCoordenadas(View v) {
        LocationManager locationManager = (LocationManager)CoordenadasDeUsuario.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            //Se ejecuta cuando cambia la ubicacion
            public void onLocationChanged(Location location) {
                tvlatitud.setText(""+location.getLatitude());
                tvlongitud.setText(""+location.getLongitude());
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
        };

        //Verifico si tengo el permiso "ACCESS_FINE_LOCATION",para el uso del GPS
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }



    //Metodo que finaliza el activity actual
    public void finalizar(View v){
        finish();
    }
}
