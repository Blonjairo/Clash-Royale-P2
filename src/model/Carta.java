package model;

public class Carta {

    private int id;
    private String nombre;
    private int costeElixir;
    private String tipo;
    private Calidad calidad;

    public Carta() {
    }

    public Carta(int id, String nombre, int costeElixir, String tipo, Calidad calidad) {
        this.id = id;
        this.nombre = nombre;
        this.costeElixir = costeElixir;
        this.tipo = tipo;
        this.calidad = calidad;
    }

    public int getId() {return id; }
    public String getNombre() {return nombre; }
    public int getCosteElixir() {return costeElixir; }
    public String getTipo() {return tipo; }
    public Calidad getCalidad() {return calidad; }

    public void setId(int id) {this.id = id; }
    public void setNombre(String nombre) {this.nombre = nombre; }
    public void setCosteElixir(int costeElixir) {this.costeElixir = costeElixir; }
    public void setTipo(String tipo) {this.tipo = tipo; }
    public void setCalidad(Calidad calidad) {this.calidad = calidad; }

    @Override
    public String toString() {
        String tipoValor = (tipo == null) ? "SIN TIPO" : tipo;
        String calidadValor = (calidad == null) ? "SIN CALIDAD" : calidad.getNombre();
        return "Carta{" +
                "id=" + id +
                ", nombre='" + nombre + "'" +
                ", costeElixir=" + costeElixir +
                ", tipo='" + tipoValor + "'" +
                ", calidad=" + calidadValor +
                "}";
    }
}
