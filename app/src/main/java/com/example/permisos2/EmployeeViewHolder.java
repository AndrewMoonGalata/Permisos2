package com.example.permisos2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeViewHolder extends RecyclerView.ViewHolder {

   private TextView nombreView, apellidosView, nlistaView, empresaView, departamentoView, puestoView, turnoView, jefeView;

    public EmployeeViewHolder(@NonNull View itemView){
        super(itemView);

        nombreView = itemView.findViewById(R.id.nombre);
        apellidosView = itemView.findViewById(R.id.apellidos);
        nlistaView = itemView.findViewById(R.id.nlista);
        empresaView = itemView.findViewById(R.id.empresa);
        departamentoView = itemView.findViewById(R.id.departamento);
        puestoView = itemView.findViewById(R.id.puesto);
        turnoView = itemView.findViewById(R.id.turno);
        jefeView = itemView.findViewById(R.id.jefe);

    }

    public void bind(Employee employee) {

        nombreView.setText(employee.getNombre());
        apellidosView.setText(employee.getApellidos());
        nlistaView.setText(employee.getNlista());
        empresaView.setText(employee.getEmpresa());
        departamentoView.setText(employee.getDepartamento());
        puestoView.setText(employee.getPuesto());
        turnoView.setText(employee.getTurno());
        jefeView.setText(employee.getJefe());
    }

}
