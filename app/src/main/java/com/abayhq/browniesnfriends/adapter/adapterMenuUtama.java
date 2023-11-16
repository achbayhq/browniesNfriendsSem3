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
import com.abayhq.browniesnfriends.settergetter.setgetMenu;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class adapterMenuUtama extends RecyclerView.Adapter<adapterMenuUtama.ViewHolder> {

    private ArrayList<setgetMenu> menunya;
    private Context context;
    private beliOnClickListener beliOnClickListener;
    private kurangOnClickListener kurangOnClickListener;
    public void setKurangOnClickListener(kurangOnClickListener listener) {
        this.kurangOnClickListener = listener;
    }
    public void setBeliOnClickListener(beliOnClickListener listener) {
        this.beliOnClickListener = listener;
    }
    public interface beliOnClickListener {
        void beliOnClick(int position);
    }
    public interface kurangOnClickListener {
        void kurangOnClick(int position);
    }

    public adapterMenuUtama(ArrayList<setgetMenu> menu, Context context) { //CATATAN(sepertinya udh Solve) : skrng masih pake EditText buat qty dan kalo input manual pake keyboard maka nilai dalam editText ngga bisa kedeteksi soalnya skrng pake settergetter jadi kalo input manual ngga bisa ke set, bisanya cuma pake button + soalnya langsung ke set qty nya ke setter getter
        this.menunya = menu;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterMenuUtama.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_menu, parent, false);
        return new adapterMenuUtama.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterMenuUtama.ViewHolder holder, int position) {
        setgetMenu menu = menunya.get(position);

        Glide.with(context).load(menu.getImg()).into(holder.Img);   //ini buat nampilin gambar dari database
        holder.namaKue.setText(menu.getNama());
        holder.desKue.setText(menu.getDeskripsi());
        holder.hargaKue.setText(String.valueOf(menu.getHarga()));
        holder.qty.setText(String.valueOf(menu.getQty()));

        if (menu.getVisibility()) {
            holder.btnBeli.setVisibility(View.GONE);
            holder.btnTambah.setVisibility(View.VISIBLE);
            holder.btnKurang.setVisibility(View.VISIBLE);
            holder.qty.setVisibility(View.VISIBLE);
        } else {
            holder.btnBeli.setVisibility(View.VISIBLE);
            holder.btnTambah.setVisibility(View.GONE);
            holder.btnKurang.setVisibility(View.GONE);
            holder.qty.setVisibility(View.GONE);
        }

        holder.btnTambah.setOnClickListener(view -> {
            String txtQty = String.valueOf(holder.qty.getText());
            int quantity = Integer.parseInt(txtQty);
            menu.setQty(quantity + 1);
            holder.qty.setText(String.valueOf(menu.getQty()));

        });

        holder.btnKurang.setOnClickListener(view -> {
            String txtQty = String.valueOf(holder.qty.getText());
            int quantity = Integer.parseInt(txtQty);
            if (quantity > 0) {
                menu.setQty(quantity - 1);
                holder.qty.setText(String.valueOf(menu.getQty()));

            } else if (quantity == 0) {
                holder.btnBeli.setVisibility(View.VISIBLE);
                holder.btnTambah.setVisibility(View.GONE);
                holder.btnKurang.setVisibility(View.GONE);
                holder.qty.setVisibility(View.GONE);
                menu.setVisibility(false);

                if (kurangOnClickListener != null) {
                    kurangOnClickListener.kurangOnClick(position);
                }
            }
        });

        holder.btnBeli.setOnClickListener(view -> {
            menu.setQty(1);
            menu.setVisibility(true);
            notifyDataSetChanged(); // Refresh the view to update the button visibility
            if (beliOnClickListener != null) {
                beliOnClickListener.beliOnClick(position);
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
        Button btnBeli, btnTambah, btnKurang, btnPenanda;
        EditText qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img = itemView.findViewById(R.id.imgMenuM);
            namaKue = itemView.findViewById(R.id.namaMenuM);
            desKue = itemView.findViewById(R.id.desMenuM);
            hargaKue = itemView.findViewById(R.id.hargaMenuM);
            btnBeli = itemView.findViewById(R.id.btnBeliM);
            btnTambah = itemView.findViewById(R.id.btnTambah);
            btnKurang = itemView.findViewById(R.id.btnKurang);
            btnPenanda = itemView.findViewById(R.id.btnPenanda);
            qty = itemView.findViewById(R.id.txtQty);
        }
    }
}
