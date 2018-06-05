package com.mehedicr.tourmate;

import com.mehedicr.tourmate.Model.CurrentWeatherResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherAPI {
    @GET
    Call<CurrentWeatherResponse> getCurrentWeatherData(@Url String urlString);
}
