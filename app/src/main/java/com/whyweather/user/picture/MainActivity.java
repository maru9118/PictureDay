package com.whyweather.user.picture;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.whyweather.user.picture.forecast.Forecast;
import com.whyweather.user.picture.weather.WeatherMain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private WeatherApi mApi;
    private Geocoder mGeocoder;
    private List<Address> mList;
    private Marker mMarker;
    private double mLat;
    private double mLng;
    private WeatherMain mWeatherData;
    private GoogleApiClient mGoogleApiClient;

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
        mGeocoder = new Geocoder(this);

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainActivity.this, "동의 됨", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한이 거절 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_weather, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_serch);

        final SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint("도시를 입력하세요.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                citySearch(query);

                if (mList.size() == 0) {
                    Toast.makeText(MainActivity.this, "재입력 하세요.", Toast.LENGTH_SHORT).show();
                } else {

                    final double lat = mList.get(0).getLatitude();
                    final double lon = mList.get(0).getLongitude();

                    LatLng latLng = new LatLng(lat, lon);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                    weatherData(lat, lon);

                    // 키보드 숨기기
                    InputMethodManager hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    hide.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("MainActivity", "onQueryTextChange: " +
                        newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delet:
                mMarker.remove();
                mWeatherData = null;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);
        if (mWeatherData == null) {

            // 기존 위치 고정
            LatLng startingPoint = new LatLng(37.56, 126.97);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 13));

// if (mGoogleApiClient == null) {
//                mGoogleApiClient = new GoogleApiClient.Builder(this)
//                        .addConnectionCallbacks(this)
//                        .addOnConnectionFailedListener(this)
//            }

        } else {

            double lat = mWeatherData.getCoord().getLat();
            double lon = mWeatherData.getCoord().getLon();

            getForecast(lat, lon);
        }
    }


    @Override
    public void onMapLongClick(final LatLng latLng) {
        mLat = latLng.latitude;
        mLng = latLng.longitude;

        weatherData(mLat, mLng);
    }

    public void citySearch(String city) {

        try {
            mList = mGeocoder.getFromLocationName(
                    city, // 지역 이름
                    10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }

        if (mList != null) {
            if (mList.size() == 0) {
            }
        }
    }

    public void weatherData(final double lat, final double lon) {

        Call<WeatherMain> data = mApi.getWeather(WeatherApi.API_KEY, lat, lon);
        data.enqueue(new Callback<WeatherMain>() {
            @Override
            public void onResponse(Call<WeatherMain> call, Response<WeatherMain> response) {
                mWeatherData = response.body();
                response.isSuccessful();
                response.code();

                if (mMarker != null) {
                    mMarker.remove();
                }

                getForecast(lat, lon);
            }

            @Override
            public void onFailure(Call<WeatherMain> call, Throwable t) {
                Toast.makeText(MainActivity.this, "실패2", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getForecast(double lat, double lon) {
        LatLng latLng = new LatLng(lat, lon);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));

        // 일출
        java.text.SimpleDateFormat sunRise = new java.text.SimpleDateFormat("hh:mm", Locale.KOREA);
        sunRise.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        // 일몰
        java.text.SimpleDateFormat sunSet = new java.text.SimpleDateFormat("kk:mm", Locale.KOREA);
        sunSet.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title("" + sunRise.format(mWeatherData.getSys().getSunrise() * 1000L)
                        + "→" + sunSet.format(mWeatherData.getSys().getSunset() * 1000L)));

        mMarker.showInfoWindow();
        mMarker.hideInfoWindow();

        Call<Forecast> data = mApi.getForecast(WeatherApi.API_KEY, lat, lon);
        data.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                final Forecast forecastData = response.body();

                GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(MainActivity.this, NowActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("data", mWeatherData);
                        intent.putExtra("forecast", forecastData);

                        startActivity(intent);
                    }
                };
                mMap.setOnInfoWindowClickListener(infoWindowClickListener);
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // 상태 저장
        outState.putSerializable("data", mWeatherData);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mWeatherData = (WeatherMain) savedInstanceState.getSerializable("data");

    }
}