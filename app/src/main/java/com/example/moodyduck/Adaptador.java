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
    ArrayList<Resultados> resultados;

    public Adaptador(Context c, ArrayList<Resultados> resultados) {
        this.c = c;
        this.resultados = resultados;
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
        Resultados r = resultados.get(position);
        holder.t.setText(r.data);
        holder.a.setText(r.area);
        holder.p.setText(r.profissao);
    }

    @Override
    public int getItemCount() {
        return resultados.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView t, p, a;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.dataCard);
            p = itemView.findViewById(R.id.profCard);
            a = itemView.findViewById(R.id.areaCard);
        }
    }
}
