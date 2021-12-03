package com.tarea.proyectoappinventor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import com.airbnb.lottie.LottieAnimationView;

public class Juego extends AppCompatActivity {
    //int
    int contador = 0; //Puntaje durante el juego
    int alto, ancho; //Medidas de la pantalla
    int tiempo_partida = 11000;
    int vecesReloj = 0;
    int var;

    //boolean
    boolean stat = false; //Por defecto 'ovni.json'
    boolean perdio = false; //Para cuando el usuario pierda
    boolean estadoReloj;

    //Otros
    //Declaracion de las variables
    LottieAnimationView sprite; //Sprite principal
    LottieAnimationView mas_tiempo; //Sprite del reloj
    String UID, USER, SCORE; //Datos necesarios
    TextView puntaje, tiempo; //Puntaje y tiempo
    Random random = new Random(); //Para los numeros aleatorios
    RelativeLayout background; //Para el cuando falle
    Dialog GameOver;
    CountDownTimer cuentaRegresiva;


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
        var = (random.nextInt(1000000) + 1);

        //Se recuperan los valores enviados por la actvidad menu
        Bundle intent = getIntent().getExtras();

        UID = intent.getString("UID");
        USER = intent.getString("USER");
        SCORE = intent.getString("SCORE");

        //Se setean los valores de contador y tiempo y se
        //obtienen medidas de la pantalla
        puntaje.setText(String.valueOf(contador));
        ObtenerMedidas();
        NuevaCuentaRegresiva();

        //Cuando se hace click en cualquier otra pare de la pantalla
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Mistake!", Toast.LENGTH_SHORT).show();
            }
        });

        //Cuando se hace click en el sprite
        sprite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                perdio = true;
                mas_tiempo.setClickable(false);
                MessageGOver();
            }
        }.start();
    }


    private void MessageGOver() {
        GameOver.setContentView(R.layout.game_over);

        AppCompatButton JugarDeNuevo = GameOver.findViewById(R.id.JugarNuevo);
        AppCompatButton IrMenu = GameOver.findViewById(R.id.IrMenu);

        //Cuando se presione Jugar de nuevo
        JugarDeNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent JugarNuevo = new Intent(getApplicationContext(), Juego.class);
                Toast.makeText(getApplicationContext(), "Jugar de nuevo", Toast.LENGTH_SHORT).show();
            }
        });

        //Cuando se presione Ir a Menu
        IrMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irMenu = new Intent(getApplicationContext(), MenuInicio.class);
                startActivity(irMenu);
            }
        });

        GameOver.show();
    }

    private void MasTiempo() {
            //Dependiendo del numero aleatorio el reloj puede aparecer o no
            if (var >= 100000 && var <= 850000) {
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
                }, 2500);

            } else {
                mas_tiempo.setImageResource(R.drawable.transparente);
                mas_tiempo.setClickable(false);
            }
    }
}