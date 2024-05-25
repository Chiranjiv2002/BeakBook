package app.ij.mlwithtensorflowlite.data;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface BirdApi {
    @GET("/v2/birds")
    Call<Birds> getBirds(
            @Header("api-key") String apiKey,
            @Query("name") String name,
            @Query("hasImage") String hasImage,
            @Query("operator") String operator,
            @Query("page") String page,
            @Query("pageSize") String pageSize

    );
}
