package com.ismet.durt.services;

//import com.facebook.stetho.okhttp3.StethoInterceptor;

import com.ismet.durt.models.responses.GenericResponse;
import com.ismet.durt.models.responses.LoginResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ismet on 28/01/16.
 */
public class DurtService {

    public static Logger logger = Logger.getLogger(DurtService.class.getSimpleName());

    public interface Services {

        @FormUrlEncoded
        @POST("login")
        Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

        @FormUrlEncoded
        @POST("signup")
        Call<GenericResponse> signup(@Field("name") String name, @Field("username") String username, @Field("email") String email, @Field("password") String password);
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String body = buffer.readUtf8();

            request = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build();

            logger.info(String.format("Sending request %s on %s%n%s",
                    request.url(), body, request.headers()));

            okhttp3.Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            logger.info(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.code()));

            return response;
        }
    }

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
//            .addInterceptor(new StethoInterceptor())
            .connectTimeout(1, TimeUnit.MINUTES)
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9000/")
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .build();

    public final Services ROUTES = retrofit.create(Services.class);
}
