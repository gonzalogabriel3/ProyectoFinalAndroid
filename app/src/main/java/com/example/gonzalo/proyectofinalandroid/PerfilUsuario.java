package com.example.gonzalo.proyectofinalandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PerfilUsuario extends AppCompatActivity {

    TextView tvNombre,tvCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        tvNombre=findViewById(R.id.tvNombre);
        tvCorreo=findViewById(R.id.tvCorreo);

        //Datos del usuario
        String nombre="UsuEjemplo";
        String correo="ejemplo-usuario@gmail.com";

        tvNombre.setText("Nombre de usuario: "+nombre);
        tvCorreo.setText("Correo electronico: "+correo);


    }

    public void finalizar(View view){
        finish();
    }

}
