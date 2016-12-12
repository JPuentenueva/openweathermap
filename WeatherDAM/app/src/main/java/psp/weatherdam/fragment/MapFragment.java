package psp.weatherdam.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import psp.weatherdam.Constantes;
import psp.weatherdam.R;
import psp.weatherdam.interfaces.GoogleMapsAPI;
import psp.weatherdam.interfaces.OpenWeatherAPI;
import psp.weatherdam.pojo.LocalizacionEnMapa;
import psp.weatherdam.pojo.TiempoDelDia;
import psp.weatherdam.pojo.googlemapsdetail.BusquedaMapsDetail;
import psp.weatherdam.pojo.openweather.BusquedaOpenWeather;
import psp.weatherdam.retrofit.RetrofitApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    private MapView mMapView;
    private GoogleMap googleMap;
    LocalizacionEnMapa localiz;
    TiempoDelDia tiempoDelDia;
    RetrofitApplication retrofitApplication;
    BusquedaOpenWeather tiempoActual;
    DateTime fecha;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

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

        mMapView = (MapView) rootView.findViewById(R.id.mapa);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng actualLocation = new LatLng( localiz.getLatitud(), localiz.getLongitud());
                MarkerOptions marker = new MarkerOptions();
                marker.position(actualLocation).title(localiz.getNombreSitio()).snippet("Description");

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);

                        // Getting reference to the TextView to set latitude
                        TextView title = (TextView) v.findViewById(R.id.txVwNombreMunicInfo);
                        ImageView estadoImg = (ImageView) v.findViewById(R.id.imgVwEstadoInfo);
                        TextView estadoText = (TextView) v.findViewById(R.id.txVwEstadoInfo);
                        TextView temperaturas = (TextView) v.findViewById(R.id.txVwTempInfo);
                        TextView viento = (TextView) v.findViewById(R.id.txVwVientoInfo);

                        title.setText(localiz.getNombreSitio());
                        Picasso.with(getActivity()).load(tiempoDelDia.getIconoEstado())
                                .resize(100,100)
                                .into(estadoImg);
                        estadoText.setText(tiempoDelDia.getEstado());
                        temperaturas.setText(tiempoDelDia.getTemperaturas().get(0));
                        viento.setText(tiempoDelDia.getVelocidadViento().get(0)+" m/s");

                        // Returning the view containing InfoWindow contents
                        return v;
                    }
                });

                googleMap.addMarker(marker);

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(actualLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }

    private void setearDatos(BusquedaMapsDetail body) {
        OpenWeatherAPI api = retrofitApplication.getRetrofitOpenWeather().create(OpenWeatherAPI.class);
        Call<BusquedaOpenWeather> llamada2 = api.getBusquedaOpenWeather(body.getResult().getGeometry().getLocation().getLat(),
                body.getResult().getGeometry().getLocation().getLng());

        //Estos son los datos meteorologicos actuales de Sevilla

        llamada2.enqueue(new Callback<BusquedaOpenWeather>() {
            @Override
            public void onResponse(Response<BusquedaOpenWeather> response, Retrofit retrofit) {
                //ESTO GUARDA LOS DATOS DE LA BUSQUEDA PARA CUANDO SE CAMBIE EL FRAGMENT A "MAPA"
                tiempoActual = response.body();

                localiz = new LocalizacionEnMapa(tiempoActual.getName(),tiempoActual.getCoord().getLat(),tiempoActual.getCoord().getLon());

                List<String> listaTemp = new ArrayList<>();
                List<String> listaViento = new ArrayList<>();

                listaTemp.add(String.valueOf(tiempoActual.getMain().getTempMin()+"º - "+tiempoActual.getMain().getTempMax()+"º"));
                listaViento.add(String.valueOf(tiempoActual.getWind().getSpeed()));

                tiempoDelDia = new TiempoDelDia(tiempoActual.getName(),
                        fecha.getDayOfWeek(),
                        fecha.getDayOfMonth()+"/"+fecha.getMonthOfYear()+"/"+fecha.getYear(),
                        tiempoActual.getWeather().get(0).getDescription(),
                        tiempoActual.getWeather().get(0).getIcon(),
                        listaTemp,
                        listaViento);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
