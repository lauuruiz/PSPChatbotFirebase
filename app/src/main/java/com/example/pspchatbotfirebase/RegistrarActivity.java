package com.example.pspchatbotfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegistrarActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasena;
    private Button btRegistrar;
    private String usuario, contrasena, email;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        initComponents();
        initEvents();
    }

    private void initComponents(){
        etUsuario = findViewById(R.id.etUsuarioR);
        etContrasena = findViewById(R.id.etContrasenaR);

        btRegistrar = findViewById(R.id.btRegistrar);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void initEvents(){
        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(etUsuario.getText().toString().isEmpty() && etContrasena.getText().toString().isEmpty())){


                }else{
                    Toast.makeText(RegistrarActivity.this, "Hay errores...", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void registrar(){
        usuario = etUsuario.getText().toString();
        contrasena = etContrasena.getText().toString();

        email = usuario+"@s.com";
        firebaseAuth.createUserWithEmailAndPassword(email, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                }else{
                    Toast.makeText(RegistrarActivity.this, "No se pudo registrar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }




}
