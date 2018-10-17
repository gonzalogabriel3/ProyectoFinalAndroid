package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String nombre = getIntent().getStringExtra("nombre");
        String user = getIntent().getStringExtra("usuario");
        String email = getIntent().getStringExtra("email");
        int id = getIntent().getIntExtra("id", 0);

        usuario.setNombre(nombre);
        usuario.setUsuario(user);
        usuario.setCorreo(email);
        usuario.setId(id);



        Toast.makeText(getApplicationContext(),"Bienvenido/a",Toast.LENGTH_SHORT).show();

    }

    /***COMENTARIO DE EJEMPLO****/

    /*Metodo que lanza un activity hacia la clase CoordenadasUsuario*/
    public void irCoordenadasUsuario (View v) {
        Intent i=new Intent(this,CoordenadasDeUsuario.class);
        startActivity(i);
    }

    public void irPerfilUsuario(View v){
        Intent i=new Intent(this,PerfilUsuario.class);
        i.putExtra("nombre",usuario.getNombre());
        i.putExtra("usuario",usuario.getUsuario());
        i.putExtra("email",usuario.getCorreo());
        i.putExtra("id",usuario.getId());

        startActivity(i);
    }
    public void irProbarConexion (View v) {
        Intent i=new Intent(this,activity_conexion.class);
        startActivity(i);
    }





}
