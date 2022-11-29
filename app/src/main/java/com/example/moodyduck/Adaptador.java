package com.example.moodyduck;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
        TextView horario, humor;
        ImageView imgHumor;
        public RegistroViewHolder(@NonNull View itemView) {
            super(itemView);
            humor = itemView.findViewById(R.id.cardHumor);
            horario = itemView.findViewById(R.id.cardHorario);
            imgHumor = itemView.findViewById(R.id.imgHumor);
        }
        public void bind(Registros registros){
            humor.setText(registros.getHumor());
            horario.setText(registros.getData());
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
        TextView sequencia, nome;
        ImageView imgObjetivo;
        CheckBox checkObjetivo;
        public ObjetivoViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeObjetivo);
            sequencia = itemView.findViewById(R.id.textoSequencia);
            imgObjetivo = itemView.findViewById(R.id.imgObjetivo);
            checkObjetivo = itemView.findViewById(R.id.checkObjetivo);
        }
        public void bind(Objetivos objetivos){
            switch (objetivos.getNome()){
                case "dormirCedo":
                    nome.setText("Dormir cedo");
                    break;
                case "estudarMais":
                    nome.setText("Estudar mais");
                    break;
                case "fazerExercicios":
                    nome.setText("Fazer exercícios");
                    break;
            }
            sequencia.setText("Sequência atual: "+objetivos.getSequencia());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    objetivos.setChecked(!objetivos.isChecked());
                    checkObjetivo.setChecked(objetivos.isChecked());
                }
            });
        }
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view;
        if(viewType == TIPO_REGISTROS) {
            view = inflater.inflate(R.layout.layout_rv_home, parent, false);
            return new RegistroViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.layout_rv_objetivos, parent, false);
            return new ObjetivoViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TIPO_OBJETIVOS){
            ((ObjetivoViewHolder) holder).bind(objetivos.get(position));
        }
        else {
            ((RegistroViewHolder) holder).bind(registros.get(position));
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(tipo == 2){
            return TIPO_OBJETIVOS;
        } else {
            return TIPO_REGISTROS;
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

    /*public ArrayList<Objetivos> getSelected() {
        ArrayList<Objetivos> selected = new ArrayList<>();
        for(int i = 0; i<objetivos.size(); i++) {
            if (objetivos.get(i).isChecked()) {
                selected.add(objetivos.get(i));
            }
        }
        return selected;
    }*/