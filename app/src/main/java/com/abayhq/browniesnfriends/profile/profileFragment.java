package com.abayhq.browniesnfriends.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.login.MainActivity;
import com.abayhq.browniesnfriends.login.RegisterActivity;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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

    Button btnLogout, btnEdit, btnUbahPP, btnGantiPass;
    TextView txtSubNama;
    EditText etNama, etPass, etTelepon, etAlamat;
    ImageView imgPP;
    byte[] imgByteArray;
    String telepon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        imgPP = root.findViewById(R.id.imgProfile);
        etNama = root.findViewById(R.id.namaProfile);
        etTelepon = root.findViewById(R.id.tlpProfile);
        etAlamat = root.findViewById(R.id.alamatProfile);
        txtSubNama = root.findViewById(R.id.txtNama);
        telepon = sharedPreferences.getString("telepon", "");
        btnUbahPP = root.findViewById(R.id.editPhoto);
        etNama.setEnabled(false);
        etTelepon.setEnabled(false);
        etAlamat.setEnabled(false);
        btnUbahPP.setVisibility(View.GONE);

        btnEdit = root.findViewById(R.id.button2);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paramSave = btnEdit.getText().toString();
                if (paramSave.equals("Edit Profile")){
                    etNama.setEnabled(true);
                    etTelepon.setEnabled(true);
                    etAlamat.setEnabled(true);
                    btnUbahPP.setVisibility(View.VISIBLE);
                    btnEdit.setText(R.string.save_profile);
                } else if (paramSave.equals("Save Profile")) {
                    String nama = etNama.getText().toString();
                    String teleponNew = etTelepon.getText().toString();
                    String alamat = etAlamat.getText().toString();
                    String gambarBase64 = Base64.encodeToString(imgByteArray, Base64.DEFAULT);

                    volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
                    volleyRequestHandler.updateProfile(gambarBase64, nama, alamat, teleponNew, telepon, new volleyRequestHandler.ResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int code = response.getInt("code");
                                if (code == 200) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("telepon", teleponNew);
                                    editor.apply();
                                    getData();
                                    Toast.makeText(getContext(), "Profile Berhasil Diperbarui" , Toast.LENGTH_SHORT).show();
                                    etNama.setEnabled(false);
                                    etTelepon.setEnabled(false);
                                    etAlamat.setEnabled(false);
                                    btnUbahPP.setVisibility(View.GONE);
                                    btnEdit.setText(R.string.edit_profile);
                                } else if (code == 400) {
                                    Toast.makeText(getContext(), response.getString("status") , Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(String error) {
                            Toast.makeText(getContext(), error , Toast.LENGTH_SHORT).show();
                            Log.e("errorPHP", error);
                        }
                    });
                }
            }
        });

        btnUbahPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(requireActivity())
                        .crop()
                        .compress(1024)
                        .galleryOnly()
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                startForProfileImageResult.launch(intent);
                                return null;
                            }
                        });
            }
        });

        btnLogout = root.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("telepon");
                editor.apply();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnGantiPass = root.findViewById(R.id.btnGantiPass);
        btnGantiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahPasswordFragment ubahPassFragment = new ubahPasswordFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.forFragment, ubahPassFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        if (!telepon.equals("")){
            getData();
        }

        return root;
    }

    void getData(){
        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(getContext());
        volleyRequestHandler.loginUser(telepon, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                if (userRespon.getCode() == 200) {
                    dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                    String userName = loggedUser.getNama();
                    String userTelepon = loggedUser.getNo_telepon();
                    String userAlamat = loggedUser.getAlamat();
                    String userPP = loggedUser.getPhoto_profile();
                    txtSubNama.setText(getString(R.string.hi)+userName);
                    etNama.setText(userName);
                    etTelepon.setText(userTelepon);
                    etAlamat.setText(userAlamat);
                    if (userPP == null){
                        imgPP.setImageResource(R.drawable.default_pp);
                    }else{
                        String URLimage = "http://" + GlobalVariable.IP + "/APIproject/image/" + userPP;
                        Glide.with(getContext()).load(URLimage).into(imgPP);
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

    private ActivityResultLauncher<Intent> startForProfileImageResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                if (resultCode == Activity.RESULT_OK) {
                    //Bitmap bitmap = data.getParcelableExtra("data");
                    Uri imageUri = data.getData();
                    if (imageUri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            imgByteArray = stream.toByteArray();
                            imgPP.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getContext(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                    }
//                    if (bitmap != null) {
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        imgByteArray = stream.toByteArray();
//
//                        imgPP.setImageBitmap(bitmap);
//                    } else {
//                        Toast.makeText(getContext(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
//                    }

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );


}