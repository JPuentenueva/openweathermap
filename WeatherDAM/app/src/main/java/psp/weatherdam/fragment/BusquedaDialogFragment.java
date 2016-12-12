package psp.weatherdam.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import org.parceler.Parcels;

import psp.weatherdam.R;
import psp.weatherdam.interfaces.GoogleMapsAPI;
import psp.weatherdam.pojo.googlemapsautocomp.Prediction;
import psp.weatherdam.pojo.googlemapsdetail.BusquedaMapsDetail;
import psp.weatherdam.retrofit.DelayAutoCompleteTextView;
import psp.weatherdam.retrofit.PlacesAutoCompleteAdapter;
import psp.weatherdam.retrofit.RetrofitApplication;
import retrofit.Call;
import retrofit.Retrofit;

/**
 * Created by Jose on 09/12/2016.
 */
public class BusquedaDialogFragment extends DialogFragment {
    Prediction prediccion;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_busqueda, null);

        EditText texto = (EditText) v.findViewById(R.id.texto_busqueda);
        final DelayAutoCompleteTextView autoComplete = (DelayAutoCompleteTextView) v.findViewById(R.id.texto_busqueda);
        autoComplete.setThreshold(3);
        autoComplete.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator));
        autoComplete.setAdapter(new PlacesAutoCompleteAdapter(getActivity()));
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                prediccion = (Prediction) adapterView.getItemAtPosition(position);
                autoComplete.setText(prediccion.getDescription());
            }
        });

        ImageButton btn = (ImageButton) v.findViewById(R.id.button_go);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreSitio = autoComplete.getText().toString();
                if (prediccion != null && nombreSitio.equalsIgnoreCase(prediccion.getDescription())) {
                    RetrofitApplication retrofitApplication = (RetrofitApplication) getActivity().getApplication();
                    retrofitApplication.iniciarRetrofit();
                    Retrofit retrofitActual = retrofitApplication.getRetrofitGoogleDetail();

                    GoogleMapsAPI api = retrofitActual.create(GoogleMapsAPI.class);
                    Call<BusquedaMapsDetail> llamadaMaps = api.getMapsDetail(prediccion.getPlaceId());

                    // Esta línea de código debe ejecutarse cuando haga click en un elemento
                    // de la lista
                    BusquedaDialogFragment.this.getDialog().cancel();
                }
            }
        });

        builder.setView(v);
        builder.setMessage("Buscar una ciudad")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
