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

    /*Metodo que lanza un activity hacia la clase CoordenadasUsuario*/
    public void irCoordenadasUsuario (View v) {
        Intent i=new Intent(this,CoordenadasDeUsuario.class);
        startActivity(i);
    }
}
