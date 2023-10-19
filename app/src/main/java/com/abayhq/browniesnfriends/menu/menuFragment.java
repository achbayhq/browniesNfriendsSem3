package com.abayhq.browniesnfriends.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterMenuUtama;
import com.abayhq.browniesnfriends.settergetter.setgetMenu;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link menuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menuFragment extends Fragment implements  adapterMenuUtama.penandaOnClickListener{

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        addMenu();
        recyclerView = root.findViewById(R.id.menuUtama);
        adapter = new adapterMenuUtama(menuArrayList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    void addMenu(){
        menuArrayList = new ArrayList<>();
        menuArrayList.add(new setgetMenu(R.drawable.menu_a, "Menu A", "Kue A ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 10.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_b, "Menu B", "Kue B ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 15.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_c, "Menu C", "Kue C ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 12.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_d, "Menu D", "Kue D ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 14.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_a, "Menu E", "Kue E ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 15.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_b, "Menu F", "Kue F ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 16.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_c, "Menu G", "Kue G ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 17.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_d, "Menu H", "Kue H ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 18.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_d, "Menu I", "Kue I ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 10.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_c, "Menu J", "Kue J ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 15.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_b, "Menu K", "Kue K ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 12.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_a, "Menu L", "Kue L ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 14.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_a, "Menu M", "Kue M ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 15.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_b, "Menu N", "Kue N ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 16.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_c, "Menu O", "Kue O ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 17.000"));
        menuArrayList.add(new setgetMenu(R.drawable.menu_d, "Menu P", "Kue P ini adalah seperti ini dan ini lalu rasanya seperti ini pokoknya ini banget", "Rp. 18.000"));
    }

    @Override
    public void penandaOnClick(int position) {
        //function untuk button penanda

    }
}