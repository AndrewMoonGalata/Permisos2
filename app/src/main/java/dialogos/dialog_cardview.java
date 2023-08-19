package dialogos;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.permisos2.R;


public class dialog_cardview extends DialogFragment {

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
   View rootView = inflater.inflate(R.layout.fragment_dialog_cardview,container,false);

    TextView motivoTextDialog = rootView.findViewById(R.id.motivotext);
    TextView motivoDialog = rootView.findViewById(R.id.motivo);

    TextView empleadoTextDialog = rootView.findViewById(R.id.empleadotext);
    TextView empleadoDialog = rootView.findViewById(R.id.empleado);

    TextView nlistaTextDialog = rootView.findViewById(R.id.nlistatext);
    TextView nlistaDialog = rootView.findViewById(R.id.nlista);

    TextView fechaTextDialog = rootView.findViewById(R.id.fechatext);
    TextView fechaDialog = rootView.findViewById(R.id.fecha);

    TextView fecharTextDialog = rootView.findViewById(R.id.fechartext);
    TextView fecharDialog = rootView.findViewById(R.id.fechar);

    TextView observacionesTextDialog = rootView.findViewById(R.id.observacionestext); // Asegúrate de que el ID sea correcto
    TextView observacionesDialog = rootView.findViewById(R.id.observaciones);


    String motivo = getArguments().getString("motivo");
    String empleado = getArguments().getString("empleado");
    String nlista = getArguments().getString("nlista");
    String fecha = getArguments().getString("fecha");
    String fechar = getArguments().getString("fechar");

    String observaciones = getArguments().getString("observaciones");




    motivoTextDialog.setText("Motivo:");
    motivoDialog.setText(motivo);

    empleadoTextDialog.setText("Empleado:");
    empleadoDialog.setText(empleado);

    nlistaTextDialog.setText("No.lista:");
    nlistaDialog.setText(nlista);

    fechaTextDialog.setText("Fecha:");
    fechaDialog.setText(fecha);

    fecharTextDialog.setText("Fecha registro:");
    fecharDialog.setText(fechar);

    observacionesTextDialog.setText("Observaciones:");
    observacionesDialog.setText(observaciones);

    return  rootView;

    }

}