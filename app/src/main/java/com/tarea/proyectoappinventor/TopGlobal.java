package com.tarea.proyectoappinventor;

import android.os.Bundle;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
public class TopGlobal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.user_item);

    }
}