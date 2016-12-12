package psp.weatherdam.pojo;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by jmpuentenueva on 05/12/2016.
 */

@Parcel
public class TiempoDelDia {
    String municipio;
    String nombreDia;
    String fecha;
    String estado;
    String iconoEstado;
    List<String> temperaturas;
    List<String> cantidadLluvia;
    List<String> velocidadViento;

    public TiempoDelDia(String municipio, int nombreDia, String fecha, String estado, String iconoEstado,
                        List<String> temperaturas, List<String> cantidadLluvia,
                        List<String> velocidadViento) {
        this.municipio = municipio;
        switch (nombreDia){
            case 1:
                this.nombreDia = "Lunes";
                break;
            case 2:
                this.nombreDia = "Martes";
                break;
            case 3:
                this.nombreDia = "Miércoles";
                break;
            case 4:
                this.nombreDia = "Jueves";
                break;
            case 5:
                this.nombreDia = "Viernes";
                break;
            case 6:
                this.nombreDia = "Sádabo";
                break;
            case 7:
                this.nombreDia = "Domingo";
                break;
            default:
                this.nombreDia = "noDay";
        }
        this.fecha = fecha;
        this.estado = estado;
        this.iconoEstado = "http://openweathermap.org/img/w/"+iconoEstado+".png";
        this.temperaturas = temperaturas;
        this.cantidadLluvia = cantidadLluvia;
        this.velocidadViento = velocidadViento;
    }

    public TiempoDelDia(String municipio, int nombreDia, String fecha, String estado, String iconoEstado,
                        List<String> temperaturas, List<String> velocidadViento) {
        this.municipio = municipio;
        switch (nombreDia){
            case 1:
                this.nombreDia = "Lunes";
                break;
            case 2:
                this.nombreDia = "Martes";
                break;
            case 3:
                this.nombreDia = "Miércoles";
                break;
            case 4:
                this.nombreDia = "Jueves";
                break;
            case 5:
                this.nombreDia = "Viernes";
                break;
            case 6:
                this.nombreDia = "Sádabo";
                break;
            case 7:
                this.nombreDia = "Domingo";
                break;
            default:
                this.nombreDia = "noDay";
        }
        this.fecha = fecha;
        this.estado = estado;
        this.iconoEstado = "http://openweathermap.org/img/w/"+iconoEstado+".png";
        this.temperaturas = temperaturas;
        this.velocidadViento = velocidadViento;
    }

    public TiempoDelDia() {
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNombreDia() {
        return nombreDia;
    }

    public void setNombreDia(String nombreDia) {
        this.nombreDia = nombreDia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIconoEstado() {
        return iconoEstado;
    }

    public void setIconoEstado(String iconoEstado) {
        this.iconoEstado = iconoEstado;
    }

    public List<String> getTemperaturas() {
        return temperaturas;
    }

    public void setTemperaturas(List<String> temperaturas) {
        this.temperaturas = temperaturas;
    }

    public List<String> getCantidadLluvia() {
        return cantidadLluvia;
    }

    public void setCantidadLluvia(List<String> cantidadLluvia) {
        this.cantidadLluvia = cantidadLluvia;
    }

    public List<String> getVelocidadViento() {
        return velocidadViento;
    }

    public void setVelocidadViento(List<String> velocidadViento) {
        this.velocidadViento = velocidadViento;
    }
}
