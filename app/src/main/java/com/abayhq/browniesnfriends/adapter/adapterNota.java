package com.abayhq.browniesnfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.settergetter.setgetNota;
import com.abayhq.browniesnfriends.settergetter.setgetPesanan;

import java.util.ArrayList;

public class adapterNota extends RecyclerView.Adapter<adapterNota.ViewHolder>{
    private ArrayList <setgetNota> items;
    private Context context;

    public adapterNota(ArrayList<setgetNota> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterNota.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_nota, parent, false);
        return new adapterNota.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterNota.ViewHolder holder, int position) {
        setgetNota nota = items.get(position);

        holder.txtQty.setText(nota.getQty());
        holder.txtBarang.setText(nota.getNama());
        holder.txtHarga.setText(nota.getHarga());
        holder.txtTotal.setText(nota.getTotal());

    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size():0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtQty, txtBarang, txtHarga, txtTotal;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txtQty = itemView.findViewById(R.id.txtQtyNota);
            txtBarang = itemView.findViewById(R.id.txtBarang);
            txtHarga = itemView.findViewById(R.id.txtHargaNota);
            txtTotal = itemView.findViewById(R.id.txtTotalNota);
        }
    }
}
