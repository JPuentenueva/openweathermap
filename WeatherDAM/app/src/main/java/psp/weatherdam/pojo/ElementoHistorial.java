package psp.weatherdam.pojo;

/**
 * Created by jmpuentenueva on 05/12/2016.
 */
public class ElementoHistorial {
    String nombre;
    String hora;

    public ElementoHistorial() {

    }

    public ElementoHistorial(String nombre, String hora) {
        this.nombre = nombre;
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
