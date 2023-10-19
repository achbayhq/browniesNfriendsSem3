package com.abayhq.browniesnfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.settergetter.setgetMenu;

import java.util.ArrayList;

public class adapterMenuDas extends RecyclerView.Adapter<adapterMenuDas.ViewHolder> {

    private ArrayList<setgetMenu> menunya;
    private Context context;

    public adapterMenuDas(ArrayList<setgetMenu> menu, Context context) {
        this.menunya = menu;
        this.context = context;
    }
    @NonNull
    @Override
    public adapterMenuDas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_home_menu, parent, false);
        return new adapterMenuDas.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterMenuDas.ViewHolder holder, int position) {
        setgetMenu menu = menunya.get(position);

        holder.Img.setImageResource(menu.getImg());
        holder.namaKue.setText(menu.getNama());
        holder.desKue.setText(menu.getDeskripsi());
        holder.hargaKue.setText(String.valueOf(menu.getHarga()));

        holder.btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (menunya != null) ? menunya.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Img;
        TextView namaKue, desKue, hargaKue;
        Button btnBeli;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img = itemView.findViewById(R.id.imgMenu);
            namaKue = itemView.findViewById(R.id.namaMenu);
            desKue = itemView.findViewById(R.id.desMenu);
            hargaKue = itemView.findViewById(R.id.hargaMenu);
            btnBeli = itemView.findViewById(R.id.btnBeli);
        }
    }
}
