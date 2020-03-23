package com.example.psikologiku.UserFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.psikologiku.MainActivity;
import com.example.psikologiku.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainUserFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_user);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserHomeFragment()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId())
                    {
                        case R.id.nav_home:
                            selectedFragment = new UserHomeFragment();
                            break;
                        case R.id.nav_psikolog:
                            selectedFragment = new UserPsikologListFragment();
                            break;
                        case R.id.nav_konseling:
                            selectedFragment = new UserListKonselingFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new UserProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(MainUserFragment.this, MainActivity.class);
                finish();
                SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.clear().commit();
                startActivity(intent);
        }
        return false;
    }
}
