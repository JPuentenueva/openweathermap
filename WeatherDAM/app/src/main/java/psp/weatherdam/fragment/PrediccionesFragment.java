package psp.weatherdam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.parceler.Parcels;

import psp.weatherdam.Constantes;
import psp.weatherdam.R;
import psp.weatherdam.interfaces.GoogleMapsAPI;
import psp.weatherdam.interfaces.IPrediccionesListener;
import psp.weatherdam.interfaces.OpenWeatherAPI;
import psp.weatherdam.pojo.TiempoDelDia;
import psp.weatherdam.pojo.TiempoProximo;
import psp.weatherdam.pojo.googlemapsdetail.BusquedaMapsDetail;
import psp.weatherdam.pojo.openweather.BusquedaOpenWeather;
import psp.weatherdam.pojo.openweatherforecast.BusquedaOpenWeatherForecast;
import psp.weatherdam.retrofit.RetrofitApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link IPrediccionesListener}
 * interface.
 */
public class PrediccionesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private IPrediccionesListener mListener;
    TiempoProximo predicciones;
    RetrofitApplication retrofitApplication;
    BusquedaOpenWeather tiempoActual;
    DateTime fecha;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PrediccionesFragment() {

    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PrediccionesFragment newInstance(int columnCount) {
        PrediccionesFragment fragment = new PrediccionesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        predicciones = new TiempoProximo();

        String placeid = getArguments().getString("placeid");

        fecha = new DateTime(new Date());
        fecha = fecha.plusHours(1);
        //Inicia los servicios Retrofit
        retrofitApplication = (RetrofitApplication) getActivity().getApplication();
        retrofitApplication.iniciarRetrofit();

        Retrofit retrofit = retrofitApplication.getRetrofitGoogleDetail();
        GoogleMapsAPI api = retrofit.create(GoogleMapsAPI.class);
        Call<BusquedaMapsDetail> llamada = api.getMapsDetail(placeid);

        llamada.enqueue(new Callback<BusquedaMapsDetail>() {
            @Override
            public void onResponse(Response<BusquedaMapsDetail> response, Retrofit retrofit) {
                setearDatos(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


        if (getArguments() != null) {
            //predicciones = Parcels.unwrap(getArguments().getParcelable(Constantes.PARCEL_PREDICTIONS));
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private void setearDatos(BusquedaMapsDetail body) {

        OpenWeatherAPI api2 = retrofitApplication.getRetrofitOpenWeather().create(OpenWeatherAPI.class);

        Call<BusquedaOpenWeatherForecast> llamada3 = api2.getBusquedaOpenWeatherForecast(body.getResult().getGeometry().getLocation().getLat(),
                body.getResult().getGeometry().getLocation().getLng());

        llamada3.enqueue(new Callback<BusquedaOpenWeatherForecast>() {
            @Override
            public void onResponse(Response<BusquedaOpenWeatherForecast> response, Retrofit retrofit) {
                //ESTO GUARDA LOS DATOS DE LA BUSQUEDA PARA CUANDO SE CAMBIE EL FRAGMENT A "PREDICCIONES"
                asignarDatosProximosDias(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });



    }

    private void asignarDatosProximosDias(BusquedaOpenWeatherForecast body) {
        List<TiempoDelDia> proximosDias = new ArrayList<>();

        DateTime _1DiaMas = fecha.plusDays(1);
        DateTime _2DiasMas = fecha.plusDays(2);
        DateTime _3DiasMas = fecha.plusDays(3);
        DateTime _4DiasMas = fecha.plusDays(4);

        List<psp.weatherdam.pojo.openweatherforecast.List> listHoy = new ArrayList<>();
        List<psp.weatherdam.pojo.openweatherforecast.List> list1DiaMas = new ArrayList<>();
        List<psp.weatherdam.pojo.openweatherforecast.List> list2DiasMas = new ArrayList<>();
        List<psp.weatherdam.pojo.openweatherforecast.List> list3DiasMas = new ArrayList<>();
        List<psp.weatherdam.pojo.openweatherforecast.List> list4DiasMas = new ArrayList<>();

        //Separo los datos de los distintos dias
        for (psp.weatherdam.pojo.openweatherforecast.List elemetoListaPredicciones : body.getList()) {


            if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(fecha.getYear() + "-" + fecha.getMonthOfYear() + "-" + fecha.getDayOfMonth())) {
                listHoy.add(elemetoListaPredicciones);
            } else if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(
                    _1DiaMas.getYear() + "-" + _1DiaMas.getMonthOfYear() + "-" + _1DiaMas.getDayOfMonth())) {
                list1DiaMas.add(elemetoListaPredicciones);
            } else if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(
                    _2DiasMas.getYear() + "-" + _2DiasMas.getMonthOfYear() + "-" + _2DiasMas.getDayOfMonth())) {
                list2DiasMas.add(elemetoListaPredicciones);
            } else if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(
                    _3DiasMas.getYear() + "-" + _3DiasMas.getMonthOfYear() + "-" + _3DiasMas.getDayOfMonth())) {
                list3DiasMas.add(elemetoListaPredicciones);
            } else if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(
                    _4DiasMas.getYear() + "-" + _4DiasMas.getMonthOfYear() + "-" + _4DiasMas.getDayOfMonth())) {
                list4DiasMas.add(elemetoListaPredicciones);
            } else {
                //Toast.makeText(getActivity(), "No entra en las listas", Toast.LENGTH_SHORT).show();
            }
        };

        List<List> listaDeListaDeDias = new ArrayList<>();
        listaDeListaDeDias.add(listHoy);
        listaDeListaDeDias.add(list1DiaMas);
        listaDeListaDeDias.add(list2DiasMas);
        listaDeListaDeDias.add(list3DiasMas);
        listaDeListaDeDias.add(list4DiasMas);

        List<TiempoDelDia> listaTiempoProximo = new ArrayList<>();

        for (List<psp.weatherdam.pojo.openweatherforecast.List> listaDias : listaDeListaDeDias) {

            String estado = null;
            String iconoEstado = null;
            List<String> listaPrecip = new ArrayList<>();
            List<String> listaTemp = new ArrayList<>();
            List<String> listaViento = new ArrayList<>();


            for (psp.weatherdam.pojo.openweatherforecast.List currentList : listaDias) {
                listaTemp.add(currentList.getMain().getTempMin() + "º-" + currentList.getMain().getTempMax() + "º");
                if (currentList.getRain() != null) {
                    listaPrecip.add(currentList.getRain().get3h() + " mm");
                }else{
                    listaPrecip.add("0 mm");
                }
                listaViento.add(currentList.getWind().getSpeed() + " m/s");
                //Solo coge el primer estado
                estado = currentList.getWeather().get(0).getDescription();
                iconoEstado = currentList.getWeather().get(0).getIcon();
            }

            listaTiempoProximo.add(new TiempoDelDia(body.getCity().getName(),
                    fecha.getDayOfWeek(),
                    fecha.getDayOfMonth() + "/" + fecha.getMonthOfYear() + "/" + fecha.getYear(),
                    estado,
                    iconoEstado,
                    listaTemp,
                    listaPrecip,
                    listaViento));
        }

        predicciones.setListaDias(listaTiempoProximo);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predicciones_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new PrediccionesAdapter(predicciones.getListaDias(), mListener, context));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IPrediccionesListener) {
            mListener = (IPrediccionesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IPrediccionesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
