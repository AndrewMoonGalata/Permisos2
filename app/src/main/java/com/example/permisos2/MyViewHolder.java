package com.example.permisos2;

import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView motivoView, empleadoView, nlistaView, fechaView, fecharView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        motivoView = itemView.findViewById(R.id.motivo);
        empleadoView = itemView.findViewById(R.id.empleado);
        nlistaView = itemView.findViewById(R.id.nlista);
        fechaView = itemView.findViewById(R.id.fecha);
        fecharView = itemView.findViewById(R.id.fechar);
        // No se usará fecharView en esta vista, así que puedes omitirlo o adaptarlo según necesites.
    }
}