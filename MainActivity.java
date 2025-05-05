package com.example.api;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button buttonload;
    TextView tvname,tvmobile,tvemail,tvaddress;
    ProgressBar progressBar;

    // AndroidManifest এ usesCleartextTraffic="false" রাখো যাতে অন্য সব HTTP কানেকশন ব্লক থাকে।
    //android:usesCleartextTraffic="true" এই লাইনটি Android অ্যাপের HTTP (unencrypted/plain text) নেটওয়ার্ক ট্রাফিক ব্যবহার করতে পারবে।
    //আপনি পুরানো API বা সার্ভিস ব্যবহার করছেন যেগুলো এখনো HTTPS সাপোর্ট করে না।
    //অ্যাপের সিকিউরিটি কমে যায়,Google Play Store থেকেও অ্যাপ রিজেক্ট হতে পারে যদি নিরাপত্তা নীতিমালা লঙ্ঘন হয়।
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        buttonload =findViewById(R.id.buttonload);
        tvname =findViewById(R.id.tvname);
        tvmobile =findViewById(R.id.tvmobile);
        tvemail =findViewById(R.id.tvemail);
        tvaddress=findViewById(R.id.tvaddress);
        progressBar=findViewById(R.id.progressBar);

        buttonload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(VISIBLE);

                //Online থেকে এবার Volley লাইব্রেরির কোডটা কপি-পেস্ট করলাম
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "https://nubsoft.xyz/NewFile.json";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //ঠিক মত কাজ করলে/Data load হলে progress bar gone হবে .
                                progressBar.setVisibility(GONE);
                                //Log.d এর কাজ হচ্ছে Developer দের জন্য । Server এর response গুলো log cat এ দেখা যাবে ।
                                Log.d("serverRes",response);
                                // সার্ভার থেকে আসা (response)/Data গুলোকে JSONObject এ কনভার্ট করবে
                                //JSONObject myvariable= new JSONObject(response); myobject নামে variable ধরলাম
                                // এখন JSONObject(response); এখানে error show করবে Red Bulb এ click করে Surround With try/Catch ব্যবহার করবো ।
                                // try এর ভিতরে যদি কোন error হয় তখন catch কল হয় আর Exception কাজ করে এর ফলে অ্যাপ ক্রাশ করে না।Unfortunately close হয় না ।
                                try {
                                    JSONObject myvariable= new JSONObject(response);

                                    //site থেকে string data  গুলো collect করতেছে

                                    String name = myvariable.getString("name");
                                    String mobile = myvariable.getString("mobile");
                                    String email = myvariable.getString("email");
                                    String address = myvariable.getString("address");

                                    //এবার text এর যায়গায় data set হবে । Load data তে Click করলে বুজবে ।

                                    tvname.setText(name);
                                    tvmobile.setText(mobile);
                                    tvemail.setText(email);
                                    tvaddress.setText(address);



                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        buttonload.setText("Volley didn't work!");
                        progressBar.setVisibility(GONE);
                    }
                });
// Add the request to the RequestQueue.
                queue.add(stringRequest);


            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
