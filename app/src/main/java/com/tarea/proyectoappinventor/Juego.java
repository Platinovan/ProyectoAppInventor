package com.tarea.proyectoappinventor;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Random;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Juego extends AppCompatActivity {
    //Declaracion de las variables

    //int
    int contador = 0; //Puntaje durante el juego
    int alto, ancho; //Medidas de la pantalla
    int tiempo_partida = 13000;
    int vecesReloj = 0;
    int var;
    int error_counter;

    //boolean
    boolean stat = false; //Por defecto 'ovni.json'
    boolean perdio = false; //Para cuando el usuario pierda
    boolean estadoReloj;

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    //Animaciones lotie
    LottieAnimationView sprite; //Sprite principal
    LottieAnimationView mas_tiempo; //Sprite del reloj
    LottieAnimationView Error1;
    LottieAnimationView Error2;
    LottieAnimationView Error3;

    //Otros
    String UID, USER, SCORE, VOL_STATUS; //Datos necesarios
    String error_animation = "error_animati.json";
    TextView puntaje, tiempo; //Puntaje y tiempo
    Random random = new Random(); //Para los numeros aleatorios
    RelativeLayout background; //Para el cuando falle
    Dialog GameOver;
    CountDownTimer cuentaRegresiva;

    //Audio
    MediaPlayer main_theme;
    MediaPlayer punto;
    MediaPlayer perder;
    int MaxMainThemeVolume = 10;
    int PuntoVolume = 10000000;
    int PerderVolume = 50;
    float log1=(float)(Math.log(MaxMainThemeVolume-8.5)/Math.log(MaxMainThemeVolume));
    float log2=(float)(Math.log(PuntoVolume+(PuntoVolume * 100))/Math.log(PuntoVolume));
    float log3=(float)(Math.log(PerderVolume-17)/Math.log(PuntoVolume));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_juego);

        //Inicializacion de la svariables
        sprite = findViewById(R.id.Sprite);
        puntaje = findViewById(R.id.Puntaje);
        tiempo = findViewById(R.id.TiempoRestante);
        background = findViewById(R.id.background);
        GameOver = new Dialog(Juego.this);
        mas_tiempo = findViewById(R.id.mas_tiempo);
        Error1 = findViewById(R.id.Error1);
        Error2 = findViewById(R.id.Error2);
        Error3 = findViewById(R.id.Error3);

        //Configuracion del sonido
        punto = MediaPlayer.create(this, R.raw.sonido_moneda);
        main_theme = MediaPlayer.create(this, R.raw.main_theme);
        perder = MediaPlayer.create(this, R.raw.sonido_perder);
        main_theme.setVolume(log1, log1);
        punto.setVolume(log2, log2);
        perder.setVolume(log3, log3);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("OvniWallop Users");

        //Se recuperan los valores enviados por la actvidad menu
        Bundle intent = getIntent().getExtras();

        UID = intent.getString("UID");
        USER = intent.getString("USER");
        SCORE = intent.getString("SCORE");
        VOL_STATUS = intent.getString("VOLSTATUS");

        //Se setean los valores de contador y tiempo y se
        //obtienen medidas de la pantalla
        puntaje.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
        puntaje.setText(String.valueOf(contador));
        ObtenerMedidas();
        NuevaCuentaRegresiva();

        //Inicia melodia principal del juego
        if(VOL_STATUS.equals("true")) {
            main_theme.setLooping(true);
            main_theme.start();
        }

        //Cuando se hace click en cualquier otra pare de la pantalla
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++error_counter;

                if(error_counter >= 3) {
                    tiempo.setText("00");
                    perdio = true;
                    mas_tiempo.setClickable(false);
                    PuntajeActualizado();
                    if(contador > Integer.parseInt(SCORE)) {
                        SubirPuntuacion("Puntuacion", contador);
                    }
                    MessageGOver();
                    if(cuentaRegresiva != null){
                        cuentaRegresiva.cancel();
                    }
                }
                switch(error_counter){
                    case 1:
                        Error1.setAnimation(error_animation);
                        Error1.playAnimation();
                        break;
                    case 2:
                        Error2.setAnimation(error_animation);
                        Error2.playAnimation();
                        break;
                    case 3:
                        Error3.setAnimation(error_animation);
                        Error3.playAnimation();
                        break;
                }
            }
        });

        //Cuando se hace click en el sprite
        sprite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VOL_STATUS.equals("true")) {
                    if (punto.isPlaying()) {
                        punto.stop();
                    }
                    punto.setVolume(log2, log2);
                    punto.start();
                }

                //punto.start();
                var = (random.nextInt(1000000) + 1);

                if (!perdio) {
                    if (stat) {
                        contador += 5;
                    } else {
                        contador += 1;
                    }

                    //Cambia la nave dependiendo del numero aleatorio en 'int var'
                    if (var <= 960000) {
                        stat = false;
                        sprite.setAnimation("ovni.json");
                        if(!estadoReloj){
                            MasTiempo();
                        }

                    } else if (var == 973642) {
                        stat = false;
                        contador += 50;
                        sprite.setAnimation("super_rare.json");
                    } else {
                        stat = true;
                        sprite.setAnimation("spaceship.json");
                    }

                    //Se inicia la animacion
                    puntaje.setText(String.valueOf(contador));
                    MoverSprite();
                    sprite.playAnimation();
                }
            }
        });

        //Cuando se presione el reloj
        mas_tiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++vecesReloj;
                if(cuentaRegresiva != null){
                    tiempo_partida += 5000;
                    cuentaRegresiva.cancel();
                }
                NuevaCuentaRegresiva();

                //Para que solo se pueda una vez
                if(vecesReloj >= 1){
                    mas_tiempo.setImageResource(R.drawable.transparente);
                    mas_tiempo.setClickable(false);
                }
            }
        });
    }


    //Metodo que obtiene las medidas de la pantalla
    private void ObtenerMedidas() {
        Display pantalla = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        pantalla.getSize(point);
        ancho = point.x;
        alto = point.y;
    }

    //Mueve el sprite a una posicion aleatoria
    private void MoverSprite() {
        //Coordenadas maximas y minimas
        int minX = 0;
        int minY = 111;
        int maxX = ancho - sprite.getWidth();
        int maxY = alto - sprite.getHeight();

        //Coordenadas aleatorias
        int x = random.nextInt(((maxX - minX) + 1) + minX);
        int y = random.nextInt(maxY + 1 - minY) + minY;

        sprite.setX(x);
        sprite.setY(y);
    }

    //Metodo para la cuenta regresiva
    private void NuevaCuentaRegresiva(){
        cuentaRegresiva = new CountDownTimer(tiempo_partida + 0, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempo.setText(String.valueOf(millisUntilFinished / 1000));
                tiempo_partida -= 1000;
            }

            @Override
            public void onFinish() {
                //Cuando se termine el tiempo
                tiempo.setText("00");
                Error1.setImageResource(R.drawable.transparente);
                Error2.setImageResource(R.drawable.transparente);
                Error3.setImageResource(R.drawable.transparente);
                perdio = true;
                mas_tiempo.setClickable(false);
                PuntajeActualizado();
                if(contador > Integer.parseInt(SCORE)) {
                    SubirPuntuacion("Puntuacion", contador);
                }
                //Inicia sonido de perder
                main_theme.stop();
                if(VOL_STATUS.equals("true")) {
                    perder.setLooping(false);
                    perder.start();
                }
                MessageGOver();
            }
        }.start();
    }


    private void MessageGOver() {
        GameOver.setContentView(R.layout.game_over);

        AppCompatButton JugarDeNuevo = GameOver.findViewById(R.id.JugarNuevo);
        AppCompatButton IrMenu = GameOver.findViewById(R.id.IrMenu);
        TextView GameOverText = GameOver.findViewById(R.id.FinalText);
        TextView PuntajeObtenido = GameOver.findViewById(R.id.PuntajeObtenido);

        //Cuando se presione Jugar de nuevo
        JugarDeNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiempo_partida = 13000;
                contador = 0;
                error_counter = 0;
                GameOver.dismiss();
                puntaje.setText("0");
                perdio = false;
                Error1.setImageResource(R.drawable.transparente);
                Error2.setImageResource(R.drawable.transparente);
                Error3.setImageResource(R.drawable.transparente);
                //Configuracion del sonido
                if(VOL_STATUS.equals("true")) {
                    main_theme = MediaPlayer.create(Juego.this, R.raw.main_theme);
                    main_theme.setLooping(true);
                    main_theme.setVolume(log1, log1);
                    main_theme.start();
                }
                NuevaCuentaRegresiva();
                MoverSprite();
            }
        });

        //Cuando se presione Ir a Menu
        IrMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                main_theme.stop();
                if(cuentaRegresiva != null){
                    cuentaRegresiva.cancel();
                }
                Intent irMenu = new Intent(getApplicationContext(), MenuInicio.class);
                startActivity(irMenu);
            }
        });

        if(error_counter >= 3){
            GameOverText.setText("Demasiados intentos fallidos");
        }
        PuntajeObtenido.setText(String.valueOf(contador));
        //Inicia sonido de perder
        main_theme.stop();
        if(VOL_STATUS.equals("true")) {
            perder.setLooping(false);
            perder.start();
        }
        GameOver.show();
    }

    //Metodo par el reloj
    private void MasTiempo() {
            //Dependiendo del numero aleatorio el reloj puede aparecer o no
            if (var >= 350000 && var <= 380000) {
                estadoReloj = true;
                int relojX, relojY;
                int minX = 0;
                int minY = 111;
                int maxX = ancho - mas_tiempo.getWidth();
                int maxY = alto - mas_tiempo.getHeight();
                relojX = random.nextInt(((maxX - minX) + 1) + minX);
                relojY = random.nextInt(maxY + 1 - minY) + minY;

                mas_tiempo.setX(relojX);
                mas_tiempo.setY(relojY);
                mas_tiempo.setAnimation("mas_tiempo.json");
                mas_tiempo.setClickable(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mas_tiempo.setImageResource(R.drawable.transparente);
                        mas_tiempo.setClickable(false);
                        estadoReloj = false;
                    }
                }, 1200);

            } else {
                mas_tiempo.setImageResource(R.drawable.transparente);
                mas_tiempo.setClickable(false);
            }
    }

    private void SubirPuntuacion(String key, int puntaje){
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put(key, puntaje);
        JUGADORES.child(user.getUid()).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    //Consultar puntaje actualizado en firebas
    private void PuntajeActualizado(){
        Query query = JUGADORES.orderByChild("Correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    //Se obtienen los datos de la base de datos
                    SCORE = ""+ds.child("Puntuacion").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //On back pressed para corregir bugs y fallos
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Corrige que el sonido se siga reproduciendo despues de salir
        if(main_theme.isPlaying()){
            main_theme.stop();
        }

        if(punto.isPlaying()){
            punto.stop();
        }

        //Corrige el bug del tiempo al regresar
        if(cuentaRegresiva != null){
            cuentaRegresiva.cancel();
        }
    }
}