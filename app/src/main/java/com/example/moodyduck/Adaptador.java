package com.example.moodyduck;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TIPO_REGISTROS = 1;
    private static final int TIPO_OBJETIVOS = 2;
    ArrayList<Registros> registros;
    ArrayList<Objetivos> objetivos;
    int tipo = 1;
    Context c;

    public Adaptador(Context c, ArrayList<Registros> registros) {
        this.c = c;
        this.registros = registros;
    }
    public Adaptador(Context c, ArrayList<Objetivos> objetivos, int tipo) {
        this.c = c;
        this.objetivos = objetivos;
        this.tipo = tipo;
    }

    public static class RegistroViewHolder extends RecyclerView.ViewHolder{
        TextView horario, humor, notas;
        ImageView imgHumor;
        public RegistroViewHolder(@NonNull View itemView) {
            super(itemView);
            humor = itemView.findViewById(R.id.cardHumor);
            horario = itemView.findViewById(R.id.cardHorario);
            imgHumor = itemView.findViewById(R.id.imgHumor);
            notas = itemView.findViewById(R.id.textoNotas);
        }
        public void bind(Registros registros){
            humor.setText(registros.getHumor());
            horario.setText(registros.getData());
            notas.setText(registros.getNotas());
            switch (registros.getHumor()) {
                case "FELIZ":
                    imgHumor.setBackgroundResource(R.drawable.img_feliz);
                    break;
                case "NEUTRO":
                    imgHumor.setBackgroundResource(R.drawable.img_neutro);
                    break;
                case "TRISTE":
                    imgHumor.setBackgroundResource(R.drawable.img_triste);
                    break;
            }
        }
    }

    public static class ObjetivoViewHolder extends RecyclerView.ViewHolder{
        TextView nome;
        ImageView imgObjetivo;
        CheckBox checkObjetivo;
        public ObjetivoViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeObjetivo);
            imgObjetivo = itemView.findViewById(R.id.imgObjetivo);
            checkObjetivo = itemView.findViewById(R.id.checkObjetivo);
        }

        public void bind(Objetivos objetivos){
            switch (objetivos.getNome()){
                case "dormirCedo":
                    nome.setText("Dormir 8 horas");
                    imgObjetivo.setBackgroundResource(R.drawable.dormir_cedo);
                    break;
                case "estudarMais":
                    nome.setText("Estudar mais");
                    imgObjetivo.setBackgroundResource(R.drawable.estudar_mais);
                    break;
                case "fazerExercicio":
                    nome.setText("Fazer exercícios");
                    imgObjetivo.setBackgroundResource(R.drawable.fazer_exercicio);
                    break;
                case "realizarFaxina":
                    nome.setText("Fazer faxina");
                    imgObjetivo.setBackgroundResource(R.drawable.fazer_faxina);
                    break;
                case "comerSaudavel":
                    nome.setText("Comer saudável");
                    imgObjetivo.setBackgroundResource(R.drawable.comer_saudavel);
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    objetivos.setChecked(!objetivos.isChecked());
                    checkObjetivo.setChecked(objetivos.isChecked());
                }
            });
        }

        public void bindAlt(Objetivos objetivos){
            switch (objetivos.getNome()){
                case "dormirCedo":
                    nome.setText("Dormir 8 horas");
                    imgObjetivo.setBackgroundResource(R.drawable.dormir_cedo);
                    break;
                case "estudarMais":
                    nome.setText("Estudar mais");
                    imgObjetivo.setBackgroundResource(R.drawable.estudar_mais);
                    break;
                case "fazerExercicio":
                    nome.setText("Fazer exercícios");
                    imgObjetivo.setBackgroundResource(R.drawable.fazer_exercicio);
                    break;
                case "realizarFaxina":
                    nome.setText("Fazer faxina");
                    imgObjetivo.setBackgroundResource(R.drawable.fazer_faxina);
                    break;
                case "comerSaudavel":
                    nome.setText("Comer saudável");
                    imgObjetivo.setBackgroundResource(R.drawable.comer_saudavel);
                    break;
            }
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Users").child(userId).child("Objetivos")
                    .child(objetivos.nome).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean ativo = Boolean.parseBoolean(snapshot.child("checked").getValue().toString());
                    checkObjetivo.setChecked(ativo);
                    objetivos.setChecked(ativo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    objetivos.setChecked(!objetivos.isChecked());
                    checkObjetivo.setChecked(objetivos.isChecked());
                }
            });
        }
    }

    public ArrayList<Objetivos> getObjetivos(){
        return objetivos;
    }

    public ArrayList<Objetivos> getSelected(){
        ArrayList<Objetivos> selected = new ArrayList<>();
        for(int i = 0; i < objetivos.size(); i++){
            if(objetivos.get(i).isChecked()) {
                selected.add(objetivos.get(i));
            }
        }
        return selected;
    }

    public ArrayList<Objetivos> getUnselected(){
        ArrayList<Objetivos> unselected = new ArrayList<>();
        for(int i = 0; i < objetivos.size(); i++){
            if(!objetivos.get(i).isChecked()) {
                unselected.add(objetivos.get(i));
            }
        }
        return unselected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view;
        if(viewType == TIPO_REGISTROS) {
            view = inflater.inflate(R.layout.layout_rv_home, parent, false);
            return new RegistroViewHolder(view);
        } else {
            int layout = R.layout.layout_rv_objetivos;
            if(tipo == 3){
                layout = R.layout.layout_rv_tela_objetivos;
            }
            view = inflater.inflate(layout, parent, false);
            return new ObjetivoViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TIPO_OBJETIVOS){
            if(tipo == 2) {
                ((ObjetivoViewHolder) holder).bind(objetivos.get(position));
            } else if (tipo == 3){
                ((ObjetivoViewHolder) holder).bindAlt(objetivos.get(position));
            }
        }
        else {
            ((RegistroViewHolder) holder).bind(registros.get(position));
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(tipo == 1){
            return TIPO_REGISTROS;
        } else {
            return TIPO_OBJETIVOS;
        }
    }
    @Override
    public int getItemCount() {
        if(tipo == 1) {
            return registros.size();
        } else {
            return objetivos.size();
        }
    }
}