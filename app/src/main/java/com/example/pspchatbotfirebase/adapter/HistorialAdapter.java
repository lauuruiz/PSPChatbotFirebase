package com.example.pspchatbotfirebase.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pspchatbotfirebase.R;
import com.example.pspchatbotfirebase.firebase.ChatSentence;

import java.util.ArrayList;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> fechas;
    private ArrayList<ChatSentence> sentences;

    public HistorialAdapter(ArrayList<String> fechas, ArrayList<ChatSentence> sentences){
        this.fechas = fechas; this.sentences = sentences;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_historial, parent, false);
        HistorialViewHolder vh = new HistorialViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        final ChatSentence sentence = sentences.get(position);
        holder.tvFecha.setText(fechas.get(position));
        holder.tvFraseEspanol.setText(sentences.get(position).getFraseEn());
        holder.tvFraseIngles.setText(sentences.get(position).getFraseEs());
        holder.tvHora.setText(sentences.get(position).getHora());
        holder.tvHablante.setText(sentences.get(position).getUsuario());
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder {

        TextView tvFecha, tvFraseEspanol, tvFraseIngles, tvHora, tvHablante;

        public HistorialViewHolder(@NonNull View itemView){
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvFraseEspanol = itemView.findViewById(R.id.tvFraseEspanol);
            tvFraseIngles = itemView.findViewById(R.id.tvFraseIngles);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvHablante = itemView.findViewById(R.id.tvHablante);
        }
    }
}
