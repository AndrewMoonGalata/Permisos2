package AddP;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.permisos2.FirstFragment;
import com.example.permisos2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddPermiso extends Fragment {

    private List<String> opcionesEmpleados = new ArrayList<>();
    private List<String> opcionesMotivo = new ArrayList<>();
    private Spinner motivoSpinner;
    private Spinner empleadoSpinner;
    private Map<String, String> mapaIdEmpleados = new HashMap<>();
    private Map<String, String> mapaIdMotivos = new HashMap<>();


    private static final String PREFS_NAME = "PreferenciaId";



    private RequestQueue rq;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_permiso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rq = Volley.newRequestQueue(requireContext());

        motivoSpinner = view.findViewById(R.id.motivoSpinner);
        AutoCompleteTextView empleadoAutoComplete = view.findViewById(R.id.empleadoAutoComplete);

        ImageButton datePickerButton = view.findViewById(R.id.datePicker);
        EditText selectedDateEditText = view.findViewById(R.id.selectedDateEditText);

        EditText observacionesEditText = view.findViewById(R.id.observacionesEditText);

        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button guardarButton = view.findViewById(R.id.guardarButton);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("PreferenciaId", Context.MODE_PRIVATE);

        SharedPreferences sharedPreference = requireContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);



        // Cargar las opciones del AutoCompleteTextView de empleados desde la API en la opción 5
        fetchEmpleadoData();

        // Configurar el adaptador personalizado para el AutoCompleteTextView de empleados
        ArrayAdapter<String> empleadoAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, opcionesEmpleados);
        empleadoAutoComplete.setAdapter(empleadoAdapter);

        empleadoAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String empleadoSeleccionado = empleadoAdapter.getItem(position);
                // Aquí puedes realizar acciones basadas en la selección del empleado
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un mensaje de cancelación
                Toast.makeText(requireContext(), "El proceso ha sido detenido", Toast.LENGTH_SHORT).show();

                // Cambiar al fragmento FirstFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new FirstFragment())
                        .commit();
            }
        });
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de los elementos UI
                String empleadoSeleccionado = empleadoAutoComplete.getText().toString();
                String motivoSeleccionado = motivoSpinner.getSelectedItem().toString();
                String fechaSeleccionada = selectedDateEditText.getText().toString();
                String observaciones = observacionesEditText.getText().toString();

                if (empleadoSeleccionado.isEmpty() || motivoSeleccionado.isEmpty() || fechaSeleccionada.isEmpty() || observaciones.isEmpty()) {
                    // Mostrar un mensaje de error si algún campo obligatorio está vacío
                    Toast.makeText(requireContext(), "Faltan campos por completar", Toast.LENGTH_SHORT).show();
                    return; // Salir del método onClick para evitar seguir con la acción
                }




                // Crear un cuadro de diálogo de confirmación
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("¿Estás seguro de subir el permiso?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                // Aquí puedes hacer la llamada a tu API con los datos recopilados
                                String idUsuario = sharedPreference.getString("idusuario", "");

                                enviarDatosALaAPI(empleadoSeleccionado, motivoSeleccionado, fechaSeleccionada, observaciones, idUsuario);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // No hacer nada si se selecciona "Cancelar"
                            }
                        });

                // Mostrar el cuadro de diálogo
                builder.create().show();
            }
        });




        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(selectedDateEditText);
            }
        });

        empleadoAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEmpleadoAdapter(empleadoAdapter, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        observacionesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Aquí puedes realizar acciones basadas en los cambios en el texto
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fetchMotivoData(); // Cargar las opciones del Spinner de motivo desde la API en la opción 37
    }

    private void enviarDatosALaAPI(String empleado, String motivo, String fecha, String observaciones, String idUsuario) {
        String url = "http://hidalgo.no-ip.info:5610/bitalaapps/controller/ControllerBitala2.php";

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        //String idsuario = sharedPreferences.getString("idusuario", "");


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Manejar la respuesta exitosa de la API si es necesario
                Log.d("AddPermiso", "Respuesta de la API: " + response);
                Toast.makeText(requireContext(), "Permiso agregado", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new FirstFragment())
                        .commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error de la solicitud a la API si es necesario
                Log.e("AddPermiso", "Error de la API: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "39"); // Cambiar la opción según lo requerido por tu API
                params.put("idUsuario", idUsuario);
                params.put("idEmpleado", obtenerIdEmpleado(empleado)); // Obtener idEmpleado según el nombre seleccionado
                params.put("idPermiso", obtenerIdPermiso(motivo)); // Obtener idPermiso según el motivo seleccionado
                params.put("Fpermiso", fecha);
                params.put("observaciones", observaciones);
                params.put("status", "activo");
                // Agregar más parámetros si es necesario
                Log.d("getParams: ", params.toString());
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        rq.add(request);
    }
    private String obtenerIdEmpleado(String nombreEmpleado) {
        String idEmpleado = mapaIdEmpleados.get(nombreEmpleado);
        Log.d("AddPermiso", "Obtener ID de empleado para " + nombreEmpleado + ": " + idEmpleado);
        return idEmpleado;
    }






    private String obtenerIdPermiso(String motivo) {
        return mapaIdMotivos.get(motivo);
    }


    private void filterEmpleadoAdapter(ArrayAdapter<String> adapter, String query) {
        adapter.getFilter().filter(query);
    }


    private void showDatePickerDialog(final TextView textView) {
        // Obtiene la fecha actual
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Crea un DatePickerDialog y establece un Listener para capturar la fecha seleccionada
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Formatea el mes y el día para asegurarte de que tengan dos dígitos
                        String formattedMonth = String.format(Locale.getDefault(), "%02d", monthOfYear + 1);
                        String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                        // Establece el texto en el TextView con la fecha formateada
                        textView.setText(year + "/" + formattedMonth + "/" + formattedDay);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }











    private void fetchMotivoData() {
        String url = "http://hidalgo.no-ip.info:5610/bitalaapps/controller/ControllerBitala2.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    // Iterar a través del JSONArray y agregar las descripciones de motivo a la lista
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String descripcion = jsonObject.getString("descripcion");
                        opcionesMotivo.add(descripcion);

                        // Obtener el ID del motivo
                        String idMotivo = jsonObject.getString("id"); // Ajusta el nombre del campo según tu respuesta JSON

                        // Almacenar el ID del motivo en el mapa
                        mapaIdMotivos.put(descripcion, idMotivo);
                    }

                    // Configurar el ArrayAdapter para el Spinner de motivo
                    ArrayAdapter<String> motivoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, opcionesMotivo);
                    motivoSpinner.setAdapter(motivoAdapter);

                    motivoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String motivoSeleccionado = opcionesMotivo.get(position);
                            // Aquí puedes realizar acciones basadas en la selección del motivo
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Aquí puedes manejar la situación en la que no se ha seleccionado nada
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Manejar error de parsing JSON
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // Manejar error de solicitud
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "37");

                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        rq.add(request);
    }

    private void fetchEmpleadoData() {
        String url = "http://hidalgo.no-ip.info:5610/bitalaapps/controller/ControllerBitala2.php";
        //final Map<String, String> mapaIdEmpleados = new HashMap<>();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    // Iterar a través del JSONArray y agregar las descripciones de empleados a la lista
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String empleado = jsonObject.getString("Nombre") + " " + jsonObject.getString("Apellidos");
                        opcionesEmpleados.add(empleado);

                        String idEmpleado = jsonObject.getString("idE");
                        mapaIdEmpleados.put(empleado, idEmpleado);

                        Log.d("AddPermiso", "Empleado: " + empleado + " - ID: " + idEmpleado);

                    }

                    // Configurar el ArrayAdapter para el AutoCompleteTextView de empleados
                    ArrayAdapter<String> empleadoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, opcionesEmpleados);
                    AutoCompleteTextView empleadoAutoComplete = requireView().findViewById(R.id.empleadoAutoComplete);
                    empleadoAutoComplete.setAdapter(empleadoAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Manejar error de parsing JSON
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // Manejar error de solicitud
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "5"); // Cambiar la opción según lo requerido por tu API
                // Agregar más parámetros si es necesario
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        rq.add(request);
    }



}
