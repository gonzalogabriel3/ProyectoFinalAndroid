package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        etNombre=findViewById(R.id.etNombre);
        etCorreo=findViewById(R.id.etCorreo);
        etPassword=findViewById(R.id.etPassword);
        etUsuario=findViewById(R.id.etUsuario);
        btn=findViewById(R.id.btnRegistrar);
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

        String url ="http://f3b5211d.ngrok.io/usuario";
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(getApplicationContext(),"EXITO :" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Toast.makeText(getApplicationContext(),"ERROR :No se pudo crear el usuario"+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            )

            {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nombre", etNombre.getText().toString());
                params.put("usuario", etUsuario.getText().toString());
                params.put("email", etCorreo.getText().toString());
                params.put("password",etPassword.getText().toString());

                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }


}
