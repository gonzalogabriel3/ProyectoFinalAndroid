package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CrearUsuario extends AppCompatActivity{
    private static final String TAG = activity_conexion.class.getName();

    EditText etNombre,etCorreo,etPassword,etUsuario;
    Button btn;
    private RequestQueue mRequestQueue;
    private StringRequest postRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        etNombre=(EditText) findViewById(R.id.etNombre);
        etCorreo=(EditText) findViewById(R.id.etCorreo);
        etPassword=(EditText) findViewById(R.id.etPassword);
        etUsuario=(EditText) findViewById(R.id.etUsuario);

        btn=(Button) findViewById(R.id.btnRegistrar);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Post();
                    }
                }
        );

    }

    private void Post() {

        String url ="http://dondeestaelcole.ddns.net:8080/usuario";

        JSONObject json = new JSONObject();
        try {
            json.put("nombre",etNombre.getText().toString());
            json.put("usuario",etUsuario.getText().toString());
            json.put("email",etCorreo.getText().toString());
            json.put("password",etPassword.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Usuario Creado",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"El Usuario no se pudo crear",Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }


}
