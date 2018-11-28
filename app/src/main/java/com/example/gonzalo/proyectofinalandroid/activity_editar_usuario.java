package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class activity_editar_usuario extends AppCompatActivity {

    EditText etNombre,etUsuario,etEmail,etPassword,etRepetir;
    int id;
    public String URL="http://dondeestaelcole.ddns.net:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        String nombre = getIntent().getStringExtra("nombre");
        String user = getIntent().getStringExtra("usuario");
        String email = getIntent().getStringExtra("email");
        id = getIntent().getIntExtra("id", 0);

        etNombre = (EditText)findViewById(R.id.etNombre);
        etUsuario = (EditText)findViewById(R.id.etUsuario);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etRepetir = (EditText)findViewById(R.id.etRepetir);

        etNombre.setText(nombre);
        etUsuario.setText(user);
        etEmail.setText(email);
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
    public void guardarCambios(View view){
        //intent que llama a la pantalla de inicio
        Boolean valido;

        valido = validarDatos();

        if(valido == true) {

            //se crea una nueva cola
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            //URL del servidor
            String url = URL + "/usuario/" + id ;

            //StringRequest inicializado
            StringRequest postRequest = new StringRequest(Request.Method.PATCH, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Toast.makeText(getApplicationContext(), "EXITO: has actualizado tu datos correctamente,estos datos se visualizaran correctamente la proxima vez que inicies sesion", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast.makeText(getApplicationContext(), "FALLO: Error al actualizar los datos del usuario ", Toast.LENGTH_LONG).show();
                        }
                    }
            ){
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

            //añadimos el request a la cola
            queue.add(postRequest);

        } else {
            Toast.makeText(getApplicationContext(),"Errores en el formulario",Toast.LENGTH_SHORT).show();
        }

    }
}
