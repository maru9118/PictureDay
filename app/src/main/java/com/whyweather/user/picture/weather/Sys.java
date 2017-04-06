
package com.whyweather.user.picture.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Sys implements Serializable {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private long sunrise;
    @SerializedName("sunset")
    @Expose
    private long sunset;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSunrise() {
        return sunRiseFormat(sunrise);
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunSetFormat(sunset);
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public String sunRiseFormat(long sunrise) {
        // 일출
        SimpleDateFormat sunRise = new SimpleDateFormat("hh:mm", Locale.KOREA);
        sunRise.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        return sunRise.format(sunrise * 1000L);
    }

    public String sunSetFormat(long sunset) {
        // 일몰
        java.text.SimpleDateFormat sunSet = new java.text.SimpleDateFormat("kk:mm", Locale.KOREA);
        sunSet.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        return sunSet.format(sunset * 1000L);
    }

}
