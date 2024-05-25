package app.ij.mlwithtensorflowlite.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;

import app.ij.mlwithtensorflowlite.BirdAdapter;
import app.ij.mlwithtensorflowlite.R;
import app.ij.mlwithtensorflowlite.data.Bird;
import app.ij.mlwithtensorflowlite.data.BirdApi;
import app.ij.mlwithtensorflowlite.data.Birds;
import app.ij.mlwithtensorflowlite.data.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment {
     EditText searchText;
     Button searchButton;
     RecyclerView birdRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        searchText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        birdRecyclerView = view.findViewById(R.id.birdrecycler);
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        BirdApi birdApi = retrofit.create(BirdApi.class);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birdApi.getBirds(RetrofitClient.apiKey,searchText.getText().toString(),"true","AND","1","25").enqueue(new Callback<Birds>() {
                    @Override
                    public void onResponse(Call<Birds> call, Response<Birds> response) {
                        if(response.isSuccessful()){
                            Birds birds = response.body();
                            BirdAdapter birdAdapter = new BirdAdapter(birds.getEntities(),requireContext(),false);
                            birdRecyclerView.setAdapter(birdAdapter);
                            birdRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            Log.e("url",call.request().url().toString());
                        }
                        else {
                            // Log the response code and error message
                            Log.e("Error", "Response code: " + response.code() + ", Error message: " + response.message());

                            // Log the raw response body
                            try {
                                Log.e("Error", "Raw response body: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Birds> call, Throwable throwable) {
                        Log.e("error",throwable.getMessage());
                    }
                });
            }
        });
        return view;
    }
}