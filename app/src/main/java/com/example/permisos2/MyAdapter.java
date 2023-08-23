package com.example.permisos2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dialogos.dialog_cardview;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Item> items;
    private FragmentManager fragmentManager;

    public MyAdapter(FragmentManager fragmentManager, List<Item> items) {
        this.fragmentManager = fragmentManager;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = items.get(position);

        holder.motivoView.setText(item.getDescripcion());
        holder.empleadoView.setText(item.getNombreEmpleado() + " " + item.getApellidosEmpleado());
        holder.nlistaView.setText(item.getNlista());
        holder.fechaView.setText(item.getFpermiso());
        holder.fecharView.setText(item.getFechaRegistro());

    }
    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView motivoView, empleadoView, nlistaView, fechaView, fecharView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            motivoView = itemView.findViewById(R.id.motivo);
            empleadoView = itemView.findViewById(R.id.empleado);
            nlistaView = itemView.findViewById(R.id.nlista);
            fechaView = itemView.findViewById(R.id.fecha);
            fecharView = itemView.findViewById(R.id.fechar);

            //OnClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Obtener la posición del elemento en el adaptador
                    int position = getAdapterPosition();

                    // Verificar si la posición es válida
                    if (position != RecyclerView.NO_POSITION) {
                        // Obtener el elemento correspondiente a la posición
                        Item item = items.get(position);

                        // Mostrar el cuadro de diálogo personalizado aquí
                        showCustomDialog(item);
                    }
                }
            });
        }

        private void showCustomDialog(Item item) {
            DialogFragment dialogFragment = new dialog_cardview();
            Bundle args = new Bundle();
            args.putString("motivo", item.getDescripcion());
            args.putString("empleado", item.getNombreEmpleado() + " " + item.getApellidosEmpleado());
            args.putString("nlista", item.getNlista());
            args.putString("fecha", item.getFpermiso());
            args.putString("fechar", item.getFechaRegistro());
            args.putString("observaciones", item.getObservaciones());

            dialogFragment.setArguments(args);
            dialogFragment.show(fragmentManager, "custom_dialog");
        }


    }
}

