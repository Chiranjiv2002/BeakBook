package app.ij.mlwithtensorflowlite.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.ij.mlwithtensorflowlite.R;

public class DashBoardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dash_board, container, false);
        view.findViewById(R.id.card_feature_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.allaboutbirds.org/guide/search";
                openUrlInBrowser(url);
            }
        });
        view.findViewById(R.id.card_feature_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.bird-sounds.net/";
                openUrlInBrowser(url);
            }
        });
        view.findViewById(R.id.card_feature_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://birdfact.com/habitats-and-biodiversity/types-of-bird-habitats";
                openUrlInBrowser(url);
            }
        });
        view.findViewById(R.id.card_feature_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.audubon.org/news/11-tips-feeding-backyard-birds";
                openUrlInBrowser(url);
            }
        });
        view.findViewById(R.id.card_feature_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://ny.audubon.org/news/bird-watching-101-guide-beginners";
                openUrlInBrowser(url);
            }
        });
        view.findViewById(R.id.card_feature_6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.birdconservancy.org/";
                openUrlInBrowser(url);
            }
        });
        view.findViewById(R.id.card_feature_7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://birdcast.info/migration-tools/";
                openUrlInBrowser(url);
            }
        });
        return view;
    }
    void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}