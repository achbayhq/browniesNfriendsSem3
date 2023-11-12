package com.abayhq.browniesnfriends.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterMenuUtama;
import com.abayhq.browniesnfriends.home.DasboardActivity;
import com.abayhq.browniesnfriends.login.MainActivity;
import com.abayhq.browniesnfriends.respons.dataBarangRespons;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataBarang;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.settergetter.setgetMenu;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link menuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menuFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public menuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment menuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static menuFragment newInstance(String param1, String param2) {
        menuFragment fragment = new menuFragment();
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
    private adapterMenuUtama adapter;
    private ArrayList<setgetMenu> menuArrayList;
    private ArrayList<setgetMenu> transaksiList = new ArrayList<>();
    private int totalHarga = 0;
    private int item = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        getDataBarang();
        recyclerView = root.findViewById(R.id.menuUtama);
        adapter = new adapterMenuUtama(menuArrayList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setBeliOnClickListener(new adapterMenuUtama.beliOnClickListener() {
            @Override
            public void beliOnClick(int position) {
                setgetMenu selectedItem = menuArrayList.get(position);
                transaksiList.add(selectedItem);
                int harga = selectedItem.getQty() * Integer.parseInt(selectedItem.getHarga());
                totalHarga += harga;
                item += 1;

                DasboardActivity dass = (DasboardActivity) getActivity();
                if (dass != null) {
                    if (item >= 1) {
                        dass.btnTransaksi.setVisibility(View.VISIBLE);
                        dass.cardTotalTr.setText(String.valueOf(harga));
                        dass.cardItemTr.setText(String.valueOf(item));
                    }
                }
            }
        });


        return root;
    }

    void getDataBarang(){
        menuArrayList = new ArrayList<>();
        Gson gson = new Gson();

        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.getBarang(new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                dataBarangRespons dataRespon = gson.fromJson(response.toString(), dataBarangRespons.class);
                if (dataRespon.getCode() == 200) {
                    List<dataBarang> barangList = dataRespon.getBarang_list();

                    for (dataBarang dataBarang : barangList) {
                        String image = dataBarang.getImage_barang();
                        String nama = dataBarang.getNama_barang();
                        String harga = dataBarang.getHarga_jual();
                        String deskripsi = dataBarang.getDeskripsi();
                        String URLimage = "http://" + GlobalVariable.IP + "/APIproject/image/" + image;

                        menuArrayList.add(new setgetMenu(URLimage, nama, deskripsi, harga));
                    }
                    adapter.notifyDataSetChanged();
                }else if (dataRespon.getCode() == 404) {
                    Toast.makeText(getContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }

}