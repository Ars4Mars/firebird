package com.revolve44.firebird5;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mushtaq on 05-11-2018.
 */

public class WeatherForecastResponse {
    @SerializedName("cod")
    public int cod;
    @SerializedName("message")
    public String message;
    @SerializedName("cnt")
    public int cnt;
    @SerializedName("list")
    public ArrayList<WeatherResponse> list = new ArrayList<WeatherResponse>();
}