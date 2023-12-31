package com.example.permisos2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.permisos2.databinding.ActivityPermisosBinding;

import androidx.appcompat.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import AddP.AddPermiso;

public class FirstFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Item> items;
    private Calendar selectedDate = Calendar.getInstance();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerview);
        items = new ArrayList<>();
        adapter = new MyAdapter(getChildFragmentManager(), items);



        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        SearchView searchView = rootView.findViewById(R.id.buscarFecha);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPermisoList(newText);
                return true;
            }
        });


        ImageButton datePickerButton = rootView.findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        ImageButton addButton = rootView.findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAddPermisoFragment();
            }
        });
        Log.d("FirstFragment", "Fetching permisos data...");
        // Realizar la solicitud a la API
        fetchPermisoData();


        return rootView;
    }

    private void openAddPermisoFragment() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        AddPermiso addPermisoFragment = new AddPermiso();
        fragmentTransaction.replace(R.id.frame_layout, addPermisoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void filterPermisoList(String query) {
        List<Item> filteredList = new ArrayList<>();
        String normalizedQuery = normalizeString(query);

        for (Item item : items) {
            String normalizedFpermiso = normalizeString(item.getFpermiso());
            String normalizedNombreEmpleado = normalizeString(item.getNombreEmpleado());
            String normalizedApellidosEmpleado = normalizeString(item.getApellidosEmpleado());

            if (normalizedFpermiso.contains(normalizedQuery) ||
                    normalizedNombreEmpleado.contains(normalizedQuery) ||
                    normalizedApellidosEmpleado.contains(normalizedQuery)) {
                filteredList.add(item);
            }
        }

        adapter.setItems(filteredList);

        TextView noResultsText = getView().findViewById(R.id.noResultsText);

        if (filteredList.isEmpty()) {
            noResultsText.setVisibility(View.VISIBLE); // Mostrar el mensaje
        } else {
            noResultsText.setVisibility(View.GONE); // Ocultar el mensaje
        }


    }

    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }



    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        // Actualizar la búsqueda con la fecha seleccionada
                        filterPermisoList(getFormattedDate(selectedDate.getTime()));

                        if (adapter.getItemCount() == 0) {
                            TextView noResultsText = getView().findViewById(R.id.noResultsText);
                            noResultsText.setText("No se encontraron permisos para esta fecha");
                            noResultsText.setVisibility(View.VISIBLE); // Mostrar el mensaje


                        }
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private String getFormattedDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }




    private void fetchPermisoData() {
        String url = "http://hidalgo.no-ip.info:5610/bitalaapps/controller/ControllerBitala2.php";
        Log.d("FetchPermisoData", "Iniciando solicitud a la API");
        //agregué esto, si deja de funcionar retirarlo

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String idUsuario = sharedPreferences.getString("idusuario", ""); // Obtener el idusuario del usuario que ha iniciado sesión
        Log.d("FetchPermisoData", "ID de Usuario: " + idUsuario);

        //
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JSONResponse", "Response from server: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    // Limpiar la lista actual de elementos
                    items.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String userId = jsonObject.getString("idUsuario"); // Obtener el idusuario asociado con el permiso

                        if (userId.equals(idUsuario)) { // Verificar si el idusuario coincide con el del usuario que ha iniciado sesión
                            String motivo = jsonObject.getString("descripcion");
                            String empleado = jsonObject.getString("nombreEmpleado");
                            String apellidos = jsonObject.getString("apellidosEmpleado");
                            String nlista = jsonObject.getString("Nlista");
                            String fechaRegistro = jsonObject.getString("fechaRegistro");
                            String fecha = jsonObject.getString("Fpermiso");
                            String observaciones = jsonObject.getString("observaciones");

                            Item item = new Item(motivo, empleado, apellidos, nlista, fecha, fechaRegistro, observaciones);
                            items.add(item);
                        }
                    }

                    // for original si deja de jalar el de arriba, reestablecer éste
                    /*for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String motivo = jsonObject.getString("descripcion");
                        String empleado = jsonObject.getString("nombreEmpleado");
                        String apellidos = jsonObject.getString("apellidosEmpleado");
                        String nlista = jsonObject.getString("Nlista");
                        String fechaRegistro = jsonObject.getString("fechaRegistro");
                        String fecha = jsonObject.getString("Fpermiso");
                        String observaciones = jsonObject.getString("observaciones");

                        Item item = new Item(motivo, empleado,apellidos, nlista, fecha, fechaRegistro, observaciones);
                        items.add(item);
                    }*/

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                    Log.d("AdapterUpdate", "Number of items in the list: " + items.size());


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONError", "Error al parsear JSON: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud
                Log.e("VolleyError", "Error al realizar la solicitud POST: " + error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "38");
                params.put("idusuario", idUsuario); // Agregar el idusuario a los parámetros de la solicitud
                // Agregar más parámetros si es necesario
                return params;
            }
        };



        RequestQueue rq = Volley.newRequestQueue(requireContext());
        rq.add(request);
    }


}
