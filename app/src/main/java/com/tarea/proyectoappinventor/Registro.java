/*
En esta actividad el usuario ingresa los datos de registro
y es registrado en la base de datos de firebase
*/

package com.tarea.proyectoappinventor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Registro extends AppCompatActivity {
    //Declaracion de variables globales
    EditText MAIL, PASSWORD, NICKNAME;
    AppCompatButton createAccount;
    FirebaseAuth auth;
    TextView CDATE;

    //para ver el password
    AppCompatButton verPass;
    boolean passStat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registro);

        //Define los valores de las variables
        MAIL = findViewById(R.id.correo);
        PASSWORD = findViewById(R.id.password);
        NICKNAME = findViewById(R.id.apodo);
        CDATE = findViewById(R.id.creationDate);
        createAccount = findViewById(R.id.Registro);
        auth = FirebaseAuth.getInstance();
        verPass = findViewById(R.id.VerPasswordRegistro);

        Date date = new Date();
        SimpleDateFormat datefor = new SimpleDateFormat("d'/'MMMM'/'yyyy");
        String d = datefor.format(date);
        CDATE.setText(d);

        //Cuando se presiona el boton para ver el password
        verPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passStat = !passStat;
                if(passStat){
                    verPass.setBackgroundResource(R.drawable.hidden);
                    PASSWORD.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    PASSWORD.setSelection(PASSWORD.length());
                }else{
                    verPass.setBackgroundResource(R.drawable.view_dos);
                    PASSWORD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    PASSWORD.setSelection(PASSWORD.length());
                }
            }
        });

        //'OnClickListener' detecta cuando el boton es presionado y valida el formato de la informacion
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = MAIL.getText().toString();
                String pass = PASSWORD.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    MAIL.setError("Direccion de correo invalida");
                    MAIL.setFocusable(true);
                }if(pass.length() < 6){
                    PASSWORD.setError("La contraseña debe tener 6 caracteres como minimo");
                    PASSWORD.setFocusable(true);
                }else{
                    singUpPlayer(mail, pass);
                }
            }
        });
    }

    private void singUpPlayer(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            int score = 0;

                            assert user != null;
                            String userUID = user.getUid();
                            String mailStr = MAIL.getText().toString();
                            String passStr = PASSWORD.getText().toString();
                            String nicknameStr = NICKNAME.getText().toString();
                            String dateStr = CDATE.getText().toString();

                            HashMap<Object, Object> userData = new HashMap<>();
                            userData.put("Uid", userUID);
                            userData.put("Correo", mailStr);
                            userData.put("Contraseña", passStr);
                            userData.put("Apodo", nicknameStr);
                            userData.put("SeUnio", dateStr);
                            userData.put("Puntuacion", score);
                            userData.put("Imagen", "");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("OvniWallop Users");
                            reference.child(userUID).setValue(userData);
                            Toast.makeText(getApplicationContext(), "Registrado con exito", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MenuInicio.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                //Solo si algo falla durante el registro
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
