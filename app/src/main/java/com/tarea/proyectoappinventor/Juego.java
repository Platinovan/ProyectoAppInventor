package com.tarea.proyectoappinventor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import com.airbnb.lottie.LottieAnimationView;

public class Juego extends AppCompatActivity {
    //Declaracion de las variables
    LottieAnimationView sprite;
    String UID, USER, SCORE;
    TextView puntaje, tiempo;
    Random random = new Random();
    int contador = 0;
    //Variable para saber cual de las naves se esta mostrando
    boolean stat = false;
    //Medidas de la pantalla
    int alto, ancho;


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

        //Se recuperan los valores enviados por la actvidad menu
        Bundle intent = getIntent().getExtras();

        UID = intent.getString("UID");
        USER = intent.getString("USER");
        SCORE = intent.getString("SCORE");

        //Se setean los valores de contador y tiempo
        puntaje.setText(String.valueOf(contador));
        //Se obtienen las medidas de la pantalla
        ObtenerMedidas();

        sprite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int var = (random.nextInt(1000000) + 1);

                //Suma el puntaje dependiendo de la nave
                if(stat){
                    contador += 5;
                }else{
                    contador += 1;
                }

                //Cambia la nave dependiendo del numero aleatorio en 'int var'
                if(var <= 960000){
                    sprite.setAnimation("ovni.json");
                }else if(var == 973642){
                    sprite.setAnimation("super_rare.json");
                }else{
                    sprite.setAnimation("spaceship.json");
                }

                //Se inicia la animacion
                puntaje.setText(String.valueOf(contador));
                MoverSprite();
                sprite.playAnimation();
            }
        });

    }

    //Metodo que obtiene las medidas de la pantalla
    private void ObtenerMedidas(){
        Display pantalla = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        pantalla.getSize(point);
        ancho = point.x;
        alto = point.y;
    }

    //Mueve el sprite a una posicion aleatoria
    private void MoverSprite(){
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
}