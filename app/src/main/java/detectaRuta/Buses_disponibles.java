package detectaRuta;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.rutas.santaelena.app.rutas.R;

import java.util.List;

public class Buses_disponibles extends FragmentActivity {
    Dialog dialog ;
    public interface OnOklineaSeleccionada{
        void seleccionada(int seleccion, LatLng puntoCercanoATomarElBus, LatLng puntoCercanoBajarElBus);
    }
    public void alertLineas(List<String> rutasDisponibles, List<String> rutasTodas, Context context, List<Double> disOrigenRuta,List<Double> disRutaDestino,
                            List<LatLng> puntoCercanoATomarElBus, List<LatLng> puntoCercanoBajarElBus,OnOklineaSeleccionada onOk ){


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.AlertDialogCustom) );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View convertView = (View) inflater.inflate(R.layout.activity_lineas_rutas_disponibles, null);

        alertDialog.setView(convertView);

        ListView listViewdisOrigenRuta = (ListView) convertView.findViewById(R.id.id_origenRuta);
        ArrayAdapter<Double> adapterOrigenRuta = new ArrayAdapter<Double>(context,android.R.layout.simple_list_item_1,disOrigenRuta);
        listViewdisOrigenRuta.setAdapter(adapterOrigenRuta);

        ListView listViewdisRutaDestino = (ListView) convertView.findViewById(R.id.id_ubicacion);
        ArrayAdapter<Double> adapterRutaDestino = new ArrayAdapter<Double>(context,android.R.layout.simple_list_item_1,disRutaDestino);
        listViewdisRutaDestino.setAdapter(adapterRutaDestino);

        ListView listViewrutasDisponibles = (ListView) convertView.findViewById(R.id.id_lineas_buses_disco);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_single_choice,rutasDisponibles);
        listViewrutasDisponibles.setAdapter(adapter);


        listViewrutasDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listViewrutasDisponibles.getItemAtPosition(position);

                for (int i = 0; i < rutasTodas.size(); i++) {
                    if (listItem.equals(rutasTodas.get(i))){
                        onOk.seleccionada(i,puntoCercanoATomarElBus.get(position),puntoCercanoBajarElBus.get(position));
                        break;
                    }
                }
                Toast.makeText(context, "Selecciono La Linea " + listItem, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        listViewrutasDisponibles.setChoiceMode(listViewrutasDisponibles.CHOICE_MODE_SINGLE);

        dialog = alertDialog.show();

    }


    public void infoMaps( Context context ){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View convertView = (View) inflater.inflate(R.layout.activity_card_helpinfo, null);

        CheckBox mCheckBox = convertView.findViewById(R.id.checkRecordarInfo);
        alertDialog.setView(convertView);

        alertDialog.setPositiveButton("Volver", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog mDialog = alertDialog.create();
        mDialog.show();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    storeDialogStatus(true,context);
                }else{
                    storeDialogStatus(false,context);
                }
            }
        });

        if(getDialogStatus(context)){
            mDialog.hide();
        }else{
            mDialog.show();
        }

//        dialog = alertDialog.show();

    }

    private void storeDialogStatus(boolean isChecked, Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("CheckItem", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("CheckItem", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
    }


    public void infoMapsForever( Context context ){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.AlertDialogCustom) );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View convertView = (View) inflater.inflate(R.layout.activity_card_helpinfo, null);

        alertDialog.setView(convertView);

        CheckBox mCheckBox = convertView.findViewById(R.id.checkRecordarInfo);
        alertDialog.setView(convertView);
        mCheckBox.setVisibility(View.INVISIBLE);

        alertDialog.setPositiveButton("Volver", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        dialog = alertDialog.show();

    }
}
