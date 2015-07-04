package com.urbangracedance.app.android.urbangracedance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.urbangracedance.app.android.urbangracedance.api.Requests;
import com.urbangracedance.app.android.urbangracedance.api.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;

//    @InjectView(R.id.UsernameField) EditText usernameField;
//    @InjectView(R.id.PasswordField) EditText passwordField;
//
//    @InjectView(R.id.LoginBtn) Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        setTitle(R.string.activity_login_name);

        // Remove this later:
//        StrictMode.ThreadPolicy policy = new StrictMode.
//                ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        if(getIntent() != null && getIntent().getData() != null) {
            if(getIntent().getData().toString().startsWith("ugd://authorize")) {

                String token = getIntent().getData().toString().replace("ugd://authorize/", "");
                System.out.println(token);

                Container.getInstance().requester = new Requests(token);


                Container.getInstance().requester.getSelf(new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        Container.getInstance().user = user;
                        finishLoginAndPersist();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        SnackbarManager.show(
                                Snackbar.with(LoginActivity.this)
                                        .textColor(Color.RED)
                                        .text("Something went wrong...!"));

                    }
                });

            }
        }


        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);


        if(prefs.getString("access-token", null)  != null) {
            Container.getInstance().requester = new Requests(prefs.getString("access-token", null));
            Container.getInstance().requester.getSelf(new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    Container.getInstance().user = user;

                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .text(String.format("Welcome back, %s!", user.first_name)));

                    openMainActivity();
                }

                @Override
                public void failure(RetrofitError error) {
                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .textColor(Color.RED)
                                    .text("Something went wrong...!"));
                }
            });

        } else {

            initBrowser();

        }




    }

    public void initBrowser() {
        Toast.makeText(this, "Please login through your browser, you'll be redirected back here afterwards.", Toast.LENGTH_LONG).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.1.99:3000/auth/authorize"));
        startActivity(browserIntent);
    }



    public void finishLoginAndPersist() {
        SnackbarManager.show(
                Snackbar.with(this)
                        .text("Logged in!"));

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);

        prefs.edit().putString("access-token", Container.getInstance().requester.getToken()).apply();

        openMainActivity();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
