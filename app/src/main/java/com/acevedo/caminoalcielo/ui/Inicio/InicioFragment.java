package com.acevedo.caminoalcielo.ui.Inicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acevedo.caminoalcielo.Login.LoginActivity;
import com.acevedo.caminoalcielo.MainActivity;
import com.acevedo.caminoalcielo.R;

import java.util.Objects;


public class InicioFragment extends Fragment {

    TextView tvNombres, tvApellidos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inicio, container, false);
        tvNombres = view.findViewById(R.id.tvNombres);
        tvApellidos = view.findViewById(R.id.tvApellidos);
        recuperarPreferencias();
        return view;
    }

    private void recuperarPreferencias() {
        SharedPreferences preferences = requireContext().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        String nombres = preferences.getString("nombres","");
        String apellidos = preferences.getString("apellidos","");

        tvNombres.setText(nombres);
        tvApellidos.setText(apellidos);

    }
}