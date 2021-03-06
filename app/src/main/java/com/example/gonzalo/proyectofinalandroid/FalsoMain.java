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

import static java.lang.Long.getLong;
import static java.lang.Long.valueOf;

public class FalsoMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Usuario usuario = new Usuario();
    WebView wb_inicio;
    private double latitudGPS,longitudGPS;
    LocationManager locationManager;
    public String URL= "http://dondeestaelcole.ddns.net:8080";
    public static final int recorridoId=0;
    //RequestQueue initialized
    Timer timerUsuario,timerColectivo;
    TimerTask timerTaskUsuario,timerTaskColectivo;
    public static int idTramo=0;

    Boolean colectivo=false;

    //Se crea un Handler que contedra el llamado a la funcion de ubicacion
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falso_main);

        //Obtengo los Datos del Usuario que se recibieron
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
        //Setteo todos los datos del usuario en un objeto del tipo Usuario
        usuario.setNombre(nombre);
        usuario.setUsuario(user);
        usuario.setCorreo(email);
        usuario.setId(id);

        //Se definen y crean todos los datos del menu
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


        //Averiguo si el usuario tiene habilitada la ubicacion de lo contrario lanzo un AlertDialog pidiendo que lo haga
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /****Mejora****/
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
        /********/


        //Preparo los datos y cargo la vista del mapa
        wb_inicio = (WebView)findViewById(R.id.wb_inicio);

        WebSettings webSettings = wb_inicio.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wb_inicio.loadUrl("file:///android_asset/prueba.html");

        Toast.makeText(getApplicationContext(),"Bienvenido : " + usuario.getUsuario() ,Toast.LENGTH_SHORT).show();

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
        // Manejo de los Items del Navigation View
        int id = item.getItemId();

        //Item de Posicion del colectivo
        if (id == R.id.nav_colectivo) {

            //Si el timer del colectivo es null llamo a la funcion de obtencion de lo contrario se para el timer que lanza la busqueda del mismo
            if(timerColectivo==null){

                obtenerTramos();

            } else {

                stoptimertaskColectivo();

            }

        //Item de obtencion de recorridos
        } else if (id == R.id.nav_recorrido) {

            //URL del servidor
            String url = URL + "/recorrido";

            //Se crea una nueva cola
            RequestQueue mRequestQueue = Volley.newRequestQueue(this);

            //String Request inicializado
            JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        // obtenemos la respuesta del JSON request
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
                                    String id_recorrido = object.getString("id");
                                    nombresRecorridos.add(nombre_recorrido);
                                    idsRecorridos.add(id_recorrido);
                                }

                                Intent intent = new Intent(getApplicationContext(), listaRecorridoActivity.class);
                                intent.putExtra("nombresRecorridos", nombresRecorridos);
                                intent.putExtra("idsRecorridos", idsRecorridos);
                                startActivityForResult(intent, 1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), "Error: no se pueden mostrar los recorridos,intentelo nuevamente", Toast.LENGTH_LONG).show();
                }
            });

            //Añadimos el request a la cola
            mRequestQueue.add(Request);

         //Item que confirma que el usuario es Pasajero de un Tramo
        } else if (id == R.id.confirmar_pasajero) {

            tramoPasajero();

        //Item que busca las paradas cercanas a la posicion de un usuario
        } else if (id == R.id.nav_paradas_cercanas) {

            alertDialogRecorridos();

        //Item que busca los puntos de recarga cercanos a la posicion de un usuario
        } else if (id == R.id.nav_puntos_de_recarga) {

            mostrarPuntos();

        //Item que carga los horarios segun el Tramo Seleccionado
        } else if (id == R.id.nav_horarios) {

            mostrarHorarios();

        //Item que carga las tarifas de los Tramos
        } else if (id == R.id.nav_tarifas) {

            Intent i=new Intent(getApplicationContext(), tarifaActivity.class);
            startActivity(i);

        //Item que confirma que un usuario finalizo el viaje
        } else if (id == R.id.nav_finaliza_viaje) {

            finalizarViaje();

        //Item que muestra los datos de Perfil de un usuario
        } else if (id == R.id.nav_perfil) {

            Intent i=new Intent(this,PerfilUsuario.class);
            i.putExtra("nombre",usuario.getNombre());
            i.putExtra("usuario",usuario.getUsuario());
            i.putExtra("email",usuario.getCorreo());
            i.putExtra("id",usuario.getId());

            startActivity(i);

        }

        //Item que Cierra la Sesion de usuario
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
        startTimerUsuario();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        cerrarSesion(usuario.getId());

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
    public void startTimerUsuario() {
        //Setteo un nuevo Timer
        timerUsuario = new Timer();

        //Inicializo la funcion que va a funcionar para ubicar al usuario
        initializeTimerTaskUsuario();

        //Indico el tiempo de inicio despues que abro el activity y indico cada cuanto tiempo se va a llamar a la funcion
        timerUsuario.schedule(timerTaskUsuario, 5000, 20000);
    }

    //Funcion que cancela el funcionamiento del TimerTask
    public void stoptimertaskUsuario() {
        //stop the timer, if it's not already null
        if (timerUsuario != null) {
            timerUsuario.cancel();
            timerUsuario = null;
        }
    }

    //Funcion que se va a ejecutar cada vez que Se Termine el tiempo del Timer
    public void initializeTimerTaskUsuario() {

        timerTaskUsuario = new TimerTask() {
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

    //Funcion que crea settea el tiempo del Timer y inicia la funcion de ubicacion del colectivo
    public void startTimerColectivo() {
        //Setteo un nuevo Timer
        timerColectivo = new Timer();

        //Inicializo la funcion que va a funcionar para ubicar al usuario
        initializeTimerTaskColectivo();

        //Indico el tiempo de inicio despues que abro el activity y indico cada cuanto tiempo se va a llamar a la funcion
        timerColectivo.schedule(timerTaskColectivo, 0, 30000);
    }

    //Funcion que cancela el funcionamiento del TimerTask
    public void stoptimertaskColectivo() {

        //Se detiene el Timer si es que este no es nulo
        if (timerColectivo != null) {
            timerColectivo.cancel();
            timerColectivo = null;
            Toast.makeText(getApplicationContext(),"Se detuvo la busqueda del colectivo",Toast.LENGTH_SHORT).show();
        }
    }

    //Funcion que se va a ejecutar cada vez que Se Termine el tiempo del Timer
    public void initializeTimerTaskColectivo() {

        timerTaskColectivo = new TimerTask() {
            public void run() {

                //Creo un Handler que va a llamar a la funcion que ubica y obtiene la posicion del colectivo
                handler.post(new Runnable() {
                    public void run() {
                        obtenerColectivo(idTramo);
                    }
                });
            }
        };
    }

    //Funcion que lanza un Alert Dialog que se lanza si el usuario no tiene habilitado el GPS
    private void AlertNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Habilite la Ubicacion para poder usar la aplicación, de lo contrario la aplicacion no funcionara de forma correcta");

        builder.setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //accion que se lanza a partir de una seleccion
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        //se crea el alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Metodo que manda las coordenadas al servidor para ser guradadas
    public void guardarPosicionDeUsuario(final int id) {

        //Creo un Objeto de la Clase GPSTracker
        GPSTracker gt=new GPSTracker(getApplicationContext());

        //Llamo al metodo getLocation para obtener la localizacion actual
        Location localizacion = gt.getLocation();

        //Si no se encuentra su posicion se le informa al usuario que encienda el GPS o espere unos segundos, de lo contrario se procede a obtener y guardar su posicion
        if (localizacion == null) {

            Toast.makeText(getApplicationContext(), "No se pudo obtener ubicacion-Asegurese de tener el GPS activado, o espere unos segundos", Toast.LENGTH_SHORT).show();

        } else {

            latitudGPS = localizacion.getLatitude();
            longitudGPS = localizacion.getLongitude();

            //Se crea una nueva cola
            RequestQueue mRequestQueue = Volley.newRequestQueue(this);

            //URL del servidor
            String url = URL+"/posicionUsuario";

            //String Request inicializado
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
                            Toast.makeText(getApplicationContext(), "FALLO: no se pudo ubicar al usuario, se intentara de nuevo en varios segundos", Toast.LENGTH_SHORT).show();
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

            //Añadimos el request a la cola
            mRequestQueue.add(postRequest);
        }
    }

    /*--------------METODOS PARA INTERACCION CON EL MAPA-------------------*/

    public void mostrarPosicion(double latitud, double longitud){

        usuario.setLatitud(latitud);
        usuario.setLongitud(longitud);

        //Llamo a la funcion de javascript tambien llamada 'mostrarPosicion' y le paso 2 parametros(latitud,y longitud)
        wb_inicio.loadUrl("javascript:mostrarPosicion("+latitud+","+longitud+")");


    }
    //Funcion que muestra todos los Puntos de Recarga registrados en la cuidad
    public void mostrarPuntos(){

        //URL del servidor
        String url=URL+"/punto";

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //String Request inicializado
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

                Toast.makeText(getApplicationContext(),"Error: no se pudieron cargar los puntos de recarga intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });

        //Añadimos el request a la cola
        mRequestQueue.add(Request);
    }

    /*
        Funcion que Solicita los recorridos registrados en el servidor y los muestra en un alert,
        a partir de eso llama a la funcion que obtiene las paradas cercanas a la posicion de ese usuario
    */
    public void alertDialogRecorridos(){

        //URL del servidor
        String url=URL+"/recorrido";

        //RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //Inicializamos un JsonObject Request
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // obtenemos la respuesta del JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Obtenemos el arreglo correspondiente a los tramos que viene en la respuesta
                            JSONArray jsonarray = response.getJSONArray("recorridos");

                            //arreglo que contendra los nombre de los tramos
                            final String[] reconom = new String[jsonarray.length()];

                            //arreglo que contendra los id de los tramos
                            final int[] recoid = new int[jsonarray.length()];

                            for (int i = 0; i < jsonarray.length(); i++) {
                                // obtenemos el JSONObject del indice i
                                JSONObject recorrido = jsonarray.getJSONObject(i);
                                //cargamos el id en el arreglo
                                recoid[i] = recorrido.getInt("id");
                                //cargamos el nombre en el arreglo
                                reconom[i] = recorrido.getString("nombre");
                            }

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(FalsoMain.this);
                            mBuilder.setTitle("Elija un Recorrido:");
                            mBuilder.setSingleChoiceItems(reconom, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mostrarParadasCercanas(recoid[i]);
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error al obtener los recorrido del colectivo",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Añadimos el request a la cola
        mRequestQueue.add(Request);
    }

    public void mostrarParadasCercanas(int idRecorrido){

        //URL del servidor
        String url=URL+"/paradasCercanas/"+usuario.getId()+"/"+idRecorrido;

        //Se crea una nueva cola
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

                        //LLamo a la funcion de javascript y le paso como parametro la cadena
                        wb_inicio.loadUrl("javascript:mostrarParadas('"+jsonString2+"');");


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error: no se pudo mostrar las paradas cercanas, intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });

        //Añadimos el request a la cola
        mRequestQueue.add(Request);
    }

    public void mostrarRecorrido(int idRecorrido){

        //URL del servidor
        String url=URL+"/mapa/"+idRecorrido;

        //Se crea una nueva cola
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // obtenemos la respuesta del JSON request
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

                Toast.makeText(getApplicationContext(),"Error: no se pudo mostrar el recorrido, intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });

        //Añadimos el request a la cola
        mRequestQueue.add(Request);
    }

    private void cerrarSesion(int id) {

        //Se crea un Intent que llama a la Activity principal
        final Intent intentMain = new Intent(this , activityLogin.class);

        //URL del servidor
        String url=URL+"/logusuarioclose";

        //Creo un Objeto Json que contendra la id del usuario
        JSONObject usuario = new JSONObject();
        try
        {
            usuario.put("id", id);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Creo una nueva cola
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //String Request inicializado
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, usuario ,
                new Response.Listener<JSONObject>() {
                    // obtenemos la respuesta del JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        //Si el response contiene la variable message se lanza el intent
                        if (response.has("message")){
                            stoptimertaskUsuario();
                            stoptimertaskColectivo();
                            Toast.makeText(getApplicationContext(),"Se cerro la sesion correctamente",Toast.LENGTH_SHORT).show();
                            startActivity(intentMain);
                            finish();

                        } else {
                                Toast.makeText(getApplicationContext(),"No se Pudo cerrar sesion, intentelo nuevamente" + response.toString() ,Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"No se Pudo cerrar sesion, intentelo nuevamente", Toast.LENGTH_SHORT).show();
            }
        });

        //Añadimos el request a la cola
        mRequestQueue.add(Request);
    }

    /*
     * Funcion que obtiene los tramos disponibles desde el servidor y los envia a una funcion que muestra dichos tramos en un AlertDialog
     */
    public void obtenerTramos() {
        String url=URL+"/tramo";
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        //Inicializamos un JsonObject Request
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // obtenemos la respuesta del JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Obtenemos el arreglo correspondiente a los tramos que viene en la respuesta
                            JSONArray jsonarray = response.getJSONArray("tramos");

                            //arreglo que contendra los nombre de los tramos
                            String[] tramosnom = new String[jsonarray.length()];

                            //arreglo que contendra los id de los tramos
                            int[] tramosid = new int[jsonarray.length()];

                            for (int i = 0; i < jsonarray.length(); i++) {
                                // obtenemos el JSONObject del indice i
                                JSONObject tramo = jsonarray.getJSONObject(i);
                                //cargamos el id en el arreglo
                                tramosid[i] = tramo.getInt("id");
                                //cargamos el nombre en el arreglo
                                tramosnom[i] = tramo.getString("nombre");
                            }

                            //funcion que muestra los tramos en un alert
                            mostrarTramos(tramosnom,tramosid);

                            } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error al obtener los tramos del colectivo",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(Request);

    }

    /*
     * Funcion que muestra un AlertDialog que tiene los tramos disponibles y a partir de la seleccion de uno llama a la funcion que obtiene
     * la posicion del colectivo dentro de ese tramo
     */
    public void mostrarTramos(final String[] tramosnom,final int[] tramosid){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FalsoMain.this);
        mBuilder.setTitle("Elija un tramo:");
        mBuilder.setSingleChoiceItems(tramosnom, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                idTramo = tramosid[i];
                startTimerColectivo();
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //Funcion que recibe el id del tramo y a partir de eso obtiene la posicion del colectivo que esta en ese tramo
    public void obtenerColectivo(int id){

        //URL del servidor
        String url=URL+"/posicionColectivo/" + id;

        //Se crea una nueva cola
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //inicializamos un JsonObject Request
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // obtenemos la respuesta del JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("message")) {
                            try {
                                JSONArray jsarray = response.getJSONArray("colectivo");

                                JSONObject puntos = jsarray.getJSONObject(0);

                                double latitud = Double.valueOf(puntos.getString("latitud"));
                                double longitud = Double.valueOf(puntos.getString("longitud"));

                                //Llamo a la funcion de javascript tambien llamada 'mostrarPosicionColectivo' y le paso 2 parametros(latitud,y longitud)
                                wb_inicio.loadUrl("javascript:mostrarPosicionColectivo(" + latitud + "," + longitud + ")");


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "no se pudo obtener correctamente la posicion del colectivo", Toast.LENGTH_SHORT).show();

                            }

                        } else if (response.has("error")){
                            Toast.makeText(getApplicationContext(), "no se pudo obtener correctamente la posicion del colectivo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Añadimos el request a la cola
        mRequestQueue.add(Request);
    }

    /*
        Funcion que obtiene los tramos registrados en el servidor y los muestra en un alert,
        a partir de eso se lanza un main que obtiene los horarios de ese tramo
    */
    public void mostrarHorarios(){

        //URL del servidor
        String url=URL+"/tramo";

        //Intent que lanza el activity de horarios
        final Intent intent = new Intent(getApplicationContext(), activity_horario.class);

        //Se crea una cola
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //Inicializamos un JsonObject Request
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // obtenemos la respuesta del JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Obtenemos el arreglo correspondiente a los tramos que viene en la respuesta
                            JSONArray jsonarray = response.getJSONArray("tramos");

                            //arreglo que contendra los nombre de los tramos
                            final String[] reconom = new String[jsonarray.length()];

                            //arreglo que contendra los id de los tramos
                            final int[] recoid = new int[jsonarray.length()];

                            for (int i = 0; i < jsonarray.length(); i++) {
                                // obtenemos el JSONObject del indice i
                                JSONObject recorrido = jsonarray.getJSONObject(i);
                                //cargamos el id en el arreglo
                                recoid[i] = recorrido.getInt("id");
                                //cargamos el nombre en el arreglo
                                reconom[i] = recorrido.getString("nombre");
                            }

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(FalsoMain.this);
                            mBuilder.setTitle("Elija un Tramo:");
                            mBuilder.setSingleChoiceItems(reconom, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    intent.putExtra("recorrido",recoid[i]);
                                    startActivity(intent);
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error al obtener los recorrido del colectivo",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Añadimos el request a la cola
        mRequestQueue.add(Request);
    }

    /* funcion que obtiene los tramos registrados en el servidor, y a partir de la seleccion de uno
        se procede a llamar a la funcion que confirma que el usuario es pasajero del colectivo de ese tramo
     */
    public void tramoPasajero(){

        //URL del servidor
        String url=URL+"/tramo";

        //Se crea una nueva cola
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //Inicializamos un JsonObject Request
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // obtenemos la respuesta del JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Obtenemos el arreglo correspondiente a los tramos que viene en la respuesta
                            JSONArray jsonarray = response.getJSONArray("tramos");

                            //arreglo que contendra los nombre de los tramos
                            final String[] tramosnom = new String[jsonarray.length()];

                            //arreglo que contendra los id de los tramos
                            final int[] tramosid = new int[jsonarray.length()];

                            for (int i = 0; i < jsonarray.length(); i++) {
                                // obtenemos el JSONObject del indice i
                                JSONObject tramo = jsonarray.getJSONObject(i);
                                //cargamos el id en el arreglo
                                tramosid[i] = tramo.getInt("id");
                                //cargamos el nombre en el arreglo
                                tramosnom[i] = tramo.getString("nombre");
                            }

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(FalsoMain.this);
                            mBuilder.setTitle("Confirmar Tramo del que es Pasajero:");
                            mBuilder.setSingleChoiceItems(tramosnom, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    confirmarPasajero(usuario.getId(),tramosid[i]);
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error al obtener los tramos del colectivo",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Añadimos el request a la cola
        mRequestQueue.add(Request);
    }


    //Funcion que recibe el id del usuario y el id del tramo y se lo envia al servidor para que registre que ese usuario es pasajero del colectivo de ese tramo
    public void confirmarPasajero(final int idUsuario, final int idTramo){

        //URL del servidor
        String url = URL + "/pasajero";

        //Se crea una nueva cola
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //Objeto Json que contiene los id's
        JSONObject usuario = new JSONObject();
        try
        {
            usuario.put("id", idUsuario);
            usuario.put("tramo", idTramo);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //JsonRequest inicializado
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,usuario,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        if(response.has("message")){
                            Toast.makeText(getApplicationContext(),"EXITO: Estas confirmado como pasajero",Toast.LENGTH_LONG).show();
                        } else if(response.has("error")){
                            Toast.makeText(getApplicationContext(),"FALLO: No estas confirmado como pasajero",Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        //Añadimos el request a la cola
        mRequestQueue.add(postRequest);

    }
    //Funcion que toma el id del usuario y lo envia al servidor para que confirme que este se bajo del colectivo y por ende finalizo su viaje
    public void finalizarViaje(){

        //URL del servidor
        String url = URL + "/finalizarviaje/"+usuario.getId();

        //Se crea una nueva cola
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //JsonRequest inicializado
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                        // response
                        if(response.has("message")){
                            Toast.makeText(getApplicationContext(),"EXITO: Finalizaste el viaje correctamente",Toast.LENGTH_LONG).show();
                        } else if(response.has("error")){
                            Toast.makeText(getApplicationContext(),"FALLO: NO Finalizaste el viaje correctamente",Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        //Añadimos el request a la cola
        mRequestQueue.add(getRequest);
    }
}
