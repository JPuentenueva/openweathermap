package psp.weatherdam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import psp.weatherdam.Constantes;
import psp.weatherdam.R;
import psp.weatherdam.interfaces.IHistorialListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link IHistorialListener}
 * interface.
 */
public class HistorialFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private IHistorialListener mListener;
    List<String> municipios;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistorialFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HistorialFragment newInstance(int columnCount) {
        HistorialFragment fragment = new HistorialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences shPre = getActivity().getApplication().getSharedPreferences(Constantes.SHARED_PREFERENCES_FILE,Context.MODE_PRIVATE);

        Set<String> historial = shPre.getStringSet(Constantes.SHARPREF_HISTORIAL, null);
        municipios = new ArrayList<>();
        List<String[]> listaAux = new ArrayList<>();

        if (historial != null) {

            for (String datos_historial : historial) {
                String[] orden_nombre = datos_historial.split(";");
                listaAux.add(orden_nombre);
            }

            //Ordena la lista
            while (municipios.size() < listaAux.size()) {
                switch (municipios.size()) {
                    case 0:
                        for (String[] municip : listaAux) {
                            if (municip[0].equalsIgnoreCase("1")) {
                                municipios.add(municip[1]+";"+municip[2]);
                            }
                        }
                        break;
                    case 1:
                        for (String[] municip : listaAux) {
                            if (municip[0].equalsIgnoreCase("2")) {
                                municipios.add(municip[1]+";"+municip[2]);
                            }
                        }
                        break;
                    case 2:
                        for (String[] municip : listaAux) {
                            if (municip[0].equalsIgnoreCase("3")) {
                                municipios.add(municip[1]+";"+municip[2]);
                            }
                        }
                        break;
                    case 3:
                        for (String[] municip : listaAux) {
                            if (municip[0].equalsIgnoreCase("4")) {
                                municipios.add(municip[1]+";"+municip[2]);
                            }
                        }
                        break;
                    case 4:
                        for (String[] municip : listaAux) {
                            if (municip[0].equalsIgnoreCase("5")) {
                                municipios.add(municip[1]+";"+municip[2]);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new HistorialAdapter(municipios , mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IHistorialListener) {
            mListener = (IHistorialListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IHistorialListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
