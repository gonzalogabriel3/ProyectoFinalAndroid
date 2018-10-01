package com.example.gonzalo.proyectofinalandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class activity_conexion extends AppCompatActivity {

    private static final String TAG = activity_conexion.class.getName();
    private Button btnRequest;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "http://dondeestaelcole.ddns.net:8080/parada/1435";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion);

        btnRequest = (Button) findViewById(R.id.buttonRequest);

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                sendAndRequestResponse();
            }
            }
        );
    }

    private void sendAndRequestResponse() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
