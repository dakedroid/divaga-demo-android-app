package com.example.kandksolutions.divaga.Modelos;

/**
 * Created by dakedroid on 26/01/2017.
 */

public class Noticia {

    String titulo;
    String descripcion;
    String imagen;



    public Noticia() {

    }

    public Noticia(String titulo, String descripcion, String imagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}


