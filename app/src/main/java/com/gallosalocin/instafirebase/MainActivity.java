package com.gallosalocin.instafirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gallosalocin.instafirebase.databinding.ActivityMainBinding;
import com.gallosalocin.instafirebase.fragments.HomeFragment;
import com.gallosalocin.instafirebase.fragments.NotificationFragment;
import com.gallosalocin.instafirebase.fragments.ProfileFragment;
import com.gallosalocin.instafirebase.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:
                    selectorFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectorFragment = new SearchFragment();
                    break;
                case R.id.nav_add:
                    selectorFragment = null;
                    startActivity(new Intent(MainActivity.this, PostActivity.class));
                    break;
                case R.id.nav_fav:
                    selectorFragment = new NotificationFragment();
                    break;
                case R.id.nav_profile:
                    selectorFragment = new ProfileFragment();
                    break;
            }

            if (selectorFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
            }

            return true;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }
}
