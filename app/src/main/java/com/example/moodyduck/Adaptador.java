package com.example.moodyduck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyViewHolder> {
    Context c;
    ArrayList<Objetivos> objetivos;

    public Adaptador(Context c, ArrayList<Objetivos> objetivos) {
        this.c = c;
        this.objetivos = objetivos;
    }

    @NonNull
    @Override
    public Adaptador.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.layout_rv_objetivos, parent, false);
        return new Adaptador.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.MyViewHolder holder, int position) {
        Objetivos o = objetivos.get(position);
        holder.t.setText(o.nome);
        holder.c.setText(o.progresso);
    }

    @Override
    public int getItemCount() {
        return objetivos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView t, c;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.nomeTxt);
            c = itemView.findViewById(R.id.conclusasTxt);
        }
    }
}
