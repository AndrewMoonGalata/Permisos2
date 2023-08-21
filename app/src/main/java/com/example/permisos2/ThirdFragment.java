package com.example.permisos2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third, container, false);
        /*
        TextView textUsuario = rootView.findViewById(R.id.txtUsuarioNombre);
        ImageView imageViewDetalles = rootView.findViewById(R.id.imageViewUser); // Asegúrate de tener el ID correcto para el ImageView

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        // Obtener los valores almacenados
        String username = sharedPreferences.getString("username", "");
        String name = sharedPreferences.getString("nombre", "");
        String lastname = sharedPreferences.getString("apellido", "");
        String rol = sharedPreferences.getString("rol", "");
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        String password = rememberMe ? sharedPreferences.getString("password", "") : "";



        textUsuario.setText(name+ " - " + lastname);
*/
        /*
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name", "");
            String lastname = bundle.getString("lastname", "");
            String username = bundle.getString("username", "");
            String rol = bundle.getString("rol", "");


            textUsuario.setText(name+ " - " + lastname);

            String foto="https://static.vecteezy.com/system/resources/previews/019/879/186/non_2x/user-icon-on-transparent-background-free-png.png";
            if (!TextUtils.isEmpty(foto)) {
                String imageUrl =  foto;
                Glide.with(this)
                        .load(imageUrl)  // URL de la foto
                        .error(R.drawable.perfil)  // Aquí se especifica la imagen en caso de error
                        .into(imageViewDetalles);
            } else {
                Glide.with(this)
                        .load(R.drawable.perfil)  // Carga la imagen predeterminada si imageUrl está vacío
                        .into(imageViewDetalles);
            }
        }
*/
        return rootView;

    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Acceder a las SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        // Obtener los valores almacenados
        String username = sharedPreferences.getString("username", "");
        String name = sharedPreferences.getString("name", "");
        String lastname = sharedPreferences.getString("lastname", "");
        String rol = sharedPreferences.getString("rol", "");
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        String password = rememberMe ? sharedPreferences.getString("password", "") : "";


       // TextView textViewLastname = view.findViewById(R.id.textViewLastname);
        TextView txtUsuarioNombre = view.findViewById(R.id.txtUsuarioNombre);

        txtUsuarioNombre.setText(name +" "+ lastname);
        // También puedes hacer lo mismo para otras vistas

        // Dependiendo de tus necesidades, puedes usar los valores para diferentes vistas en tu fragmento
    }
}