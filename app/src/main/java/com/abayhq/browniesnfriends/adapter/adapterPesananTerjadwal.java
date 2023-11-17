package com.abayhq.browniesnfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.settergetter.setgetPesanan;

import java.util.ArrayList;

public class adapterPesananTerjadwal extends RecyclerView.Adapter<adapterPesananTerjadwal.ViewHolder> {

    private ArrayList<setgetPesanan> terjadwalPesan;
    private Context context;

    public adapterPesananTerjadwal(ArrayList<setgetPesanan> terjadwalPesan, Context context) {
        this.terjadwalPesan = terjadwalPesan;
        this.context = context;
    }
    @NonNull
    @Override
    public adapterPesananTerjadwal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_pesanan_terjadwal, parent, false);
        return new adapterPesananTerjadwal.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterPesananTerjadwal.ViewHolder holder, int position) {
        setgetPesanan pesan = terjadwalPesan.get(position);

        holder.Img.setImageResource(pesan.getImgStatus());
        holder.txtTgl.setText(pesan.getTanggal_pengambilan());
        holder.txtStatus.setText(pesan.getStatus());
        holder.txtHarga.setText(pesan.getGrand_total());
    }

    @Override
    public int getItemCount() {
        return (terjadwalPesan != null) ? terjadwalPesan.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Img;
        TextView txtTgl, txtHarga, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img = itemView.findViewById(R.id.imgStatusTerjadwal);
            txtTgl = itemView.findViewById(R.id.tglTerjadwal);
            txtStatus = itemView.findViewById(R.id.statusTerjadwal);
            txtHarga = itemView.findViewById(R.id.txtTotalHargaTerjadwal);
        }
    }
}
