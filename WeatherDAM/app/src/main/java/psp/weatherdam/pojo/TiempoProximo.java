package psp.weatherdam.pojo;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmpuentenueva on 12/12/2016.
 */
@Parcel
public class TiempoProximo {
    List<TiempoDelDia> listaDias;

    public TiempoProximo() {
        listaDias = new ArrayList<>();
    }

    public TiempoProximo(List<TiempoDelDia> listaDias) {
        this.listaDias = listaDias;
    }

    public List<TiempoDelDia> getListaDias() {
        return listaDias;
    }

    public void setListaDias(List<TiempoDelDia> listaDias) {
        this.listaDias = listaDias;
    }
}
