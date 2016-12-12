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
import android.widget.Toast;

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
import psp.weatherdam.pojo.TiempoProximo;
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
    private String currentPlaceId;
    private Bundle extras;
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
        cargarDatosIniciales();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        extras = new Bundle();
        retrofitApplication = (RetrofitApplication) getApplication();
        retrofitApplication.iniciarRetrofit();
        /* Intento limpiar el historial al iniciar la aplicación, pero hace que
           la busqueda de Sevilla predeterminada no salga en él

        SharedPreferences shPref = getApplication().getSharedPreferences(Constantes.SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();
        editor.clear();
        editor.commit();
        */
        setearDatos(placeidSevilla);

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
            DialogFragment dialogoBusqueda = new BusquedaDialogFragment(this);
            dialogoBusqueda.show(getSupportFragmentManager(), "Diálogo");
            dialogoBusqueda.setCancelable(true);
        }else{

        }

        return true;

    }

    @Override
    public void onClickBuscar(BusquedaMapsDetail busquedaMapsDetail) {
        setearDatos(busquedaMapsDetail.getResult().getPlace_id());
    }

    @Override
    public void onClickMunicipio(ElementoHistorial municipio) {
        actualRetrofit = retrofitApplication.getRetrofitGoogleAuto();
        GoogleMapsAPI api = actualRetrofit.create(GoogleMapsAPI.class);
        Call<BusquedaMapsAutocomp> busqueda = api.getMapsAutocomp(municipio.getNombre());
        BusquedaMapsAutocomp resultados = null;

        busqueda.enqueue(new Callback<BusquedaMapsAutocomp>() {
            @Override
            public void onResponse(Response<BusquedaMapsAutocomp> response, Retrofit retrofit) {
                setearDatos(response.body().getPredictions().get(0).getPlace_id());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onDiaClick(TiempoDelDia dia) {

    }

    void setearDatos(String placeid){
        currentPlaceId = placeid;
        fechaAux = new DateTime(new Date());
        //Por alguna razon se crea con una hora menos de la actual, asi que se la sumo
        fechaAux = fechaAux.plusHours(1);

        GoogleMapsAPI api = retrofitApplication.getRetrofitGoogleDetail().create(GoogleMapsAPI.class);
        Call<BusquedaMapsDetail> llamada = api.getMapsDetail(currentPlaceId);

        llamada.enqueue(new Callback<BusquedaMapsDetail>() {
            @Override
            public void onResponse(Response<BusquedaMapsDetail> response, Retrofit retrofit) {
                BusquedaMapsDetail datosDelSitio = response.body();

                /* SETEO DEL HISTORIAL */
                if(String.valueOf(fechaAux.getMinuteOfHour()).length() < 2) {
                    registrarEnHistorial(new ElementoHistorial(
                            datosDelSitio.getResult().getName(),
                            fechaAux.getHourOfDay() + ":0" + fechaAux.getMinuteOfHour()));
                } else {
                    registrarEnHistorial(new ElementoHistorial(
                            datosDelSitio.getResult().getName(),
                            fechaAux.getHourOfDay() + ":" + fechaAux.getMinuteOfHour()));
                }

                extras.putString("placeid",currentPlaceId);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


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
            switch (position) {
                case TAB_HISTORIAL:
                    fragmentActual = new HistorialFragment();
                    break;
                case TAB_MAPA:
                    fragmentActual = new MapFragment();
                    break;
                case TAB_PREDICCIONES:
                    fragmentActual = new PrediccionesFragment();
                    break;
                default:
                    fragmentActual = new HistorialFragment();
            }

            fragmentActual.setArguments(extras);
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
