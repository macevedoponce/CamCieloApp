package com.acevedo.caminoalcielo.Adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.caminoalcielo.Clases.Alumnos;
import com.acevedo.caminoalcielo.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Objects;

public class AlumnoTopAdapter extends RecyclerView.Adapter<AlumnoTopAdapter.ViewHolder> {

    Context context;
    List<Alumnos> alumnosList;


    public AlumnoTopAdapter(Context context, List<Alumnos> alumnosList) {
        this.context = context;
        this.alumnosList = alumnosList;
    }

    @NonNull
    @Override
    public AlumnoTopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_alumno, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoTopAdapter.ViewHolder holder, int position) {
        Alumnos alumno = alumnosList.get(position);
        String nombres = alumno.getUser_nombres();
        String apellidos = alumno.getUser_apellidos();
        String total_puntos = alumno.getTotal_puntos();
        String foto = alumno.getUser_foto();

        holder.tvPuesto.setText(String.valueOf(position+1));
        holder.tvPuntos.setText(total_puntos);
        holder.tvNombres.setText(nombres);
        holder.tvApellidos.setText(apellidos);

//        float strokeWidthInSp = 2f; // Valor deseado en sp
//
//        float strokeWidthInPixels = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_SP, strokeWidthInSp, context.getResources().getDisplayMetrics()
//        );
//        holder.cvFoto.setStrokeWidth((int) strokeWidthInPixels);


        // Cambiar el color del tvNombre basado en la posición
        if (position == 0) { // Primer puesto
            holder.cvPuesto.setCardBackgroundColor(context.getResources().getColor(R.color.primer_p));
            holder.cvPuntos.setCardBackgroundColor(context.getResources().getColor(R.color.primer_p));
            holder.cvFoto.setStrokeColor(ContextCompat.getColor(context, R.color.primer_p));
            holder.ivTrofeo.setVisibility(View.VISIBLE);
            holder.ivCorona.setVisibility(View.VISIBLE);


        } else if (position == 1) { // Segundo puesto
            holder.cvPuesto.setCardBackgroundColor(context.getResources().getColor(R.color.segundo_p));
            holder.cvPuntos.setCardBackgroundColor(context.getResources().getColor(R.color.segundo_p));
            holder.cvFoto.setStrokeColor(ContextCompat.getColor(context, R.color.segundo_p));


        } else if (position == 2) { // Tercer puesto
            holder.cvPuesto.setCardBackgroundColor(context.getResources().getColor(R.color.tercer_p));
            holder.cvPuntos.setCardBackgroundColor(context.getResources().getColor(R.color.tercer_p));
            holder.cvFoto.setStrokeColor(ContextCompat.getColor(context, R.color.tercer_p));


        } else if (position == 3) { // Cuarto puesto
            holder.cvPuesto.setCardBackgroundColor(context.getResources().getColor(R.color.cuarto_p));
            holder.cvPuntos.setCardBackgroundColor(context.getResources().getColor(R.color.cuarto_p));
            holder.cvFoto.setStrokeColor(ContextCompat.getColor(context, R.color.cuarto_p));


        } else {
            // Otros puestos (opcional: puedes establecer un color por defecto aquí)
        }

        if(Objects.equals(foto, "null")){
            holder.ivFoto.setImageResource(R.drawable.person5);

        }else{
            int id_resource = context.getResources().getIdentifier(alumno.getUser_foto(), "drawable", context.getPackageName());
            holder.ivFoto.setImageResource(id_resource);
        }
    }

    @Override
    public int getItemCount() {
        return alumnosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cvFoto, cvPuesto, cvPuntos;
        TextView tvNombres, tvApellidos, tvPuntos, tvPuesto;
        ImageView ivFoto,ivTrofeo,ivCorona;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTrofeo = itemView.findViewById(R.id.ivTrofeo);
            cvFoto = itemView.findViewById(R.id.cvFoto);
            cvPuesto = itemView.findViewById(R.id.cvPuesto);
            cvPuntos = itemView.findViewById(R.id.cvPuntos);
            tvNombres = itemView.findViewById(R.id.tvNombres);
            tvApellidos = itemView.findViewById(R.id.tvApellidos);
            tvPuntos = itemView.findViewById(R.id.tvPuntos);
            tvPuesto = itemView.findViewById(R.id.tvPuesto);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            ivCorona = itemView.findViewById(R.id.ivCorona);

            tvNombres.setSelected(true);
            tvApellidos.setSelected(true);

        }

    }
}
