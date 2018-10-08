package com.example.gonzalo.proyectofinalandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PerfilUsuario extends AppCompatActivity {

    private TextView tv_id,tv_nombre,tv_usuario,tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        String nombre = getIntent().getStringExtra("nombre");
        String user = getIntent().getStringExtra("user");
        String email = getIntent().getStringExtra("email");
        int id = getIntent().getIntExtra("id", 0);


        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setUsuario(user);
        usuario.setCorreo(email);
        usuario.setId(id);

        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_usuario = (TextView) findViewById(R.id.tv_usuario);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_email = (TextView) findViewById(R.id.tv_email);

        tv_id.setText(String.valueOf(usuario.getId()));
        tv_usuario.setText(usuario.getUsuario());
        tv_nombre.setText(usuario.getNombre());
        tv_email.setText(usuario.getCorreo());
    }

    public void finalizar(View view){
        finish();
    }
}
