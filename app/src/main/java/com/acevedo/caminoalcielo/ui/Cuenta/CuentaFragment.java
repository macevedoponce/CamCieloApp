package com.acevedo.caminoalcielo.ui.Cuenta;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.caminoalcielo.Clases.Lenguajes;
import com.acevedo.caminoalcielo.MainActivity;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.ui.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;


public class CuentaFragment extends Fragment {

    MaterialCardView cvCerrarSesion, cvEditProfile, cvIdioma;
    ImageView ivFoto, ivCerrarSesion, ivProfile, ivIdioma, ivTamFuente, ivNextProfile, ivNextIdioma;
    TextView tvNombres, tvApellidos, tvDni;

    String id, dni, nombres, apellidos;

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
        cvIdioma = view.findViewById(R.id.cvIdioma);
        cvEditProfile = view.findViewById(R.id.cvEditProfile);
        ivFoto = view.findViewById(R.id.ivFotoUsuario);
        tvNombres = view.findViewById(R.id.tvNombresUsuario);
        tvApellidos = view.findViewById(R.id.tvApellidosUsuario);
        tvDni = view.findViewById(R.id.tvDniUsuario);
        ivCerrarSesion = view.findViewById(R.id.ivCerrarSesion);
        ivProfile = view.findViewById(R.id.ivProfile);
        ivIdioma = view.findViewById(R.id.ivIdioma);
        ivNextProfile = view.findViewById(R.id.ivNextProfile);
        ivNextIdioma = view.findViewById(R.id.ivNextIdioma);
        recuperarPreferencias();
        ThemeActive();
        cvCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getActivity().getSharedPreferences("userLoginDocenteCaminoCieloApp", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);

                getActivity().finish();
            }
        });

        cvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("id_usuario", id);
                i.putExtra("nombres", nombres);
                i.putExtra("apellidos", apellidos);
                i.putExtra("dni", dni);
                startActivity(i);
            }
        });

        cvIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCambiarLenguaje();
            }
        });
        return view;
    }

    private void showCambiarLenguaje() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogo_lenguajes);

        NumberPicker lenguaje = dialog.findViewById(R.id.lenguaje);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);

        Lenguajes.initLenguajes();


        lenguaje.setMaxValue(Lenguajes.getLenguajesArrayList().size()-1);
        lenguaje.setDisplayedValues(Lenguajes.LenguajesNames());

        lenguaje.setMinValue(0);

        lenguaje.setValue(0);



        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idioma="Español";
                if(lenguaje.getValue() > -1){
                    idioma = Lenguajes.getLenguajesArrayList().get(lenguaje.getValue()).getLenguaje();
                }
                if(idioma.equals("Ingles")){
                    setLocale("en");
                }else{
                    setLocale("es");
                } dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void setLocale(String lenguaje) {
        Locale locale = new Locale(lenguaje);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getContext().getResources().updateConfiguration(configuration,this.getResources().getDisplayMetrics());

        SharedPreferences preferences = getActivity().getSharedPreferences("userLoginDocenteCaminoCieloApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lenguaje",lenguaje);
        editor.commit();
    }

    private void recuperarPreferencias() {
        SharedPreferences preferences = getContext().getSharedPreferences("userLoginDocenteCaminoCieloApp", Context.MODE_PRIVATE);

        id = preferences.getString("id","");
        dni = preferences.getString("dni","");
        nombres = preferences.getString("nombres","");
        apellidos = preferences.getString("apellidos","");
        String foto = preferences.getString("foto","");

        tvNombres.setText(nombres);
        tvApellidos.setText(apellidos);
        tvDni.setText(dni);
        if(foto.isEmpty() || foto.equals("null")){
            ivFoto.setImageResource(R.drawable.bg_bottom_create_user);
        }else{
            Glide.with(CuentaFragment.this)
                .load(foto)
                .centerCrop()
                .into(ivFoto);
        }
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
                ivProfile.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                ivIdioma.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                ivTamFuente.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                ivNextProfile.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                ivNextIdioma.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
        }
    }
}