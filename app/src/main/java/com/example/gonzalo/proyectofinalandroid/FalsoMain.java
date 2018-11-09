package com.example.gonzalo.proyectofinalandroid;

import android.app.Activity;
import android.location.LocationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Long.valueOf;

public class FalsoMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Usuario usuario = new Usuario();
    WebView wb_inicio;
    private double latitudGPS,longitudGPS;
    LocationManager locationManager;
    public String URL="http://dd5cbfad.ngrok.io";
    public static final int recorridoId=0;

    Timer timer;
    TimerTask timerTask;

    //Se crea un Handler que contedra el llamado a la funcion de ubicacion
    final Handler handler = new Handler();

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
    //MENU DE SELECCION
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_colectivo) {
            //vacio
        } else if (id == R.id.nav_recorrido) {

            String url=URL+"/recorrido";
            //RequestQueue initialized
            RequestQueue mRequestQueue = Volley.newRequestQueue(this);

            //String Request initialized
            JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                    new Response.Listener<JSONObject>() {
                        // Takes the response from the JSON request
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ArrayList<String> nombresRecorridos = new ArrayList<String>();
                                ArrayList<String> idsRecorridos = new ArrayList<String>();

                                JSONArray jsonarray = response.getJSONArray("recorridos");

                                JSONObject usuario = jsonarray.getJSONObject(0);

                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);
                                    String nombre_recorrido = object.getString("nombre");
                                    String id_recorrido=object.getString("id");
                                    nombresRecorridos.add(nombre_recorrido);
                                    idsRecorridos.add(id_recorrido);
                                }

                                Intent intent = new Intent(getApplicationContext(), listaRecorridoActivity.class);
                                intent.putExtra("nombresRecorridos", nombresRecorridos);
                                intent.putExtra("idsRecorridos", idsRecorridos);
                                startActivityForResult(intent,1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(),"Error: no se pueden mostrar los recorridos,intentelo nuevamente", Toast.LENGTH_LONG).show();
                }
            });

            mRequestQueue.add(Request);



        } else if (id == R.id.nav_paradas_cercanas) {
            mostrarParadasCercanas();
        } else if (id == R.id.nav_puntos_de_recarga) {
            mostrarPuntos();

        } else if (id == R.id.nav_horarios) {
            //vacio
        } else if (id == R.id.nav_tarifas) {
             irTarifas();
        } else if (id == R.id.nav_posicion) {
            guardarPosicionDeUsuario(usuario.getId());

        } else if (id == R.id.nav_perfil) {
            Intent i=new Intent(this,PerfilUsuario.class);
            i.putExtra("nombre",usuario.getNombre());
            i.putExtra("usuario",usuario.getUsuario());
            i.putExtra("email",usuario.getCorreo());
            i.putExtra("id",usuario.getId());

            startActivity(i);

        }
        else if (id == R.id.nav_cerrar_sesion) {
            cerrarSesion(usuario.getId());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //FIN MENU DE SELECCION

    @Override
    protected void onResume() {
        super.onResume();

        //onResume va a iniciar el timer cuando la aplicacion este en primer plano
        startTimer();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String recorridoId=data.getStringExtra("recorridoId");
                int recorrido_id=Integer.parseInt(recorridoId);
                //Toast.makeText(getApplicationContext(),"id: "+recorrido_id,Toast.LENGTH_LONG).show();
                mostrarRecorrido(recorrido_id);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    //Funcion que crea settea el tiempo del Timer y inicia la funcion de ubicacion del usuario
    public void startTimer() {
        //Setteo un nuevo Timer
        timer = new Timer();

        //Inicializo la funcion que va a funcionar para ubicar al usuario
        initializeTimerTask();

        //Indico el tiempo de inicio despues que abro el activity y indico cada cuanto tiempo se va a llamar a la funcion
        timer.schedule(timerTask, 5000, 45000);
    }

    /*
    Funcion que cancela el funcionamiento del TimerTask
    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
*/
    //Funcion que se va a ejecutar cada vez que Se Termine el tiempo del Timer
    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //Creo un Handler que va a llamar a la funcion que ubica y guarda la posicion del usuario
                handler.post(new Runnable() {
                    public void run() {

                        guardarPosicionDeUsuario(usuario.getId());

                    }
                });
            }
        };
    }

    private void AlertNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Habilite la Ubicacion para poder usar la aplicación, de lo contrario la aplicacion no funcionara de forma correcta");

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
            Toast.makeText(getApplicationContext(), "No se pudo obtener ubicacion-Asegurese de tener el GPS activado, o espere unos segundos", Toast.LENGTH_LONG).show();
        } else {
            latitudGPS = localizacion.getLatitude();
            longitudGPS = localizacion.getLongitude();


            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = URL+"/posicionUsuario";

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mostrarPosicion(latitudGPS,longitudGPS);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast.makeText(getApplicationContext(), "FALLO: no se pudo ubicar al usuario, se intentara de nuevo en varios segundos", Toast.LENGTH_LONG).show();
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

    public void irTarifas(){
        Intent i=new Intent(getApplicationContext(), tarifaActivity.class);
        startActivity(i);
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

                Toast.makeText(getApplicationContext(),"Error: no se pudieron cargar los puntos de recarga intente nuevamente", Toast.LENGTH_LONG).show();
            }
    });
        mRequestQueue.add(Request);
    }

    public void mostrarParadasCercanas(){

        String url=URL+"/paradasCercanas/"+usuario.getId();

        //RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

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

                        //LLamo a la funcion de javascript y le paso como parametro la cadena
                        wb_inicio.loadUrl("javascript:mostrarParadas('"+jsonString2+"');");


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error: no se pudo mostrar las paradas cercanas, intente nuevamente", Toast.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(Request);
    }

    public void mostrarRecorrido(int idRecorrido){

        String url=URL+"/mapa/"+idRecorrido;

        //RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

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

                        //LLamo a la funcion de javascript y le paso como parametro la cadena
                        wb_inicio.loadUrl("javascript:mostrarRecorrido('"+jsonString2+"');");


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error: no se pudo mostrar el recorrido, intente nuevamente", Toast.LENGTH_LONG).show();
            }
        });
        mRequestQueue.add(Request);
    }

    private void cerrarSesion(int id) {
        final Intent intentMain = new Intent(this , activityLogin.class);

        String url=URL+"/logusuarioclose";

        //RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);


        JSONObject usuario = new JSONObject();
        try
        {
            usuario.put("id", id);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //String Request initialized
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, usuario ,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                            if (response.has("message")){

                                Toast.makeText(getApplicationContext(),"Se cerro la sesion correctamente",Toast.LENGTH_SHORT).show();
                                startActivity(intentMain);

                            } else {

                                Toast.makeText(getApplicationContext(),"No se pudo cerrar la sesion correctamente intentelo nuevamente",Toast.LENGTH_SHORT).show();

                            }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"No se pudo cerrar la sesion correctamente intentelo nuevamente", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(Request);
    }

}
