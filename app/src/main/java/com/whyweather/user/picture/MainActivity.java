//package com.whyweather.user.picture;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.whyweather.user.picture.forecast.Forecast;
//import com.whyweather.user.picture.weather.WeatherMain;
//
//import java.io.IOException;
//import java.text.MessageFormat;
//import java.util.List;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class NewMainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationButtonClickListener {
//
//    private GoogleMap mMap;
//    private WeatherApi mApi;
//    private Geocoder mGeocoder;
//    private List<Address> mList;
//    private Marker mMarker;
//    private double mLat;
//    private double mLog;
//    private WeatherMain mWeatherData;
//    private float mZoomLevel = 13f;
//    private GoogleApiClient mGoogleApiClient;
//    private final int REQUEST_CODE_PERMISSONS = 1000;
//    private final int SEND_SMS_CODE = 1001;
//    private Location mLastLocation;
//    private String mSunRise;
//    private String mSunSet;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        Retrofit mRetrofit = new Retrofit.Builder()
//                .baseUrl(WeatherApi.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        mApi = mRetrofit.create(WeatherApi.class);
//        mGeocoder = new Geocoder(this);
//
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_weather, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.menu_serch);
//
//        final SearchView searchView = (SearchView)
//                MenuItemCompat.getActionView(searchItem);
//
//        searchView.setQueryHint("도시를 입력하세요.");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                citySearch(query);
//
//                if (mList.size() == 0) {
//                    Toast.makeText(MainActivity.this, "재입력 하세요.", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    final double lat = mList.get(0).getLatitude();
//                    final double lon = mList.get(0).getLongitude();
//
//                    weatherData(lat, lon);
//
//                    // 키보드 숨기기
//                    InputMethodManager hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    hide.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
//                }
//                return true;
//            }
//
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.d("MainActivity", "onQueryTextChange: " +
//                        newText);
//                return false;
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_delet:
//                mMarker.remove();
//                mWeatherData = null;
//                break;
//
//            case R.id.menu_share:
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_PERMISSONS);
//                }
//                phoneNumBerPick(mLat, mLog);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//            mMap.setOnMyLocationButtonClickListener(this);
//        }
//        mMap.setOnMapLongClickListener(this);
//
//        if (mWeatherData != null) {
//            double lat = mWeatherData.getCoord().getLat();
//            double lon = mWeatherData.getCoord().getLon();
//            getForecast(lat, lon);
//        }
//    }
//
//
//    @Override
//    public void onMapLongClick(final LatLng latLng) {
//        mLat = latLng.latitude;
//        mLog = latLng.longitude;
//
//        weatherData(mLat, mLog);
//    }
//
//    public void citySearch(String city) {
//
//        try {
//            mList = mGeocoder.getFromLocationName(
//                    city, // 지역 이름
//                    10); // 읽을 개수
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void weatherData(final double lat, final double lon) {
//
//        Call<WeatherMain> data = mApi.getWeather(WeatherApi.API_KEY, lat, lon);
//        data.enqueue(new Callback<WeatherMain>() {
//            @Override
//            public void onResponse(Call<WeatherMain> call, Response<WeatherMain> response) {
//                mWeatherData = response.body();
//                response.isSuccessful();
//                response.code();
//
//                if (mMarker != null) {
//                    mMarker.remove();
//                }
//
//                getForecast(lat, lon);
//            }
//
//            @Override
//            public void onFailure(Call<WeatherMain> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "실패2", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void getForecast(double lat, double lon) {
//        LatLng latLng = new LatLng(lat, lon);
//
//        mZoomLevel = mMap.getCameraPosition().zoom;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mZoomLevel));
//
//        mSunRise = mWeatherData.getSys().getSunrise();
//        mSunSet = mWeatherData.getSys().getSunset();
//
//        mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
//                .title(mSunRise + "→" + mSunSet));
//
//        mMarker.showInfoWindow();
//        mMarker.hideInfoWindow();
//
//        Call<Forecast> data = mApi.getForecast(WeatherApi.API_KEY, lat, lon);
//        data.enqueue(new Callback<Forecast>() {
//            @Override
//            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
//                final Forecast forecastData = response.body();
//
//                GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
//                    @Override
//                    public void onInfoWindowClick(Marker marker) {
//                        Intent intent = new Intent(MainActivity.this, NowActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.putExtra("data", mWeatherData);
//                        intent.putExtra("forecast", forecastData);
//
//                        startActivity(intent);
//                    }
//                };
//                mMap.setOnInfoWindowClickListener(infoWindowClickListener);
//            }
//
//            @Override
//            public void onFailure(Call<Forecast> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        // 상태 저장
//        outState.putSerializable("data", mWeatherData);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        mWeatherData = (WeatherMain) savedInstanceState.getSerializable("data");
//
//    }
//
//    @Override
//    protected void onStart() {
//        mGoogleApiClient.connect();
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        mGoogleApiClient.disconnect();
//        super.onStop();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSONS);
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//
//            mLat = mLastLocation.getLatitude();
//            mLog = mLastLocation.getLongitude();
//
//            LatLng startingPoint = new LatLng(mLat, mLog);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 13));
//
//            weatherData(mLat, mLog);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case REQUEST_CODE_PERMISSONS:
//                if (ActivityCompat.checkSelfPermission(this,
//                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
//                        PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(this,
//                                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
//                                PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "권한 체크 거부 됨",
//                            Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                return;
//
//            case SEND_SMS_CODE:
//                if (ActivityCompat.checkSelfPermission(this,
//                        Manifest.permission.SEND_SMS) !=
//                        PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "권한 체크 거부 됨", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                return;
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public boolean onMyLocationButtonClick() {
//        if (mMarker != null) {
//            mMarker.remove();
//        }
//
//        mLat = mLastLocation.getLatitude();
//        mLog = mLastLocation.getLongitude();
//
//        weatherData(mLat, mLog);
//
//        return false;
//    }
//
//    public String getAddress(double lat, double lng) {
//        String adderss = null;
//
//        mGeocoder = new Geocoder(this, Locale.KOREA);
//
//        List<Address> list = null;
//
//        try {
//            list = mGeocoder.getFromLocation(lat, lng, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (list == null) {
//            return null;
//        }
//
//        if (list.size() > 0) {
//            Address addr = list.get(0);
//            adderss = addr.getCountryName() + " "
//                    + addr.getAdminArea() + " "
//                    + addr.getLocality() + " "
//                    + addr.getSubLocality() + " "
//                    + addr.getThoroughfare() + " "
//                    + addr.getSubThoroughfare();
//        }
//        return adderss;
//    }
//
//    public void phoneNumBerPick(final double lat, final double log) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View view = LayoutInflater.from(this).inflate(R.layout.dialog_number, null, false);
//        TextView numText = (TextView) view.findViewById(R.id.sms_text);
//        numText.setText(getAddress(lat, log) + " 의 정보를 공유하시겠습니까?");
//        final EditText numEditText = (EditText) view.findViewById(R.id.num_edit);
//        builder.setView(view);
//        builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String phhoneNumber = numEditText.getText().toString();
//                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phhoneNumber));
//                intent.putExtra("sms_body", messageFormat());
//                startActivity(intent);
//            }
//
//            public String messageFormat() {
//                final String patten = "일출 : {0}, 일몰 : {1}\n지도에서 보기 : http://maps.google.com/maps?q={2},{3}";
//                return MessageFormat.format(patten, new Object[]{mSunRise, mSunSet, lat, log});
//            }
//
//        });
//        builder.setNegativeButton("취소", null);
//        builder.show();
//    }
//}