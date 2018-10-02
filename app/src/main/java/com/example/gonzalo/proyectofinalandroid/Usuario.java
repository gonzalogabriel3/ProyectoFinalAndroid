package com.example.gonzalo.proyectofinalandroid;

public class Usuario {

    private Integer id;
    private String nombre;
    private String correo;
    private String password;
    private float latitud;
    private float longitud;

    public Usuario(String nombre,String correo,String password) {
        this.nombre=nombre;
        this.correo=correo;
        this.password=password;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }
}
