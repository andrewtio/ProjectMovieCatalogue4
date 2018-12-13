package com.andrew.associate.projectmoviecatalogue3.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.andrew.associate.projectmoviecatalogue3.R;
import com.andrew.associate.projectmoviecatalogue3.adapter.MovieAdapter;
import com.andrew.associate.projectmoviecatalogue3.adapter.ViewPagerAdapter;
import com.andrew.associate.projectmoviecatalogue3.entity.MovieResult;
import com.andrew.associate.projectmoviecatalogue3.fragment.ComingSoonFragment;
import com.andrew.associate.projectmoviecatalogue3.fragment.NowPlayingFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.INTENT_SEARCH;
import static com.andrew.associate.projectmoviecatalogue3.utils.Utils.TAG_INTENT;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    TabLayout tab;
    ViewPager pager;
    NavigationView navigation;
    DrawerLayout drawer;

    private Cursor list;
    private MovieAdapter movieAdapter;

    List<MovieResult> listMovies = new ArrayList<>();

    CircleImageView profileImage;
    String profileImageUrl = "https://pbs.twimg.com/profile_images/378800000404869500/e9aa3c64d7a545bc84fb5a26cc199283_400x400.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.nav_view);
        pager = findViewById(R.id.viewpager);
        tab = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        pagerSetUp(pager);
        tab.setupWithViewPager(pager);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigation.setNavigationItemSelectedListener(this);

        profileImage= navigation.getHeaderView(0).findViewById(R.id.imageProfile);
        Picasso.get()
                .load(profileImageUrl)
                .into(profileImage);
    }

    void pagerSetUp(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.populateFragment(new NowPlayingFragment(), getString(R.string.now_playing));
        adapter.populateFragment(new ComingSoonFragment(), getString(R.string.upcoming));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String cari) {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.putExtra(INTENT_SEARCH, cari);
                searchIntent.putExtra(TAG_INTENT, "cari");
                startActivity(searchIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings_language) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
            Toast.makeText(getApplicationContext(), "Change Language", Toast.LENGTH_SHORT).show();
            return false;
        } else if (item.getItemId() == R.id.action_settings_notification){
                Intent mIntent = new Intent(this, SettingActivity.class);
                startActivity(mIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            Toast.makeText(getApplicationContext(), "Back to Home", Toast.LENGTH_SHORT).show();
            finish();
            return false;

        } else if (id == R.id.nav_search){
            Intent searchIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(searchIntent);
            Toast.makeText(getApplicationContext(), "Press search icon on above right corner to search Movies", Toast.LENGTH_SHORT).show();
            return false;

        } else if (id == R.id.nav_favorite) {
            Intent favIntent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(favIntent);
            Toast.makeText(getApplicationContext(), "Showing your Favorite Movies", Toast.LENGTH_SHORT).show();
            return false;

        } else if (item.getItemId() == R.id.action_settings_language) {
            Intent settingIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(settingIntent);
            Toast.makeText(getApplicationContext(), "Change Language", Toast.LENGTH_SHORT).show();
            return false;
        } else if (item.getItemId() == R.id.action_settings_notification){
            Intent mIntent = new Intent(this, SettingActivity.class);
            startActivity(mIntent);
            Toast.makeText(getApplicationContext(), "Notification Settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
