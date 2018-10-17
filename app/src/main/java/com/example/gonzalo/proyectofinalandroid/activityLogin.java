package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class activityLogin extends AppCompatActivity {

    private EditText etUsuario,etPassword;
    private Button btnRequest;
    private String url = "http://18b26002.ngrok.io/logusuario";
    private RequestQueue mRequestQueue;
    private JsonObjectRequest Request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario=findViewById(R.id.etUsuario);
        etPassword=findViewById(R.id.etPassword);

        btnRequest=findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v){
                                                   inicioSesion();
                                               }
                                           }
        );

    }

    public void inicioSesion(){
        final Intent intentMain = new Intent(this , FalsoMain.class);
        String urla = url + "/" + etUsuario.getText().toString() + "/" + etPassword.getText().toString();
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

                            intentMain.putExtra("nombre",nombre);
                            intentMain.putExtra("usuario",user);
                            intentMain.putExtra("email",email);
                            intentMain.putExtra("id",id);

                            startActivity(intentMain);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error: no se pudo iniciar sesion, el usuario o la contraseña son incorrectos\n"+error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(Request);
    }


    public void irRegistrarUsuario(View v){
        Intent i=new Intent(getApplicationContext(),activity_RegistrarUsuario.class);
        startActivity(i);
    }
}
