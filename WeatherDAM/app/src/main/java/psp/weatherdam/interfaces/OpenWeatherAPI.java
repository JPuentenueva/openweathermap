package psp.weatherdam.interfaces;

import psp.weatherdam.pojo.openweather.BusquedaOpenWeather;
import psp.weatherdam.pojo.openweatherforecast.BusquedaOpenWeatherForecast;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by jmpuentenueva on 07/12/2016.
 */

public interface OpenWeatherAPI {
    String ENDPOINT = "http://api.openweathermap.org/";

    //Coordenadas de Salesianos de Triana
    @GET("data/2.5/weather")
    Call<BusquedaOpenWeather> getBusquedaOpenWeather(@Query("lat") Double lat, @Query("lon") Double lon);

    @GET("data/2.5/forecast")
    Call<BusquedaOpenWeatherForecast> getBusquedaOpenWeatherForecast(@Query("lat") Double lat, @Query("lon") Double lon);
}
