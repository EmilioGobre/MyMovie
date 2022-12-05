package com.example.mymovie.controladores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymovie.R;
import com.example.mymovie.modelos.peliculas;

import java.util.List;

public class adaptador_peliculas extends RecyclerView.Adapter<adaptador_peliculas.PelisViewHolder> {

    private Context mCtx;
    private List<peliculas> peliculasList;

    public adaptador_peliculas(Context mCtx, List<peliculas> peliculasList) {
        this.mCtx = mCtx;
        this.peliculasList = peliculasList;
    }

    @Override
    public PelisViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_layout, viewGroup, false);

        return new PelisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PelisViewHolder holder, int position) {
        peliculas peli = peliculasList.get(position);


        Glide.with(mCtx)
                .load(peli.getimagen())
                .into(holder.imageView);

        holder.tvname.setText(peli.getname());
        holder.tvhorario.setText(peli.gethorario());
        holder.tvtickets.setText(String.valueOf(peli.gettickets_disponibles()));

    }


    @Override
    public int getItemCount() {
        return peliculasList.size();
    }


    public static class PelisViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvhorario,tvtickets;
        ImageView imageView;

        public PelisViewHolder(View itemView) {
            super(itemView);


            tvname = (TextView) itemView.findViewById(R.id.tvNombre_card);
            tvhorario = (TextView) itemView.findViewById(R.id.tvHorario_card);
            tvtickets = (TextView) itemView.findViewById(R.id.tvTickets_card);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_card);
        }
    }

}
