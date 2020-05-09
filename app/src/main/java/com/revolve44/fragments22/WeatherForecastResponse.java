package com.revolve44.fragments22;


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
    public ArrayList<WeatherResponse> list = new ArrayList<>();
    @SerializedName("dt")
    public int dt;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("all")
    public int all;
//    @SerializedName("cod")
//    public int cod;
//    @SerializedName("message")
//    public String message;
//    @SerializedName("cnt")
//    public int cnt;
//    @SerializedName("list")
//    public ArrayList<WeatherResponse> list = new ArrayList<WeatherResponse>();
}