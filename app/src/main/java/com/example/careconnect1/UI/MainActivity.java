package com.example.careconnect1.UI;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.careconnect1.Admin.AdminActivity;
import com.example.careconnect1.Fragments.AboutFragment;
import com.example.careconnect1.Fragments.FragmentBookingParent;
import com.example.careconnect1.Fragments.FragmentBookingProvider;
import com.example.careconnect1.Fragments.FragmentPolicy;
import com.example.careconnect1.Fragments.FragmentProfile;
import com.example.careconnect1.Fragments.FragmentSupport;
import com.example.careconnect1.Fragments.HomeFragment;
import com.example.careconnect1.Fragments.ReviewsFragment;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    private UserData userData;
    private String user_role = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize UserData object
        UserData userData = new UserData(getApplicationContext());

        // Retrieve user role from UserData object
        String user_role = userData.getRole();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    openFragment(new HomeFragment());
                } else if (itemId == R.id.support) {
                    openFragment(new FragmentSupport());
                } else if (itemId == R.id.profile) {
                    openFragment(new FragmentProfile());
                } else if (item.getItemId() == R.id.booking) {
                    if (user_role.equals("parent")) {
                        openFragment(new FragmentBookingParent());
                    } else if (user_role.equals("admin")) {
                        return false;
                    } else {
                        openFragment(new FragmentBookingProvider());
                    }
                    return true;
                }
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_profile) {
            openFragment(new FragmentProfile());
        } else if (itemId == R.id.nav_help) {
            openFragment(new FragmentSupport());
        } else if (itemId == R.id.nav_policy) {
            openFragment(new FragmentPolicy());
        } else if (itemId == R.id.nav_rate) {
            Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        } else if (itemId == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=" + getPackageName();
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Let's find providers");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (itemId == R.id.nav_more) {
            Uri url = Uri.parse("http://play.google.com/store/search?q=pub:providers");
            Intent launch = new Intent(Intent.ACTION_VIEW, url);
            startActivity(launch);
        } else if (itemId == R.id.nav_logout) {
            logoutUser();
        } else if (itemId == R.id.nav_about) {
            openFragment(new AboutFragment());
        } else if (itemId == R.id.nav_admin) {
            openActivity(AdminActivity.class);
            return true;
        } else if (itemId == R.id.nav_payments) {
            openActivity(AllPayments.class);
            return true;
        } else if (itemId == R.id.nav_reviews) {
            if (!user_role.equals("parent")) {
                openFragment(new ReviewsFragment());
            }
            return true;
        } else if (itemId == R.id.nav_offers) {
            if (user_role.equals("parent")) {
                openActivity(AllOffers.class);
            } else {
                openActivity(ProviderOffers.class);
            }
            return true;
        } else if (item.getItemId() == R.id.booking) {
            if (user_role.equals("parent")) {
                openFragment(new FragmentBookingParent());
            } else if (user_role.equals("admin")) {
                return false;
            } else {
                openFragment(new FragmentBookingProvider());
            }
            return true;
        }
        return false;
    }

    private void logoutUser() {
        // Clear any user session data or preferences
        // For example, clear SharedPreferences or reset user authentication status
        // Navigate to the login screen or any other desired screen
        Intent intent = new Intent(this, LogIn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish current activity to prevent returning to it when pressing back
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openActivity(Class<?> activity) {
        Intent intent = new Intent(MainActivity.this, activity);
        intent.putExtra("user_id", userData.getId());
        startActivity(intent);

    }


}
