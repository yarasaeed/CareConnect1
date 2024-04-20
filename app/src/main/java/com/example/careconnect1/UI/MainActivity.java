package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;
import static com.example.careconnect1.Utilities.Config.USER_IMAGES_DIR;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.careconnect1.About;
import com.example.careconnect1.Admin.AdminActivity;
import com.example.cleanup.Fragments.FragmentBookingCleaner;
import com.example.cleanup.Fragments.FragmentBookingCustomer;
import com.example.cleanup.Fragments.FragmentHome;
import com.example.cleanup.Fragments.FragmentProfile;
import com.example.cleanup.Fragments.FragmentSupport;
import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.UserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Locale;

public class MainActivity extends AppCompatClass {
    private BottomNavigationView bottomNavigationView;
    private UserData userData;
    private NavigationView navigationView;
    private ShapeableImageView icon, icon_small;
    private TextView name, role, text_login;
    private String user_role ="";
    private ShapeableImageView icon_menu;
    private String current_tab = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setMethods("","");
        getUserData(userData.getId());
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    public void loadNavigation() {
        DrawerLayout drawerLayout = findViewById(R.id.home);

        icon_menu.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.nav_profile:
                    bottomNavigationView.setSelectedItemId(R.id.profile);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                case R.id.nav_booking:
                    bottomNavigationView.setSelectedItemId(R.id.booking);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                case R.id.nav_help:
                    bottomNavigationView.setSelectedItemId(R.id.support);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                case R.id.nav_rate:
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
                    return true;
                case R.id.nav_share:
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "https://play.google.com/store/apps/details?id="+getPackageName();
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Lets find cleaners");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    return true;
                case R.id.nav_more:
                    Uri url=Uri.parse("http://play.google.com/store/search?q=pub:cleaners");
                    Intent launch=new Intent(Intent.ACTION_VIEW,url);
                    startActivity(launch);

                    return true;
                case R.id.nav_about:
                    openActivity(About.class);
                    return true;

                case R.id.nav_admin:
                    openActivity(AdminActivity.class);
                    return true;

                case R.id.nav_payments:
                    openActivity(AllPayments.class);
                    return true;

                case R.id.nav_policy:
                    openActivity(Policy.class);
                    return true;

                case R.id.nav_reviews:
                    if(!user_role.equals("customer")){
                        openActivity(Reviews.class);
                    }
                    return true;

                case R.id.nav_offers:
                    if(user_role.equals("customer")){
                        openActivity(AllOffers.class);
                    }else{
                        openActivity(CleanerOffers.class);
                    }
                    return true;

            }
            return false;
        });
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        getWindow().setStatusBarColor(getResources().getColor(R.color.status, null));
        bottomNavigationView =findViewById(R.id.bottomNavigation);
        text_login =findViewById(R.id.text_login);
        icon_menu =findViewById(R.id.icon_menu);
        icon_small =findViewById(R.id.icon_small);
        icon =findViewById(R.id.icon);
        role =findViewById(R.id.user_role);
        navigationView = findViewById(R.id.nav2);
        name =findViewById(R.id.user_name);
        userData = new UserData(MainActivity.this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            current_tab = bundle.getString("tab", "1");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setActions() {
        super.setActions();
        if(!userData.isLogin()){
            text_login.setVisibility(View.VISIBLE);
            text_login.setOnClickListener(v -> openActivity(LogIn.class));
        }else{
            text_login.setVisibility(View.GONE);
        }
    }

    public void switchFragment(Fragment fragment ){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myFragment,fragment);
        fragmentTransaction.commit();
        //fragmentTransaction.addToBackStack(null); save process in back press;
    }

    private void getUserData(String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, IP + "select_user.php?id="+ id, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                int i = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if(jsonArray.length() == 0){
                        userData.logout();

                        Menu nav_Menu = navigationView.getMenu();
                        nav_Menu.findItem(R.id.nav_admin).setVisible(false);
                        nav_Menu.findItem(R.id.nav_reviews).setVisible(false);
                        nav_Menu.findItem(R.id.nav_payments).setVisible(false);

                    }
                    while (i < jsonArray.length()) {
                        JSONObject jSONObject = jsonArray.getJSONObject(i);
                        user_role = jSONObject.getString("UserRole");
                        if(user_role.toLowerCase(Locale.ROOT).equals("company")){
                            name.setText(jSONObject.getString("f_name"));
                        }else{
                            name.setText(jSONObject.getString("f_name") + " " +jSONObject.getString("l_name"));
                        }
                        role.setText(user_role.toUpperCase(Locale.ROOT));
                        Glide.with(MainActivity.this)
                                .load(USER_IMAGES_DIR + jSONObject.getString("icon"))
                                .error(R.drawable.ic_user)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(icon);
                        Glide.with(MainActivity.this)
                                .load(USER_IMAGES_DIR + jSONObject.getString("icon"))
                                .error(R.drawable.ic_person)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(icon_small);
                        if(user_role.toLowerCase(Locale.ROOT).equals("customer")){
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_reviews).setVisible(false);
                        }
                        if(user_role.toLowerCase(Locale.ROOT).equals("admin")){
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_reviews).setVisible(false);
                            nav_Menu.findItem(R.id.nav_payments).setVisible(false);
                            nav_Menu.findItem(R.id.nav_booking).setVisible(false);

                        }
                        if(!user_role.toLowerCase(Locale.ROOT).equals("admin")){
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_admin).setVisible(false);
                        }

                        if(user_role.toLowerCase(Locale.ROOT).equals("company") ||
                                user_role.toLowerCase(Locale.ROOT).equals("individual")){
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_sites).setVisible(false);
                            nav_Menu.findItem(R.id.nav_payments).setVisible(false);
                        }

                        i++;
                    }
                    bottomNavigationView.setOnItemSelectedListener(item -> {
                        if (item.getItemId()==R.id.home) {
                            switchFragment(new FragmentHome());
                            return true;
                        }
                        else if (item.getItemId() == R.id.booking) {
                            if(user_role.equals("customer")){

                                switchFragment(new FragmentBookingCustomer());
                            }else if(user_role.equals("admin")){
                                return false;

                            }else{
                                switchFragment(new FragmentBookingCleaner());
                            }
                            return true;

                        }

                        else if (item.getItemId() == R.id.support) {
                            switchFragment(new FragmentSupport());
                            return true;
                        }

                        else if (item.getItemId() == R.id.profile) {
                            switchFragment(new FragmentProfile());
                            return true;
                        }
                        return false;
                    });
                    switch (current_tab){
                        case "2":
                            if(user_role.equals("customer")){
                                switchFragment(new FragmentBookingCustomer());
                            }else{

                                switchFragment(new FragmentBookingCleaner());
                            }
                            bottomNavigationView.setSelectedItemId(R.id.booking);

                            break;
                        case "3":
                            switchFragment(new FragmentSupport());
                            bottomNavigationView.setSelectedItemId(R.id.support);
                            break;
                        case "4":
                            switchFragment(new FragmentProfile());
                            bottomNavigationView.setSelectedItemId(R.id.profile);
                            break;
                        default:
                            switchFragment(new FragmentHome());
                            bottomNavigationView.setSelectedItemId(R.id.home);
                            break;
                    }


                }catch (Exception | Error ignored){

                }

                loadNavigation();

            }
        }, error -> {

        });


        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }




    public void openActivity(Class<?> activity){
        Intent intent=new Intent(MainActivity.this,activity);
        intent.putExtra("user_id", userData.getId());
        startActivity(intent);

    }
}