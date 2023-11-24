package com.abayhq.browniesnfriends.pesanan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterMenuUtama;
import com.abayhq.browniesnfriends.adapter.adapterPesananTerjadwal;
import com.abayhq.browniesnfriends.nota.notaActivity;
import com.abayhq.browniesnfriends.respons.dataBarangRespons;
import com.abayhq.browniesnfriends.respons.pesananAdminRespons;
import com.abayhq.browniesnfriends.respons.pesananRespons;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataBarang;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.settergetter.setgetMenu;
import com.abayhq.browniesnfriends.settergetter.setgetPesanan;
import com.abayhq.browniesnfriends.settergetter.setgetPesananAdmin;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pesananTerjadwalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pesananTerjadwalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public pesananTerjadwalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pesananTerjadwalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static pesananTerjadwalFragment newInstance(String param1, String param2) {
        pesananTerjadwalFragment fragment = new pesananTerjadwalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView recyclerView;
    private adapterPesananTerjadwal adapter;
    private ArrayList<setgetPesanan> pesananArrayList;
    String telepon, akses;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pesanan_terjadwal, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        telepon = sharedPreferences.getString("telepon", "");

        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.loginUser(telepon, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                if (userRespon.getCode() == 200) {
                    dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                    akses = loggedUser.getAkses();

                    if (akses.equals("customer")) {
                        getPesanTerjadwal();
                        recyclerView = root.findViewById(R.id.recyclerViewTerjadwal);
                        adapter = new adapterPesananTerjadwal(pesananArrayList,getContext());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                        adapter.setLihatNotaOnClickListener(new adapterPesananTerjadwal.lihatNotaOnClickListener() {
                            @Override
                            public void lihatNotaOnClick(int position) {
                                setgetPesanan selectedNota = pesananArrayList.get(position);
                                String nota = selectedNota.getNota();
                                Intent intent = new Intent(getContext(), notaActivity.class);
                                intent.putExtra("notaTerjadwal", nota);
                                startActivity(intent);
                            }
                        });

                    }else if(akses.equals("karyawan")){
                        getPesanTerjadwalAdmin();
                        recyclerView = root.findViewById(R.id.recyclerViewTerjadwal);
                        adapter = new adapterPesananTerjadwal(pesananArrayList,getContext());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                        adapter.setLihatNotaOnClickListener(new adapterPesananTerjadwal.lihatNotaOnClickListener() {
                            @Override
                            public void lihatNotaOnClick(int position) {
                                setgetPesanan selectedNota = pesananArrayList.get(position);
                                String nota = selectedNota.getNota();
                                Intent intent = new Intent(getContext(), notaActivity.class);
                                intent.putExtra("notaTerjadwal", nota);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
            @Override
            public void onError(String error) {

            }
        });

        return root;
    }

    void getPesanTerjadwal(){
        pesananArrayList = new ArrayList<>();
        Gson gson = new Gson();

        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.pesananTerjadwal(telepon, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                pesananRespons pesananRespons = gson.fromJson(response.toString(), pesananRespons.class);
                if (pesananRespons.getCode() == 200) {
                    List<setgetPesanan> pesananList = pesananRespons.getTransaksi_list();

                    for (setgetPesanan setgetPesanan : pesananList) {
                        String tgl = setgetPesanan.getTanggal_pengambilan();
                        String status = setgetPesanan.getStatus();
                        String harga = setgetPesanan.getGrand_total();
                        String nota = setgetPesanan.getNota();
                        int imgStatus = 0;
                        String formattedDate = "";

                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                        String formattedGrandTotal = numberFormat.format(Integer.parseInt(harga));

                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = inputFormat.parse(tgl);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id"));
                            formattedDate = outputFormat.format(date);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }

                        if (status.equals("pesanan masuk")){
                            imgStatus = R.drawable.logo_menunggu;
                            status = "Menunggu Konfirmasi";
                        }else if (status.equals("pesanan diproses")){
                            imgStatus = R.drawable.logo_terdaftar;
                            status = "Pesanan Terdaftar";
                        }else if (status.equals("permintaan batal")){
                            imgStatus = R.drawable.logo_cancel;
                            status = "Permintaan Batal";
                        }

                        pesananArrayList.add(new setgetPesanan(formattedDate, status, imgStatus, formattedGrandTotal, nota));
                    }
                    adapter.notifyDataSetChanged();
                }else if (pesananRespons.getCode() == 400) {
                    Toast.makeText(getContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }else if (pesananRespons.getCode() == 404) {
                    Toast.makeText(getContext(), "pesanan User Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }

    void getPesanTerjadwalAdmin(){
        pesananArrayList = new ArrayList<>();
        Gson gson = new Gson();

        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.pesananTerjadwalAdmin(new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                pesananAdminRespons pesananRespons = gson.fromJson(response.toString(), pesananAdminRespons.class);
                if (pesananRespons.getCode() == 200) {
                    List<setgetPesananAdmin> pesananList = pesananRespons.getTransaksi_list();

                    for (setgetPesananAdmin setgetPesanan : pesananList) {
                        String nota = setgetPesanan.getNo_nota();
                        String nama = setgetPesanan.getNama();
                        String harga = setgetPesanan.getGrand_total();

                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                        String formattedGrandTotal = numberFormat.format(Integer.parseInt(harga));

                        pesananArrayList.add(new setgetPesanan(nama, nota, R.drawable.logo_nota, formattedGrandTotal, nota));
                    }
                    adapter.notifyDataSetChanged();
                }else if (pesananRespons.getCode() == 400) {
                    Toast.makeText(getContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }
}