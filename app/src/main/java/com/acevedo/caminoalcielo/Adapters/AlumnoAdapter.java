package com.acevedo.caminoalcielo.Adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.caminoalcielo.Clases.Alumnos;
import com.acevedo.caminoalcielo.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Objects;

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder> implements View.OnClickListener{

    Context context;
    List<Alumnos> alumnos;

    View.OnClickListener listener;


    public AlumnoAdapter(Context context, List<Alumnos> alumnos) {
        this.context = context;
        this.alumnos = alumnos;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alumno, parent, false);
        view.setOnClickListener(this);
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoAdapter.AlumnoViewHolder holder, int position) {
        Alumnos alumno = alumnos.get(position);
        String puntPart = alumno.getPuntos_participacion();
        String puntAsis = alumno.getPuntos_asistencia();
        String puntBib = alumno.getPuntos_biblia();

        holder.tvNombres.setText(alumno.getUser_nombres());
        holder.tvApellidos.setText(alumno.getUser_apellidos());
        String foto = alumno.getUser_foto();
        if(Objects.equals(foto, "null")){
            holder.ivFoto.setImageResource(R.drawable.person5);

        }else{
            int id_resource = context.getResources().getIdentifier(alumno.getUser_foto(), "drawable", context.getPackageName());
            holder.ivFoto.setImageResource(id_resource);
        }

        holder.estadoPuntos(puntPart, puntAsis, puntBib);

    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }
    
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }

    public class AlumnoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombres, tvApellidos;
        ImageView ivFoto;

        MaterialCardView cvAlumno;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombres = itemView.findViewById(R.id.tvNombres);
            tvApellidos = itemView.findViewById(R.id.tvApellidos);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            cvAlumno = itemView.findViewById(R.id.cvAlumno);
        }

        public void estadoPuntos(String puntPart, String puntAsis, String puntBib) {
            float strokeWidthInSp = 2f; // Valor deseado en sp

            float strokeWidthInPixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, strokeWidthInSp, context.getResources().getDisplayMetrics()
            );
            cvAlumno.setStrokeWidth((int) strokeWidthInPixels);

            if(!puntPart.equals("null") && !puntAsis.equals("null") && !puntBib.equals("null")){
                cvAlumno.setStrokeColor(ContextCompat.getColor(context, R.color.success));
            }else{
                cvAlumno.setStrokeColor(ContextCompat.getColor(context, R.color.error));
            }
        }
    }
}
