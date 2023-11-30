package com.abayhq.browniesnfriends.pesanan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.QRscanner;
import com.abayhq.browniesnfriends.adapter.adapterPesananTerjadwal;
import com.abayhq.browniesnfriends.adapter.adapterTabPesanan;
import com.abayhq.browniesnfriends.nota.notaActivity;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tabPesananFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tabPesananFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public tabPesananFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tabPesananFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static tabPesananFragment newInstance(String param1, String param2) {
        tabPesananFragment fragment = new tabPesananFragment();
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

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    TextView txtPesanan, txtCari;
    String telepon, akses;
    Button btnScan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_tab_pesanan, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.forFrameTab);
        txtPesanan = root.findViewById(R.id.txtPesanan);
        btnScan = root.findViewById(R.id.btnScanQR);
        txtCari = root.findViewById(R.id.txtCariPesanan);

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
                        txtPesanan.setText("Pesanan Saya");
                    }else if(akses.equals("karyawan")){
                        txtPesanan.setText("Daftar Pesanan");
                    }
                }
            }
            @Override
            public void onError(String error) {

            }
        });

        adapterTabPesanan adapterTabPesanan = new adapterTabPesanan(getActivity());

        adapterTabPesanan.addFragment(new pesananRiwayatFragment(), "Riwayat");
        adapterTabPesanan.addFragment(new pesananProsesFragment(), "Dalam Proses");
        adapterTabPesanan.addFragment(new pesananTerjadwalFragment(), "Terjadwal");

        viewPager.setAdapter(adapterTabPesanan);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapterTabPesanan.getPageTitle(position).toString())
        ).attach();



        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner();
            }
        });

        return root;
    }

    private void scanner(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume Up To Flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(QRscanner.class);
        launcher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> launcher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null){
            String getNotaFromQRcode = result.getContents();
            Intent intent = new Intent(getContext(), notaActivity.class);
            intent.putExtra("notaProsesAdmin", getNotaFromQRcode);
            startActivity(intent);
        }
    });
}