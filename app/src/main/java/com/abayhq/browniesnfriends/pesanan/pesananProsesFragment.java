package com.abayhq.browniesnfriends.pesanan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterPesananProses;
import com.abayhq.browniesnfriends.respons.pesananRespons;
import com.abayhq.browniesnfriends.settergetter.setgetPesanan;
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
 * Use the {@link pesananProsesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pesananProsesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public pesananProsesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pesananProsesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static pesananProsesFragment newInstance(String param1, String param2) {
        pesananProsesFragment fragment = new pesananProsesFragment();
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
    private adapterPesananProses adapter;
    private ArrayList<setgetPesanan> pesananArrayList;
    String telepon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pesanan_proses, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        telepon = sharedPreferences.getString("telepon", "");

        getPesanProses();
        recyclerView = root.findViewById(R.id.recyclerViewProses);
        adapter = new adapterPesananProses(pesananArrayList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    void getPesanProses(){
        pesananArrayList = new ArrayList<>();
        //pesananArrayList.add(new setgetPesanan("30 November 2023", "Pesanan Sedang Diproses", R.drawable.logo_proses, "250.000"));
        Gson gson = new Gson();

        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.pesananProses(telepon, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                pesananRespons pesananRespons = gson.fromJson(response.toString(), pesananRespons.class);
                if (pesananRespons.getCode() == 200) {
                    List<setgetPesanan> pesananList = pesananRespons.getTransaksi_list();

                    for (setgetPesanan setgetPesanan : pesananList) {
                        String tgl = setgetPesanan.getTanggal_pengambilan();
                        String status = setgetPesanan.getStatus();
                        String harga = setgetPesanan.getGrand_total();
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
                        status = "Pesanan Sedang Diproses";
                        pesananArrayList.add(new setgetPesanan(formattedDate, status, R.drawable.logo_proses, formattedGrandTotal));
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
}