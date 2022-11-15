package com.example.moodyduck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyViewHolder> {
    Context c;
    ArrayList<Registros> registros;

    public Adaptador(Context c, ArrayList<Registros> registros) {
        this.c = c;
        this.registros = registros;
    }

    @NonNull
    @Override
    public Adaptador.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.layout_rv_home, parent, false);
        return new Adaptador.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.MyViewHolder holder, int position) {
        Registros r = registros.get(position);
        holder.humor.setText(r.nome);
        holder.horario.setText(r.data);
        if(r.img == 3) {
            holder.imgHumor.setBackgroundResource(R.drawable.placeholder_feliz);
        } else if(r.img == 2) {
            holder.imgHumor.setBackgroundResource(R.drawable.placeholder_neutro);
        } else if(r.img == 1){
            holder.imgHumor.setBackgroundResource(R.drawable.placeholder_triste);
        }
    }

    @Override
    public int getItemCount() {
        return registros.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView horario, humor;
        ImageView imgHumor;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            humor = itemView.findViewById(R.id.cardHumor);
            horario = itemView.findViewById(R.id.cardHorario);
            imgHumor = itemView.findViewById(R.id.imgHumor);
        }
    }
}
