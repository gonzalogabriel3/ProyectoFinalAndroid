package com.example.gonzalo.proyectofinalandroid;

import android.location.LocationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Long.valueOf;

public class FalsoMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Usuario usuario = new Usuario();
    WebView wb_inicio;
    private double latitudGPS,longitudGPS;
    LocationManager locationManager;
    String URL="http://20b196be.ngrok.io";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falso_main);

        String nombre = getIntent().getStringExtra("nombre");
        String user = getIntent().getStringExtra("usuario");
        String email = getIntent().getStringExtra("email");


        int id = getIntent().getIntExtra("id", 0);

        if(getIntent().hasExtra("latitud") || getIntent().hasExtra("longitud")) {
            String latitud = getIntent().getStringExtra("latitud");
            String longitud = getIntent().getStringExtra("longitud");
            //Casteo longitud y latitud del usuario
            double latitudUsuario = Double.parseDouble(latitud);
            double longitudUsuario = Double.parseDouble(longitud);
            usuario.setLatitud(latitudUsuario);
            usuario.setLongitud(longitudUsuario);

        }
        usuario.setNombre(nombre);
        usuario.setUsuario(user);
        usuario.setCorreo(email);
        usuario.setId(id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /****Mejora****/
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
        /********/


        wb_inicio = (WebView)findViewById(R.id.wb_inicio);

        WebSettings webSettings = wb_inicio.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wb_inicio.loadUrl("file:///android_asset/prueba.html");

        Toast.makeText(getApplicationContext(),"Bienvenido : " + usuario.getUsuario() ,Toast.LENGTH_SHORT).show();

        guardarPosicionDeUsuario(usuario.getId());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.falso_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_colectivo) {
            //vacio
        } else if (id == R.id.nav_recorrido) {
            //vacio
        } else if (id == R.id.nav_paradas_cercanas) {
            //vacio
        } else if (id == R.id.nav_puntos_de_recarga) {
            mostrarPuntos();

        } else if (id == R.id.nav_horarios) {
            //vacio
        } else if (id == R.id.nav_posicion) {
            guardarPosicionDeUsuario(usuario.getId());

        } else if (id == R.id.nav_sugerencias) {

        } else if (id == R.id.nav_perfil) {
            Intent i=new Intent(this,PerfilUsuario.class);
            i.putExtra("nombre",usuario.getNombre());
            i.putExtra("usuario",usuario.getUsuario());
            i.putExtra("email",usuario.getCorreo());
            i.putExtra("id",usuario.getId());

            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void AlertNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Habilite la Ubicacion para poder usar la aplicación, de lo contrario la aplicacion funcionara de forma incorrecta");

        builder.setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //perform any action
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        //creating alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Metodo que manda las coordenadas al servidor para ser guradadas
    public void guardarPosicionDeUsuario(final int id) {

        GPSTracker gt=new GPSTracker(getApplicationContext());

        //Llamo al metodo getLocation para obtener la localizacion actual
        Location localizacion = gt.getLocation();
        if (localizacion == null) {
            Toast.makeText(getApplicationContext(), "No se pudo obtener ubicacion-Asegurese de tener el GPS activado", Toast.LENGTH_LONG).show();
        } else {
            latitudGPS = localizacion.getLatitude();
            longitudGPS = localizacion.getLongitude();


            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = URL+"/posicionUsuario";

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Toast.makeText(getApplicationContext(), "EXITO: se guardaron las coordenadas" + response.toString(), Toast.LENGTH_LONG).show();

                            mostrarPosicion(latitudGPS,longitudGPS);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast.makeText(getApplicationContext(), "FALLO:" + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                //Añado parametros al POST
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(id));
                    params.put("latitud", String.valueOf(latitudGPS));
                    params.put("longitud", String.valueOf(longitudGPS));

                    return params;
                }
            };
            queue.add(postRequest);

        }
    }

    /*--------------METODOS PARA INTERACCION CON EL MAPA-------------------*/

    public void mostrarPosicion(double latitud, double longitud){

        usuario.setLatitud(latitud);
        usuario.setLongitud(longitud);


        //Llamo a la funcion de javascript tambien llamada 'mostrarPosicion' y le paso 2 parametros(latitud,y longitud)
        wb_inicio.loadUrl("javascript:mostrarPosicion("+latitud+","+longitud+")");


    }

    public void mostrarPuntos(){

        String url=URL+"/punto";
        //RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        //Transformo en String el JSON  que me devuelve el servidor,añado comillas simples para darle formato
                        String jsonString=response.toString();

                        //Reemplazo caracteres especiales ('\"') para que quede una cadena limpia
                        String jsonString2=jsonString.replace("\\"+'"',"");

                        //Toast.makeText(getApplicationContext(),jsonString2,Toast.LENGTH_LONG).show();

                        //LLamo a la funcion de javascript y le paso como parametro la cadena
                        wb_inicio.loadUrl("javascript:mostrarPuntos('"+jsonString2+"');");


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error: no se pudo mostrar los puntos de recarga\n"+error.toString(), Toast.LENGTH_LONG).show();
            }
    });
        mRequestQueue.add(Request);
    }

}
