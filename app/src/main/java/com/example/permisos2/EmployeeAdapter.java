package com.example.permisos2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {


    private List<Employee> employeeList;

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);

        holder.bind(employee);
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }


    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreView,apellidosView, nlistaView, empresaView, departamentoView, puestoView, turnoView, jefeView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreView = itemView.findViewById(R.id.nombre);
            apellidosView = itemView.findViewById(R.id.apellidos);
            nlistaView = itemView.findViewById(R.id.nlista);
            empresaView = itemView.findViewById(R.id.empresa);
            departamentoView = itemView.findViewById(R.id.departamento);
            puestoView = itemView.findViewById(R.id.puesto);
            turnoView = itemView.findViewById(R.id.turno);
            jefeView = itemView.findViewById(R.id.jefe);

            nombreView.setText("Nombre:");
            apellidosView.setText("Apellidos:");
            nlistaView.setText("No.Lista:");
            empresaView.setText("Empresa:");
            departamentoView.setText("Departamento:");
            puestoView.setText("Puesto:");
            turnoView.setText("Turno:");
            jefeView.setText("Jefe:");
        }


        public void bind(Employee employee) {
            nombreView.setText(" " + employee.getNombre());
            apellidosView.setText(" " + employee.getApellidos());
            nlistaView.setText(" " + employee.getNlista());
            empresaView.setText(" " + employee.getEmpresa());
            departamentoView.setText(" " + employee.getDepartamento());
            puestoView.setText(" " + employee.getPuesto());
            turnoView.setText(" " + employee.getTurno());
            jefeView.setText(" " + employee.getJefe());
        }
    }
}
