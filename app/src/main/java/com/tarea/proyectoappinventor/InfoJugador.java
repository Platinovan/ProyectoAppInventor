package com.tarea.proyectoappinventor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoJugador extends AppCompatActivity {
    //Declaracion de variables
    CircleImageView IMAGEN;
    TextView APODO;
    TextView Correo;
    TextView MejorPuntuacion;
    TextView UID;
    TextView Fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_info_jugador);

        //Inicializacion de las variables
        IMAGEN = findViewById(R.id.ImagenDetalle);
        APODO = findViewById(R.id.ApodoDetalle);
        Correo = findViewById(R.id.CorreoDetalle);
        MejorPuntuacion = findViewById(R.id.PuntuacionDetalle);
        UID = findViewById(R.id.UIDDetalle);
        Fecha = findViewById(R.id.FechaDetalle);

        //Obtencion de los datos enviados por el intent
        String imagenPerfil = getIntent().getStringExtra("Imagen");
        String apodoPerfil = getIntent().getStringExtra("Apodo");
        String uidPerfil = getIntent().getStringExtra("UID");
        String puntuacionPerfil = getIntent().getStringExtra("PUNTUACION");
        String correoPerfil = getIntent().getStringExtra("Correo");
        String fechaPerfil = getIntent().getStringExtra("SeUnio");

        //Seteo de los valores obtenidos en la vista 

        //Imagen
        try {
            Picasso.get().load(imagenPerfil).into(IMAGEN);
        }catch (Exception e){
            Picasso.get().load(R.drawable.default_image_profile_dos).into(IMAGEN);
        }

        //Apodo
        APODO.setText(apodoPerfil);
        MejorPuntuacion.setText(puntuacionPerfil);
        Correo.setText(correoPerfil);
        UID.setText(uidPerfil);
        Fecha.setText(fechaPerfil);
    }
}