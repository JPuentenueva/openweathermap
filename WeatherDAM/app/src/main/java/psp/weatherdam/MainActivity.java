package psp.weatherdam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.joda.time.DateTime;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import psp.weatherdam.fragment.BusquedaDialogFragment;
import psp.weatherdam.fragment.HistorialFragment;
import psp.weatherdam.fragment.MapFragment;
import psp.weatherdam.fragment.PrediccionesFragment;
import psp.weatherdam.interfaces.GoogleMapsAPI;
import psp.weatherdam.interfaces.IBusquedaListener;
import psp.weatherdam.interfaces.IHistorialListener;
import psp.weatherdam.interfaces.IPrediccionesListener;
import psp.weatherdam.interfaces.OpenWeatherAPI;
import psp.weatherdam.pojo.ElementoHistorial;
import psp.weatherdam.pojo.LocalizacionEnMapa;
import psp.weatherdam.pojo.TiempoDelDia;
import psp.weatherdam.pojo.googlemapsautocomp.BusquedaMapsAutocomp;
import psp.weatherdam.pojo.googlemapsdetail.BusquedaMapsDetail;
import psp.weatherdam.pojo.googlemapsdetail.Location;
import psp.weatherdam.pojo.openweather.BusquedaOpenWeather;
import psp.weatherdam.pojo.openweatherforecast.BusquedaOpenWeatherForecast;
import psp.weatherdam.retrofit.RetrofitApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements IBusquedaListener, IPrediccionesListener, IHistorialListener{
    private Fragment fragmentActual;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static RetrofitApplication retrofitApplication;
    private Retrofit actualRetrofit;
    private TiempoDelDia datosDelTiempoActual;
    private List<TiempoDelDia> datosDelTiempoProximo;
    private LocalizacionEnMapa localizacionEnMapa;
    private DateTime fechaAux;
    private static final int TAB_HISTORIAL = 0;
    private static final int TAB_MAPA = 2;
    private static final int TAB_PREDICCIONES = 1;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cargarDatosIniciales();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //No lo uso, asi que lo marco como GONE
        fab.setVisibility(View.GONE);

    }

    public void cargarDatosIniciales(){
        String placeidSevilla = "ChIJkWK-FBFsEg0RSFb-HGIY8DQ";
        /* Intento limpiar el historial al iniciar la aplicación, pero hace que
           la busqueda de Sevilla predeterminada no salga en él

        SharedPreferences shPref = getApplication().getSharedPreferences(Constantes.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();
        editor.clear();
        editor.commit();
        */

        //Inicia los servicios Retrofit
        retrofitApplication = (RetrofitApplication) this.getApplication();
        retrofitApplication.iniciarRetrofit();
        actualRetrofit = retrofitApplication.getRetrofitGoogleDetail();
        GoogleMapsAPI api = actualRetrofit.create(GoogleMapsAPI.class);
        Call<BusquedaMapsDetail> llamada = api.getMapsDetail(placeidSevilla);

        llamada.enqueue(new Callback<BusquedaMapsDetail>() {
            @Override
            public void onResponse(Response<BusquedaMapsDetail> response, Retrofit retrofit) {
                setearDatos(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_buscar) {
            DialogFragment dialogoBusqueda = new BusquedaDialogFragment();
            dialogoBusqueda.show(getSupportFragmentManager(), "Diálogo");
            dialogoBusqueda.setCancelable(true);
        }else{

        }

        return true;

    }

    @Override
    public void onClickBuscar(BusquedaMapsDetail busquedaMapsDetail) {
        setearDatos(busquedaMapsDetail);
    }

    @Override
    public void onClickMunicipio(ElementoHistorial municipio) {
        actualRetrofit = retrofitApplication.getRetrofitGoogleAuto();
        GoogleMapsAPI api = actualRetrofit.create(GoogleMapsAPI.class);
        Call<BusquedaMapsAutocomp> busqueda = api.getMapsAutocomp(municipio.getNombre());
        BusquedaMapsAutocomp resultados = null;

        try {
             resultados = busqueda.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        api = retrofitApplication.getRetrofitGoogleDetail().create(GoogleMapsAPI.class);

        Call<BusquedaMapsDetail> busqueda2 = api.getMapsDetail(resultados.getPredictions().get(0).getPlaceId());
        BusquedaMapsDetail resultados2 = null;

        try {
            resultados2 = busqueda2.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setearDatos(resultados2);
    }

    @Override
    public void onDiaClick(TiempoDelDia dia) {

    }

    void setearDatos(BusquedaMapsDetail datosDelSitio){
        fechaAux = new DateTime(new Date());
        //Por alguna razon se crea con una hora menos de la actual, asi que se la sumo
        fechaAux.plusHours(1);

        OpenWeatherAPI api = retrofitApplication.getRetrofitOpenWeather().create(OpenWeatherAPI.class);
        //Consigo la latitud y longitud del resultado de la anterior llamada
        Location location = datosDelSitio.getResult().getGeometry().getLocation();

        /* TODO SETEO DEL MAPA */

        Call<BusquedaOpenWeather> llamada2 = api.getBusquedaOpenWeather(location.getLat(), location.getLng());

        //Estos son los datos meteorologicos actuales de Sevilla
        BusquedaOpenWeather tiempoActual = null;

        llamada2.enqueue(new Callback<BusquedaOpenWeather>() {
            @Override
            public void onResponse(Response<BusquedaOpenWeather> response, Retrofit retrofit) {
                //ESTO GUARDA LOS DATOS DE LA BUSQUEDA PARA CUANDO SE CAMBIE EL FRAGMENT A "MAPA"
                asignarDatosActuales(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        localizacionEnMapa = new LocalizacionEnMapa(datosDelSitio.getResult().getName(),
                datosDelSitio.getResult().getGeometry().getLocation().getLat(),
                datosDelSitio.getResult().getGeometry().getLocation().getLng());

        /* SETEO DEL HISTORIAL */

        registrarEnHistorial(new ElementoHistorial(
                datosDelSitio.getResult().getName(),
                fechaAux.getHourOfDay()+":"+fechaAux.getMinuteOfHour()));

        /* TODO SETEO DE LAS PREDICCIONES */

        Call<BusquedaOpenWeatherForecast> llamada3 = api.getBusquedaOpenWeatherForecast(location.getLat(), location.getLng());

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

    private void asignarDatosActuales(BusquedaOpenWeather tiempoActual) {
        List<String> listaTemp = new ArrayList<>();
        List<String> listaViento = new ArrayList<>();

        listaTemp.add(String.valueOf(tiempoActual.getMain().getTempMin()+"º - "+tiempoActual.getMain().getTempMax()+"º"));
        listaViento.add(String.valueOf(tiempoActual.getWind().getSpeed()));

        TiempoDelDia diaActual = new TiempoDelDia(tiempoActual.getName(),
                fechaAux.getDayOfWeek(),
                fechaAux.getDayOfMonth()+"/"+fechaAux.getMonthOfYear()+"/"+fechaAux.getYear(),
                tiempoActual.getWeather().get(0).getDescription(),
                tiempoActual.getWeather().get(0).getIcon(),
                listaTemp,
                listaViento);

        datosDelTiempoActual = diaActual;
    }

    //TODO inacabado
    private void asignarDatosProximosDias(BusquedaOpenWeatherForecast tiempoProximosDias) {
        List<TiempoDelDia> proximosDias = new ArrayList<>();

        DateTime _1DiaMas = fechaAux.plusDays(1);
        DateTime _2DiasMas = fechaAux.plusDays(2);
        DateTime _3DiasMas = fechaAux.plusDays(3);
        DateTime _4DiasMas = fechaAux.plusDays(4);

        List<psp.weatherdam.pojo.openweatherforecast.List> listHoy = new ArrayList<>();
        List<psp.weatherdam.pojo.openweatherforecast.List> list1DiaMas = new ArrayList<>();
        List<psp.weatherdam.pojo.openweatherforecast.List> list2DiasMas = new ArrayList<>();
        List<psp.weatherdam.pojo.openweatherforecast.List> list3DiasMas = new ArrayList<>();
        List<psp.weatherdam.pojo.openweatherforecast.List> list4DiasMas = new ArrayList<>();

        //Separo los datos de los distintos dias
        for (psp.weatherdam.pojo.openweatherforecast.List elemetoListaPredicciones : tiempoProximosDias.getList()) {
            if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(fechaAux.getYear() + "-" + fechaAux.getMonthOfYear() + "-" + fechaAux.getDayOfMonth())) {
                listHoy.add(elemetoListaPredicciones);
            } else if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(_1DiaMas.getYear() + "-" + _1DiaMas.getMonthOfYear() + "-" + _1DiaMas.getDayOfMonth())) {
                list1DiaMas.add(elemetoListaPredicciones);
            } else if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(_2DiasMas.getYear() + "-" + _2DiasMas.getMonthOfYear() + "-" + _2DiasMas.getDayOfMonth())) {
                list2DiasMas.add(elemetoListaPredicciones);
            } else if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(_3DiasMas.getYear() + "-" + _3DiasMas.getMonthOfYear() + "-" + _3DiasMas.getDayOfMonth())) {
                list3DiasMas.add(elemetoListaPredicciones);
            } else if (elemetoListaPredicciones.getDt_txt().split(" ")[0].equalsIgnoreCase(_4DiasMas.getYear() + "-" + _4DiasMas.getMonthOfYear() + "-" + _4DiasMas.getDayOfMonth())) {
                list4DiasMas.add(elemetoListaPredicciones);
            }
        };

        List<List> listaDeListaDeDias = new ArrayList<>();
        listaDeListaDeDias.add(listHoy);
        listaDeListaDeDias.add(list1DiaMas);
        listaDeListaDeDias.add(list2DiasMas);
        listaDeListaDeDias.add(list3DiasMas);
        listaDeListaDeDias.add(list4DiasMas);

        datosDelTiempoProximo = new ArrayList<>();


        for (List<psp.weatherdam.pojo.openweatherforecast.List> listaDias : listaDeListaDeDias) {

            String estado = null;
            String iconoEstado = null;
            List<String> listaPrecip = new ArrayList<>();
            List<String> listaTemp = new ArrayList<>();
            List<String> listaViento = new ArrayList<>();


            for (psp.weatherdam.pojo.openweatherforecast.List currentList : listaDias) {
                listaTemp.add(currentList.getMain().getTempMin() + "º-" + currentList.getMain().getTempMax() + "º");
                listaPrecip.add(currentList.getRain().get3h() + " mm");
                listaViento.add(currentList.getWind().getSpeed() + " m/s");
                //Solo coge el primer estado
                estado = currentList.getWeather().get(0).getDescription();
                iconoEstado = currentList.getWeather().get(0).getIcon();

            }

            datosDelTiempoProximo.add(new TiempoDelDia(tiempoProximosDias.getCity().getName(),
                    fechaAux.getDayOfWeek(),
                    fechaAux.getDayOfMonth() + "/" + fechaAux.getMonthOfYear() + "/" + fechaAux.getYear(),
                    estado,
                    iconoEstado,
                    listaTemp,
                    listaPrecip,
                    listaViento));
        }

        datosDelTiempoProximo = proximosDias;
    }

    void registrarEnHistorial(ElementoHistorial elementoHistorial) {
        SharedPreferences sp = getApplication().getSharedPreferences(Constantes.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        Set<String> historialActual = sp.getStringSet(Constantes.SHARPREF_HISTORIAL, null);
        Set<String> historialNuevo = new HashSet<>();

        if (historialActual != null){

            while (historialNuevo.size() < historialActual.size()+1 && historialNuevo.size() < 5){
                for (String line : historialActual){
                    String[] orden_nombre_hora = line.split(";");

                    switch (historialNuevo.size()) {
                        case 0:
                            historialNuevo.add("1;"+elementoHistorial.getNombre()+";"+elementoHistorial.getHora());
                            break;
                        case 1:
                            if (orden_nombre_hora[0].equalsIgnoreCase("1")){
                                historialNuevo.add("2;"+orden_nombre_hora[1]+";"+orden_nombre_hora[2]);
                            }

                            break;
                        case 2:
                            if (orden_nombre_hora[0].equalsIgnoreCase("2")){
                                historialNuevo.add("3;"+orden_nombre_hora[1]+";"+orden_nombre_hora[2]);
                            }

                            break;
                        case 3:
                            if (orden_nombre_hora[0].equalsIgnoreCase("3")){
                                historialNuevo.add("4;"+orden_nombre_hora[1]+";"+orden_nombre_hora[2]);
                            }

                            break;
                        case 4:
                            if (orden_nombre_hora[0].equalsIgnoreCase("4")){
                                historialNuevo.add("5;"+orden_nombre_hora[1]+";"+orden_nombre_hora[2]);
                            }

                            break;
                        default:

                            break;

                    }
                }
            }
        } else {
            historialNuevo.add("1;"+elementoHistorial.getNombre()+";"+elementoHistorial.getHora());
        }
        //Esto guarda el historial en el SharedPreferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(Constantes.SHARPREF_HISTORIAL, historialNuevo);
        editor.commit();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle extras = null;
            switch (position) {
                case TAB_HISTORIAL:
                    fragmentActual = new HistorialFragment();

                    break;
                case TAB_MAPA:
                    fragmentActual = new MapFragment();

                    extras = new Bundle();
                    extras.putParcelable(Constantes.PARCEL_LOCATION, Parcels.wrap(localizacionEnMapa));
                    extras.putParcelable(Constantes.PARCEL_WEATHERNOW, Parcels.wrap(datosDelTiempoActual));
                    fragmentActual.setArguments(extras);

                    break;
                case TAB_PREDICCIONES:
                    fragmentActual = new PrediccionesFragment();

                    extras = new Bundle();
                    extras.putParcelable(Constantes.PARCEL_PREDICTIONS, Parcels.wrap(datosDelTiempoProximo));

                    break;
                default:
                    fragmentActual = new HistorialFragment();
            }

            return fragmentActual;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TAB_HISTORIAL:
                    return getString(R.string.tab_historial_title);
                case TAB_MAPA:
                    return getString(R.string.tab_mapa_title);
                case TAB_PREDICCIONES:
                    return getString(R.string.tab_predicciones_title);
            }
            return null;
        }
    }
}
