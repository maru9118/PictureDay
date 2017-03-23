package com.example.user.picture;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.picture.Weather.WeatherMain;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private WeatherApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(WeatherApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApi = mRetrofit.create(WeatherApi.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_weather, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_serch:
                Toast.makeText(this, "설정 미구현", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delet:
                Toast.makeText(this, "설정 미구현2", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
    }


    @Override
    public void onMapLongClick(final LatLng latLng) {
        final double lat = latLng.latitude;
        final double lng = latLng.longitude;

        Call<WeatherMain> data = mApi.getData(WeatherApi.API_KEY, lat, lng);
        data.enqueue(new Callback<WeatherMain>() {
            @Override
            public void onResponse(Call<WeatherMain> call, Response<WeatherMain> response) {
                final WeatherMain result = response.body();

                // 일출
                SimpleDateFormat sunRise = new SimpleDateFormat("hh:mm", Locale.KOREA);
                sunRise.setTimeZone(TimeZone.getTimeZone("UTC"));

                // 일몰
                SimpleDateFormat sunSet = new SimpleDateFormat("kk:mm", Locale.KOREA);
                sunSet.setTimeZone(TimeZone.getTimeZone("UTC"));

                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("" + sunRise.format(result.getSys().getSunrise() * 1000L)
                                + "→" + sunSet.format(result.getSys().getSunset() * 1000L)));

                marker.showInfoWindow();
                marker.hideInfoWindow();
//
//                GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
//                    @Override
//                    public void onInfoWindowClick(Marker marker) {
//                        Intent intent = new Intent(MainActivity.this, NowActivity.class);
////                        intent.putExtra("data", result);
//
//                        startActivity(intent);
//                    }
//                };
//                mMap.setOnInfoWindowClickListener(infoWindowClickListener);

            }

            @Override
            public void onFailure(Call<WeatherMain> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
