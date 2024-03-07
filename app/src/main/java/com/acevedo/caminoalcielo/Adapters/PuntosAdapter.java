package com.acevedo.caminoalcielo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.caminoalcielo.Clases.Puntos;
import com.acevedo.caminoalcielo.R;

import java.util.List;

public class PuntosAdapter extends RecyclerView.Adapter<PuntosAdapter.PuntosViewHolder> {

    Context context;
    List<Puntos> puntos;

    public PuntosAdapter(Context context, List<Puntos> puntos) {
        this.context = context;
        this.puntos = puntos;
    }


    @NonNull
    @Override
    public PuntosAdapter.PuntosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reporte_puntos, parent, false);
        return new PuntosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PuntosAdapter.PuntosViewHolder holder, int position) {
        Puntos punto = puntos.get(position);
        String fecha = punto.getPuntos_fecha();
        int ppart = punto.getPuntos_participacion();
        int pasis = punto.getPuntos_asistencia();
        int pbib = punto.getPuntos_biblia();

        holder.setDatos(fecha, ppart, pasis, pbib);
    }

    @Override
    public int getItemCount() {
        return puntos.size();
    }

    public class PuntosViewHolder extends RecyclerView.ViewHolder {

        TextView tvFecha, tvPParticipacion, tvPAsistencia, tvPBiblia;
        public PuntosViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvPParticipacion = itemView.findViewById(R.id.tvPParticipacion);
            tvPAsistencia = itemView.findViewById(R.id.tvPAsistencia);
            tvPBiblia = itemView.findViewById(R.id.tvPBiblia);
        }

        public void setDatos(String fecha, int ppart, int pasis, int pbib) {
            tvFecha.setText(fecha);
            tvPParticipacion.setText(String.valueOf(ppart));
            tvPAsistencia.setText(String.valueOf(pasis));
            tvPBiblia.setText(String.valueOf(pbib));
        }
    }
}
