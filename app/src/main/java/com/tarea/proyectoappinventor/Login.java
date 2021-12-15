package com.tarea.proyectoappinventor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Locale;

public class Login extends AppCompatActivity {
    //Declaracion de variables
    EditText mail, password;
    AppCompatButton login;
    AppCompatButton verPass;
    FirebaseAuth auth;

    //Para ver la contraseña
    boolean estadoPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        //Definicion de las variables
        mail = findViewById(R.id.CorreoLogin);
        password = findViewById(R.id.PasswordLogin);
        login = findViewById(R.id.InicioSesion);
        verPass = findViewById(R.id.VerPasswordLogin);
        auth = FirebaseAuth.getInstance();

        //Para poder ver la contraseña
        verPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estadoPass = !estadoPass;
                //Para ver la password
                if(estadoPass){
                    verPass.setBackgroundResource(R.drawable.hidden);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.length());
                }else{
                    verPass.setBackgroundResource(R.drawable.view_dos);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.length());
                }
            }
        });

        //Click listener para el boton de 'entrar'
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = mail.getText().toString();
                String pass = password.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    mail.setError("Direccion de correo invalida");
                    mail.setFocusable(true);
                }if(pass.length() < 6){
                    password.setError("Minimo 6 caracteres");
                    password.setFocusable(true);
                }else{
                    LoginJugador(correo, pass);
                }
            }
        });
    }

    //Metodo para logear al jugador
    private void LoginJugador(String correo, String pass) {
        auth.signInWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MenuInicio.class));
                            assert user != null; //Confirma que el usuario no es nulo
                            Toast.makeText(getApplicationContext(), "Iniciaste sesion como"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    //Si el inicio de sesion falla se muestra el toast
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}