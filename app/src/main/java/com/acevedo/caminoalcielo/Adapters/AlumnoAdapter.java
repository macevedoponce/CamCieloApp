package com.acevedo.caminoalcielo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.caminoalcielo.Clases.Alumnos;
import com.acevedo.caminoalcielo.R;

import java.util.List;
import java.util.Objects;

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder> {

    private Context context;
    private List<Alumnos> alumnos;

    public AlumnoAdapter(Context context, List<Alumnos> alumnos) {
        this.context = context;
        this.alumnos = alumnos;
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alumno, parent, false);
        return new AlumnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoAdapter.AlumnoViewHolder holder, int position) {
        Alumnos alumno = alumnos.get(position);
        holder.tvNombres.setText(alumno.getUser_nombres());
        holder.tvApellidos.setText(alumno.getUser_apellidos());
        String foto = alumno.getUser_foto();
        if(Objects.equals(foto, "null")){
            holder.ivFoto.setImageResource(R.drawable.person5);

        }else{
            int id_resource = context.getResources().getIdentifier(alumno.getUser_foto(), "drawable", context.getPackageName());
            holder.ivFoto.setImageResource(id_resource);
        }

    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    public class AlumnoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombres, tvApellidos;
        ImageView ivFoto;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombres = itemView.findViewById(R.id.tvNombres);
            tvApellidos = itemView.findViewById(R.id.tvApellidos);
            ivFoto = itemView.findViewById(R.id.ivFoto);
        }
    }
}
