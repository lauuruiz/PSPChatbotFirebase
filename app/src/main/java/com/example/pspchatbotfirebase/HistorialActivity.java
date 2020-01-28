package com.example.pspchatbotfirebase;

import android.os.Bundle;

import com.example.pspchatbotfirebase.adapter.HistorialAdapter;
import com.example.pspchatbotfirebase.firebase.ChatSentence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class HistorialActivity extends AppCompatActivity {

    private ArrayList<ChatSentence> sentences = new ArrayList<>();
    //private ArrayList<String> fechas = new ArrayList<>();
    private RecyclerView rvHistorial;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvFecha;

    private String usuario, fraseEn, fraseEs, hora, fecha;
    private ArrayList<String> fechas = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        initComponents();

        Bundle datos = this.getIntent().getExtras();
        final String nombre = datos.getString("nombre");

        sacarInfo(nombre);
    }

    private void initComponents() {
        rvHistorial = findViewById(R.id.rvHistorial);

        layoutManager = new LinearLayoutManager(this);
        rvHistorial.setLayoutManager(layoutManager);

    }

    private void sacarInfo(String nombre){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference referenciaItem = database.getReference("item/"+nombre);

        referenciaItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    fecha = snapshot.getKey();

                    referenciaItem.child(fecha).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                                fecha = snapshot.getKey();
                                fechas.add(fecha);

                                usuario = snapshot2.child("usuario").getValue().toString();
                                fraseEn = snapshot2.child("fraseEn").getValue().toString();
                                fraseEs = snapshot2.child("fraseEs").getValue().toString();
                                hora = snapshot2.child("hora").getValue().toString();

                                sentences.add(new ChatSentence(fraseEs, fraseEn, usuario, hora));
                            }

                            HistorialAdapter historialAdapter = new HistorialAdapter(fechas, sentences);
                            rvHistorial.setAdapter(historialAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*referenciaItem.child("20200124").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //nos devuelve los valores
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        usuario = snapshot.child("usuario").getValue().toString();
                        fraseEn = snapshot.child("fraseEn").getValue().toString();
                        fraseEs = snapshot.child("fraseEs").getValue().toString();
                        hora = snapshot.child("hora").getValue().toString();

                        sentences.add(new ChatSentence(fraseEs, fraseEn, usuario, hora));
                    }

                HistorialAdapter historialAdapter = new HistorialAdapter(sentences);
                rvHistorial.setAdapter(historialAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
}
