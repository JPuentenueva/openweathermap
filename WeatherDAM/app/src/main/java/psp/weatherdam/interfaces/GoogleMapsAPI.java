package psp.weatherdam.interfaces;

import psp.weatherdam.pojo.googlemapsautocomp.BusquedaMapsAutocomp;
import psp.weatherdam.pojo.googlemapsdetail.BusquedaMapsDetail;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by jmpuentenueva on 07/12/2016.
 */

public interface GoogleMapsAPI {
    String ENDPOINT = "https://maps.googleapis.com/";

    @GET("maps/api/place/autocomplete/json")
    Call<BusquedaMapsAutocomp> getMapsAutocomp(@Query("input") String input);

    @GET("maps/api/place/details/json")
    Call<BusquedaMapsDetail> getMapsDetail(@Query("placeid") String placeid);
}
