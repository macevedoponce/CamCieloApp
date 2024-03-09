package com.acevedo.caminoalcielo.Util;

import com.acevedo.caminoalcielo.Clases.Avatars;
import com.acevedo.caminoalcielo.R;

import java.util.ArrayList;

public class Util {
    public static ArrayList<Avatars> listAvatars = null;
    public static Avatars avatarSeleccion = null;
    public static int avatarIdSeleccion = 0;

    public static String nombreAvatar = "";

    public static void obtenerListaAvatars(){
        listAvatars = new ArrayList<Avatars>();
        listAvatars.add(new Avatars(1, R.drawable.person1, "person1"));
        listAvatars.add(new Avatars(2, R.drawable.person2, "person2"));
        listAvatars.add(new Avatars(3, R.drawable.person3, "person3"));
        listAvatars.add(new Avatars(4, R.drawable.person4, "person4"));
        listAvatars.add(new Avatars(5, R.drawable.person5, "person5"));
        listAvatars.add(new Avatars(6, R.drawable.person6, "person6"));
        listAvatars.add(new Avatars(7, R.drawable.person7, "person7"));
        listAvatars.add(new Avatars(8, R.drawable.person8, "person8"));
        listAvatars.add(new Avatars(9, R.drawable.person9, "person9"));

        avatarSeleccion = listAvatars.get(0);
    }

    public static final String RUTA="http://192.168.18.53/ccieloAPI/";
    public static final String RUTA_CREAR_USUARIO = RUTA + "crear_usuario.php";
    public static final String RUTA_VALIDAR_USUARIO = RUTA + "validar_usuario.php";
    public static final String RUTA_LIST_ALUMNOS = RUTA + "list_alumnos.php";
    public static final String RUTA_CREAR_ALUMNOS = RUTA + "crear_alumno.php";
    public static final String RUTA_REGISTRAR_PUNTOS = RUTA + "registrar_puntos.php";
    public static final String RUTA_REPORTE_PUNTOS = RUTA + "list_reporte_puntos.php";
    public static final String RUTA_LIST_TOP_PUNTOS = RUTA + "list_top_alumnos.php";

}
