package com.example.psikologiku.UserFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.psikologiku.BroadCast;
import com.example.psikologiku.MainActivity;
import com.example.psikologiku.R;
import com.example.psikologiku.TopupWallet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.psikologiku.AppChannel.CHANNEL_1_ID;

public class MainUserFragment extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_user);
        notificationManager = NotificationManagerCompat.from(this);
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
        Intent intent;
        switch (item.getItemId()){
            case R.id.logout:
                intent = new Intent(MainUserFragment.this, MainActivity.class);
                finish();
                SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.clear().commit();
                startActivity(intent);
                break;
            case R.id.topup:
                intent = new Intent(MainUserFragment.this, TopupWallet.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
