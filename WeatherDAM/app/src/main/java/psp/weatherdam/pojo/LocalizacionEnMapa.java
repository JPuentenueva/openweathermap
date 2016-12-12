package psp.weatherdam.pojo;

import org.parceler.Parcel;

/**
 * Created by Jose on 11/12/2016.
 */

@Parcel
public class LocalizacionEnMapa {
    String nombreSitio;
    double latitud;
    double longitud;

    public LocalizacionEnMapa() {
    }

    public LocalizacionEnMapa(String nombreSitio, Double latitud, Double longitud) {
        this.nombreSitio = nombreSitio;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public LocalizacionEnMapa(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
