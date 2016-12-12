package psp.weatherdam.fragment;


import android.Manifest;
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

import org.parceler.Parcels;

import psp.weatherdam.Constantes;
import psp.weatherdam.R;
import psp.weatherdam.pojo.LocalizacionEnMapa;
import psp.weatherdam.pojo.TiempoDelDia;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    LocalizacionEnMapa localiz;
    TiempoDelDia tiempoDelDia;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapa);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately


        localiz = Parcels.unwrap(getArguments().getParcelable(Constantes.PARCEL_LOCATION));
        tiempoDelDia = Parcels.unwrap(getArguments().getParcelable(Constantes.PARCEL_WEATHERNOW));

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
}
