package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class FalsoMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Usuario usuario = new Usuario();
    WebView wb_inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falso_main);

        String nombre = getIntent().getStringExtra("nombre");
        String user = getIntent().getStringExtra("usuario");
        String email = getIntent().getStringExtra("email");
        String latitud=getIntent().getStringExtra("latitud");
        String longitud=getIntent().getStringExtra("longitud");
        int id = getIntent().getIntExtra("id", 0);

        //Casteo longitud y latitud
        double latitudUsuario=Double.parseDouble(latitud);
        double longitudUsuario=Double.parseDouble(longitud);

        usuario.setNombre(nombre);
        usuario.setUsuario(user);
        usuario.setCorreo(email);
        usuario.setLatitud((float) latitudUsuario);
        usuario.setLongitud((float) longitudUsuario);
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
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_colectivo) {
            Intent i=new Intent(this,CoordenadasDeUsuario.class);
            startActivity(i);
        } else if (id == R.id.nav_recorrido) {
            Intent i=new Intent(this,activity_conexion.class);
            startActivity(i);
        } else if (id == R.id.nav_paradas_cercanas) {
            mostrarPosicion();
        } else if (id == R.id.nav_puntos_de_recarga) {
            guardarPosicionDeUsuario(usuario.getId());

        } else if (id == R.id.nav_horarios) {

        } else if (id == R.id.nav_comentarios) {

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

    //metodo que manda las coordenadas al servidor para ser guradadas
    public void guardarPosicionDeUsuario(final int id){

        final double latitud=-43.30430;
        final double longitud=-65.04948;


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://ddc25220.ngrok.io/posicionUsuario";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(getApplicationContext(),"EXITO: se guradaron las coordenadas",Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(),"FALLO:"+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            //Añado parametros al POST
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("latitud", String.valueOf(latitud));
                params.put("longitud", String.valueOf(longitud));

                return params;
            }
        };
        queue.add(postRequest);

    }



    /*--------------METODOS PARA INTERACCION CON EL MAPA-------------------*/

    public void mostrarPosicion(){
        /*Metodo que obtiene la latitud y longitud ya guardados en la base de datos,es decir que previamente
        dichos datos tendran que haber sido guardados*/

        double latitud=usuario.getLatitud();

        double longitud=usuario.getLongitud();

        //Llamo a la funcion de javascript tambien llamada 'mostrarPosicion' y le paso 2 parametros(latitud,y longitud)
        wb_inicio.loadUrl("javascript:mostrarPosicion("+latitud+","+longitud+")");


    }

}
