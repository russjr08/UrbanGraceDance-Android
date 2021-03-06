package com.urbangracedance.app.android.urbangracedance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.urbangracedance.app.android.urbangracedance.api.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.mainDrawerLayout) DrawerLayout drawerLayout;
    @InjectView(R.id.nav_view) NavigationView navigationView;

    @InjectView(R.id.toolbar) Toolbar toolbar;

    ActionBarDrawerToggle toggle;

    private StudentActivityFragment studentActivity = new StudentActivityFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        if(findViewById(R.id.mainFragmentContainer) != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, studentActivity).commit();

        }


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        setupNavigationDrawer();

    }

    public void setupNavigationDrawer() {
        TextView name, emailAddress, adminText;
        name = (TextView) navigationView.findViewById(R.id.name);
        emailAddress = (TextView) navigationView.findViewById(R.id.email);
        adminText = (TextView) navigationView.findViewById(R.id.adminStatus);

        name.setText(Container.getInstance().user.first_name + " " + Container.getInstance().user.last_name);
        emailAddress.setText(Container.getInstance().user.email_address);
        if(Container.getInstance().user.isAdmin) {
            adminText.setText("Administrator");
        } else {
            adminText.setVisibility(View.GONE);
        }



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.studentMenuItem) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, studentActivity).commit();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);

            prefs.edit().remove("access-token").apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        } else if (id == R.id.action_refresh) {
            Container.getInstance().requester.getSelf(new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    Container.getInstance().user = user;
                    studentActivity.notifyDataWasRefreshed();
                    SnackbarManager.show(
                            Snackbar.with(MainActivity.this)
                                    .text("Account data refreshed successfully!"));
                }

                @Override
                public void failure(RetrofitError error) {
                    SnackbarManager.show(
                            Snackbar.with(MainActivity.this)
                                    .text("Failed to refresh Account Data! :("));
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
