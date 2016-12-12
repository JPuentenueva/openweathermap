package psp.weatherdam.retrofit;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.parceler.Parcel;

import java.io.IOException;

import psp.weatherdam.interfaces.GoogleMapsAPI;
import psp.weatherdam.interfaces.OpenWeatherAPI;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Jose on 08/12/2016.
 */

@Parcel
public class RetrofitApplication extends Application {
    private final static int GOOGLE_AUTOCOMP = 1;
    private final static int GOOGLE_DETAIL = 2;
    private final static int OPENWEATHER = 3;
    private static Retrofit retrofitGoogleAuto, retrofitGoogleDetail, retrofitOpenWeather;

    public boolean iniciarRetrofit(){
        Gson gson = new GsonBuilder()
                .create();

        retrofitGoogleAuto = new Retrofit.Builder()
                .baseUrl(GoogleMapsAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(initHttp(GOOGLE_AUTOCOMP))
                .build();

        retrofitGoogleDetail = new Retrofit.Builder()
                .baseUrl(GoogleMapsAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(initHttp(GOOGLE_DETAIL))
                .build();

        retrofitOpenWeather = new Retrofit.Builder()
                .baseUrl(OpenWeatherAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(initHttp(OPENWEATHER))
                .build();

        return true;
    }

    private static OkHttpClient initHttp(final int tipoServicio) {

        Interceptor interceptor = new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {

                Request original = chain.request();

                HttpUrl originalHttpUrl = original.httpUrl();
                HttpUrl url = null;

                switch (tipoServicio) {
                    case GOOGLE_AUTOCOMP:
                        url = originalHttpUrl.newBuilder()
                                .addQueryParameter("location", "37.380413,-6.007445")
                                .addQueryParameter("radius", "50000")
                                .addQueryParameter("types", "(cities)")
                                .addQueryParameter("language", "es_ES")
                                .addQueryParameter("key", "AIzaSyBwZ60_83Uo8Ry1ohKeI4nEHDJq22GeY5c")
                                .build();
                        break;
                    case GOOGLE_DETAIL:
                        url = originalHttpUrl.newBuilder()
                                .addQueryParameter("language", "es_ES")
                                .addQueryParameter("key", "AIzaSyBwZ60_83Uo8Ry1ohKeI4nEHDJq22GeY5c")
                                .build();
                        break;
                    case OPENWEATHER:
                        url = originalHttpUrl.newBuilder()
                                .addQueryParameter("units","metric")
                                .addQueryParameter("lang","es")
                                .addQueryParameter("appid", "80da6c8a5383f2c64dde94f5990e8a93")
                                .build();
                        break;
                    default:
                        break;
                }

                Request newRequest = chain.request().newBuilder()
                        .url(url)
                        .build();


                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interceptor);

        return client;

    }

    public static Retrofit getRetrofitGoogleAuto() { return retrofitGoogleAuto; }

    public static Retrofit getRetrofitGoogleDetail() { return retrofitGoogleDetail; }

    public static Retrofit getRetrofitOpenWeather() { return retrofitOpenWeather; }
}
