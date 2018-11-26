package com.example.gonzalo.proyectofinalandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class activity_horario extends AppCompatActivity {
    String URL="http://dondeestaelcole.ddns.net:8080";
    private ListView lv1;
    String[] horarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        int recorrido = getIntent().getIntExtra("recorrido",0);

        lv1=findViewById(R.id.lv1);

        //URL del servidor
        String url=URL+"/horariotramo/"+recorrido;

        //RequestQueue initialized
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonObjectRequest Request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null ,
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            ArrayList<String> horarioArrayList = new ArrayList<String>();
                            horarioArrayList.add("|  Salida     |  Llegada     |  Dias                         |");

                            JSONArray jsonarray = response.getJSONArray("horarios");

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject object = jsonarray.getJSONObject(i);
                                String salida = object.getString("salida");
                                String llegada = object.getString("llegada");
                                String dias= object.getString("dias");
                                horarioArrayList.add("|  "+salida+"      |  "+llegada+"         |  "+dias+"  |" );

                            }

                            horarios=horarioArrayList.toArray(new String[horarioArrayList.size()]);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, horarios);
                            lv1.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //añadimos el request a la cola
        mRequestQueue.add(Request);




    }
    public void finalizar(View v){
        finish();
    }
}
