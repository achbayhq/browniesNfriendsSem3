package com.abayhq.browniesnfriends.pesanan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterTabPesanan;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_tab_pesanan, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.forFrameTab);

        adapterTabPesanan adapterTabPesanan = new adapterTabPesanan(getActivity());

        adapterTabPesanan.addFragment(new pesananRiwayatFragment(), "Riwayat");
        adapterTabPesanan.addFragment(new pesananProsesFragment(), "Dalam Proses");
        adapterTabPesanan.addFragment(new pesananTerjadwalFragment(), "Terjadwal");

        viewPager.setAdapter(adapterTabPesanan);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapterTabPesanan.getPageTitle(position).toString())
        ).attach();

        return root;
    }
}