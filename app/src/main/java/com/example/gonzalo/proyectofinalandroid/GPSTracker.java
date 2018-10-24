package com.example.gonzalo.proyectofinalandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

public class GPSTracker implements LocationListener {
    Context context;

    //En el constructor se pasa un contexto,en este caso sera el MainActivity porque es ahi donde se va a crear una instancia de esta clase
    public GPSTracker(Context context) {
        super();
        this.context = context;
    }

    //Metodo que devuelve la localizacion de un usuario,si todo esta bien retorna un objeto de tipo LOCATION
    public Location getLocation(){
        //Verifico que el permiso ACCESS_FINE_LOCATION no este denegado,si lo esta se retorna null
        if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            Log.e("fist","error");
            return null;
        }
        try {
            /*Creo un nuevo objeto de la clase LocationManager,esta clase proporciona acceso a los servicios de localización del sistema.
            Estos servicios permiten a las aplicaciones obtener actualizaciones periódicas de la ubicación geográfica del dispositivo*/
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            //Mediante locationManager obtengo si el GPS esta activado o no
            boolean GPSActivado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (GPSActivado){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000,10,this);
                /*El metodo "getLastKnowLocation"  de locationManager devuelve una ubicación que indica los datos de la última corrección de ubicación conocida obtenida
                del proveedor determinado.
                */
                /*Creo un nuevo objeto de tipo Location,"Location" es una clase de datos que representa una ubicación geográfica.
                Un location puede constar de latitud, longitud, marca de tiempo y otra información, como rumbo, altitud y velocidad.*/
                Location localizacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return localizacion;
            }else{
                Log.e("sec","errpr");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //Al ser una interfaz "LocationListener",deben definirse estos metodos,pero no hace falta implementarlos
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
