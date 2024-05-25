package app.ij.mlwithtensorflowlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.ij.mlwithtensorflowlite.fragments.AboutUsFragment;
import app.ij.mlwithtensorflowlite.fragments.DashBoardFragment;
import app.ij.mlwithtensorflowlite.fragments.IdentifyFragment;
import app.ij.mlwithtensorflowlite.fragments.LikeFragment;
import app.ij.mlwithtensorflowlite.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Load the default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new DashBoardFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.dashfragment:
                            selectedFragment = new DashBoardFragment();
                            break;
                        case R.id.searchfragment:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.identifyfragment:
                            selectedFragment = new IdentifyFragment();
                            break;
                        case R.id.likefragment:
                            selectedFragment = new LikeFragment();
                            break;
                        case R.id.aboutusfragment:
                            selectedFragment = new AboutUsFragment();
                            break;
                    }

                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}
