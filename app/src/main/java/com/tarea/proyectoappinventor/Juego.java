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
    //Declaracion de las variables
    LottieAnimationView sprite; //Sprite principal
    LottieAnimationView mas_tiempo; //Sprite del reloj
    String UID, USER, SCORE; //Datos necesarios
    TextView puntaje, tiempo; //Puntaje y tiempo
    Random random = new Random(); //Para los numeros aleatorios
    RelativeLayout background;
    int contador = 0; //Puntaje durante el juego
    int tiempo_partida = 6000;
    //Variable para saber cual de las naves se esta mostrando
    boolean stat = false; //Por defecto 'ovni.json'
    int alto, ancho; //Medidas de la pantalla
    boolean perdio = false; //Para cuando el usuario pierda
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

        //Se recuperan los valores enviados por la actvidad menu
        Bundle intent = getIntent().getExtras();

        UID = intent.getString("UID");
        USER = intent.getString("USER");
        SCORE = intent.getString("SCORE");

        //Se setean los valores de contador y tiempo y se
        //obtienen medidas de la pantalla
        puntaje.setText(String.valueOf(contador));
        ObtenerMedidas();
        //CuentaRegresiva();
        NuevaCuentaRegresiva();

        //Cuando se hace click en cualquier otra pare de la pantalla
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Mistake!", Toast.LENGTH_SHORT).show();
            }
        });

        //Cuando se hace click en el sprite
        sprite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (random.nextInt(1000000) + 1);

                if (!perdio) {
                    //Suma el puntaje dependiendo de la nave
                    if (stat) {
                        contador += 5;
                    } else {
                        contador += 1;
                    }

                    //Cambia la nave dependiendo del numero aleatorio en 'int var'
                    if (var <= 960000) {
                        stat = false;
                        sprite.setAnimation("ovni.json");

                        //Dependiendo el numero puede aparecer el reloj
                        if (var <= 960000) {
                            mas_tiempo.setAnimation("mas_tiempo.json");
                            mas_tiempo.playAnimation();
                            //Cuando se presione el reloj
                            mas_tiempo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Se dan cinco segundos extra
                                    if(cuentaRegresiva != null){
                                        tiempo_partida += 10000;
                                        cuentaRegresiva.cancel();
                                    }else{
                                        tiempo_partida = 0;
                                    }
                                    NuevaCuentaRegresiva();
                                }
                            });
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

    private void CuentaRegresiva() {
        //tiempo_pasado = 0;
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                tiempo.setText(String.valueOf(millisUntilFinished / 1000));
                //tiempo_pasado += 1000;
            }

            public void onFinish() {
                tiempo.setText("00");
                perdio = true;
                MessageGOver();
            }
        }.start();

    }

    private void NuevaCuentaRegresiva(){
        cuentaRegresiva = new CountDownTimer(tiempo_partida + 0, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempo.setText(String.valueOf(millisUntilFinished / 1000));
                tiempo_partida -= 1000;
            }

            @Override
            public void onFinish() {
                tiempo.setText("00");
                perdio = true;
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
                startActivity(JugarNuevo);
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
}