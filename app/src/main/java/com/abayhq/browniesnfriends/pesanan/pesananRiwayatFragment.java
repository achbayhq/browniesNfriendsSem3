package com.abayhq.browniesnfriends.pesanan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterPesananRiwayat;
import com.abayhq.browniesnfriends.settergetter.setgetPesanan;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pesananRiwayatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pesananRiwayatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public pesananRiwayatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pesananRiwayatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static pesananRiwayatFragment newInstance(String param1, String param2) {
        pesananRiwayatFragment fragment = new pesananRiwayatFragment();
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
    private adapterPesananRiwayat adapter;
    private ArrayList<setgetPesanan> pesananArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pesanan_riwayat, container, false);

        addMenu();
        recyclerView = root.findViewById(R.id.recyclerViewRiwayat);
        adapter = new adapterPesananRiwayat(pesananArrayList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    void addMenu(){
        pesananArrayList = new ArrayList<>();
        pesananArrayList.add(new setgetPesanan("1 Juli 2023", "Pesanan Sudah Diambil", R.drawable.logo_diambil, "210.000"));
        pesananArrayList.add(new setgetPesanan("5 Juli 2023", "Pesanan Dibatalkan", R.drawable.logo_dibatalkan, "140.000"));
        pesananArrayList.add(new setgetPesanan("10 Juli 2023", "Pesanan Sudah Diambil", R.drawable.logo_diambil, "200.000"));
        pesananArrayList.add(new setgetPesanan("22 Juli 2023", "Pesanan Sudah Diambil", R.drawable.logo_diambil, "200.000"));

    }
}