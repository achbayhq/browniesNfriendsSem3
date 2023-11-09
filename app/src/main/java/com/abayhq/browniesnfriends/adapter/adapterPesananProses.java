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
import com.abayhq.browniesnfriends.settergetter.setgetPesanan;
import com.abayhq.browniesnfriends.settergetter.setgetRincianBeli;

import java.util.ArrayList;

public class adapterPesananProses extends RecyclerView.Adapter<adapterPesananProses.ViewHolder> {

    private ArrayList<setgetPesanan> prosesPesan;
    private Context context;

    public adapterPesananProses(ArrayList<setgetPesanan> prosesPesan, Context context) {
        this.prosesPesan = prosesPesan;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterPesananProses.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_pesanan_dalamproses, parent, false);
        return new adapterPesananProses.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterPesananProses.ViewHolder holder, int position) {
        setgetPesanan pesan = prosesPesan.get(position);

        holder.Img.setImageResource(pesan.getImgStatus());
        holder.txtTgl.setText(pesan.getTgl());
        holder.txtStatus.setText(pesan.getStatus());
        holder.txtHarga.setText(pesan.getHarga());
    }

    @Override
    public int getItemCount() {
        return (prosesPesan != null) ? prosesPesan.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Img;
        TextView txtTgl, txtHarga, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img = itemView.findViewById(R.id.imgStatusProses);
            txtTgl = itemView.findViewById(R.id.tglProses);
            txtStatus = itemView.findViewById(R.id.statusProses);
            txtHarga = itemView.findViewById(R.id.txtTotalHargaProses);
        }
    }
}
