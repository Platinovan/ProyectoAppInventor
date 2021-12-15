package com.tarea.proyectoappinventor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
public class TopGlobal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_top_global);
    }
}