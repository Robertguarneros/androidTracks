package dsa.upc.edu.listapp.tracks;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface TracksService {

    String URL = "http://10.0.2.2:8080/dsaApp/";

    @GET("tracks")
    Call<List<Song>> songs();
    @GET("tracks/{id}")
    Call<Song> songById(@Path("id") String id);
    @DELETE("tracks/{id}")
    Call<Void> deleteSong(@Path("id") String id);
    @POST("tracks")
    Call<Song> createSong(@Body Song song);
    @PUT("tracks")
    Call<Void> updateSong(@Body Song song);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
