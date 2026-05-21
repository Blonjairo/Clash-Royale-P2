package model;

public class Calidad {

    private int id;
    private String nombre;

    public Calidad() {}

    public Calidad(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId()     { return id; }
    public String getNombre() { return nombre;}

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) {this.nombre = nombre;}

    @Override
    public String toString() {
        return "Calidad{id=" + id + ", nombre='" + nombre + "'}";
    }
}
