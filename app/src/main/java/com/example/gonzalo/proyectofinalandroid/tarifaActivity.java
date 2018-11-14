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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class tarifaActivity extends AppCompatActivity {

    String URL="http://dondeestaelcole.ddns.net:8080";
    private ListView lv1;
    String[] tarifas;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa);

        lv1=findViewById(R.id.lv1);

        String url=URL+"/tarifa";
        //RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            ArrayList<String> tarifasArrayList = new ArrayList<String>();


                            JSONArray jsonarray = response.getJSONArray("tarifas");

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                String nombre_tramo = object.getString("nombre");
                                String monto=object.getString("monto").toString();
                                tarifasArrayList.add(nombre_tramo+"/$"+monto);

                            }

                            tarifas=tarifasArrayList.toArray(new String[tarifasArrayList.size()]);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, tarifas);
                            lv1.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error: no se pueden mostrar las tarifas,intentelo nuevamente", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        mRequestQueue.add(Request);


    }

    public void finalizar(View v){
        finish();
    }
}
