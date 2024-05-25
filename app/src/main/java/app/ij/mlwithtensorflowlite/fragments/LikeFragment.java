package app.ij.mlwithtensorflowlite.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.ij.mlwithtensorflowlite.BirdAdapter;
import app.ij.mlwithtensorflowlite.R;
import app.ij.mlwithtensorflowlite.data.Bird;
import app.ij.mlwithtensorflowlite.data.BirdStorage;

public class LikeFragment extends Fragment {
    RecyclerView birdRecycler;
    TextView nobird;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_like, container, false);
        birdRecycler = view.findViewById(R.id.birdrecycler2);
        nobird = view.findViewById(R.id.nobirdliked);
        List<Bird> birdList = BirdStorage.getAllBirds(requireContext());
        if(birdList.isEmpty()){
            nobird.setVisibility(View.VISIBLE);
        }
        Log.e("bird",birdList.toString());
        BirdAdapter birdAdapter = new BirdAdapter(birdList,requireContext(),true);
        birdRecycler.setAdapter(birdAdapter);
        birdRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }
}