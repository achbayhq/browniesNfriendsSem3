package com.abayhq.browniesnfriends.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ubahPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ubahPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ubahPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ubahPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ubahPasswordFragment newInstance(String param1, String param2) {
        ubahPasswordFragment fragment = new ubahPasswordFragment();
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

    EditText etPass1, etPass2, etPass3;
    Button btnBack, btnSubmitPass;
    private String telepon, password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ubah_password, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        telepon = sharedPreferences.getString("telepon", "");
        etPass1 = root.findViewById(R.id.passGantiPass1);
        etPass2 = root.findViewById(R.id.passGantiPass2);
        etPass3 = root.findViewById(R.id.passGantiPass3);
        Button visibilityButton1 = root.findViewById(R.id.visibilityButton1);
        Button visibilityButton2 = root.findViewById(R.id.visibilityButton2);
        Button visibilityButton3 = root.findViewById(R.id.visibilityButton3);
        getPass();

        visibilityButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass1.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Ubah ke tipe password yang tersembunyi
                    etPass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    visibilityButton1.setBackgroundResource(R.drawable.tutup_pass);
                } else {
                    // Ubah ke tipe teks biasa
                    etPass1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    visibilityButton1.setBackgroundResource(R.drawable.buka_pass);
                }
                etPass1.setSelection(etPass1.getText().length());
            }
        });

        visibilityButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass2.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Ubah ke tipe password yang tersembunyi
                    etPass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    visibilityButton2.setBackgroundResource(R.drawable.tutup_pass);
                } else {
                    // Ubah ke tipe teks biasa
                    etPass2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    visibilityButton2.setBackgroundResource(R.drawable.buka_pass);
                }
                etPass2.setSelection(etPass2.getText().length());
            }
        });

        visibilityButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass3.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Ubah ke tipe password yang tersembunyi
                    etPass3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    visibilityButton3.setBackgroundResource(R.drawable.tutup_pass);
                } else {
                    // Ubah ke tipe teks biasa
                    etPass3.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    visibilityButton3.setBackgroundResource(R.drawable.buka_pass);
                }
                etPass3.setSelection(etPass3.getText().length());
            }
        });

        btnBack = root.findViewById(R.id.btnBackUbahPass);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragment profileFragment = new profileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forFragment, profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnSubmitPass = root.findViewById(R.id.btnSubmitPass);
        btnSubmitPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passLama = etPass1.getText().toString();
                String passBaru = etPass2.getText().toString();
                String passKonfirm = etPass3.getText().toString();

                if (passLama.equals(password)) {
                    if (passBaru.equals(passKonfirm)) {
                        Gson gson = new Gson();
                        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
                        volleyRequestHandler.gantiPassword(passBaru, telepon, new volleyRequestHandler.ResponseListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int code = response.getInt("code");
                                    if (code == 200) {
                                        profileFragment profileFragment = new profileFragment();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.forFragment, profileFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "Password Baru Tidak Sesuai", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Password Anda Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    void getPass(){
        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.loginUser(telepon, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                if (userRespon.getCode() == 200) {
                    dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                    password = loggedUser.getPassword();
                }else if (userRespon.getCode() == 404) {
                    Toast.makeText(getContext(), "User Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }
}