package com.tarea.proyectoappinventor;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuInicio extends AppCompatActivity {
    //Declaracion de variables
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebasedata;
    DatabaseReference JUGADORES;
    AppCompatButton opciones;
    AppCompatButton Jugar;
    TextView userField; //campo para nombre de usuario
    TextView puntuacion;
    LottieAnimationView Mute;

    //Strings para los datos de firebase
    String usr;
    String score;
    String uid;

    //Variables para la configuracion del audio
    static int contador_boton;
    static boolean audio_status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_inicio);

        //Configuracion del audio
        Mute = findViewById(R.id.Mute);
        if(contador_boton == 0){
            Mute.setAnimation("activate_sound.json");
            Mute.playAnimation();
        }else{
            if(audio_status){
                Mute.setAnimation("activate_sound.json");
                Mute.playAnimation();
            }else{
                Mute.setImageResource(R.drawable.mute);
            }
        }

        //Instanciacion de las variables declaradas
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        opciones = findViewById(R.id.MasOpciones);
        Jugar = findViewById(R.id.Jugar);
        firebasedata = FirebaseDatabase.getInstance();
        JUGADORES = firebasedata.getReference("OvniWallop Users");
        userField = findViewById(R.id.user_field);
        puntuacion = findViewById(R.id.Puntuacion);

        //cuando se presione el boton Mas Opciones
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuOpciones.class);
                startActivity(intent);
            }
        });

        //Activar o desactivar el volumen
        Mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contador_boton = 1;
                audio_status = !audio_status;
                if(audio_status){
                    Mute.setAnimation("activate_sound.json");
                    Mute.playAnimation();
                }else{
                    Mute.setImageResource(R.drawable.mute);
                }

            }
        });

        //Inicia actividad del juego
        Jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent juego = new Intent(getApplicationContext(), Juego.class);

                //Se envian los parametros necesarios a la actividad del juego
                String audioStatus = Boolean.toString(audio_status);
                juego.putExtra("UID", uid);
                juego.putExtra("USER", usr);
                juego.putExtra("SCORE", score);
                juego.putExtra("VOLSTATUS", audioStatus);

                //Se incia la actividad del juego
                startActivity(juego);
            }
        });

    }
    //El metodo onStart verifica que la aplicacion esta abierta y ejecuta el metodo
    //que comprueba si el usuario ya inicio sesion o no
    @Override
    protected void onStart(){
        UsuarioenLinea();
        super.onStart();
    }

    //Este metodo comprueba si el usuario esta en linea
    private void UsuarioenLinea(){
        if(user != null){
            ConsultarDatos();
        }else{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void ConsultarDatos(){
        Query query = JUGADORES.orderByChild("Correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    //Se obtienen los datos de la base de datos
                    usr = ""+ds.child("Apodo").getValue();
                    score = ""+ds.child("Puntuacion").getValue();
                    uid = ""+ds.child("Uid").getValue();

                    //Se ponen los valores en el campo de texto
                    userField.setText(usr);
                    puntuacion.setText(score);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}