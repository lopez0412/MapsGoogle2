package com.lopez.mapsgoogle;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    LinearLayout searchPanel;
    Button searchButton;
    EditText searchText;
    double lat= 0.0, lng= 0.0;
    String marcador;
    ListView lista;
    List<String> ciudades=new ArrayList<String>() {
    };

    Geocoder geoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        searchPanel = (LinearLayout) findViewById(R.id.searchPanel);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchText = (EditText) findViewById(R.id.searchText);
        lista=(ListView)findViewById(R.id.listaresult);
        mapFragment.getMapAsync(this);
        geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

        searchButton.setOnClickListener(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


       /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    @Override
    public void onClick(View v) {
        String searchFor = searchText.getText().toString();

        String ciudad,pais,localidad;
        try {
            ciudades.clear();
            final List<Address> myList = geoCoder.getFromLocationName(searchFor,5);
            if (myList.size() > 0) {
                for (int i=0;i<myList.size();i++){


                    localidad=myList.get(i).getAdminArea();
                    pais=myList.get(i).getCountryName();
                    ciudad=myList.get(i).getFeatureName();
                    ciudades.add(ciudad+", "+localidad+", "+pais);
                }

                lista.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,ciudades));
                lista.setVisibility(View.VISIBLE);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mMap.clear();
                       lat= myList.get(position).getLatitude();
                       lng= myList.get(position).getLongitude();
                        marcador=ciudades.get(position);
                        LatLng ciudad = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions().position(ciudad).title(marcador));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ciudad,8));

                        lista.setVisibility(View.INVISIBLE);
                    }
                });




            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

