package com.abayhq.browniesnfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.settergetter.listBarangNota;

import java.util.ArrayList;

public class adapterNota extends RecyclerView.Adapter<adapterNota.ViewHolder>{
    private ArrayList <listBarangNota> listBarang;
    private Context context;

    public adapterNota(ArrayList<listBarangNota> listBarang, Context context) {
        this.listBarang = listBarang;
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
        listBarangNota nota = listBarang.get(position);

        holder.txtQty.setText(nota.getQty());
        holder.txtBarang.setText(nota.getNama_barang());
        holder.txtHarga.setText(nota.getHarga_jual());
        holder.txtTotal.setText(nota.getTotal());

    }

    @Override
    public int getItemCount() {
        return (listBarang != null) ? listBarang.size():0;
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
