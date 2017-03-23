
package com.example.user.picture.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Main implements Serializable {

    @SerializedName("temp")
    @Expose
    private Float temp;
    @SerializedName("pressure")
    @Expose
    private Integer pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("temp_min")
    @Expose
    private Float tempMin;
    @SerializedName("temp_max")
    @Expose
    private Float tempMax;

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Float getTempMin() {
        return tempMin;
    }

    public void setTempMin(Float tempMin) {
        this.tempMin = tempMin;
    }

    public Float getTempMax() {
        return tempMax;
    }

    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }

}
