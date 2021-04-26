package com.example.demo_wifi_on_off;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button mbtnon,mbtnoff,mbtnlist;
    WifiManager wifiManager;
    TextView mtv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mbtnon=findViewById(R.id.btn0n);
        mbtnoff=findViewById(R.id.btnoff);
        mbtnlist=findViewById(R.id.getList);
        mtv1 = findViewById(R.id.tv1);

        mbtnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);

                Toast.makeText(MainActivity.this,"wifi on",Toast.LENGTH_LONG).show();
            }
        });

        mbtnoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);

                Toast.makeText(MainActivity.this,"wifi off",Toast.LENGTH_LONG).show();


            }
        });
        mbtnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS)!= PackageManager.PERMISSION_GRANTED)
               {
                   ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},123);

               }else{
                   wifiMethod();
               }
            }
        });
    }

    private void wifiMethod() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        getApplicationContext().registerReceiver(broadcastReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case 123:{
                if(grantResults.length >= 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    wifiMethod();
                }else{
                    Toast.makeText(MainActivity.this,"Allow The Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
              List<ScanResult> result =  wifiManager.getScanResults();
              StringBuffer sb = new StringBuffer();
              sb.append("Number of wifi list: -"+result.size());
              for(int i=0;i<result.size();i++){
                  sb.append("SSID:- "+result.get(i).SSID+"\n");
                  sb.append("BSSID:- "+result.get(i).BSSID+"\n");
              }
              mtv1.setText(sb.toString());
          }
        }
    };
}
