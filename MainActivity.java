package com.example.admin.ebreak;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity {

    String IP = "";
    String server_link = "";

    ImageView logo;
    long backPress;

    void getIPVal(){
        IP = getResources().getString(R.string.ip_add);
        server_link = IP +"/main/last_kwh.php";
    }

    public void MessageBox(String message, String title, Context context) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setCancelable(false);
        ab.setPositiveButton(
                "Reconnect",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        checkIfConnected();
                    }
                });
        ab.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog ad = ab.create();
        ad.show();
    }

    public void checkIfConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork;
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.getActiveNetworkInfo();

            if (activeNetwork != null) {

                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    checkInternet();

                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    checkInternet();
                }
            } else {
                MessageBox("There was an error connecting to the server. \n\n Kindly check your internet connection.", "Unable to Connect", MainActivity.this);
            }

        }
    }

    public void checkInternet() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(1500);

        client.get(server_link, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                logo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toasty.warning(MainActivity.this, "Connected", Toast.LENGTH_SHORT, false).show();
                startActivity(new Intent(MainActivity.this, Main3Activity.class));

                finish();
                Bungee.fade(MainActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                logo.setVisibility(View.GONE);
                MessageBox("There was a problem connecting to the server. \n\n Check your internet connection.", "Connection Problem", MainActivity.this);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIPVal();

        logo = (ImageView) findViewById(R.id.img_logo);
        Glide.with(MainActivity.this).load(R.drawable.loaders).into(logo);
        checkIfConnected();

       /*startActivity(new Intent(getBaseContext(), Main2Activity.class));
       finish();*/
    }

    @Override
    public void onBackPressed() {
        if (backPress + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toasty.info(MainActivity.this, "Press back again to close", Toast.LENGTH_SHORT, true).show();
        }
        backPress = System.currentTimeMillis();
    }
}
