package com.urbangracedance.app.android.urbangracedance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.urbangracedance.app.android.urbangracedance.api.Requests;
import com.urbangracedance.app.android.urbangracedance.api.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;

    @InjectView(R.id.UsernameField) EditText usernameField;
    @InjectView(R.id.PasswordField) EditText passwordField;

    @InjectView(R.id.LoginBtn) Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        setTitle(R.string.activity_login_name);

        // Remove this later:
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        final String oldText = loginBtn.getText().toString();

        if(prefs.getString("access-token", null)  != null) {
            loginBtn.setEnabled(false);
            loginBtn.setText("Please wait. Restoring session...");
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
                    loginBtn.setEnabled(true);
                    loginBtn.setText(oldText);
                    SnackbarManager.show(
                            Snackbar.with(LoginActivity.this)
                                    .textColor(Color.RED)
                                    .text("Something went wrong...!"));
                }
            });

        }


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

    @OnClick(R.id.LoginBtn)
    public void login() {

        Container.getInstance().requester = new Requests();

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);

        try {
            Container.getInstance().requester.login(usernameField.getText().toString(),
                    passwordField.getText().toString());

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
        } catch (Exception e) {
            e.printStackTrace();
            SnackbarManager.show(
                    Snackbar.with(LoginActivity.this)
                            .textColor(Color.RED)
                            .text("Failed to login!"));
        }


    }
}
