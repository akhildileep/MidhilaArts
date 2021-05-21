package com.wisewolf.midhilaarts;



import com.wisewolf.midhilaarts.Model.LoginMod;
import com.wisewolf.midhilaarts.Model.NumChk;
import com.wisewolf.midhilaarts.Model.Pack;
import com.wisewolf.midhilaarts.Model.PlanTypeMod;
import com.wisewolf.midhilaarts.Model.Transactions;
import com.wisewolf.midhilaarts.qa_model.QAModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class RetrofitClientInstance {
    private static Retrofit retrofit, retrofitOut;
    private static final String BASE_URL = "https://us-central1-midhila-art.cloudfunctions.net/";

    public static Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }



    public interface GetDataService {


        @GET("/app/api//product")
        Call<List<Pack>> getPackage();

        @GET("/app/api//gallery")
        Call<List<Gallery>> getGallery();

        @GET
        Call<List<Transactions>> getTransaction(@Url String link);



        @GET
        Call<List<QAModel>> getQAList(@Url String message);

        @GET
        Call<List<LoginMod>> loginCheck(@Url String message);

        @GET
        Call<List<PlanTypeMod>> planType(@Url String message);

        @GET
        Call<ArrayList> checkNum(@Url String message);

    }

}
