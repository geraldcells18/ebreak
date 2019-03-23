package com.example.admin.ebreak;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import me.rishabhkhanna.customtogglebutton.CustomToggleButton;
import spencerstudios.com.bungeelib.Bungee;

public class Main4Activity extends AppCompatActivity {

    String IP = "";
    String updated_relayStat_link = "";

    String r1, r2;

    CustomToggleButton customToggleButton1, customToggleButton2;

    void getIPVal(){
        IP = getResources().getString(R.string.ip_add);
        updated_relayStat_link = IP + "/main/last_relay.php";
    }

    private void getSwitchStat() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);

        asyncHttpClient.get(updated_relayStat_link, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String json_data = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(json_data);

                    r1 = jsonObject.get("Relay1").toString();
                    r2 = jsonObject.get("Relay2").toString();

                    r1 = r1.trim();
                    r2 = r2.trim();

                    if (r1.equals("1")) {
                        customToggleButton1.setChecked(true);
                    }
                    if (r1.equals("0")) {
                        customToggleButton1.setChecked(false);
                    }
                    if (r2.equals("1")) {
                        customToggleButton2.setChecked(true);
                    }
                    if (r2.equals("0")) {
                        customToggleButton2.setChecked(false);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void checkRelayButtonStat() {
        if (customToggleButton1.isChecked()) {
            r1 = "1";
        }
        if (!customToggleButton1.isChecked()) {
            r1 = "0";
        }
        if (customToggleButton2.isChecked()) {
            r2 = "1";
        }
        if (!customToggleButton2.isChecked()) {
            r2 = "0";
        }
    }

    private void MessageBox(String message, String title, Context context, final int btn) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle(title);
        ab.setMessage(message);
        ab.setCancelable(false);
        ab.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        checkRelayButtonStat();
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        asyncHttpClient.setTimeout(1500);
                        asyncHttpClient.get(IP + "/main/relay.php?relay1=" + r1 + "&relay2=" + r2, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                if (btn == 1) {
                                    if (r1.equals("1")) {
                                        Toasty.info(Main4Activity.this, "Lightning Outlet Switch On", Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.info(Main4Activity.this, "Lightning Outlet Switch Off", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                                if (btn == 2) {
                                    if (r2.equals("1")) {
                                        Toasty.info(Main4Activity.this, "Wall Outlet Switch On", Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.info(Main4Activity.this, "Wall Outlet Switch Off", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });

                        dialog.cancel();
                    }
                });

        ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                if (btn == 1) {
                    if (r1.equals("1")) {
                        customToggleButton1.setChecked(true);
                    }
                    if (r1.equals("0")) {
                        customToggleButton1.setChecked(false);
                    }
                }
                if (btn == 2) {
                    if (r1.equals("1")) {
                        customToggleButton2.setChecked(true);
                    }
                    if (r1.equals("0")) {
                        customToggleButton2.setChecked(false);
                    }
                }
            }
        });

        AlertDialog ad = ab.create();
        ad.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        getIPVal();

        customToggleButton1 = (CustomToggleButton) findViewById(R.id.relay1);
        customToggleButton2 = (CustomToggleButton) findViewById(R.id.relay2);

        customToggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBox("Would you like to switch " + customToggleButton1.getText().toString() + " Lightning outlet?", getResources().getString(R.string.controller), Main4Activity.this, 1);
            }
        });

        customToggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBox("Would you like to switch " + customToggleButton2.getText().toString() + " Wall outlet?", getResources().getString(R.string.controller), Main4Activity.this, 2);
            }
        });
        checkRelayButtonStat();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Main4Activity.this, Main3Activity.class));
        finish();
        Bungee.slideRight(Main4Activity.this);
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        getSwitchStat();
        super.onResume();
    }

    @Override
    public void onStart() {
        getSwitchStat();
        super.onStart();
    }
}
