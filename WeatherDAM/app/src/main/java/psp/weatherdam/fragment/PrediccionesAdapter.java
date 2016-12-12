package psp.weatherdam.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import psp.weatherdam.R;
import psp.weatherdam.interfaces.IPrediccionesListener;
import psp.weatherdam.pojo.TiempoDelDia;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TiempoDelDia} and makes a call to the
 * specified {@link IPrediccionesListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PrediccionesAdapter extends RecyclerView.Adapter<PrediccionesAdapter.ViewHolder> {

    private final List<TiempoDelDia> mValues;
    private final IPrediccionesListener mListener;
    private final Context ctx;

    public PrediccionesAdapter(List<TiempoDelDia> items, IPrediccionesListener listener, Context ctx) {
        mValues = items;
        mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_predicciones, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Picasso.with(ctx).load(holder.mItem.getIconoEstado())
                .resize(130,130)
                .into(holder.mImagenEstadoView);

        holder.mDiaView.setText(holder.mItem.getNombreDia()+", "+holder.mItem.getFecha());
        holder.mEstadoView.setText(holder.mItem.getEstado());

        for(int i=0;i<holder.mItem.getTemperaturas().size();i++){
            switch(i) {
                case 0:
                    holder.mTemp1View.setText(holder.mItem.getTemperaturas().get(i));
                    break;
                case 1:
                    holder.mTemp2View.setText(holder.mItem.getTemperaturas().get(i));
                    break;
                case 2:
                    holder.mTemp3View.setText(holder.mItem.getTemperaturas().get(i));
                    break;
                case 3:
                    holder.mTemp4View.setText(holder.mItem.getTemperaturas().get(i));
                    break;
                case 4:
                    holder.mTemp5View.setText(holder.mItem.getTemperaturas().get(i));
                    break;
                case 5:
                    holder.mTemp6View.setText(holder.mItem.getTemperaturas().get(i));
                    break;
                case 6:
                    holder.mTemp7View.setText(holder.mItem.getTemperaturas().get(i));
                    break;
                case 7:
                    holder.mTemp8View.setText(holder.mItem.getTemperaturas().get(i));
                    break;
                default:
                    break;
            }
        }

        if (holder.mItem.getCantidadLluvia() != null) {
            for (int i = 0; i < holder.mItem.getCantidadLluvia().size(); i++) {
                switch (i) {
                    case 0:
                        holder.mPrecipitacion1View.setText(holder.mItem.getCantidadLluvia().get(i));
                        break;
                    case 1:
                        holder.mPrecipitacion2View.setText(holder.mItem.getCantidadLluvia().get(i));
                        break;
                    case 2:
                        holder.mPrecipitacion3View.setText(holder.mItem.getCantidadLluvia().get(i));
                        break;
                    case 3:
                        holder.mPrecipitacion4View.setText(holder.mItem.getCantidadLluvia().get(i));
                        break;
                    case 4:
                        holder.mPrecipitacion5View.setText(holder.mItem.getCantidadLluvia().get(i));
                        break;
                    case 5:
                        holder.mPrecipitacion6View.setText(holder.mItem.getCantidadLluvia().get(i));
                        break;
                    case 6:
                        holder.mPrecipitacion7View.setText(holder.mItem.getCantidadLluvia().get(i));
                        break;
                    case 7:
                        holder.mPrecipitacion8View.setText(holder.mItem.getCantidadLluvia().get(i));
                        break;
                    default:
                        break;
                }
            }
        }
            for(int i=0;i<holder.mItem.getVelocidadViento().size();i++){
                switch(i) {
                    case 0:
                        holder.mViento1View.setText(holder.mItem.getVelocidadViento().get(i));
                        break;
                    case 1:
                        holder.mViento1View.setText(holder.mItem.getVelocidadViento().get(i));
                        break;
                    case 2:
                        holder.mViento1View.setText(holder.mItem.getVelocidadViento().get(i));
                        break;
                    case 3:
                        holder.mViento1View.setText(holder.mItem.getVelocidadViento().get(i));
                        break;
                    case 4:
                        holder.mViento1View.setText(holder.mItem.getVelocidadViento().get(i));
                        break;
                    case 5:
                        holder.mViento1View.setText(holder.mItem.getVelocidadViento().get(i));
                        break;
                    case 6:
                        holder.mViento1View.setText(holder.mItem.getVelocidadViento().get(i));
                        break;
                    case 7:
                        holder.mViento1View.setText(holder.mItem.getVelocidadViento().get(i));
                        break;
                    default:
                        break;
                }
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.

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
        public final ImageView mImagenEstadoView;
        public final TextView mEstadoView;
        public final TextView mDiaView;
        public final TextView mPrecipitacion1View;
        public final TextView mPrecipitacion2View;
        public final TextView mPrecipitacion3View;
        public final TextView mPrecipitacion4View;
        public final TextView mPrecipitacion5View;
        public final TextView mPrecipitacion6View;
        public final TextView mPrecipitacion7View;
        public final TextView mPrecipitacion8View;
        public final TextView mViento1View;
        public final TextView mViento2View;
        public final TextView mViento3View;
        public final TextView mViento4View;
        public final TextView mViento5View;
        public final TextView mViento6View;
        public final TextView mViento7View;
        public final TextView mViento8View;
        public final TextView mTemp1View;
        public final TextView mTemp2View;
        public final TextView mTemp3View;
        public final TextView mTemp4View;
        public final TextView mTemp5View;
        public final TextView mTemp6View;
        public final TextView mTemp7View;
        public final TextView mTemp8View;

        public TiempoDelDia mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImagenEstadoView = (ImageView) view.findViewById(R.id.imageViewEstado);
            mEstadoView = (TextView) view.findViewById(R.id.textViewEstadoCielo);
            mDiaView = (TextView) view.findViewById(R.id.textViewDia);
            mPrecipitacion1View = (TextView) view.findViewById(R.id.txVwPrec1);
            mPrecipitacion2View = (TextView) view.findViewById(R.id.txVwPrec2);
            mPrecipitacion3View = (TextView) view.findViewById(R.id.txVwPrec3);
            mPrecipitacion4View = (TextView) view.findViewById(R.id.txVwPrec4);
            mPrecipitacion5View = (TextView) view.findViewById(R.id.txVwPrec5);
            mPrecipitacion6View = (TextView) view.findViewById(R.id.txVwPrec6);
            mPrecipitacion7View = (TextView) view.findViewById(R.id.txVwPrec7);
            mPrecipitacion8View = (TextView) view.findViewById(R.id.txVwPrec8);
            mViento1View = (TextView) view.findViewById(R.id.txVwVien1);
            mViento2View = (TextView) view.findViewById(R.id.txVwVien2);
            mViento3View = (TextView) view.findViewById(R.id.txVwVien3);
            mViento4View = (TextView) view.findViewById(R.id.txVwVien4);
            mViento5View = (TextView) view.findViewById(R.id.txVwVien5);
            mViento6View = (TextView) view.findViewById(R.id.txVwVien6);
            mViento7View = (TextView) view.findViewById(R.id.txVwVien7);
            mViento8View = (TextView) view.findViewById(R.id.txVwVien8);
            mTemp1View = (TextView) view.findViewById(R.id.txVwTemp1);
            mTemp2View = (TextView) view.findViewById(R.id.txVwTemp2);
            mTemp3View = (TextView) view.findViewById(R.id.txVwTemp3);
            mTemp4View = (TextView) view.findViewById(R.id.txVwTemp4);
            mTemp5View = (TextView) view.findViewById(R.id.txVwTemp5);
            mTemp6View = (TextView) view.findViewById(R.id.txVwTemp6);
            mTemp7View = (TextView) view.findViewById(R.id.txVwTemp7);
            mTemp8View = (TextView) view.findViewById(R.id.txVwTemp8);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
