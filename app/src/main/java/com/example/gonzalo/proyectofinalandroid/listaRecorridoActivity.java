package com.example.gonzalo.proyectofinalandroid;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class listaRecorridoActivity extends AppCompatActivity {

    private ListView lv1;
    public String[] idsRecorridosArray;
    public String[] nombresRecorridosArray;
    public int recorrido_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_recorrido);

        lv1=findViewById(R.id.lv1);


        ArrayList<String> idsRecorridos = (ArrayList<String>) getIntent().getSerializableExtra("idsRecorridos");
        ArrayList<String> nombresRecorridos = (ArrayList<String>) getIntent().getSerializableExtra("nombresRecorridos");


        idsRecorridosArray=idsRecorridos.toArray(new String[idsRecorridos.size()]);

        nombresRecorridosArray = nombresRecorridos.toArray(new String[nombresRecorridos.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nombresRecorridosArray);
        lv1.setAdapter(adapter);



        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                //Toast.makeText(getApplicationContext(),"Ha seleccionado recorrido con id "+idsRecorridosArray[posicion],Toast.LENGTH_LONG).show();
                String recorridoId=idsRecorridosArray[posicion];
                Intent returnIntent = new Intent();
                returnIntent.putExtra("recorridoId",recorridoId);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });



    }

    public void finalizar(View v){
        finish();
    }
}
