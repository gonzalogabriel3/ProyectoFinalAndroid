package com.example.gonzalo.proyectofinalandroid;

public class Usuario {

    private Integer id;
    private String nombre;
    private String correo;
    private String password;
    private String usuario;
    private double latitud;
    private double longitud;

    public Usuario(){}
    
    public Usuario(String Usuario,String nombre,String correo,String password) {
        this.nombre=nombre;
        this.correo=correo;
        this.password=password;
        this.usuario=Usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }
}
