package com.example.hikers_watch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitude,longitude,altitude,accuracy,address_text_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Toast.makeText(getApplicationContext(),"Getting location wait",Toast.LENGTH_SHORT).show();
                update_location_info(location);
            }
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    private void init()
    {
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        altitude=findViewById(R.id.altitude);
        accuracy=findViewById(R.id.accuracy);
        address_text_view=findViewById(R.id.address);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    public void update_location_info(Location location)
    {
        latitude.setText("Latitude: "+Double.toString(location.getLatitude()));
        longitude.setText("Longitude: "+Double.toString(+location.getLongitude()));
        altitude.setText("Altitude: "+Double.toString(location.getAltitude()));
        accuracy.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        String address="Address could not be found";
        Geocoder geocoder= new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addresses!=null && addresses.size()>0)
            {
                address="Address\n";
                /*if(addresses.get(0).getThoroughfare()!=null)
                {
                    address+=addresses.get(0).getThoroughfare()+"\n";
                }*/
                if(addresses.get(0).getAddressLine(0)!=null)
                {
                    address+=addresses.get(0).getAddressLine(0)+"\n";
                }
                if(addresses.get(0).getPostalCode()!=null)
                {
                    address+="Postal Code: "+addresses.get(0).getPostalCode();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        address_text_view.setText(address);
    }
}