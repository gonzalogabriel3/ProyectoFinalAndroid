package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class activity_conexion extends AppCompatActivity {

    private static final String TAG = activity_conexion.class.getName();
    private Button btnRequest;

    private RequestQueue mRequestQueue;
    private JsonObjectRequest Request;
    private String url = "http://f3b5211d.ngrok.io/logusuario";

    private EditText et_usuario,et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion);

        btnRequest = (Button) findViewById(R.id.buttonRequest);
        et_usuario = (EditText) findViewById(R.id.et_usuario);
        et_password = (EditText) findViewById(R.id.et_password);


        btnRequest.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v){
                                              Put();
                                          }
                                      }
        );
    }
    private void Put() {
        final Intent intentPerfil = new Intent(this , PerfilUsuario.class);
        String urla = url + "/" + et_usuario.getText().toString() + "/" + et_password.getText().toString();
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, urla, null ,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonarray = response.getJSONArray("usuario");

                            JSONObject usuario = jsonarray.getJSONObject(0);

                            int id = usuario.getInt("id");
                            String nombre = usuario.getString("nombre");
                            String user = usuario.getString("usuario");
                            String email = usuario.getString("email");


                            intentPerfil.putExtra("nombre", nombre);
                            intentPerfil.putExtra("user", user);
                            intentPerfil.putExtra("email", email);
                            intentPerfil.putExtra("id", id);
                            startActivity(intentPerfil);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
                Toast.makeText(getApplicationContext(),"Error: no se pudo iniciar sesion, el usuario o la contraseña fue incorrecto", Toast.LENGTH_LONG).show();//display the response on screen
            }
        });

        mRequestQueue.add(Request);
    }
}
