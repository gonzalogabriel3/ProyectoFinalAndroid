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

    EditText etNombre,etUsuario,etEmail,etPassword,etRepetir;
    Button btn;
    String URL="http://dondeestaelcole.ddns.net:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__registrar_usuario);

        etNombre=findViewById(R.id.etNombre);
        etUsuario=findViewById(R.id.etUsuario);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etRepetir=findViewById(R.id.etRepetir);

        btn=findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v){
                                              Post();
                                          }
                                      }
        );

    }
    public Boolean validarDatos(){
        if(etNombre.getText().toString().isEmpty() && etNombre.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),"Campo: Nombre se encuentra vacio o tiene menos de 5 caracteres",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etUsuario.getText().toString().isEmpty() && etUsuario.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),"Campo: Usuario se encuentra vacio o tiene menos de 5 caracteres",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etEmail.getText().toString().isEmpty() && etEmail.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),"Campo: Email se encuentra vacio o tiene menos de 5 caracteres",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etPassword.getText().toString().isEmpty() && etPassword.getText().toString().length() < 8){
            Toast.makeText(getApplicationContext(),"Campo: Password se encuentra vacio o tiene menos de 8 caracteres",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etRepetir.getText().toString().isEmpty() && etRepetir.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),"Campo: Repetir contraseña se encuentra vacio o tiene menos de 8 caracteres",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!etPassword.getText().toString().contentEquals(etRepetir.getText().toString())){
            Toast.makeText(getApplicationContext(),"Las contraseñas ingresadas son distintas",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void Post(){
        Boolean valido;

        valido = validarDatos();

        if(valido == true) {
            //intent que llama a la pantalla de inicio
            final Intent intentLogin = new Intent(this, activityLogin.class);

            //se crea una nueva cola
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            //URL del servidor
            String url = URL + "/usuario";

            //StringRequest inicializado
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("error")){
                                Toast.makeText(getApplicationContext(), "No se pudo registrar, ya que esa direccion de email ya esta en uso", Toast.LENGTH_SHORT).show();
                            } else {
                                // response
                                Toast.makeText(getApplicationContext(), "EXITO: Te has registrado correctamente", Toast.LENGTH_LONG).show();
                                startActivity(intentLogin);
                                finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast.makeText(getApplicationContext(), "FALLO: Error al registrar usuario, por favor intentelo nuevamente", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                //Añado parametros al POST
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("nombre", etNombre.getText().toString());
                    params.put("usuario", etUsuario.getText().toString());
                    params.put("email", etEmail.getText().toString());
                    params.put("password", etPassword.getText().toString());

                    return params;
                }
            };

            //añadimos el request a la cola
            queue.add(postRequest);
        } else {
            Toast.makeText(getApplicationContext(),"Error en el Formulario",Toast.LENGTH_SHORT).show();
        }
    }
}
