package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /***COMENTARIO DE EJEMPLO****/

    /*Metodo que lanza un activity hacia la clase CoordenadasUsuario*/
    public void irCoordenadasUsuario (View v) {
        Intent i=new Intent(this,CoordenadasDeUsuario.class);
        startActivity(i);
    }

    public void irPerfilUsuario(View v){
        Intent i=new Intent(this,PerfilUsuario.class);

        startActivity(i);
    }
    public void irProbarConexion (View v) {
        Intent i=new Intent(this,activity_conexion.class);
        startActivity(i);
    }

    public void irRegistrarUsuario(View v){
        Intent i=new Intent(this,activity_RegistrarUsuario.class);
        startActivity(i);
    }



}
