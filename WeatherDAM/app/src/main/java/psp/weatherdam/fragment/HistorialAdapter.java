package psp.weatherdam.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import psp.weatherdam.R;
import psp.weatherdam.interfaces.IHistorialListener;
import psp.weatherdam.pojo.ElementoHistorial;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ElementoHistorial} and makes a call to the
 * specified {@link IHistorialListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private final List<String> mValues;
    private final IHistorialListener mListener;

    public HistorialAdapter(List<String> items, IHistorialListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_historial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mNombreView.setText(holder.mItem.split(";")[0]);
        holder.mHoraView.setText(holder.mItem.split(";")[1]);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //TODO definir OnClick para acceder a los datos de este municipio
                    //mListener.onClickMunicipio(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombreView;
        public final TextView mHoraView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNombreView = (TextView) view.findViewById(R.id.textViewMunicipioHist);
            mHoraView = (TextView) view.findViewById(R.id.textViewFechaHist);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
