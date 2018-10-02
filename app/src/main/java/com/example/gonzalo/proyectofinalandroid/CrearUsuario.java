package com.example.gonzalo.proyectofinalandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class CrearUsuario extends AppCompatActivity {

    EditText etNombre,etCorreo,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        etNombre=findViewById(R.id.etNombre);
        etCorreo=findViewById(R.id.etCorreo);
        etPassword=findViewById(R.id.etPassword);
    }

    public void registrarUsuario(){

        String nombre=this.etNombre.getText().toString();
        String correo=this.etCorreo.getText().toString();
        String password=this.etPassword.getText().toString();

        //Verifico que los campos no esten vacios
        if(!nombre.equals("") || !correo.equals("") || !password.equals("")){
            Usuario usuario=new Usuario(nombre,correo,password);


        }else{
            Toast.makeText(this,"Los campos no deben estar vacios",Toast.LENGTH_SHORT).show();
        }

    }


}
