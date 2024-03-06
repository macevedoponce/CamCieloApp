package com.acevedo.caminoalcielo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.caminoalcielo.Clases.Avatars;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;

import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolderAvatar> {

    List<Avatars> listaAvatars;
    View vista;
    int posicionMarcada = 0;
    public AvatarAdapter(List<Avatars> listaAvatars){
        this.listaAvatars = listaAvatars;
    }

    @NonNull
    @Override
    public ViewHolderAvatar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avatar, parent, false);
        return new ViewHolderAvatar(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAvatar holder, int position) {
        holder.ivAvatar.setImageResource(listaAvatars.get(position).getAvatarId());
        final int pos = position;
        holder.cvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posicionMarcada = pos;
                Util.avatarSeleccion = listaAvatars.get(pos);
                Util.avatarIdSeleccion = pos+1;
                Util.nombreAvatar = listaAvatars.get(pos).getNombre();
                notifyDataSetChanged();
            }
        });

        if(posicionMarcada == position){
            holder.cvSeleccion.setCardBackgroundColor(vista.getResources().getColor(R.color.selected));
        }else{
            holder.cvSeleccion.setCardBackgroundColor(vista.getResources().getColor(R.color.transparente));


        }
    }

    @Override
    public int getItemCount() {
        return listaAvatars.size();
    }

    public class ViewHolderAvatar extends RecyclerView.ViewHolder{

        CardView cvAvatar, cvSeleccion;
        ImageView ivAvatar;

        public ViewHolderAvatar(@NonNull View itemView) {
            super(itemView);
            cvAvatar = itemView.findViewById(R.id.cvAvatar);
            cvSeleccion = itemView.findViewById(R.id.cvSeleccion);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
