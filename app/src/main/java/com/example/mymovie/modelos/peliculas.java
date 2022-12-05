package com.example.mymovie.modelos;

public class peliculas {
    public String name,horario,clasificacion,imagen;
    public int tickets_disponibles;
    public double precio;

    public peliculas(String name, String horario, int tickets_disponibles,double precio, String clasificacion, String imagen) {
        this.name = name;
        this.horario = horario;
        this.tickets_disponibles = tickets_disponibles;
        this.precio = precio;
        this.clasificacion = clasificacion;
        this.imagen = imagen;
    }
    public String getname() {
        return name;
    }
    public String gethorario() {
        return horario;
    }
    public int gettickets_disponibles() {
        return tickets_disponibles;
    }
    public double getprecio() {
        return precio;
    }
    public String getclasificacion() {
        return clasificacion;
    }
    public String getimagen() {
        return imagen;
    }
}
