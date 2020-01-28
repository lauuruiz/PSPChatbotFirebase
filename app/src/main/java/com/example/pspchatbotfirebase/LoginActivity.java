package com.example.pspchatbotfirebase;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Button btLogin, btRegistrarse;
    private EditText etUsuario, etContrasena;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initComponents();
        initEvents();
    }

    private void initComponents(){
        fab = findViewById(R.id.fab);

        btLogin = findViewById(R.id.btLogin);

        etUsuario = findViewById(R.id.etLoginUsuario);
        etContrasena = findViewById(R.id.etLoginContra);

        btRegistrarse = findViewById(R.id.btRegistrarse);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initEvents(){
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etUsuario.getText().toString().isEmpty() && !etContrasena.getText().toString().isEmpty()){
                    lanzarActividad();
                }else{
                   Toast.makeText(v.getContext(), "Hay algun fallo...", Toast.LENGTH_LONG).show();
                }

            }
        });

        btRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RegistrarActivity.class);
                startActivity(i);
            }
        });
    }

    private void lanzarActividad(){
        Intent i = new Intent(this, ConversacionActivity.class);
        i.putExtra("nombre", etUsuario.getText().toString());
        startActivity(i);
    }

    /*private void lanzarActividad(){
        //final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(etUsuario.getText().toString(), etContrasena.getText().toString()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if(task.isSuccessful()){
                            Intent i = new Intent(LoginActivity.this, ConversacionActivity.class);
                            i.putExtra("nombre", etUsuario.getText().toString());
                            startActivity(i);
                        }else{
                            Toast.makeText(LoginActivity.this, "Hay algun fallo...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/

}
