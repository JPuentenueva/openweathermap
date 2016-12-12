package psp.weatherdam.retrofit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import psp.weatherdam.interfaces.GoogleMapsAPI;
import psp.weatherdam.pojo.googlemapsautocomp.BusquedaMapsAutocomp;
import psp.weatherdam.pojo.googlemapsautocomp.Prediction;
import retrofit.Call;
import retrofit.Retrofit;

/**
 * Created by Luismi on 06/12/2016.
 */

public class PlacesAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<Prediction> resultList = new ArrayList<Prediction>();

    public PlacesAutoCompleteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Prediction getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position).getDescription());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            /*
                SEGUN LA DOCUMENTACIÓN DE ANDROID, ESTE MÉTODO NO SE INVOCA
                EN EL HILO UI, POR LO QUE NO ES NECESARIO QUE EL PROGRAMADOR
                SE ENCARGUE DE LA GESTIÓN DE LOS HILOS.
             */
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //Resultado del filtrado.
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    List<Prediction> places = autoComplete(mContext, constraint.toString());

                    // Asignamos los valores recogidos al FilterResults
                    if (places != null ) {
                        filterResults.values = places;
                        filterResults.count = places.size();
                    } else {
                        filterResults.values = null;
                        filterResults.count = 0;
                    }

                }
                return filterResults;
            }

             /*
                SEGUN LA DOCUMENTACIÓN DE ANDROID, ESTE MÉTODO SI SE INVOCA
                EN EL HILO UI, Y RECIBE DEL MÉTODO ANTERIOR LOS RESULTADOS
                A MOSTRAR. NO DEBEMOS TEMER UN FALLO DEL LOOPER POR INTENTAR
                MODIFICAR LA UI DESDE OTRO HILO.
             */

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Prediction>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    /*
        EN ESTE MÉTODO, LA PETICIÓN QUE HACEMOS ES SÍNCRONA, YA QUE
        ANDROID SE ENCARGARÁ DE EJECUTARLO EN UN HILO INDEPENDIENTE
     */
    private List<Prediction> autoComplete(Context context, String input) {

        List<Prediction> result = null;

        //Desde el contexto llegamos a nuestra clase Application
        if (context instanceof Activity) {
            Activity act = (Activity) context;

            RetrofitApplication application =
                    (RetrofitApplication) act.getApplication();

            Retrofit retrofit = application.getRetrofitGoogleAuto();
            //Obtenemos nuestra instancia "singleton" de Retrofit
            //Retrofit retrofit = ((RetrofitApplication) act.getApplication()).getRetrofitGoogle();

            //Creamos el API
            GoogleMapsAPI api = retrofit.create(GoogleMapsAPI.class);
            //Obtenemos la petición lista para realizarla
            Call<BusquedaMapsAutocomp> callAutoComplete = api.getMapsAutocomp(input);

            try {
                //La ejecutamos con execute
                result = callAutoComplete.execute().body().getPredictions();
            } catch (IOException e) {
                //Gestión del error
                Log.e("ERROR IO", e.getMessage());

            }

        }
        //Devolvemos el resultado
        return result;


    }
}