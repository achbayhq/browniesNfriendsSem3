package com.abayhq.browniesnfriends.menu;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterMenuUtama;
import com.abayhq.browniesnfriends.databasesqlite.dbTransaksiHelper;
import com.abayhq.browniesnfriends.home.DasboardActivity;
import com.abayhq.browniesnfriends.login.MainActivity;
import com.abayhq.browniesnfriends.login.RegisterActivity;
import com.abayhq.browniesnfriends.respons.dataBarangRespons;
import com.abayhq.browniesnfriends.respons.dataPaketRespons;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataBarang;
import com.abayhq.browniesnfriends.settergetter.dataPaket;
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
    public ArrayList<setgetMenu> transaksiList = new ArrayList<>();
    public ArrayList<setgetMenu> getTransaksiList() {
        return transaksiList;
    }
    private int item;
    Button filterAll, filterBestSeller, filterManis, filterGurih, filterPaket;
    EditText txtCariMenu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        filterAll = root.findViewById(R.id.btnAll);
        filterBestSeller = root.findViewById(R.id.btnBestSeller);
        filterGurih = root.findViewById(R.id.btnKueAsin);
        filterManis = root.findViewById(R.id.btnKueManis);
        filterPaket = root.findViewById(R.id.btnPaket);
        txtCariMenu = root.findViewById(R.id.txtCariMenu);

        Bundle bundle = getArguments();
        ColorStateList selectedColor = ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.selectedFilter));
        ColorStateList defaultColor = ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.defaultFilter));
        menuArrayList = new ArrayList<>();

        txtCariMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (bundle != null) {
            String filter = bundle.getString("filter");
            if (filter != null) {
                if (filter.equals("best seller")) {

                    filterBestSeller();
                    filterAll.setBackgroundTintList(defaultColor);
                    filterBestSeller.setBackgroundTintList(selectedColor);
                    filterGurih.setBackgroundTintList(defaultColor);
                    filterManis.setBackgroundTintList(defaultColor);
                    filterPaket.setBackgroundTintList(defaultColor);
                }else if (filter.equals("manis")){

                    filterBarang("manis");
                    filterAll.setBackgroundTintList(defaultColor);
                    filterBestSeller.setBackgroundTintList(defaultColor);
                    filterGurih.setBackgroundTintList(defaultColor);
                    filterManis.setBackgroundTintList(selectedColor);
                    filterPaket.setBackgroundTintList(defaultColor);
                }else if (filter.equals("asin")){

                    filterBarang("asin");
                    filterAll.setBackgroundTintList(defaultColor);
                    filterBestSeller.setBackgroundTintList(defaultColor);
                    filterGurih.setBackgroundTintList(selectedColor);
                    filterManis.setBackgroundTintList(defaultColor);
                    filterPaket.setBackgroundTintList(defaultColor);
                }
            }
        }else{
            getDataBarang();
            getDataPaket();
        }

        recyclerView = root.findViewById(R.id.menuUtama);
        adapter = new adapterMenuUtama(menuArrayList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DasboardActivity dass = (DasboardActivity) getActivity();

        filterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuArrayList = new ArrayList<>();
                getDataBarang();
                getDataPaket();
                recyclerView = root.findViewById(R.id.menuUtama);
                adapter = new adapterMenuUtama(menuArrayList,getContext());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                filterAll.setBackgroundTintList(selectedColor);
                filterBestSeller.setBackgroundTintList(defaultColor);
                filterGurih.setBackgroundTintList(defaultColor);
                filterManis.setBackgroundTintList(defaultColor);
                filterPaket.setBackgroundTintList(defaultColor);
            }
        });

        filterBestSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuArrayList = new ArrayList<>();
                filterBestSeller();
                recyclerView = root.findViewById(R.id.menuUtama);
                adapter = new adapterMenuUtama(menuArrayList,getContext());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                filterAll.setBackgroundTintList(defaultColor);
                filterBestSeller.setBackgroundTintList(selectedColor);
                filterGurih.setBackgroundTintList(defaultColor);
                filterManis.setBackgroundTintList(defaultColor);
                filterPaket.setBackgroundTintList(defaultColor);
            }
        });

        filterManis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuArrayList = new ArrayList<>();
                filterBarang("manis");
                recyclerView = root.findViewById(R.id.menuUtama);
                adapter = new adapterMenuUtama(menuArrayList,getContext());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                filterAll.setBackgroundTintList(defaultColor);
                filterBestSeller.setBackgroundTintList(defaultColor);
                filterGurih.setBackgroundTintList(defaultColor);
                filterManis.setBackgroundTintList(selectedColor);
                filterPaket.setBackgroundTintList(defaultColor);
            }
        });

        filterGurih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuArrayList = new ArrayList<>();
                filterBarang("gurih");
                recyclerView = root.findViewById(R.id.menuUtama);
                adapter = new adapterMenuUtama(menuArrayList,getContext());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                filterAll.setBackgroundTintList(defaultColor);
                filterBestSeller.setBackgroundTintList(defaultColor);
                filterGurih.setBackgroundTintList(selectedColor);
                filterManis.setBackgroundTintList(defaultColor);
                filterPaket.setBackgroundTintList(defaultColor);
            }
        });
        filterPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuArrayList = new ArrayList<>();
                getDataPaket();
                recyclerView = root.findViewById(R.id.menuUtama);
                adapter = new adapterMenuUtama(menuArrayList,getContext());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                filterAll.setBackgroundTintList(defaultColor);
                filterBestSeller.setBackgroundTintList(defaultColor);
                filterGurih.setBackgroundTintList(defaultColor);
                filterManis.setBackgroundTintList(defaultColor);
                filterPaket.setBackgroundTintList(selectedColor);
            }
        });

        dbTransaksiHelper dbHelper = new dbTransaksiHelper(getContext(), dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        item = dbHelper.countIsiTransaksi();

        adapter.setBeliOnClickListener(new adapterMenuUtama.beliOnClickListener() {
            @Override
            public void beliOnClick(int position) {
                setgetMenu selectedItem = menuArrayList.get(position);
                transaksiList.add(selectedItem);
                item += 1;

                if (dass != null) {
                    if (item >= 1) {
                        dass.btnTransaksi.setVisibility(View.VISIBLE);
                        dass.cardItemTr.setText(String.valueOf(item));
                    }
                }
            }
        });

        adapter.setKurangOnClickListener(new adapterMenuUtama.kurangOnClickListener() {
            @Override
            public void kurangOnClick(int position) {
                item -= 1;

                if (dass != null) {
                    if (item >=1) {
                        dass.btnTransaksi.setVisibility(View.VISIBLE);
                        dass.cardItemTr.setText(String.valueOf(item));
                    }else{
                        dass.btnTransaksi.setVisibility(View.GONE);
                        dass.cardItemTr.setText(String.valueOf(item));
                    }
                }
            }
        });

        return root;
    }

    void getDataBarang(){
        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.getBarang(new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                dataBarangRespons dataRespon = gson.fromJson(response.toString(), dataBarangRespons.class);
                if (dataRespon.getCode() == 200) {
                    List<dataBarang> barangList = dataRespon.getBarang_list();

                    for (dataBarang dataBarang : barangList) {
                        String id = dataBarang.getId_barang();
                        String image = dataBarang.getImage_barang();
                        String nama = dataBarang.getNama_barang();
                        String harga = dataBarang.getHarga_jual();
                        String deskripsi = dataBarang.getDeskripsi();
                        String URLimage = "http://" + GlobalVariable.IP + "/APIproject/image/" + image;

                        menuArrayList.add(new setgetMenu(URLimage, id, nama, deskripsi, harga, "barang"));
                    }
                    adapter.notifyDataSetChanged();
                }else if (dataRespon.getCode() == 404) {
                    Toast.makeText(getContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error , Toast.LENGTH_SHORT).show();
                Log.e("err Menu Fragment", error);
            }
        });
    }

    private void getDataPaket() {
        Gson gson = new Gson();

        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.getPaket(new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                dataPaketRespons dataRespon = gson.fromJson(response.toString(), dataPaketRespons.class);
                if (dataRespon.getCode() == 200) {
                    List<dataPaket> barangList = dataRespon.getPaket_list();

                    for (dataPaket dataBarang : barangList) {
                        String id = dataBarang.getId_paket();
                        String image = dataBarang.getGambar_paket();
                        String nama = dataBarang.getNama_paket();
                        String harga = dataBarang.getHarga_jual();
                        String deskripsi = dataBarang.getDeskripsi();
                        String URLimage = "http://" + GlobalVariable.IP + "/APIproject/image/" + image;

                        menuArrayList.add(new setgetMenu(URLimage, id, nama, deskripsi, harga, "paket"));
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

    void filterBarang(String jenisBarang){
        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.filterJenisBarang(jenisBarang, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                dataBarangRespons dataRespon = gson.fromJson(response.toString(), dataBarangRespons.class);
                if (dataRespon.getCode() == 200) {
                    List<dataBarang> barangList = dataRespon.getBarang_list();

                    for (dataBarang dataBarang : barangList) {
                        String id = dataBarang.getId_barang();
                        String image = dataBarang.getImage_barang();
                        String nama = dataBarang.getNama_barang();
                        String harga = dataBarang.getHarga_jual();
                        String deskripsi = dataBarang.getDeskripsi();
                        String URLimage = "http://" + GlobalVariable.IP + "/APIproject/image/" + image;

                        menuArrayList.add(new setgetMenu(URLimage, id, nama, deskripsi, harga, "barang"));
                    }
                    adapter.notifyDataSetChanged();
                }else if (dataRespon.getCode() == 404) {
                    Toast.makeText(getContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error , Toast.LENGTH_SHORT).show();
                Log.e("err Menu Fragment", error);
            }
        });
    }

    void filterBestSeller(){
        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.filterBestSeller(new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                dataBarangRespons dataRespon = gson.fromJson(response.toString(), dataBarangRespons.class);
                if (dataRespon.getCode() == 200) {
                    List<dataBarang> barangList = dataRespon.getBarang_list();

                    for (dataBarang dataBarang : barangList) {
                        String id = dataBarang.getId_barang();
                        String image = dataBarang.getImage_barang();
                        String nama = dataBarang.getNama_barang();
                        String harga = dataBarang.getHarga_jual();
                        String deskripsi = dataBarang.getDeskripsi();
                        String URLimage = "http://" + GlobalVariable.IP + "/APIproject/image/" + image;

                        menuArrayList.add(new setgetMenu(URLimage, id, nama, deskripsi, harga, "barang"));
                    }
                    adapter.notifyDataSetChanged();
                }else if (dataRespon.getCode() == 404) {
                    Toast.makeText(getContext(), "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error , Toast.LENGTH_SHORT).show();
                Log.e("err Menu Fragment", error);
            }
        });
    }

}