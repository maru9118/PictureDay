package com.whyweather.user.picture;

import android.os.Parcelable;

import java.io.Serializable;
import java.security.Policy;

/**
 * Created by user on 2017-04-28.
 */

public class History implements Serializable {

    private String address;

    public History(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "History{" +
                "address='" + address + '\'' +
                '}';
    }
}
