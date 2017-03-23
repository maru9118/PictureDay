package com.example.user.picture;

import com.example.user.picture.Weather.WeatherMain;
import com.example.user.picture.forecast.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 2017-03-22.
 */

public interface WeatherApi {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    String API_KEY = "0a66e9e9f3c2674e0f1bf8a15a627693";

    @GET("weather")
    Call<WeatherMain> getWeather(@Query("APPID") String appid,
                                 @Query("lat") double lat,
                                 @Query("lon") double lon);

    @GET("forecast")
    Call<Forecast> getForecast(@Query("APPID") String appid,
                               @Query("lat") double lat,
                               @Query("lon") double lon);

}
