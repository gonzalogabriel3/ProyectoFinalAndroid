package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PerfilUsuario extends AppCompatActivity {

    private TextView tv_id,tv_nombre,tv_usuario,tv_email;
    String nombre, user, email;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        nombre = getIntent().getStringExtra("nombre");
        user = getIntent().getStringExtra("usuario");
        email = getIntent().getStringExtra("email");
        id = getIntent().getIntExtra("id", 0);

        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_usuario = (TextView) findViewById(R.id.tv_usuario);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_email = (TextView) findViewById(R.id.tv_email);

        tv_id.setText(String.valueOf(id));
        tv_usuario.setText(user);
        tv_nombre.setText(nombre);
        tv_email.setText(email);
    }

    public void finalizar(View view){
        finish();
    }

    public void ModificarPerfil(View view){
        Intent i=new Intent(this,activity_editar_usuario.class);
        i.putExtra("nombre",nombre);
        i.putExtra("usuario",user);
        i.putExtra("email",email);
        i.putExtra("id",id);

        startActivity(i);
    }

}
