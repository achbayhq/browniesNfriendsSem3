package com.abayhq.browniesnfriends.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.login.MainActivity;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.settergetter.setgetMenu;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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

    TextView txtNama;
    ImageView photoProfile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String telepon = sharedPreferences.getString("telepon", "");

        photoProfile = root.findViewById(R.id.photoProfile);
        txtNama = root.findViewById(R.id.txtNamaHome);

        if (!telepon.equals("")){
            Gson gson = new Gson();
            volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
            volleyRequestHandler.loginUser(telepon, new volleyRequestHandler.ResponseListener() {
                @Override
                public void onResponse(JSONObject response) {
                    userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                    if (userRespon.getCode() == 200) {
                        dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                        String userName = loggedUser.getNama();
                        String userPP = loggedUser.getPhoto_profile();
                        txtNama.setText(getString(R.string.hi)+ userName +getString(R.string.tanda_seru));
                        if (userPP.equals("")){
                            photoProfile.setImageResource(R.drawable.default_pp);
                        }else{
//                            byte[] imagePP = Base64.decode(userPPbase64, Base64.DEFAULT);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(imagePP, 0, imagePP.length);
//                            photoProfile.setImageBitmap(bitmap);
                            String URLimage = "http://" + GlobalVariable.IP + "/APIproject/image/" + userPP;
                            Glide.with(getContext()).load(URLimage).into(photoProfile);
                        }
                    }else if (userRespon.getCode() == 404) {
                        Toast.makeText(getContext(), "User Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }

        return root;
    }
}