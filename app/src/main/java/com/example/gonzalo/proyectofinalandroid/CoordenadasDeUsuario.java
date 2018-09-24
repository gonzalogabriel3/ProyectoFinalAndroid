package com.example.gonzalo.proyectofinalandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CoordenadasDeUsuario extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    //TextView tvlatitud,tvlongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenadas_de_usuario);

    }

    public void finalizar(View view){
        finish();
    }
}