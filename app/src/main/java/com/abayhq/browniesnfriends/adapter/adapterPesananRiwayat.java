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
import com.abayhq.browniesnfriends.settergetter.setgetPesanan;

import java.util.ArrayList;

public class adapterPesananRiwayat extends RecyclerView.Adapter<adapterPesananRiwayat.ViewHolder> {

    private ArrayList<setgetPesanan> riwayatPesan;
    private Context context;
    private pesanLagiOnClickListener pesanLagiOnClickListener;

    public void setPesanLagiOnClickListener(pesanLagiOnClickListener listener) {
        this.pesanLagiOnClickListener = listener;
    }
    public interface pesanLagiOnClickListener {
        void pesanLagiOnClick(int position);
    }

    public adapterPesananRiwayat(ArrayList<setgetPesanan> riwayatPesan, Context context) {
        this.riwayatPesan = riwayatPesan;
        this.context = context;
    }
    @NonNull
    @Override
    public adapterPesananRiwayat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_pesanan_riwayat, parent, false);
        return new adapterPesananRiwayat.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterPesananRiwayat.ViewHolder holder, int position) {
        setgetPesanan pesan = riwayatPesan.get(position);

        holder.Img.setImageResource(pesan.getImgStatus());
        holder.txtTgl.setText(pesan.getTanggal_pengambilan());
        holder.txtStatus.setText(pesan.getStatus());
        holder.txtHarga.setText(pesan.getGrand_total());

        if (pesan.getStatus().equals("pesanan dibatalkan")){
            holder.btnPesanLagi.setEnabled(false);
        }

        holder.btnPesanLagi.setOnClickListener(view ->{
            if (pesanLagiOnClickListener != null) {
                pesanLagiOnClickListener.pesanLagiOnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (riwayatPesan != null) ? riwayatPesan.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Img;
        TextView txtTgl, txtHarga, txtStatus;
        Button btnPesanLagi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Img = itemView.findViewById(R.id.imgStatusRiwayat);
            txtTgl = itemView.findViewById(R.id.tglRiwayat);
            txtStatus = itemView.findViewById(R.id.statusRiwayat);
            txtHarga = itemView.findViewById(R.id.txtTotalHargaRiwayat);
            btnPesanLagi = itemView.findViewById(R.id.btnPesanLagi);
        }
    }
}
