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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;

public class activity_RegistrarUsuario extends AppCompatActivity {

    EditText etNombre,etUsuario,etEmail,etPassword;
    Button btn;
    String URL="http://dondeestaelcole.ddns.net:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__registrar_usuario);

        etNombre=findViewById(R.id.etNombre);
        etUsuario=findViewById(R.id.etUsuario);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);

        btn=findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v){
                                              Post();
                                          }
                                      }
        );

    }

    public void Post(){
        final Intent intentLogin = new Intent(this , activityLogin.class);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = URL+"/usuario";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,null,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(getApplicationContext(),"EXITO: Te has registrado correctamente",Toast.LENGTH_LONG).show();
                        startActivity(intentLogin);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(),"FALLO: Error al registrar usuario/ "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            //Añado parametros al POST
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("nombre", etNombre.getText().toString());
                params.put("usuario", etUsuario.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());

                return params;
            }
        };
        queue.add(postRequest);

    }
}
