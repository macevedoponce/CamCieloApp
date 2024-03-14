package com.acevedo.caminoalcielo.ui.Cuenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acevedo.caminoalcielo.MainActivity;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.ui.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;


public class CuentaFragment extends Fragment {

    MaterialCardView cvCerrarSesion;
    ImageView ivFoto, ivCerrarSesion;
    TextView tvNombres, tvApellidos, tvDni;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);
        cvCerrarSesion = view.findViewById(R.id.cvCerrarSesion);
        ivFoto = view.findViewById(R.id.ivFotoUsuario);
        tvNombres = view.findViewById(R.id.tvNombresUsuario);
        tvApellidos = view.findViewById(R.id.tvApellidosUsuario);
        tvDni = view.findViewById(R.id.tvDniUsuario);
        ivCerrarSesion = view.findViewById(R.id.ivCerrarSesion);
        recuperarPreferencias();
        ThemeActive();
        cvCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getActivity().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);

                getActivity().finish();
            }
        });


        return view;
    }

    private void recuperarPreferencias() {
        SharedPreferences preferences = getContext().getSharedPreferences("userLogin", Context.MODE_PRIVATE);

        String id = preferences.getString("id","");
        String dni = preferences.getString("dni","");
        String nombres = preferences.getString("nombres","");
        String apellidos = preferences.getString("apellidos","");
        String foto = preferences.getString("foto","");

        tvNombres.setText(nombres);
        tvApellidos.setText(apellidos);
        tvDni.setText(dni);
        Glide
                .with(CuentaFragment.this)
                .load(foto)
                .centerCrop()
                .into(ivFoto);

    }

    private void ThemeActive() {
        // Verificar el modo actual del tema
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Modo claro

                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Modo oscuro
                ivCerrarSesion.setImageTintList(ColorStateList.valueOf(Color.WHITE));

                break;
        }
    }
}