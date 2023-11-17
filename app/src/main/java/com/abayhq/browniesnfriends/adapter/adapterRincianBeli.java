package com.abayhq.browniesnfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.settergetter.setgetRincianBeli;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class adapterRincianBeli extends RecyclerView.Adapter<adapterRincianBeli.ViewHolder> {

    private ArrayList<setgetRincianBeli> dibeli;
    private Context context;
    private adapterRincianBeli.kurangOnClickListener kurangOnClickListener;
    private adapterRincianBeli.tambahOnClickListener tambahOnClickListener;

    public void setKurangOnClickListener(kurangOnClickListener listener) {
        this.kurangOnClickListener = listener;
    }
    public void setTambahOnClickListener(tambahOnClickListener listener) {
        this.tambahOnClickListener = listener;
    }
    public interface kurangOnClickListener {
        void kurangOnClick(int position);
    }
    public interface tambahOnClickListener {
        void tambahOnClick(int position);
    }
    public adapterRincianBeli(ArrayList<setgetRincianBeli> beli, Context context) {
        this.dibeli = beli;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterRincianBeli.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_rincian_beli, parent, false);
        return new adapterRincianBeli.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterRincianBeli.ViewHolder holder, int position) {

        setgetRincianBeli menu = dibeli.get(position);

        Glide.with(context).load(menu.getImg()).into(holder.Img);
        holder.namaKue.setText(menu.getNama());
        holder.hargaKue.setText(String.valueOf(menu.getHarga()));
        holder.qty.setText(String.valueOf(menu.getQty()));

        holder.btnTambah.setOnClickListener(view -> {
            String txtQty = String.valueOf(holder.qty.getText());
            int quantity = Integer.parseInt(txtQty);
            menu.setQty(quantity + 1);
            holder.qty.setText(String.valueOf(menu.getQty()));
            if (tambahOnClickListener != null) {
                tambahOnClickListener.tambahOnClick(position);
            }
        });

        holder.btnKurang.setOnClickListener(view -> {
            String txtQty = String.valueOf(holder.qty.getText());
            int quantity = Integer.parseInt(txtQty);
            if (quantity > 0) {
                menu.setQty(quantity - 1);
                holder.qty.setText(String.valueOf(menu.getQty()));

                if (kurangOnClickListener != null) {
                    kurangOnClickListener.kurangOnClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dibeli != null) ? dibeli.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Img;
        TextView namaKue, hargaKue;
        Button btnTambah, btnKurang;
        EditText qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img = itemView.findViewById(R.id.imgMenu);
            namaKue = itemView.findViewById(R.id.namaMenu);
            hargaKue = itemView.findViewById(R.id.txtHarga);
            btnTambah = itemView.findViewById(R.id.btnTambah);
            btnKurang = itemView.findViewById(R.id.btnKurang);
            qty = itemView.findViewById(R.id.txtQty);
        }
    }
}
