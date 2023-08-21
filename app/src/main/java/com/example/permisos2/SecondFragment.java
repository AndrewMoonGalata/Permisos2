package com.example.permisos2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.fragment.NavHostFragment;
import androidx.appcompat.widget.SearchView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.permisos2.databinding.FragmentSecondBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private EmployeeAdapter adapter;
    private List<Employee> employeeList;

    private SearchView searchView;
    private RequestQueue rq;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        employeeList = new ArrayList<>();
        rq = Volley.newRequestQueue(requireContext());
        recyclerView = binding.recyclerView;
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new EmployeeAdapter(employeeList); // Pasar la lista al constructor del adaptador
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterEmployeeList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEmployeeList(newText);
                return true;
            }
        });



        // Llamar a un método para obtener los datos de la API
        fetchEmployeeData();
    }

    private void filterEmployeeList(String query) {
        List<Employee> filteredList = new ArrayList<>();
        String normalizedQuery = normalizeString(query);

        for (Employee employee : employeeList) {
            String fullName = employee.getNombre() + " " + employee.getApellidos();
            String normalizedFullName = normalizeString(fullName);
            String normalizedNombre = normalizeString(employee.getNombre());
            String normalizedApellidos = normalizeString(employee.getApellidos());

            if (containsNormalizedWords(normalizedFullName, normalizedQuery) ||
                    containsNormalizedWords(normalizedNombre, normalizedQuery) ||
                    containsNormalizedWords(normalizedApellidos, normalizedQuery) ||
                    normalizeString(employee.getNlista()).contains(normalizedQuery)) {

                filteredList.add(employee);
            }
        }

        adapter = new EmployeeAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    private boolean containsNormalizedWords(String text, String query) {
        String[] words = query.split("\\s+");
        for (String word : words) {
            if (!text.contains(word)) {
                return false;
            }
        }
        return true;
    }


    private String normalizeString(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }






    private void fetchEmployeeData() {
        String url = "http://hidalgo.no-ip.info:5610/bitalaapps/controller/ControllerBitala2.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    // Limpiar la lista actual de empleados
                    employeeList.clear();

                    // Iterar a través del JSONArray y agregar los empleados a la lista
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nombre = jsonObject.getString("Nombre");
                        String apellidos = jsonObject.getString("Apellidos");
                        String nlista = jsonObject.getString("Nlista");
                        String empresa = jsonObject.getString("empresa");
                        String departamento = jsonObject.getString("departamento");
                        String puesto = jsonObject.getString("puesto");
                        String turno = jsonObject.getString("turno");
                        String jefe = jsonObject.getString("jefe");

                        Employee employee = new Employee(nombre,apellidos, nlista, empresa, departamento, puesto, turno, jefe);
                        employeeList.add(employee);
                    }

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();

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
            // Agregar parámetros POST si es necesario
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "5");
                // Agregar más parámetros si es necesario
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        rq.add(request);
    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}